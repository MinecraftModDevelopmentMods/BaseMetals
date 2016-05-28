package cyano.basemetals.events;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.FillBucketEvent;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class BucketHandler {

	private static BucketHandler instance = null;
	private static Lock initLock = new ReentrantLock();
	public final Map<Block, Item> buckets;

	private BucketHandler() {
		buckets = new HashMap<Block, Item>(); 
	}

	public static BucketHandler getInstance() {
		if(instance == null) {
			initLock.lock();
			try {
				if(instance == null) {
					instance = new BucketHandler();
				}
			} finally {
				initLock.unlock();
			}
		}
		return instance;
	}

	@SubscribeEvent(priority=EventPriority.HIGH) 
	public void onBucketFill(FillBucketEvent event) {

		ItemStack result = fillCustomBucket(event.world, event.target);

		if (result == null)
			return;

		event.result = result;
		event.setResult(Result.ALLOW);
	}

	private ItemStack fillCustomBucket(World world, MovingObjectPosition location) {
		BlockPos pos = location.getBlockPos();
		Block block = world.getBlockState(pos).getBlock();

		Item bucket = buckets.get(block);
		if (bucket != null && block.getMetaFromState(world.getBlockState(pos)) == 0) {
			world.setBlockToAir(pos);
			return new ItemStack(bucket);
		} else
			return null;
	}
}
