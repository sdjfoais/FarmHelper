package com.jelly.farmhelper.features;

import com.jelly.farmhelper.FarmHelper;
import com.jelly.farmhelper.macros.MacroHandler;
import com.jelly.farmhelper.utils.BlockUtils;
import com.jelly.farmhelper.utils.Clock;
import com.jelly.farmhelper.utils.KeyBindUtils;
import com.jelly.farmhelper.world.GameState;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class Antistuck {
    private static final Minecraft mc = Minecraft.getMinecraft();
    public static boolean stuck;
    private static double lastX;
    private static double lastZ;
    private static double lastY;
    public static final Clock cooldown = new Clock();

    public static boolean unstuckThreadIsRunning = false;
    public static final Runnable unstuckThread = () -> {
        try {

            KeyBindUtils.stopMovement();
            Thread.sleep(20);
            KeyBindUtils.holdThese(mc.gameSettings.keyBindLeft);
            Thread.sleep(500);
            KeyBindUtils.stopMovement();
            Thread.sleep(20);
            KeyBindUtils.holdThese(mc.gameSettings.keyBindRight);
            Thread.sleep(500);
            KeyBindUtils.stopMovement();
            Thread.sleep(20);
            KeyBindUtils.holdThese(mc.gameSettings.keyBindForward);
            Thread.sleep(500);
            KeyBindUtils.stopMovement();
            Thread.sleep(20);
            KeyBindUtils.holdThese(mc.gameSettings.keyBindBack);
            Thread.sleep(200);
            KeyBindUtils.stopMovement();
            Thread.sleep(200);
            Antistuck.stuck = false;
            Antistuck.cooldown.schedule(3500);
            unstuckThreadIsRunning = false;
        } catch (Throwable e) {
            e.printStackTrace();
        }
    };

    @SubscribeEvent
    public final void tick(TickEvent.ClientTickEvent event) {
        if(event.phase == TickEvent.Phase.END)
            return;

        if (MacroHandler.currentMacro == null || !MacroHandler.currentMacro.enabled || mc.thePlayer == null || mc.theWorld == null || FarmHelper.gameState.currentLocation != GameState.location.ISLAND || mc.currentScreen != null) {
            lastX = 10000;
            lastZ = 10000;
            lastY = 10000;
            stuck = false;
            cooldown.reset();
            return;
        }
        if(!MacroHandler.isMacroing)
            return;
        if (cooldown.passed()) {
            Block blockIn = BlockUtils.getRelativeBlock(0, 0, 0);
            stuck = (!blockIn.equals(Blocks.end_portal_frame) && !BlockUtils.isWalkable(blockIn)) || (Math.abs(mc.thePlayer.posX - lastX) < 1 && Math.abs(mc.thePlayer.posZ - lastZ) < 1 && Math.abs(mc.thePlayer.posY - lastY) < 1);
            lastX = mc.thePlayer.posX;
            lastZ = mc.thePlayer.posZ;
            lastY = mc.thePlayer.posY;
            cooldown.schedule(3000);
        }
    }
}
