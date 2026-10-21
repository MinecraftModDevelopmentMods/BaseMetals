package review.worldgen;

import com.google.gson.JsonObject;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.terraingen.OreGenEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/** Checks imported vanilla rules alongside Base Metals in a packaged server. */
public final class VanillaTakeoverChecks {
    private static final VanillaTakeoverChecks INSTANCE = new VanillaTakeoverChecks();
    private static int diamondEvents;
    private static int deniedDiamondEvents;

    private VanillaTakeoverChecks() {
    }

    static void register() {
        MinecraftForge.ORE_GEN_BUS.register(INSTANCE);
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void observe(final OreGenEvent.GenerateMinable event) {
        if (event.getType() == OreGenEvent.GenerateMinable.EventType.DIAMOND) {
            diamondEvents++;
            if (event.getResult() == Event.Result.DENY) deniedDiamondEvents++;
        }
    }

    static void check(final WorldServer world, final JsonObject profile, final JsonObject report) {
        final boolean managed = Boolean.parseBoolean(System.getProperty("worldgen.vanillaManaged"));
        require(profile.get("manage_vanilla_ores").getAsBoolean() == managed, "Wrong vanilla-management setting");

        final JsonObject imported = profile.getAsJsonObject("ores").getAsJsonObject("orespawn:legacy/diamond_ore");
        require(imported != null, "The saved imported diamond rule disappeared");
        final JsonObject placement = imported.getAsJsonObject("dimension_selectors")
                .getAsJsonObject("orespawn:all_except_nether_end");
        require(placement.get("frequency").getAsDouble() == 0.375D, "Custom diamond frequency changed");
        require(placement.get("min_y").getAsInt() == 7 && placement.get("max_y").getAsInt() == 11,
                "Custom diamond height changed");
        require(placement.get("quantity").getAsInt() == 4, "Custom diamond vein size changed");
        require(!imported.get("retrogen").getAsBoolean(), "The imported retrogen setting changed");

        if (Boolean.getBoolean("worldgen.openVanillaOld")) {
            for (int x = 64; x <= 70; x++) {
                for (int z = 64; z <= 70; z++) world.getChunkFromChunkCoords(x, z);
            }
            report.addProperty("oldVanillaChunksOpened", 49);
        }

        diamondEvents = 0;
        deniedDiamondEvents = 0;
        final int base = Integer.getInteger("worldgen.vanillaSampleBase", 64);
        for (int x = base - 1; x <= base + 7; x++) {
            for (int z = base - 1; z <= base + 7; z++) world.getChunkFromChunkCoords(x, z);
        }

        int diamonds = 0;
        int outsideCustomHeight = 0;
        int baseMetalsOres = 0;
        final BlockPos.MutableBlockPos position = new BlockPos.MutableBlockPos();
        for (int cx = base; cx < base + 7; cx++) {
            for (int cz = base; cz < base + 7; cz++) {
                for (int x = 0; x < 16; x++) {
                    for (int z = 0; z < 16; z++) {
                        for (int y = 0; y < 32; y++) {
                            position.setPos((cx << 4) + x, y, (cz << 4) + z);
                            final Block block = world.getBlockState(position).getBlock();
                            if (block == Blocks.DIAMOND_ORE) {
                                diamonds++;
                                if (y < 7 || y > 11) outsideCustomHeight++;
                            }
                            if (block.getRegistryName() != null
                                    && "basemetals".equals(block.getRegistryName().getResourceDomain())
                                    && block.getRegistryName().getResourcePath().endsWith("_ore")) baseMetalsOres++;
                        }
                    }
                }
            }
        }

        report.addProperty("vanillaManaged", managed);
        report.addProperty("diamondEvents", diamondEvents);
        report.addProperty("deniedDiamondEvents", deniedDiamondEvents);
        report.addProperty("diamonds", diamonds);
        report.addProperty("diamondsOutsideCustomHeight", outsideCustomHeight);
        report.addProperty("baseMetalsOres", baseMetalsOres);

        if (!Boolean.getBoolean("worldgen.vanillaReload")) {
            require(diamondEvents > 0, "No new-chunk diamond generation events were observed");
            require(deniedDiamondEvents == (managed ? diamondEvents : 0), "The vanilla takeover switch was not respected");
        }
        require(diamonds > 0, "No diamond ore was placed");
        require(managed ? outsideCustomHeight == 0 : outsideCustomHeight > 0,
                "The wrong diamond placement settings were used");
        require(baseMetalsOres > 0, "Vanilla management disabled Base Metals ore generation");
    }

    private static void require(final boolean condition, final String message) {
        if (!condition) throw new AssertionError(message);
    }
}
