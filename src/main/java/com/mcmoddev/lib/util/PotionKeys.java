package com.mcmoddev.lib.util;

import com.mcmoddev.lib.data.SharedStrings;

import net.minecraft.util.ResourceLocation;

public class PotionKeys {

	public static final ResourceLocation SPEED = new ResourceLocation("speed");
	public static final ResourceLocation SLOWNESS = new ResourceLocation("slowness");
	public static final ResourceLocation HASTE = new ResourceLocation("haste");
	public static final ResourceLocation MINING_FATIGUE = new ResourceLocation("mining_fatigue");
	public static final ResourceLocation STRENGTH = new ResourceLocation("strength");
	public static final ResourceLocation INSTANT_HEALTH = new ResourceLocation("instant_health");
	public static final ResourceLocation INSTANT_DAMAGE = new ResourceLocation("instant_damage");
	public static final ResourceLocation JUMP_BOOST = new ResourceLocation("jump_boost");
	public static final ResourceLocation NAUSEA = new ResourceLocation("nausea");
	public static final ResourceLocation REGENERATION = new ResourceLocation("regeneration");
	public static final ResourceLocation RESISTANCE = new ResourceLocation("resistance");
	public static final ResourceLocation FIRE_RESISTANCE = new ResourceLocation("fire_resistance");
	public static final ResourceLocation WATER_BREATHING = new ResourceLocation("water_breathing");
	public static final ResourceLocation INVISIBILITY = new ResourceLocation("invisibility");
	public static final ResourceLocation BLINDNESS = new ResourceLocation("blindness");
	public static final ResourceLocation NIGHT_VISION = new ResourceLocation("night_vision");
	public static final ResourceLocation HUNGER = new ResourceLocation("hunger");
	public static final ResourceLocation WEAKNESS = new ResourceLocation("weakness");
	public static final ResourceLocation POISON = new ResourceLocation("poison");
	public static final ResourceLocation WITHER = new ResourceLocation("wither");
	public static final ResourceLocation HEALTH_BOOST = new ResourceLocation("health_boost");
	public static final ResourceLocation ABSORBTION = new ResourceLocation("absorption");
	public static final ResourceLocation SATURATION = new ResourceLocation("saturation");
	public static final ResourceLocation GLOWING = new ResourceLocation("glowing");
	public static final ResourceLocation LEVITATION = new ResourceLocation("levitation");
	public static final ResourceLocation LUCK = new ResourceLocation("luck");
	public static final ResourceLocation UNLUCK = new ResourceLocation("unluck");

	private PotionKeys() {
		throw new IllegalAccessError(SharedStrings.NOT_INSTANTIABLE);
	}
}
