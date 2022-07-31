package dev.matthe815.staminamod.capabilities.stamina;

public interface IStamina {

	/**
	 * Set the player's current stamina.
	 */
	void Set(Stamina stamina);

	/**
	 * Get the player's current stamina.
	 */
	Stamina Get();

	void Add(Stamina value);

	void Remove(Stamina value);

}
