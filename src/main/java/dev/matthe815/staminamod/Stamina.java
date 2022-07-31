package dev.matthe815.staminamod;

import dev.matthe815.staminamod.capabilities.stamina.IStamina;
import dev.matthe815.staminamod.capabilities.stamina.StaminaCapability;
import dev.matthe815.staminamod.capabilities.stamina.StaminaProvider;
import dev.matthe815.staminamod.capabilities.stamina.StaminaStorage;
import dev.matthe815.staminamod.networking.StaminaUpdatePacket;
import dev.matthe815.staminamod.ui.UIHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("staminamod")
public class Stamina {

    // Directly reference a log4j logger.
    private static final Logger LOGGER = LogManager.getLogger();

    private static final String PROTOCOL_VERSION = "1";

    public static final SimpleChannel network = NetworkRegistry.ChannelBuilder
            .named(new ResourceLocation("staminamod", "sync"))
            .clientAcceptedVersions(s -> true)
            .serverAcceptedVersions(s -> true)
            .networkProtocolVersion(() -> PROTOCOL_VERSION)
            .simpleChannel();

    public Stamina() {
        // Register the setup method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setup(final FMLCommonSetupEvent event) {
        net.minecraftforge.common.capabilities.CapabilityManager.INSTANCE.register(IStamina.class, new StaminaStorage(), StaminaCapability::new);

        network.registerMessage(1, StaminaUpdatePacket.class, StaminaUpdatePacket::encode, StaminaUpdatePacket::decode, StaminaUpdatePacket.Handler::handle);
        UIHandler.Init();
        MinecraftForge.EVENT_BUS.register(new EventHandler());
    }

    @SubscribeEvent
    public void attachCapability(AttachCapabilitiesEvent<Entity> event)
    {
        if (!(event.getObject() instanceof PlayerEntity))
            return;

        event.addCapability(new ResourceLocation("staminamod", "stamina"), new StaminaProvider());
    }
}
