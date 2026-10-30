"""Check Minecraft's SDL/OpenGL requirements before starting a headless client."""

import argparse
import ctypes
import os
from pathlib import Path
import tempfile
from zipfile import ZipFile


def check_context(library: Path) -> None:
    sdl = ctypes.CDLL(str(library))
    sdl.SDL_Init.argtypes = [ctypes.c_uint32]
    sdl.SDL_Init.restype = ctypes.c_bool
    sdl.SDL_GL_LoadLibrary.argtypes = [ctypes.c_char_p]
    sdl.SDL_GL_LoadLibrary.restype = ctypes.c_bool
    sdl.SDL_GL_SetAttribute.argtypes = [ctypes.c_int, ctypes.c_int]
    sdl.SDL_GL_SetAttribute.restype = ctypes.c_bool
    sdl.SDL_CreateWindow.argtypes = [ctypes.c_char_p, ctypes.c_int, ctypes.c_int, ctypes.c_uint64]
    sdl.SDL_CreateWindow.restype = ctypes.c_void_p
    sdl.SDL_GL_CreateContext.argtypes = [ctypes.c_void_p]
    sdl.SDL_GL_CreateContext.restype = ctypes.c_void_p
    sdl.SDL_GL_DestroyContext.argtypes = [ctypes.c_void_p]
    sdl.SDL_DestroyWindow.argtypes = [ctypes.c_void_p]
    sdl.SDL_GetError.restype = ctypes.c_char_p

    def require(success: object, operation: str) -> None:
        if not success:
            error = sdl.SDL_GetError().decode('utf-8', errors='replace')
            raise RuntimeError(f'{operation}: {error}')

    window = None
    context = None

    try:
        require(sdl.SDL_Init(0x20), 'SDL video initialization failed')
        require(sdl.SDL_GL_LoadLibrary(b'libGL.so.1'), 'OpenGL library could not load')

        # Match GlBackend: OpenGL 3.3 core, forward-compatible, with an sRGB framebuffer.
        for attribute, value in ((17, 3), (18, 3), (20, 1), (19, 2), (22, 1)):
            require(sdl.SDL_GL_SetAttribute(attribute, value), 'OpenGL attribute was rejected')

        window = sdl.SDL_CreateWindow(b'Base Metals graphics check', 320, 480, 2147614730)
        require(window, 'OpenGL window could not open')
        context = sdl.SDL_GL_CreateContext(window)
        require(context, 'OpenGL context could not open')
        print('SDL_OPENGL_CONTEXT_PASS version=3.3 profile=core srgb=requested')
    finally:
        if context:
            sdl.SDL_GL_DestroyContext(context)
        if window:
            sdl.SDL_DestroyWindow(window)
        sdl.SDL_Quit()


def main() -> None:
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('native_jar', type=Path, help='The checksum-verified LWJGL SDL Linux native JAR')
    args = parser.parse_args()

    if not os.environ.get('DISPLAY'):
        raise RuntimeError('No X11 display is available. Run this check under xvfb-run.')
    if os.environ.get('SDL_OPENGL_FORCE_SRGB_FRAMEBUFFER'):
        raise RuntimeError('Remove the sRGB override; this check must use Minecraft\'s graphics requirements.')

    with tempfile.TemporaryDirectory(prefix='basemetals-sdl-check-') as directory:
        library = Path(directory) / 'libSDL3.so'
        with ZipFile(args.native_jar) as archive:
            entries = [entry for entry in archive.namelist() if entry.endswith('/libSDL3.so')]
            if len(entries) != 1:
                raise RuntimeError('Expected exactly one SDL3 library in the verified native JAR')
            library.write_bytes(archive.read(entries[0]))

        check_context(library)


if __name__ == '__main__':
    main()
