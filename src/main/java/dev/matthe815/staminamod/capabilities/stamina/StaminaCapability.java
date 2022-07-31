package dev.matthe815.staminamod.capabilities.stamina;

import dev.matthe815.staminamod.networking.StaminaUpdatePacket;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.world.Difficulty;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.fml.network.NetworkDirection;

public class StaminaCapability implements IStamina
{
	private Stamina stamina = new Stamina().Set(100);
	private float lastStamina = 0;
	
	private PlayerEntity player;
	public static float[] staminaCaps = new float[] {10, 15, 18, 22, 25, 29, 32, 34, 39, 45, 49, 52, 59, 65, 68, 71, 84, 92, 94, 96, 100};

	public StaminaCapability() {}
	
	/**
	 * Get the stamina regeneration rate.
	 */
	public float GetRegenerationRate() 
	{
		float regenRate = 0.33f; // Base regen rate.

		return player.world.getDifficulty()== Difficulty.PEACEFUL ? regenRate*2 : regenRate;
	}

    /**
     * Set the player's current stamina.
     */
	@Override
	public void Set(Stamina stamina) 
	{
		this.stamina = stamina;
	}

	/**
	 * Get the player's current stamina.
	 */
	@Override
	public Stamina Get() 
	{
		return stamina;
	}
	
	@Override
	public void Add(Stamina value) 
	{
		stamina.stamina += value.stamina;
	}

	@Override
	public void Remove(Stamina value) 
	{
		this.stamina.stamina -= value.stamina;
	}

	/**
	 * Try an action, if it fails false is returned.
	 * @param value
	 * @return
	 */
	public boolean TryAction(Stamina value)
	{
		// If not enough stamina, just return.
		if (stamina.stamina < value.stamina) return false;
		Remove(value);
		return true;
	}
	
	public void SetPlayer (PlayerEntity player)
	{
		this.player = player;
	}

	public void OnTick(TickEvent.Phase phase)
	{
		if (player.world.isRemote) // Do not attempt to do anything to the local copy.
			return;

		if (HasChanged()) sendClientUpdate(); // Update if the stamina changes.

		// Reduce stamina while running!
		if (player.isSprinting() && !player.isCreative())
		{
			if (!TryAction(new Stamina().Set(0.33f))) // If running fails to occur, cancel running.
			{
				player.setSprinting(false);
			}
			
			return; // Halt stamina regeneration process if running.
		}

		this.stamina.stamina = Math.min(this.stamina.stamina, this.stamina.GetMaxStamina(player)); // Prevent stamina from exceeding max.
		this.Add(new Stamina().Set(this.GetRegenerationRate()));
	}

	public boolean HasChanged() 
	{
		return lastStamina != Get().stamina;
	}

	public void sendClientUpdate()
	{
		lastStamina = stamina.stamina;
		dev.matthe815.staminamod.Stamina.network.sendTo(new StaminaUpdatePacket(Get()), ((ServerPlayerEntity)player).connection.netManager, NetworkDirection.PLAY_TO_CLIENT);
	}

}
