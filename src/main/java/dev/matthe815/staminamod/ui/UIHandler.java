package dev.matthe815.staminamod.ui;

import java.util.Random;

import com.mojang.blaze3d.matrix.MatrixStack;
import dev.matthe815.staminamod.capabilities.stamina.IStamina;
import dev.matthe815.staminamod.capabilities.stamina.StaminaProvider;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.client.gui.ForgeIngameGui;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import org.lwjgl.opengl.GL11;

public class UIHandler
{
    protected int offset;
    protected int disappearTicks = 0;

    private final static Random random = new Random();
    private final Minecraft minecraft = Minecraft.getInstance();

    private MatrixStack stack = new MatrixStack();

    private static int updateCounter;

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event)
    {
        if (event.phase == TickEvent.Phase.END && !minecraft.isGamePaused())
            updateCounter++;
    }

    private static final ResourceLocation icons = new ResourceLocation("staminamod", "textures/gui/icons.png");

    @SubscribeEvent(priority= EventPriority.LOW)
    public void onPreRender(RenderGameOverlayEvent.Pre event)
    {
        if (event.getType() != ElementType.AIR)
            return;

        offset = ForgeIngameGui.right_height;

        if (event.isCanceled())
            return;

        PlayerEntity player = minecraft.player;
        int width = event.getWindow().getScaledWidth();
        int height = event.getWindow().getScaledHeight();

        int left = width / 2 + 27;
        int top = (height / 2) + 20;

        if (!player.getCapability(StaminaProvider.STAMINA).isPresent()) return;

        IStamina stamina = player.getCapability(StaminaProvider.STAMINA).resolve().get();

        if (stamina.Get().stamina < stamina.Get().GetMaxStamina(player))
            disappearTicks=0;

        disappearTicks++;

        draw_circle(1, 1, 1f);

        if (disappearTicks<150)
            drawStamina(stamina, minecraft, left-15, top, 1);
    }

    void draw_circle(float x, float y, float radius) {
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glPushMatrix();
        GL11.glLoadIdentity();
        GL11.glTranslatef(x, y, 0.0f);
        int circle_points = 100;
        float angle = 2.0f * 3.1416f / circle_points;

        // this code (mostly) copied from question:
        GL11.glBegin(GL11.GL_POLYGON);
        double angle1=0.0;
        GL11.glVertex2d(radius * Math.cos(0.0) , radius * Math.sin(0.0));
        int i;
        for (i=0; i<circle_points; i++)
        {
            GL11.glVertex2d(radius * Math.cos(angle1), radius *Math.sin(angle1));
            angle1 += angle;
        }
        GL11.glEnd();
        GL11.glPopMatrix();
    }

    public void drawStamina (IStamina stamina, Minecraft mc, int left, int top, float alpha)
    {
        mc.getTextureManager().bindTexture(icons);

        float maxExhaustion = stamina.Get().GetMaxStamina(mc.player);
        float ratio = stamina.Get().stamina / maxExhaustion;
        int width = (int) (ratio * 30);
        int height = 6;
        int startY = top;

        if ((ratio*60) <= 20 && updateCounter % (4 * 3 + 1) == 0)
            startY = top + (random.nextInt(3) - 1);

        mc.ingameGUI.blit(stack, left - 30+4-1, startY-1, 81 - 30, 54, 32, (height+2)-4);
        mc.ingameGUI.blit(stack, left - width+4, startY, 81 - width, 45, width, height-4);
    }

    public static void Init()
    {
        MinecraftForge.EVENT_BUS.register(new UIHandler());
    }

}
