package cyano.basemetals.events;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import cyano.basemetals.BaseMetals;
import net.minecraftforge.event.terraingen.OreGenEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.eventhandler.Event.Result;

public class VanillaOreGenDisabler {

	@SubscribeEvent(priority=EventPriority.HIGHEST) 	
	public void handleOreGenEvent(OreGenEvent event){
		if(event instanceof BaseMetalsOreGenEvent){
			if(!((BaseMetalsOreGenEvent)event).modID.equals(BaseMetals.MODID)) {
				// other mod or vanilla
				event.setResult(Result.DENY);
			}
		} else {
			// other mod or vanilla
			event.setResult(Result.DENY);
		}
	}

	private VanillaOreGenDisabler() {
		// do nothing
	}

	private static VanillaOreGenDisabler instance = null;

	private static final Lock initLock = new ReentrantLock();

	public static VanillaOreGenDisabler getInstance() {
		if(instance == null) {
			initLock.lock();
			try{
				if(instance == null) {
					instance = new VanillaOreGenDisabler();
				}
			} finally {
				initLock.unlock();
			}
		}
		return instance;
	}
}
