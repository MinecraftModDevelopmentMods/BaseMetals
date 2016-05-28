package cyano.basemetals.entities;

import cyano.basemetals.BaseMetals;
import cyano.basemetals.util.VillagerTradeHelper;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraftforge.fml.common.FMLLog;
import org.apache.logging.log4j.Level;

/**
 * Use <code>VillagerTradeHelper.insertTrades(profession, career, level, tradeList)</code> instead!<br>
 * This class is just a villager with customizable trades. An event handler converts all 
 * EntityVillagers that spawn into EnityBetterVillagers
 * @author DrCyano
 *
 */
@Deprecated
public class EntityBetterVillager {

	/**
	 * Use <code>VillagerTradeHelper.insertTrades(profession, career, level, tradeList)</code> instead
	 * @param profession Villager profession ID (0-4)
	 * @param career Villager career ID (1-3)
	 * @param level Level of trade (1+)
	 * @param tradeList Trades to add to the given level
	 */
	@Deprecated
	public static void addVillagerTrades(int profession, int career, int level, EntityVillager.ITradeList... tradeList) {
		try {
			VillagerTradeHelper.insertTrades(profession, career, level, tradeList);
		} catch (NoSuchFieldException | IllegalAccessException ex) {
			FMLLog.log(Level.ERROR, ex, "%s: %s.addVillagerTrades(%s, %s, %s, EntityVillager.ITradeList[%s]) failed!",
					BaseMetals.MODID, EntityBetterVillager.class.getCanonicalName(), profession, career, level, tradeList.length);
		}
	}
}
