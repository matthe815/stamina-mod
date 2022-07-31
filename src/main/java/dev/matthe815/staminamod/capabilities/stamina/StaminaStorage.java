package dev.matthe815.staminamod.capabilities.stamina;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;

public class StaminaStorage implements Capability.IStorage<IStamina> {

    @Nullable
    @Override
    public INBT writeNBT(Capability<IStamina> capability, IStamina instance, Direction side) {
        CompoundNBT tag = new CompoundNBT();
        tag.putFloat("stamina", instance.Get().stamina);
        return tag;
    }

    @Override
    public void readNBT(Capability<IStamina> capability, IStamina instance, Direction side, INBT nbt) {
        CompoundNBT tag = (CompoundNBT) nbt;
        instance.Set(new Stamina().Set(tag.getFloat("stamina")));
    }
}
