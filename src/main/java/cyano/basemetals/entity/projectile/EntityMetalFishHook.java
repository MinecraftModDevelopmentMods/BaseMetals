package cyano.basemetals.entity.projectile;

import java.util.List;

import cyano.basemetals.items.ItemMetalFishingRod;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootTableList;

/**
 *
 * @author Jasmine Iwanek
 *
 */
public class EntityMetalFishHook extends EntityFishHook {

	private static final DataParameter<Integer> DATA_HOOKED_ENTITY = EntityDataManager.<Integer>createKey(EntityMetalFishHook.class, DataSerializers.VARINT);
	private int xTile;
	private int yTile;
	private int zTile;
	private Block inTile;
	private boolean inGround;
	private float fishApproachAngle;
	private int ticksInGround;
	private int ticksInAir;
	private int ticksCatchable;
	private int ticksCaughtDelay;
	private int ticksCatchableDelay;
	private int fishPosRotationIncrements;
	private double fishX;
	private double fishY;
	private double fishZ;
	private double fishYaw;
	private double fishPitch;

	/**
	 *
	 * @param worldIn
	 */
	public EntityMetalFishHook(World worldIn) {
		super(worldIn);
	}

	/**
	 *
	 * @param worldIn
	 * @param x
	 * @param y
	 * @param z
	 * @param anglerIn
	 */
	public EntityMetalFishHook(World worldIn, double x, double y, double z, EntityPlayer anglerIn) {
		super(worldIn, x, y, z, anglerIn);
	}

	/**
	 *
	 * @param worldIn
	 * @param fishingPlayer
	 */
	public EntityMetalFishHook(World worldIn, EntityPlayer fishingPlayer) {
		super(worldIn, fishingPlayer);
	}

	@Override
	protected void entityInit() {
		this.getDataManager().register(DATA_HOOKED_ENTITY, Integer.valueOf(0));
	}

	@Override
	public void notifyDataManagerChange(DataParameter<?> key) {
		if (DATA_HOOKED_ENTITY.equals(key)) {
			final int i = ((Integer)this.getDataManager().get(DATA_HOOKED_ENTITY)).intValue();

			if ((i > 0) && (this.caughtEntity != null)) {
				this.caughtEntity = null;
			}
		}

		super.notifyDataManagerChange(key);
	}

