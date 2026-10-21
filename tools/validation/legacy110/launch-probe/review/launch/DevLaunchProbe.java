package review.launch;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.Future;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameType;
import net.minecraft.world.WorldServer;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.WorldType;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

/** Opens a disposable world and checks a named stack through a second launch. */
@Mod(modid = "basemetalslaunchprobe", name = "Base Metals launch checks", version = "1",
        acceptableRemoteVersions = "*", dependencies = "required-after:basemetals;after:orespawn")
public final class DevLaunchProbe {

    @Mod.EventHandler
    public void init(final FMLInitializationEvent event) {
        if (System.getProperty("launch.phase") == null) return;
        if (FMLCommonHandler.instance().getSide().isClient()) {
            MinecraftForge.EVENT_BUS.register(new Client());
        }
    }

    @Mod.EventHandler
    public void started(final FMLServerStartedEvent event) throws Exception {
        if (System.getProperty("launch.phase") == null) return;
        final MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
        if (!server.isDedicatedServer()) return;

        try {
            checkWorld(server);
            report(true, "dedicated server and saved inventory");
        } catch (final Throwable failure) {
            report(false, failure.toString());
            throw failure;
        } finally {
            server.initiateShutdown();
        }
    }

    private static void checkWorld(final MinecraftServer server) {
        final String expectedVersion = System.getProperty("launch.expectedVersion", "2.5.1.110021");
        require(expectedVersion.equals(Loader.instance().getIndexedModList().get("basemetals").getVersion()), "Wrong Base Metals version");
        require(Loader.isModLoaded("orespawn"), "The ordinary launch did not load OreSpawn");
        for (final String optional : new String[]{"tconstruct", "ic2", "cofhcore", "Mekanism", "taiga", "golems_metal"}) {
            require(!Loader.isModLoaded(optional), "Optional mod leaked into ordinary launch: " + optional);
        }

        final WorldServer world = server.worldServerForDimension(0);
        final BlockPos position = new BlockPos(8, 80, 8);
        final ResourceLocation copper = new ResourceLocation("basemetals", "copper_block");
        final Block block = net.minecraftforge.fml.common.registry.ForgeRegistries.BLOCKS.getValue(copper);
        final net.minecraft.item.Item ingot = net.minecraftforge.fml.common.registry.ForgeRegistries.ITEMS
                .getValue(new ResourceLocation("basemetals", "copper_ingot"));

        if ("fresh".equals(System.getProperty("launch.phase"))) {
            world.setBlockState(position, block.getDefaultState(), 2);
            world.setBlockState(position.up(), Blocks.CHEST.getDefaultState(), 2);
            final ItemStack stack = new ItemStack(ingot, 3);
            stack.setStackDisplayName("FG7 saved copper");
            final NBTTagCompound marker = new NBTTagCompound();
            marker.setString("Check", "kept across reload");
            stack.getTagCompound().setTag("BaseMetalsBuildCheck", marker);
            ((TileEntityChest) world.getTileEntity(position.up())).setInventorySlotContents(0, stack);
        }

        require(world.getBlockState(position).getBlock() == block, "Saved copper block changed");
        final TileEntityChest chest = (TileEntityChest) world.getTileEntity(position.up());
        final ItemStack stack = chest.getStackInSlot(0);
        require(stack != null && stack.getItem() == ingot && stack.stackSize == 3
                && "FG7 saved copper".equals(stack.getDisplayName())
                && "kept across reload".equals(stack.getTagCompound()
                        .getCompoundTag("BaseMetalsBuildCheck").getString("Check")), "Saved stack data changed");
        world.getChunkFromBlockCoords(position).setChunkModified();
        if (Boolean.getBoolean("launch.remoteMarker")) {
            final BlockPos remote = new BlockPos(1288, 80, -1272);
            world.setBlockState(remote, block.getDefaultState(), 2);
            world.setBlockState(remote.up(), Blocks.CHEST.getDefaultState(), 2);
            final ItemStack saved = chest.getStackInSlot(0).copy();
            ((TileEntityChest) world.getTileEntity(remote.up())).setInventorySlotContents(0, saved);
            world.getChunkFromBlockCoords(remote).setChunkModified();
        }
        server.saveAllWorlds(false);
    }

    private static void report(final boolean passed, final String detail) throws Exception {
        final JsonObject report = new JsonObject();
        report.addProperty("phase", System.getProperty("launch.phase"));
        report.addProperty("passed", passed);
        report.addProperty("detail", detail);
        Files.write(Paths.get(System.getProperty("launch.phase") + "-launch.json"),
                (new GsonBuilder().setPrettyPrinting().create().toJson(report) + "\n").getBytes(StandardCharsets.UTF_8));
        System.out.println("BASE_METALS_LAUNCH_" + (passed ? "PASS" : "FAIL") + " " + detail);
    }

    private static void require(final boolean condition, final String message) {
        if (!condition) throw new AssertionError(message);
    }

    public static final class Client {
        private final long started = System.currentTimeMillis();
        private boolean opening;
        private int menuTicks;
        private Future<?> check;
        private boolean finished;

        @SubscribeEvent
        public void tick(final TickEvent.ClientTickEvent event) throws Exception {
            if (event.phase != TickEvent.Phase.END || finished) return;
            final Minecraft client = Minecraft.getMinecraft();

            try {
                if (!opening && client.currentScreen instanceof GuiMainMenu && ++menuTicks >= 20) {
                    opening = true;
                    System.out.println("BASE_METALS_LAUNCH_MENU");
                    client.launchIntegratedServer("fg7-launch-check", "FG7 Launch Check",
                            new WorldSettings(8675309L, GameType.CREATIVE, false, false, WorldType.FLAT));
                }
                if (opening && client.world != null && client.player != null && check == null) {
                    check = client.getIntegratedServer().addScheduledTask(() -> checkWorld(client.getIntegratedServer()));
                }
                if (check != null && check.isDone()) {
                    check.get();
                    report(true, "main menu, integrated world and saved inventory");
                    finished = true;
                    client.shutdown();
                }
                if (System.currentTimeMillis() - started > 180000) throw new AssertionError("Client startup timed out");
            } catch (final Throwable failure) {
                report(false, failure.toString());
                finished = true;
                client.shutdown();
            }
        }
    }
}
