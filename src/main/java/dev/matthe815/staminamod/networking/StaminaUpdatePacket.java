package dev.matthe815.staminamod.networking;

import dev.matthe815.staminamod.capabilities.stamina.IStamina;
import dev.matthe815.staminamod.capabilities.stamina.Stamina;
import dev.matthe815.staminamod.capabilities.stamina.StaminaProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class StaminaUpdatePacket {

	  private float stamina;

	  public StaminaUpdatePacket() {

	  }

	  public StaminaUpdatePacket(Stamina stamina)
	  {
		  this.stamina = stamina.stamina;
	  }

	  public static StaminaUpdatePacket decode(PacketBuffer buf)
	  {
		  return new StaminaUpdatePacket(new Stamina().Set(buf.readFloat()));
	  }

	  public static void encode(StaminaUpdatePacket data, PacketBuffer buf)
	  {
		  buf.writeFloat(data.stamina);
	  }

	  public static class Handler {
		  public static void handle(StaminaUpdatePacket message, Supplier<NetworkEvent.Context> ctx) {
			  IStamina stamina = Minecraft.getInstance().player.getCapability(StaminaProvider.STAMINA, null).resolve().get();

			  stamina.Set(new Stamina().Set(message.stamina));
		  }
	  }
}
