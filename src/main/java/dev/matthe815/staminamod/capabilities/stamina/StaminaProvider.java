package dev.matthe815.staminamod.capabilities.stamina;

import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

public class StaminaProvider implements ICapabilitySerializable<INBT>
{
    @CapabilityInject(IStamina.class)
    public static final Capability<IStamina> STAMINA = null;
	private LazyOptional<IStamina> holder = LazyOptional.of(STAMINA::getDefaultInstance);

	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> capability, Direction facing) {
		return capability == STAMINA ? holder.cast() : LazyOptional.empty();
	}

	@Override
	public INBT serializeNBT() {

		return STAMINA.getStorage().writeNBT(STAMINA, this.holder.orElseThrow(() -> new IllegalArgumentException("Lazy optional cannot be empty")),  null);
	}

	@Override
	public void deserializeNBT(INBT nbt) {
		if (StaminaProvider.STAMINA == null)
			return;

		STAMINA.getStorage().readNBT(STAMINA, this.holder.orElseThrow(() -> new IllegalArgumentException("Lazy optional cannot be empty")), null, nbt);
	}
}