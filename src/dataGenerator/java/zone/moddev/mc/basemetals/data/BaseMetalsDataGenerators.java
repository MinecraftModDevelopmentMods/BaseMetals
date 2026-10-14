package zone.moddev.mc.basemetals.data;

import zone.moddev.mc.basemetals.BaseMetals;

import net.minecraft.core.Direction;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.loaders.DynamicBucketModelBuilder;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;

import zone.moddev.mc.basemetals.content.CompatibilityDoubleSlabBlock;
import zone.moddev.mc.basemetals.content.BaseMetalAnvilBlock;
import zone.moddev.mc.basemetals.content.HumanDetectorBlock;
import zone.moddev.mc.basemetals.content.ModContent;
import zone.moddev.mc.basemetals.content.PlateBlock;
import zone.moddev.mc.basemetals.material.MaterialCatalogue;

import net.minecraft.world.level.block.AnvilBlock;
import net.minecraft.world.level.block.ButtonBlock;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.IronBarsBlock;
import net.minecraft.world.level.block.LeverBlock;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.PressurePlateBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.WallBlock;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

@Mod.EventBusSubscriber(modid = BaseMetals.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class BaseMetalsDataGenerators {
    private BaseMetalsDataGenerators() {}

    @SubscribeEvent
    public static void gather(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        if (event.includeClient()) {
            generator.addProvider(new States(generator, event.getExistingFileHelper()));
            generator.addProvider(new Items(generator, event.getExistingFileHelper()));
        }
        generator.addProvider(new GeneratedData(generator));
    }

    private static final class States extends BlockStateProvider {
        private States(DataGenerator generator, ExistingFileHelper helper) {
            super(generator, BaseMetals.MOD_ID, helper);
        }

        @Override
        protected void registerStatesAndModels() {
            ModContent.blocksById().forEach((id, object) -> {
                net.minecraft.world.level.block.Block block = object.get();
                if (block instanceof LiquidBlock || id.endsWith("_ore")) return;
                if (block instanceof PlateBlock) {
                    plateBlock(block, existing(id));
                } else if (block instanceof IronBarsBlock bars) {
                    barsBlock(bars, id);
                } else if (block instanceof DoorBlock door) {
                    doorBlock(door, existing(id + "_bottom"), existing(id + "_bottom_rh"),
                            existing(id + "_top"), existing(id + "_top_rh"));
                } else if (block instanceof TrapDoorBlock trapdoor) {
                    trapdoorBlock(trapdoor, existing(id + "_bottom"), existing(id + "_top"),
                            existing(id + "_open"), true);
                } else if (block instanceof ButtonBlock button) {
                    buttonBlock(button, existing(id), existing(id + "_pressed"));
                } else if (block instanceof SlabBlock slab) {
                    String base = id.startsWith("double_") ? id.substring(7, id.length() - 5) : id.substring(0, id.length() - 5);
                    slabBlock(slab, existing("half_slab_" + base), existing("upper_slab_" + base),
                            fullBlockModel(base));
                } else if (block instanceof PressurePlateBlock plate) {
                    pressurePlateBlock(plate, existing(id + "_up"), existing(id + "_down"));
                } else if (block instanceof StairBlock stairs) {
                    stairsBlock(stairs, existing(id), existing(baseName(id) + "_inner_stairs"),
                            existing(baseName(id) + "_outer_stairs"));
                } else if (block instanceof WallBlock wall) {
                    wallBlock(wall, existing(id + "_post"), existing(id + "_side"),
                            models().wallSideTall(id + "_side_tall", fullBlockTexture(id.substring(0, id.length() - 5))));
                } else if (block instanceof LeverBlock lever) {
                    getVariantBuilder(lever).forAllStates(state -> ConfiguredModel.builder()
                            .modelFile(existing(state.getValue(BlockStateProperties.POWERED) ? id : id + "_off")).build());
                } else if (block instanceof HumanDetectorBlock detector) {
                    pressurePlateBlock(detector, existing("human_detector_up"), existing("human_detector_down"));
                } else if (block instanceof BaseMetalAnvilBlock anvil) {
                    getVariantBuilder(anvil).forAllStates(state -> ConfiguredModel.builder()
                            .modelFile(existing(id + switch (state.getValue(BaseMetalAnvilBlock.DAMAGE)) {
                                case 0 -> "_undamaged";
                                case 1 -> "_slightly_damaged";
                                default -> "_very_damaged";
                            }))
                            .rotationY(switch (state.getValue(AnvilBlock.FACING)) {
                                case SOUTH -> 0;
                                case WEST -> 90;
                                case NORTH -> 180;
                                case EAST -> 270;
                                default -> 0;
                            }).build());
                } else {
                    simpleBlock(block, existing(id));
                }
            });
        }

        private void plateBlock(net.minecraft.world.level.block.Block block, ModelFile model) {
            getVariantBuilder(block).forAllStates(state -> {
                Direction facing = state.getValue(PlateBlock.FACING);
                int x = switch (facing) {
                    case DOWN -> 90;
                    case UP -> 270;
                    default -> 0;
                };
                int y = switch (facing) {
                    case EAST -> 90;
                    case SOUTH -> 180;
                    case WEST -> 270;
                    default -> 0;
                };
                return ConfiguredModel.builder()
                        .modelFile(model)
                        .rotationX(x)
                        .rotationY(y)
                        .build();
            });
        }

        private ModelFile existing(String name) {
            return models().getExistingFile(modLoc("block/" + name));
        }

        private ModelFile fullBlockModel(String material) {
            return MaterialCatalogue.BY_NAME.containsKey(material)
                    ? existing(material + "_block")
                    : models().getExistingFile(mcLoc("block/" + (material.equals("obsidian")
                            ? "obsidian" : material + "_block")));
        }

        private net.minecraft.resources.ResourceLocation fullBlockTexture(String material) {
            return MaterialCatalogue.BY_NAME.containsKey(material)
                    ? modLoc("block/" + material + "_block")
                    : mcLoc("block/" + switch (material) {
                        case "obsidian" -> "obsidian";
                        case "quartz" -> "quartz_block_side";
                        default -> material + "_block";
                    });
        }

        private void barsBlock(IronBarsBlock bars, String id) {
            var ends = models().getBuilder(id + "_post_ends").ao(false)
                    .texture("particle", modLoc("block/" + id))
                    .texture("edge", modLoc("block/" + id));

            for (float height : new float[] {0.001F, 15.999F}) {
                ends.element().from(7, height, 7).to(9, height, 9)
                        .face(Direction.DOWN).uvs(7, 7, 9, 9).texture("#edge").end()
                        .face(Direction.UP).uvs(7, 7, 9, 9).texture("#edge").end()
                        .end();
            }

            var properties = java.util.Map.of(
                    Direction.NORTH, BlockStateProperties.NORTH,
                    Direction.EAST, BlockStateProperties.EAST,
                    Direction.SOUTH, BlockStateProperties.SOUTH,
                    Direction.WEST, BlockStateProperties.WEST);
            var directions = java.util.List.of(Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST);
            var builder = getMultipartBuilder(bars);
            builder.part().modelFile(ends).addModel().end();

            // Iron bars use a centre post only when isolated, and a cap only at a loose end.
            // The generic pane layout draws these faces over the connecting sides.
            var isolatedPost = builder.part().modelFile(existing(id + "_post")).addModel();
            for (Direction direction : directions) {
                isolatedPost.condition(properties.get(direction), false);
            }
            isolatedPost.end();

            for (Direction direction : directions) {
                boolean alternate = direction == Direction.SOUTH || direction == Direction.WEST;
                int rotation = direction.getAxis() == Direction.Axis.X ? 90 : 0;
                var cap = builder.part().modelFile(existing(id + (alternate ? "_cap_alt" : "_cap")))
                        .rotationY(rotation).addModel();

                for (Direction connection : directions) {
                    cap.condition(properties.get(connection), connection == direction);
                }
                cap.end();

                builder.part().modelFile(existing(id + (alternate ? "_side_alt" : "_side")))
                        .rotationY(rotation).addModel()
                        .condition(properties.get(direction), true).end();
            }
        }

        private static String baseName(String stairs) {
            return stairs.substring(0, stairs.length() - "_stairs".length());
        }
    }

    /** Models for the few new/forwarded IDs not already present in the 1.12 assets. */
    private static final class Items extends ItemModelProvider {
        private Items(DataGenerator generator, ExistingFileHelper helper) {
            super(generator, BaseMetals.MOD_ID, helper);
        }

        @Override
        protected void registerModels() {
            MaterialCatalogue.ALL.stream().filter(material -> material.hasOre() && !material.name().equals("copper"))
                    .forEach(material -> basicFromTexture(material.name() + "_raw", material.name() + "_raw"));
            for (String material : java.util.List.of("diamond", "gold", "iron")) {
                basicFromTexture(material + "_blend", material + "_powder");
                basicFromTexture(material + "_smallblend", material + "_smallpowder");
            }

            ModContent.fluids().forEach((name, fluid) -> getBuilder(name + "_bucket")
                    .parent(getExistingFile(new net.minecraft.resources.ResourceLocation("forge", "item/bucket")))
                    .customLoader(DynamicBucketModelBuilder::begin)
                    .fluid(fluid.source().get()));

            ModContent.fluids().forEach((name, fluid) -> getBuilder(name)
                    .parent(getExistingFile(mcLoc("item/generated")))
                    .texture("layer0", modLoc("block/" + (name.equals("mercury")
                            ? "mercury_still" : "molten_metal_still"))));
        }

        private void basicFromTexture(String id, String texture) {
            getBuilder(id).parent(getExistingFile(mcLoc("item/generated")))
                    .texture("layer0", modLoc("item/" + texture));
        }
    }
}
