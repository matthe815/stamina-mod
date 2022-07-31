package dev.matthe815.staminamod;

import dev.matthe815.staminamod.capabilities.stamina.StaminaCapability;
import dev.matthe815.staminamod.capabilities.stamina.StaminaProvider;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class EventHandler {

    @SubscribeEvent
    public void onTick(TickEvent.PlayerTickEvent tick) {
        StaminaCapability capability = (StaminaCapability)tick.player.getCapability(StaminaProvider.STAMINA).resolve().get();
        capability.SetPlayer(tick.player);
        capability.OnTick(tick.phase); // Tick the server.
    }
}
