package com.mcmoddev.basemetals.loot;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.mcmoddev.basemetals.BaseMetals;
import com.mcmoddev.basemetals.content.ContentPolicy;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.conditions.LootConditionManager;

import java.util.Random;

/** Hides Base Metals-owned auxiliary loot entries disallowed by the startup policy. */
public final class ContentModeLootCondition implements LootCondition {
	public static final ResourceLocation ID = new ResourceLocation(BaseMetals.MODID, "content_policy");
	private static boolean registered;

	private final ResourceLocation item;

	ContentModeLootCondition(final ResourceLocation item) {
		this.item = item;
	}

	public static synchronized void register() {
		if (!registered) {
			LootConditionManager.registerCondition(new Serializer());
			registered = true;
		}
	}

	@Override
	public boolean testCondition(final Random random, final LootContext context) {
		return ContentPolicy.active().allows(item);
	}

	public static final class Serializer extends LootCondition.Serializer<ContentModeLootCondition> {
		public Serializer() {
			super(ID, ContentModeLootCondition.class);
		}

		@Override
		public void serialize(final JsonObject json, final ContentModeLootCondition value,
				final JsonSerializationContext context) {
			json.addProperty("item", value.item.toString());
		}

		@Override
		public ContentModeLootCondition deserialize(final JsonObject json,
				final JsonDeserializationContext context) {
			return new ContentModeLootCondition(new ResourceLocation(
					JsonUtils.getString(json, "item")));
		}
	}
}
