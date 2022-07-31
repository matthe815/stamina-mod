package dev.matthe815.staminamod.capabilities.stamina;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.FoodStats;

public class Stamina {

	public float stamina;
	
	public Stamina () {}
	
	public Stamina Set (float stamina)
	{
		this.stamina = stamina;
		return this;
	}
	
	/**
	 * Get the player's max stamina.
	 */
	public float GetMaxStamina(PlayerEntity player)
	{
		FoodStats stats = player.getFoodStats();
		return StaminaCapability.staminaCaps[stats.getFoodLevel()];
	}
	
}