	/**
	 * Called to update the entity's position/logic.
	 */
	@Override
	public void onUpdate() {
		// super.onUpdate();

		if (!this.worldObj.isRemote)
			this.setFlag(6, this.isGlowing());

		this.onEntityUpdate();

		if (this.worldObj.isRemote) {
			final int i = ((Integer)this.getDataManager().get(DATA_HOOKED_ENTITY)).intValue();

			if ((i > 0) && (this.caughtEntity == null))
				this.caughtEntity = this.worldObj.getEntityByID(i - 1);
		} else {
			final ItemStack itemstack = this.angler.getHeldItemMainhand();

			if (this.angler.isDead || !this.angler.isEntityAlive() || (itemstack == null) || !(itemstack.getItem() instanceof ItemMetalFishingRod) || (this.getDistanceSqToEntity(this.angler) > 1024.0D)) {
				this.setDead();
				this.angler.fishEntity = null;
				return;
			}
		}

		if (this.caughtEntity != null) {
			if (!this.caughtEntity.isDead) {
				this.posX = this.caughtEntity.posX;
				final double d17 = (double)this.caughtEntity.height;
				this.posY = this.caughtEntity.getEntityBoundingBox().minY + (d17 * 0.8D);
				this.posZ = this.caughtEntity.posZ;
				return;
			}

			this.caughtEntity = null;
		}

		if (this.fishPosRotationIncrements > 0) {
			final double d3 = this.posX + ((this.fishX - this.posX) / (double)this.fishPosRotationIncrements);
			final double d4 = this.posY + ((this.fishY - this.posY) / (double)this.fishPosRotationIncrements);
			final double d6 = this.posZ + ((this.fishZ - this.posZ) / (double)this.fishPosRotationIncrements);
			final double d8 = MathHelper.wrapDegrees(this.fishYaw - (double)this.rotationYaw);
			this.rotationYaw = (float)((double)this.rotationYaw + (d8 / (double)this.fishPosRotationIncrements));
			this.rotationPitch = (float)((double)this.rotationPitch + ((this.fishPitch - (double)this.rotationPitch) / (double)this.fishPosRotationIncrements));
			--this.fishPosRotationIncrements;
			this.setPosition(d3, d4, d6);
			this.setRotation(this.rotationYaw, this.rotationPitch);
		} else {
			if (this.inGround) {
				if (this.worldObj.getBlockState(new BlockPos(this.xTile, this.yTile, this.zTile)).getBlock() == this.inTile) {
					++this.ticksInGround;
					if (this.ticksInGround == 1200)
						this.setDead();

					return;
				}

				this.inGround = false;
				this.motionX *= (double)(this.rand.nextFloat() * 0.2F);
				this.motionY *= (double)(this.rand.nextFloat() * 0.2F);
				this.motionZ *= (double)(this.rand.nextFloat() * 0.2F);
				this.ticksInGround = 0;
				this.ticksInAir = 0;
			} else {
				++this.ticksInAir;
			}

			if (!this.worldObj.isRemote) {
				Vec3d vec3d1 = new Vec3d(this.posX, this.posY, this.posZ);
				Vec3d vec3d = new Vec3d(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
				RayTraceResult raytraceresult = this.worldObj.rayTraceBlocks(vec3d1, vec3d);
				vec3d1 = new Vec3d(this.posX, this.posY, this.posZ);
				vec3d = new Vec3d(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);

				if (raytraceresult != null)
					vec3d = new Vec3d(raytraceresult.hitVec.xCoord, raytraceresult.hitVec.yCoord, raytraceresult.hitVec.zCoord);

				Entity entity = null;
				final List<Entity> list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.getEntityBoundingBox().addCoord(this.motionX, this.motionY, this.motionZ).expandXyz(1.0D));
				double d0 = 0.0D;

				for (int j = 0; j < list.size(); ++j) {
					final Entity entity1 = (Entity)list.get(j);

					if (entity1.canBeCollidedWith() && ((entity1 != this.angler) || (this.ticksInAir >= 5))) {
						final AxisAlignedBB axisalignedbb1 = entity1.getEntityBoundingBox().expandXyz(0.30000001192092896D);
						final RayTraceResult raytraceresult1 = axisalignedbb1.calculateIntercept(vec3d1, vec3d);

						if (raytraceresult1 != null) {
							final double d1 = vec3d1.squareDistanceTo(raytraceresult1.hitVec);

							if ((d1 < d0) || (d0 == 0.0D)) {
								entity = entity1;
								d0 = d1;
							}
						}
					}
				}

				if (entity != null)
					raytraceresult = new RayTraceResult(entity);

				if (raytraceresult != null) {
					if (raytraceresult.entityHit != null) {
						this.caughtEntity = raytraceresult.entityHit;
						this.getDataManager().set(DATA_HOOKED_ENTITY, Integer.valueOf(this.caughtEntity.getEntityId() + 1));
					} else {
						this.inGround = true;
					}
				}
			}

			if (!this.inGround) {
				this.moveEntity(this.motionX, this.motionY, this.motionZ);
				final float f2 = MathHelper.sqrt_double((this.motionX * this.motionX) + (this.motionZ * this.motionZ));
				this.rotationYaw = (float)(MathHelper.atan2(this.motionX, this.motionZ) * (180D / Math.PI));

				for (this.rotationPitch = (float)(MathHelper.atan2(this.motionY, (double)f2) * (180D / Math.PI)); (this.rotationPitch - this.prevRotationPitch) < -180.0F; this.prevRotationPitch -= 360.0F) {
					;
				}

				while ((this.rotationPitch - this.prevRotationPitch) >= 180.0F) {
					this.prevRotationPitch += 360.0F;
				}

				while ((this.rotationYaw - this.prevRotationYaw) < -180.0F) {
					this.prevRotationYaw -= 360.0F;
				}

				while ((this.rotationYaw - this.prevRotationYaw) >= 180.0F) {
					this.prevRotationYaw += 360.0F;
				}

				this.rotationPitch = this.prevRotationPitch + ((this.rotationPitch - this.prevRotationPitch) * 0.2F);
				this.rotationYaw = this.prevRotationYaw + ((this.rotationYaw - this.prevRotationYaw) * 0.2F);
				float f3 = 0.92F;

				if (this.onGround || this.isCollidedHorizontally)
					f3 = 0.5F;

				final int k = 5;
				double d5 = 0.0D;

				for (int l = 0; l < k; ++l) {
					final AxisAlignedBB axisalignedbb = this.getEntityBoundingBox();
					final double d9 = axisalignedbb.maxY - axisalignedbb.minY;
					final double d10 = axisalignedbb.minY + ((d9 * (double)l) / (double)k);
					final double d11 = axisalignedbb.minY + ((d9 * (double)(l + 1)) / (double)k);
					final AxisAlignedBB axisalignedbb2 = new AxisAlignedBB(axisalignedbb.minX, d10, axisalignedbb.minZ, axisalignedbb.maxX, d11, axisalignedbb.maxZ);

					if (this.worldObj.isAABBInMaterial(axisalignedbb2, Material.WATER))
						d5 += 1.0D / (double)k;
				}

				if (!this.worldObj.isRemote && (d5 > 0.0D)) {
					final WorldServer worldserver = (WorldServer)this.worldObj;
					int i1 = 1;
					final BlockPos blockpos = (new BlockPos(this)).up();

					if ((this.rand.nextFloat() < 0.25F) && this.worldObj.isRainingAt(blockpos))
						i1 = 2;

					if ((this.rand.nextFloat() < 0.5F) && !this.worldObj.canSeeSky(blockpos))
						--i1;

					if (this.ticksCatchable > 0) {
						--this.ticksCatchable;

						if (this.ticksCatchable <= 0) {
							this.ticksCaughtDelay = 0;
							this.ticksCatchableDelay = 0;
						}
					} else if (this.ticksCatchableDelay > 0) {
						this.ticksCatchableDelay -= i1;

						if (this.ticksCatchableDelay <= 0) {
							this.motionY -= 0.20000000298023224D;
							this.playSound(SoundEvents.ENTITY_BOBBER_SPLASH, 0.25F, 1.0F + ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.4F));
							final float f6 = (float)MathHelper.floor_double(this.getEntityBoundingBox().minY);
							worldserver.spawnParticle(EnumParticleTypes.WATER_BUBBLE, this.posX, (double)(f6 + 1.0F), this.posZ, (int)(1.0F + (this.width * 20.0F)), (double)this.width, 0.0D, (double)this.width, 0.20000000298023224D, new int[0]);
							worldserver.spawnParticle(EnumParticleTypes.WATER_WAKE, this.posX, (double)(f6 + 1.0F), this.posZ, (int)(1.0F + (this.width * 20.0F)), (double)this.width, 0.0D, (double)this.width, 0.20000000298023224D, new int[0]);
							this.ticksCatchable = MathHelper.getRandomIntegerInRange(this.rand, 10, 30);
						} else {
							this.fishApproachAngle = (float)((double)this.fishApproachAngle + this.rand.nextGaussian() * 4.0D);
							final float f5 = this.fishApproachAngle * 0.017453292F;
							final float f8 = MathHelper.sin(f5);
							final float f10 = MathHelper.cos(f5);
							final double d13 = this.posX + (double)(f8 * (float)this.ticksCatchableDelay * 0.1F);
							final double d15 = (double)((float)MathHelper.floor_double(this.getEntityBoundingBox().minY) + 1.0F);
							final double d16 = this.posZ + (double)(f10 * (float)this.ticksCatchableDelay * 0.1F);
							final Block block1 = worldserver.getBlockState(new BlockPos((int)d13, (int)d15 - 1, (int)d16)).getBlock();

							if (block1 == Blocks.WATER || block1 == Blocks.FLOWING_WATER) {
								if (this.rand.nextFloat() < 0.15F)
									worldserver.spawnParticle(EnumParticleTypes.WATER_BUBBLE, d13, d15 - 0.10000000149011612D, d16, 1, (double)f8, 0.1D, (double)f10, 0.0D, new int[0]);

								final float f = f8 * 0.04F;
								final float f1 = f10 * 0.04F;
								worldserver.spawnParticle(EnumParticleTypes.WATER_WAKE, d13, d15, d16, 0, (double)f1, 0.01D, (double)(-f), 1.0D, new int[0]);
								worldserver.spawnParticle(EnumParticleTypes.WATER_WAKE, d13, d15, d16, 0, (double)(-f1), 0.01D, (double)f, 1.0D, new int[0]);
							}
						}
					} else if (this.ticksCaughtDelay > 0) {
						this.ticksCaughtDelay -= i1;
						float f4 = 0.15F;

						if (this.ticksCaughtDelay < 20) {
							f4 = (float)((double)f4 + ((double)(20 - this.ticksCaughtDelay) * 0.05D));
						} else if (this.ticksCaughtDelay < 40) {
							f4 = (float)((double)f4 + ((double)(40 - this.ticksCaughtDelay) * 0.02D));
						} else if (this.ticksCaughtDelay < 60) {
							f4 = (float)((double)f4 + ((double)(60 - this.ticksCaughtDelay) * 0.01D));
						}

						if (this.rand.nextFloat() < f4) {
							final float f7 = MathHelper.randomFloatClamp(this.rand, 0.0F, 360.0F) * 0.017453292F;
							final float f9 = MathHelper.randomFloatClamp(this.rand, 25.0F, 60.0F);
							final double d12 = this.posX + (double)(MathHelper.sin(f7) * f9 * 0.1F);
							final double d14 = (double)((float)MathHelper.floor_double(this.getEntityBoundingBox().minY) + 1.0F);
							final double d2 = this.posZ + (double)(MathHelper.cos(f7) * f9 * 0.1F);
							final Block block = worldserver.getBlockState(new BlockPos((int)d12, (int)d14 - 1, (int)d2)).getBlock();

							if ((block == Blocks.WATER) || (block == Blocks.FLOWING_WATER))
								worldserver.spawnParticle(EnumParticleTypes.WATER_SPLASH, d12, d14, d2, 2 + this.rand.nextInt(2), 0.10000000149011612D, 0.0D, 0.10000000149011612D, 0.0D, new int[0]);
						}

						if (this.ticksCaughtDelay <= 0) {
							this.fishApproachAngle = MathHelper.randomFloatClamp(this.rand, 0.0F, 360.0F);
							this.ticksCatchableDelay = MathHelper.getRandomIntegerInRange(this.rand, 20, 80);
						}
					} else {
						this.ticksCaughtDelay = MathHelper.getRandomIntegerInRange(this.rand, 100, 900);
						this.ticksCaughtDelay -= EnchantmentHelper.getLureModifier(this.angler) * 20 * 5;
					}

					if (this.ticksCatchable > 0)
						this.motionY -= (double)(this.rand.nextFloat() * this.rand.nextFloat() * this.rand.nextFloat()) * 0.2D;
				}

				final double d7 = (d5 * 2.0D) - 1.0D;
				this.motionY += 0.03999999910593033D * d7;

				if (d5 > 0.0D) {
					f3 = (float)((double)f3 * 0.9D);
					this.motionY *= 0.8D;
				}

				this.motionX *= (double)f3;
				this.motionY *= (double)f3;
				this.motionZ *= (double)f3;
				this.setPosition(this.posX, this.posY, this.posZ);
			}
		}
	}

	@Override
	public int handleHookRetraction() {
		if (this.worldObj.isRemote) {
			return 0;
		} else {
			int i = 0;

			if (this.caughtEntity != null) {
				this.bringInHookedEntity();
				this.worldObj.setEntityState(this, (byte)31);
				i = this.caughtEntity instanceof EntityItem ? 3 : 5;
			} else if (this.ticksCatchable > 0) {
				final LootContext.Builder lootcontext = new LootContext.Builder((WorldServer)this.worldObj);
				lootcontext.withLuck((float)EnchantmentHelper.getLuckOfSeaModifier(this.angler) + this.angler.getLuck());

				for (final ItemStack itemstack : this.worldObj.getLootTableManager().getLootTableFromLocation(LootTableList.GAMEPLAY_FISHING).generateLootForPools(this.rand, lootcontext.build())) {
					EntityItem entityitem = new EntityItem(this.worldObj, this.posX, this.posY, this.posZ, itemstack);
					final double d0 = this.angler.posX - this.posX;
					final double d1 = this.angler.posY - this.posY;
					final double d2 = this.angler.posZ - this.posZ;
					final double d3 = (double)MathHelper.sqrt_double((d0 * d0) + (d1 * d1) + (d2 * d2));
					final double d4 = 0.1D;
					entityitem.motionX = d0 * d4;
					entityitem.motionY = (d1 * d4) + ((double)MathHelper.sqrt_double(d3) * 0.08D);
					entityitem.motionZ = d2 * d4;
					this.worldObj.spawnEntityInWorld(entityitem);
					this.angler.worldObj.spawnEntityInWorld(new EntityXPOrb(this.angler.worldObj, this.angler.posX, this.angler.posY + 0.5D, this.angler.posZ + 0.5D, this.rand.nextInt(6) + 1));
				}

				i = 1;
			}

			if (this.inGround)
				i = 2;

			this.setDead();
			this.angler.fishEntity = null;
			return i;
		}
	}
}
