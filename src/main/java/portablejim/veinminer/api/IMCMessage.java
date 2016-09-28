package portablejim.veinminer.api;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.event.FMLInterModComms;

/**
 * Helper functions to send IMC messages to VeinMiner
 */
public class IMCMessage {

	/**
	 *
	 * @param type
	 * @param itemName
	 */
	public static void addTool(String type, String itemName) {
		sendWhitelistMessage("item", type, itemName);
	}

	/**
	 *
	 * @param type
	 * @param blockName
	 */
	public static void addBlock(String type, String blockName) {
		sendWhitelistMessage("block", type, blockName);
	}

	private static void sendWhitelistMessage(String itemType, String toolType, String blockName) {
		final NBTTagCompound message = new NBTTagCompound();
		message.setString("whitelistType", itemType);
		message.setString("toolType", toolType);
		message.setString("blockName", blockName);
		FMLInterModComms.sendMessage("VeinMiner", "whitelist", message);
	}

	/**
	 *
	 * @param type
	 * @param name
	 * @param icon
	 */
	public static void addToolType(String type, String name, String icon) {
		final NBTTagCompound message = new NBTTagCompound();
		message.setString("toolType", type);
		message.setString("toolName", name);
		message.setString("toolIcon", icon);
		FMLInterModComms.sendMessage("VeinMiner", "addTool", message);
	}

	/**
	 *
	 * @param existingBlock
	 * @param newBlock
	 */
	public static void addBlockEquivalence(String existingBlock, String newBlock) {
		final NBTTagCompound message = new NBTTagCompound();
		message.setString("existingBlock", existingBlock);
		message.setString("newBlock", newBlock);
		FMLInterModComms.sendMessage("VeinMiner", "addEqualBlocks", message);
	}
}
