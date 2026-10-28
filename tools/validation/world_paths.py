"""Find active save files before and after Minecraft's 26.1 storage changes."""


def dimensions(world):
    folders = {'': world, 'DIM-1': world / 'DIM-1', 'DIM1': world / 'DIM1'}
    names = {'overworld': '', 'the_nether': 'DIM-1', 'the_end': 'DIM1'}
    native = world / 'dimensions'
    if native.is_dir():
        for namespace in sorted(native.iterdir()):
            if not namespace.is_dir():
                continue
            for folder in sorted(namespace.rglob('*')):
                if not folder.is_dir() or folder.name not in ('region', 'entities'):
                    continue
                parent = folder.parent
                key = parent.relative_to(world).as_posix()
                if namespace.name == 'minecraft' and parent.parent == namespace:
                    key = names.get(parent.name, key)
                folders[key] = parent
    return sorted(folders.items())


def player_files(world, kind, suffix):
    legacy = 'playerdata' if kind == 'data' else kind
    files = {file.name: file for file in (world / legacy).glob('*' + suffix)}
    files.update({file.name: file for file in (world / 'players' / kind).glob('*' + suffix)})
    return [files[name] for name in sorted(files)]
