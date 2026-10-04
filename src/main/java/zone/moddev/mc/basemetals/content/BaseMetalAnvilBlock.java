package zone.moddev.mc.basemetals.content;

import net.minecraft.block.BlockAnvil;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerRepair;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.IInteractionObject;
import net.minecraft.world.World;

/** Uses the vanilla repair screen and keeps the same block ID as the anvil wears out. */
public final class BaseMetalAnvilBlock extends BlockAnvil {
    public static final IntegerProperty DAMAGE = IntegerProperty.create("damage", 0, 2);

    public BaseMetalAnvilBlock(Properties properties) {
        super(properties);
        setDefaultState(stateContainer.getBaseState().with(FACING, EnumFacing.NORTH)
                .with(DAMAGE, Integer.valueOf(0)));
    }

    @Override
    public IBlockState getStateForPlacement(BlockItemUseContext context) {
        return getDefaultState().with(FACING, context.getPlacementHorizontalFacing().rotateY());
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<net.minecraft.block.Block, IBlockState> builder) {
        builder.add(FACING, DAMAGE);
    }

    /** Entry points used by the Forge 25 compatibility transformer in BlockAnvil.damage. */
    public static boolean isBaseMetalAnvil(IBlockState state) {
        return state != null && state.getBlock() instanceof BaseMetalAnvilBlock;
    }

    public static IBlockState damageBaseMetalAnvil(IBlockState state) {
        int damage = state.get(DAMAGE).intValue();
        return damage >= 2 ? null : state.with(DAMAGE, Integer.valueOf(damage + 1));
    }

    @Override
    public boolean onBlockActivated(IBlockState state, World world, BlockPos pos, EntityPlayer player,
            EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (!world.isRemote) player.displayGui(new Interaction(world, pos, this));
        return true;
    }

    private static final class Interaction implements IInteractionObject {
        private final World world;
        private final BlockPos pos;
        private final BaseMetalAnvilBlock block;
        private Interaction(World world, BlockPos pos, BaseMetalAnvilBlock block) {
            this.world = world;
            this.pos = pos;
            this.block = block;
        }
        @Override public ITextComponent getName() { return new TextComponentTranslation(block.getTranslationKey()); }
        @Override public boolean hasCustomName() { return false; }
        @Override public ITextComponent getCustomName() { return null; }
        @Override public String getGuiID() { return "minecraft:anvil"; }
        @Override public Container createContainer(InventoryPlayer inventory, EntityPlayer player) {
            return new ContainerRepair(inventory, world, pos, player) {
                @Override public boolean canInteractWith(EntityPlayer candidate) {
                    return world.getBlockState(pos).getBlock() == block
                            && candidate.getDistanceSq(pos.getX() + 0.5D, pos.getY() + 0.5D,
                                    pos.getZ() + 0.5D) <= 64.0D;
                }
            };
        }
    }
}
