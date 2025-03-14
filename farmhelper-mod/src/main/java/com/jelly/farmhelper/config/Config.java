package com.jelly.farmhelper.config;

import cc.polyfrost.oneconfig.config.annotations.Number;
import cc.polyfrost.oneconfig.config.annotations.*;
import cc.polyfrost.oneconfig.config.core.OneKeyBind;
import cc.polyfrost.oneconfig.config.data.InfoType;
import cc.polyfrost.oneconfig.config.data.Mod;
import cc.polyfrost.oneconfig.config.data.ModType;
import cc.polyfrost.oneconfig.config.data.OptionSize;
import com.jelly.farmhelper.FarmHelper;
import com.jelly.farmhelper.config.structs.Rewarp;
import com.jelly.farmhelper.features.Autosell;
import com.jelly.farmhelper.features.PetSwapper;
import com.jelly.farmhelper.hud.ProfitCalculatorHUD;
import com.jelly.farmhelper.hud.StatusHUD;
import com.jelly.farmhelper.macros.MacroHandler;
import com.jelly.farmhelper.network.DiscordWebhook;
import com.jelly.farmhelper.utils.BlockUtils;
import com.jelly.farmhelper.utils.LogUtils;
import com.jelly.farmhelper.utils.Utils;
import com.jelly.farmhelper.world.GameState;
import net.minecraft.client.Minecraft;
import net.minecraft.util.BlockPos;
import org.lwjgl.input.Keyboard;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

// THIS IS RAT - CatalizCS
public class Config extends cc.polyfrost.oneconfig.config.Config {
	private transient static final Minecraft mc = Minecraft.getMinecraft();
	private transient static final String GENERAL = "General";
	private transient static final String MISCELLANEOUS = "Miscellaneous";
	private transient static final String FAILSAFE = "Fail Safes";
	private transient static final String VISITORS_MACRO = "Visitors Macro";
	private transient static final String DELAYS = "Delays";
	private transient static final String WEBHOOK = "Webhook";
	private transient static final String DEBUG = "Debug";
	private transient static final String EXPERIMENTAL = "Experimental";

	private transient static final File configRewarpFile = new File("farmhelper_rewarp.json");

	public static List<Rewarp> rewarpList = new ArrayList<>();

	public String proxyAddress = "";
	public String proxyUsername = "";
	public String proxyPassword = "";
	public int proxyType = 0;
	public boolean connectAtStartup = false;


	// START GENERAL
     public enum VerticalMacroEnum {
		NORMAL_TYPE
     }

     public enum SMacroEnum {
         NORMAL_TYPE,
		 PUMPKIN_MELON,
         SUGAR_CANE,
		 CACTUS,
		 COCOA_BEANS,
		 COCOA_BEANS_RG,
		 MUSHROOM,
		 MUSHROOM_ROTATE
     }

	public enum CropEnum {
	 	CARROT,
		NETHER_WART,
		POTATO,
		WHEAT,
		SUGAR_CANE,
		MELON,
		PUMPKIN,
		CACTUS,
		COCOA_BEANS,
		MUSHROOM,
	}

	public static void addRewarp(Rewarp rewarp) {
		rewarpList.add(rewarp);
		LogUtils.scriptLog("Added rewarp: " + rewarp.toString());
		saveRewarpConfig();
	}

	public static void removeRewarp(Rewarp rewarp) {
		rewarpList.remove(rewarp);
		LogUtils.scriptLog("Removed the closest rewarp: " + rewarp.toString());
		saveRewarpConfig();
	}

	public static void removeAllRewarps() {
		rewarpList.clear();
		LogUtils.scriptLog("Removed all saved rewarp positions");
		saveRewarpConfig();
	}

	public static void saveRewarpConfig() {
		try {
			Files.write(configRewarpFile.toPath(), FarmHelper.gson.toJson(rewarpList).getBytes(StandardCharsets.UTF_8));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@DualOption(
		name = "Macro Type", category = GENERAL, subcategory = "Macro",
		description = "The macro to use for farming",
		left = "Vertical",
		right = "S Shape",
		size = OptionSize.DUAL
	)
	public boolean macroType = false;
	@Dropdown(
		name = "Vertical Farm", category = GENERAL, subcategory = "Macro",
		description = "Vertical farm type",
		options = {
			"Wheat/Potato/Carrot/Nether Wart" // 0
		}
	)
    public int VerticalMacroType = 0;
	@Dropdown(
		name = "S Shape Farm", category = GENERAL, subcategory = "Macro",
		description = "S Shape farm type",
		options = {
			"Wheat/Potato/Carrot/Nether Wart", // 0
			"Pumpkin/Melon", // 1
			"Sugar Cane", // 2
			"Cactus", // 3
			"Cocoa Beans", // 4
			"Cocoa Beans (RoseGold version)", // 5
			"Mushroom (45°)", // 6
			"Mushroom (30° with rotations)", // 7
		}
	)
    public int SShapeMacroType = 0;
	@Switch(
		name = "Auto Ungrab Mouse", category = GENERAL, subcategory = "Macro",
		description = "Automatically unfocuses your mouse, so you can safely alt-tab"
	)
	public boolean autoUngrabMouse = true;

	@Switch(
		name = "Custom pitch", category = GENERAL, subcategory = "Macro",
		description = "Set pitch to custom level after starting the macro"
	)
	public boolean customPitch = false;

	@Number(
		name = "Custom pitch level", category = GENERAL, subcategory = "Macro",
		description = "Set custom pitch level after starting the macro",
		min = -90, max = 90
	)
	public int customPitchLevel = 0;

	@Switch(
	name = "Go back with Ladders", category = GENERAL, subcategory = "Macro",
	description = "Select this if you're using ladder design"
	)
	public boolean ladderDesign = false;
	@Switch(
		name = "Rotate After Warped", category = GENERAL, subcategory = "Macro",
		description = "Rotates the player after re-warping"
	)
	public boolean rotateAfterWarped = false;
	@Switch(
		name = "Rotate After Drop", category = GENERAL, subcategory = "Macro",
		description = "Rotates after the player falls down"
	)
	public boolean rotateAfterDrop = false;

	@KeyBind(
		name = "Toggle Farm Helper", category = GENERAL, subcategory = "Keybinds",
		description = "Toggles the macro on/off"
	)
	public OneKeyBind toggleMacro = new OneKeyBind(Keyboard.KEY_GRAVE);

	@KeyBind(
			name = "Open GUI", category = GENERAL, subcategory = "Keybinds",
			description = "Open's the main FarmHelper GUI"
	)

	public OneKeyBind openGuiKeybind = new OneKeyBind(Keyboard.KEY_F);

	@Slider(
			name = "Minimum time between changing rows", category = GENERAL, subcategory = "Delays",
			description = "The minimum time to wait before changing rows (in milliseconds)", min = 300, max = 2000
	)
	public float minTimeBetweenChangingRows = 300f;

	@Slider(
			name = "Maximum time between changing rows", category = GENERAL, subcategory = "Delays",
			description = "The maximum time to wait before changing rows (in milliseconds)", min = 300, max = 2000
	)
	public float maxTimeBetweenChangingRows = 700f;

	@Switch(
		name = "Highlight rewarp points", category = GENERAL, subcategory = "Rewarp",
		description = "Highlights all rewarp points you have added",
		size = OptionSize.DUAL
	)
	public boolean highlightRewarp = true;
	@Button(
		name = "Add Rewarp", category = GENERAL, subcategory = "Rewarp",
		description = "Adds a rewarp position",
		text = "Add Rewarp"
	)
	Runnable _addRewarp = () -> {
		BlockPos pos = BlockUtils.getRelativeBlockPos(0, 0, 0);
		rewarpList.add(new Rewarp(pos.getX(), pos.getY(), pos.getZ()));
		save();
		LogUtils.scriptLog("Rewarp position has been added. BlockPos: " + pos);
	};
	@Info(
		text = "Don't forget to add rewarp points!",
		type = InfoType.WARNING,
		category = GENERAL,
		subcategory = "Rewarp"
	)
	public static boolean ignored;
	@Button(
		name = "Remove Rewarp", category = GENERAL, subcategory = "Rewarp",
		description = "Removes a rewarp position",
		text = "Remove Rewarp"
	)
	Runnable _removeRewarp = () -> {
		Rewarp closest = null;
		if (rewarpList.size() == 0) {
			LogUtils.scriptLog("No rewarp locations set");
			return;
		}
		double closestDistance = Double.MAX_VALUE;
		for (Rewarp rewarp : rewarpList) {
			double distance = rewarp.getDistance(BlockUtils.getRelativeBlockPos(0, 0, 0));
			if (distance < closestDistance) {
				closest = rewarp;
				closestDistance = distance;
			}
		}
		if (closest != null) {
			removeRewarp(closest);
		}
	};
	@Button(
		name = "Remove All Rewarps", category = GENERAL, subcategory = "Rewarp",
		description = "Removes all rewarp positions",
		text = "Remove All Rewarps"
	)
	Runnable _removeAllRewarps = () -> {
		removeAllRewarps();
		LogUtils.scriptLog("All rewarp positions has been removed");
	};

	// END GENERAL

	// START MISCELLANEOUS
	@Switch(
		name = "Xray Mode", category = MISCELLANEOUS, subcategory = "Miscellaneous",
		description = "Hides blocks to reduce resource usage"
	)
	public boolean xrayMode = false;
	@Switch(
		name = "Mute The Game", category = MISCELLANEOUS, subcategory = "Miscellaneous",
		description = "Mutes the game while farming"
	)
	public boolean muteTheGame = false;
	@Switch(
		name = "Auto GodPot", category = MISCELLANEOUS, subcategory = "Miscellaneous",
		description = "Automatically purchases and consumes a God Pot"
	)
	public boolean autoGodPot = false;
	@Switch(
		name = "Auto Cookie", category = MISCELLANEOUS, subcategory = "Miscellaneous",
		description = "Automatically purchases and consumes a booster cookie"
	)
	public boolean autoCookie = false;
	@Switch(
		name = "Fast Change Direction Cane", category = MISCELLANEOUS, subcategory = "Miscellaneous",
		description = "Fast change direction cane"
	)
	public boolean fastChangeDirectionCane = false;
	@Switch(
		name = "Hold left click when changing row", category = MISCELLANEOUS, subcategory = "Miscellaneous",
		description = "Hold left click when change row"
	)
	public boolean holdLeftClickWhenChangingRow = true;
	@Switch(
		name = "Count RNG to $/Hr in Profit Calculator", category = MISCELLANEOUS, subcategory = "Miscellaneous",
		description = "Count RNG to $/Hr"
	)
	public boolean countRNGToProfitCalc = false;

	@Switch(
		name = "Debug Mode", category = MISCELLANEOUS, subcategory = "Miscellaneous",
		description = "Prints to chat what the bot is currently executing. Useful if you are having issues."
	)
	public boolean debugMode = false;

	@Switch(
		name = "Enable Auto Sell", category = MISCELLANEOUS, subcategory = "Auto Sell",
		description = "Enables auto sell"
	)
	public boolean enableAutoSell = false;
	@Switch(
		name = "Sell To NPC" , category = MISCELLANEOUS, subcategory = "Auto Sell",
		description = "Automatically sells crops to NPC or Bazaar"
	)
	public boolean sellToNPC = false;
	@Number(
		name = "Inventory Full Time", category = MISCELLANEOUS, subcategory = "Auto Sell",
		description = "The time to wait for inventory to be full (in seconds)",
		min = 1, max = 20
	)
	public int inventoryFullTime = 6;
	@Number(
		name = "Inventory Full Ratio", category = MISCELLANEOUS, subcategory = "Auto Sell",
		description = "The ratio to wait for inventory to be full (in percentage)",
		min = 1, max = 100
	)
	public int inventoryFullRatio = 65;
	@Button(
		name = "Sell Inventory Now", category = MISCELLANEOUS, subcategory = "Auto Sell",
		description = "Sells crops in your inventory",
		text = "Sell Inventory Now"
	)
	Runnable autoSellFunction = () -> {
		mc.thePlayer.closeScreen();
		Autosell.enable();
	};
	@Switch(
			name = "Swap Pet", category = MISCELLANEOUS, subcategory = "Miscellaneous",
			description = "Swaps pet to the selected pet during Jacob's contest. Selects the first one from the pet list."
	)
	public boolean enablePetSwapper = false;
	@Slider(
			name = "Pet Swap Delay", category = MISCELLANEOUS, subcategory = "Miscellaneous",
			description = "The delay between clicking GUI during swapping the pet (in milliseconds)",
			min = 200, max = 3000
	)
	public int petSwapperDelay = 1000;
	@Text(
			name = "Pet Name", placeholder = "Mushroom Cow", secure = false, multiline = false,
			category = MISCELLANEOUS, subcategory = "Miscellaneous"
	)
	public String petSwapperName = null;
	@Switch(
		name = "Increase Cocoa Hitboxes", category = MISCELLANEOUS, subcategory = "Bigger Hitboxes",
		description = "Allows you to farm cocoa beans more efficient on higher speeds by making the hitboxes bigger"
	)
	public boolean increasedCocoaBeans = true;
	@Switch(
		name = "Increase Crop Hitboxes", category = MISCELLANEOUS, subcategory = "Bigger Hitboxes",
		description = "Allows you to farm crops more efficient by making the hitboxes bigger"
	)
	public boolean increasedCrops = true;
	@Switch(
		name = "Increase Nether Wart Hitboxes", category = MISCELLANEOUS, subcategory = "Bigger Hitboxes",
		description = "Allows you to farm nether warts more efficient on higher speeds by making the hitboxes bigger"
	)
	public boolean increasedNetherWarts = true;

	// END MISCELLANEOUS

	// START DELAYS

	@Slider(
		name = "Stop Script Delay Time", category = DELAYS, subcategory = "Delays",
		description = "The time to wait before stopping the script (in seconds)",
		min = 1, max = 10
	)
	public float delayedStopScriptTime = 3f;
	@Slider(
		name = "Stop Script Delay Random Time", category = DELAYS, subcategory = "Delays",
		description = "The maximum random time added to the delay time before stopping the script (in seconds)",
		min = 1, max = 5
	)
	public float delayedStopScriptTimeRandomness = 1f;
	@Slider(
		name = "Rotation Time", category = DELAYS, subcategory = "Delays",
		description = "The time it takes to rotate the player (in seconds)",
		min = 0.2f, max = 10
	)
	public float rotationTime = 0.4f;
	@Slider(
		name = "Rotation Random Time", category = DELAYS, subcategory = "Delays",
		description = "The maximum random time added to the delay time it takes to rotate the player (in seconds)",
		min = 0.2f, max = 10
	)
	public float rotationTimeRandomness = 0.2f;

	// END DELAYS

	// START VISITORS_MACRO

	@Switch(
		name = "Enable visitors macro", category = VISITORS_MACRO, subcategory = "Visitors Macro",
		description = "Enables visitors macro"
	)
	public boolean visitorsMacro = false;
	@Switch(
		name = "Pause When in Contests", category = VISITORS_MACRO, subcategory = "Visitors Macro",
		description = "Pauses the visitors macro when in contests"
	)
	public boolean pauseWhenInContests = true;
	@Switch(
		name = "Only Accept Profitable Visitors", category = VISITORS_MACRO, subcategory = "Visitors Macro",
		description = "Only accepts visitors that are profitable"
	)
	public boolean onlyAcceptProfitableVisitors = false;
	@Number(
		name = "Visitors Macro Coins Threshold", category = VISITORS_MACRO, subcategory = "Visitors Macro",
		description = "The maximum amount of coins to be considered profitable",
		min = 1, max = 20
	)
	public int visitorsMacroCoinsThreshold = 1;


	@Button(
		name = "Set Visitor's Desk", category = VISITORS_MACRO, subcategory = "Visitor's Desk",
		description = "Sets the visitor's desk position",
		text = "Set Visitor's Desk"
	)
	Runnable setVisitorDesk = () -> {
		BlockPos pos = BlockUtils.getRelativeBlockPos(0, 0, 0);
		visitorsDeskPosX = pos.getX();
		visitorsDeskPosY = pos.getY();
		visitorsDeskPosZ = pos.getZ();
		save();
		LogUtils.scriptLog("Visitors desk position has been set. BlockPos: " + pos);
	};

	@Button(
		name = "Reset Visitor's Desk", category = VISITORS_MACRO, subcategory = "Visitor's Desk",
		description = "Resets the visitor's desk position",
		text = "Reset Visitor's Desk"
	)
	Runnable resetVisitorDesk = () -> {
		visitorsDeskPosX = 0;
		visitorsDeskPosY = 0;
		visitorsDeskPosZ = 0;
		save();
		LogUtils.scriptLog("Visitors desk position has been reset");
	};
	@Number(
		name = "Visitors Desk X", category = VISITORS_MACRO, subcategory = "Visitor's Desk",
		description = "Visitors desk X coordinate",
		min = -30000000, max = 30000000
	)
	public int visitorsDeskPosX = 0;
	@Number(
		name = "Visitors Desk Y", category = VISITORS_MACRO, subcategory = "Visitor's Desk",
		description = "Visitors desk Y coordinate",
		min = -30000000, max = 30000000
	)
	public int visitorsDeskPosY = 0;
	@Number(
		name = "Visitors Desk Z", category = VISITORS_MACRO, subcategory = "Visitor's Desk",
		description = "Visitors desk Z coordinate",
		min = -30000000, max = 30000000
	)
	public int visitorsDeskPosZ = 0;
	@KeyBind(
		name = "Visitors Desk Keybind", category = VISITORS_MACRO, subcategory = "Visitor's Desk",
		description = "Visitors desk keybind"
	)
	public OneKeyBind visitorsDeskKeybind = new OneKeyBind(0);

	// END VISITORS_MACRO

	// START WEBHOOK

	@Switch(
        name = "Enable webhook messages", category = WEBHOOK, subcategory = "Discord Webhook",
        description = "Allows to send messages via Discord webhooks"
	)
	public boolean enableWebHook = false;
	@Switch(
		name = "Send Logs", category = WEBHOOK, subcategory = "Discord Webhook",
		description = "Sends all messages about the macro, spams a lot of messages"
	)
	public boolean sendLogs = false;
	@Switch(
		name = "Send Status Updates", category = WEBHOOK, subcategory = "Discord Webhook",
		description = "Sends messages about the macro, such as when it started, stopped, etc"
	)
	public boolean sendStatusUpdates = false;
	@Number(
		name = "Status Update Interval (in minutes)", category = WEBHOOK, subcategory = "Discord Webhook",
		description = "The interval between sending messages about status updates",
		min = 1, max = 60
	)
	public int statusUpdateInterval = 5;
	@Text(
		name = "WebHook URL", category = WEBHOOK, subcategory = "Discord Webhook",
		description = "The URL to use for the webhook",
		placeholder = "https://discord.com/api/webhooks/...",
		secure = true, multiline = false
	)
	public String webHookURL = "";
	@Button(
		name = "Apply WebHook URL", category = WEBHOOK, subcategory = "Discord Webhook",
		description = "Applies the webhook URL",
		text = "Apply WebHook URL"
	)
	Runnable _applyWebhook = () -> {
		if (webHookURL.isEmpty()) {
			LogUtils.scriptLog("Webhook URL is empty");
			return;
		}
		if (!webHookURL.startsWith("https://discord.com/api/webhooks/")) {
			LogUtils.scriptLog("Invalid webhook URL");
			return;
		}
		GameState.webhook = new DiscordWebhook(FarmHelper.config.webHookURL);
		GameState.webhook.setUsername("Jelly - Farm Helper");
		GameState.webhook.setAvatarUrl("https://media.discordapp.net/attachments/946792534544379924/965437127594749972/Jelly.png");
		LogUtils.scriptLog("Webhook URL has been applied");
		save();
	};

	@Switch(
		name = "Enable Remote Control (BROKEN)", category = WEBHOOK, subcategory = "Remote Control",
		description = "Enables remote control via Discord messages"
	)
	public boolean enableRemoteControl = false;
	@Info(
		text = "You don't need to configure this. It's for advanced users only.",
		type = InfoType.INFO,
		category = WEBHOOK,
		subcategory = "Remote Control"
	)
	public static boolean ignored2;
	@Text(
		name = "WebSocket IP (DANGEROUS)", category = WEBHOOK, subcategory = "Remote Control",
		description = "The IP to use for the WebSocket server",
		secure = false, multiline = false

	)
	public String webSocketIP = "";
	@Text(
		name = "WebSocket Password", category = WEBHOOK, subcategory = "Remote Control",
		description = "The password to use for the WebSocket server",
		secure = true, multiline = false
	)
	public String webSocketPassword = "";

	// END WEBHOOK

	// START FAILSAFE

	@Switch(
		name = "Pop-up Notification", category = FAILSAFE, subcategory = "Miscellaneous",
		description = "Enable pop-up notification"
	)
	public boolean popUpNotification = true;
	@Switch(
		name = "Fake Movements", category = FAILSAFE, subcategory = "Miscellaneous",
		description = "Tries to act like a real player by moving around"
	)
	public boolean fakeMovements = true;
	@Switch(
		name = "Auto alt-tab when failsafe triggered", category = FAILSAFE, subcategory = "Miscellaneous",
		description = "Automatically alt-tabs to the game when the dark times come"
	)
	public boolean autoAltTab = true;
	@Switch(
		name = "Check Desync", category = FAILSAFE, subcategory = "Miscellaneous",
		description = "If client desynchronization is detected, it activates a failsafe. Turn this off if the network is weak or if it happens frequently."
	)
	public boolean checkDesync = true;
	@Switch(
		name = "Auto TP On World Change", category = FAILSAFE, subcategory = "Miscellaneous",
		description = "Automatically warps back to garden on server reboot, server update, etc"
	)
	public boolean autoTPOnWorldChange = true;
	@Slider(
		name = "Rotation Check Sensitivity", category = FAILSAFE, subcategory = "Miscellaneous",
		description = "The sensitivity of rotation check, the lower the sensitivity, the more accurate the check is, but it will also increase the chance of getting false positives",
		min = 1, max = 10
	)
	public float rotationCheckSensitivity = 2;
	@Switch(
			name = "Enable Failsafe Trigger Sound", category = FAILSAFE, subcategory = "Failsafe Trigger Sound", size = OptionSize.DUAL,
			description = "Makes a sound when a failsafe has been triggered"
	)
	public boolean enableFailsafeSound = true;
	@Dropdown(
			name = "Sound", category = FAILSAFE, subcategory = "Failsafe Trigger Sound",
			description = "The sound to play when a failsafe has been triggered",
			options = {
					"Ping", // 0
					"Voice", // 1
					"Metal Pipe", // 2
					"AAAAAAAAAA", // 3
					"Loud Buzz" // 4
			}
	)
	public int failsafeSoundSelected = 0;
	@Button(
			name = "", category = FAILSAFE, subcategory = "Failsafe Trigger Sound",
			description = "Plays the selected sound",
			text = "Play"
	)
	Runnable _playFailsafeSoundButton = () -> {
		if (failsafeSoundSelected == 0)
			Utils.playPingFailsafeSound();
		else
			Utils.playFailsafeSound(failsafeSoundSelected);
	};
	@Slider(
			name = "Failsafe Sound Volume (in dB)", category = FAILSAFE, subcategory = "Failsafe Trigger Sound",
			description = "The volume of the failsafe sound",
			min = -6f, max = 6f
	)
	public float failsafeSoundVolume = 0.0f;
	@Switch(
			name = "Enable Scheduler", category = FAILSAFE, subcategory = "Scheduler", size = OptionSize.DUAL,
			description = "Farms for X amount of minutes then takes a break for X amount of minutes"
	)
	public boolean enableScheduler = false;
	@Slider(
		name = "Farming time (in minutes)", category = FAILSAFE, subcategory = "Scheduler",
		description = "How long to farm",
		min = 1, max = 300, step = 1
	)
	public int schedulerFarmingTime = 30;
	@Slider(
		name = "Farming time randomness (in minutes)", category = FAILSAFE, subcategory = "Scheduler",
		description = "How much randomness to add to the farming time",
		min = 0, max = 15, step = 1
	)
	public int schedulerFarmingTimeRandomness = 0;
	@Slider(
		name = "Break time (in minutes)", category = FAILSAFE, subcategory = "Scheduler",
		description = "How long to take a break",
		min = 1, max = 120, step = 1
	)
	public int schedulerBreakTime = 5;
	@Slider(
		name = "Break time randomness (in minutes)", category = FAILSAFE, subcategory = "Scheduler",
		description = "How much randomness to add to the break time",
		min = 0, max = 15, step = 1
	)
	public int schedulerBreakTimeRandomness = 0;

	@Switch(
		name = "Enable Restart After FailSafe", category = FAILSAFE, subcategory = "Restart After FailSafe",
		description = "Restarts the macro after a while when a failsafe has been triggered"
	)
	public boolean enableRestartAfterFailSafe = false;
	@Switch(
		name = "Leave after failsafe triggered", category = FAILSAFE, subcategory = "Restart After FailSafe",
		description = "Leaves the server after a failsafe has been triggered"
	)
	public boolean leaveAfterFailSafe = false;
	@Slider(
		name = "Restart Delay", category = FAILSAFE, subcategory = "Restart After FailSafe",
		description = "The delay to restart after failsafe (in seconds)",
		min = 0, max = 600
	)
	public int restartAfterFailSafeDelay = 30;
	@Switch(
		name = "Enable Auto Set Spawn", category = FAILSAFE, subcategory = "Auto Set Spawn",
		description = "Enables auto set spawn"
	)
	public boolean enableAutoSetSpawn = false;
	@Switch(
		name = "Set Spawn Before Evacuation", category = FAILSAFE, subcategory = "Auto Set Spawn",
		description = "Set spawn before evacuate"
	)
	public boolean setSpawnBeforeEvacuate = false;
	@Number(
		name = "Set Spawn min delay", category = FAILSAFE, subcategory = "Auto Set Spawn",
		description = "The minimum delay between setting a new spawn (in seconds)",
		min = 1, max = 120
	)
	public int autoSetSpawnMinDelay = 15;
	@Number(
		name = "Set Spawn max delay", category = FAILSAFE, subcategory = "Auto Set Spawn",
		description = "The maximum delay between setting a new (in seconds)",
		min = 1, max = 120
	)
	public int autoSetSpawnMaxDelay = 25;

	@Switch(
		name = "Enable Banwave Checker", category = FAILSAFE, subcategory = "Banwave Checker",
		description = "Checks for banwave and shows you the number of players banned in the last 15 minutes"
	)
	public boolean banwaveCheckerEnabled = true;
	@Switch(
		name = "Leave during banwave", category = FAILSAFE, subcategory = "Banwave Checker",
		description = "Automatically disconnects from the server when banwave detected"
	)
	public boolean enableLeaveOnBanwave = false;
	@Slider(
		name = "Banwave Disconnect Threshold", category = FAILSAFE, subcategory = "Banwave Checker",
		description = "The threshold to disconnect from the server on banwave",
		min = 1, max = 100
	)
	public int banwaveThreshold = 50;
	@Number(
		name = "Delay Before Reconnecting", category = FAILSAFE, subcategory = "Banwave Checker",
		description = "The delay before reconnecting after leaving on banwave (in seconds)",
		min = 1, max = 20
	)
	public int delayBeforeReconnecting = 5;

	@Number(
		name = "Rotation Acting times", category = FAILSAFE, subcategory = "Custom Failsafe times",
		description = "When the rotation failsafe is triggered, the number of times to act before leaving (recommend from 5 to 8)",
		min = 4, max = 20,
		step = 1
	)
	public int rotationActingTimes = 6;

	@Number(
		name = "Bedrock Acting times", category = FAILSAFE, subcategory = "Custom Failsafe times",
		description = "When the Bedrock failsafe is triggered, the number of times to act before leaving (recommend from 8 to 10)",
		min = 4, max = 20,
		step = 1
	)
	public int bedrockActingTimes = 6;

	@Text(
		name = "Rotation messages", category = FAILSAFE, subcategory = "Custom Failsafe Messages",
		description = "The messages to send to the chat when a rotation failsafe has been triggered (use '|' to split messages)",
		placeholder = "Leave empty to use a random message",
		multiline = true
	)
	public static String customRotationMessages = "";

	@Text(
		name = "Bedrock messages", category = FAILSAFE, subcategory = "Custom Failsafe Messages",
		description = "The messages to send to the chat when a bedrock failsafe has been triggered (use '|' to split messages)",
		placeholder = "Leave empty to use a random message",
		multiline = true
	)
	public static String customBedrockMessages = "";


	// END FAILSAFE

	// START JACOB

	@Switch(
		name = "Enable Jacob Failsafes", category = FAILSAFE, subcategory = "Jacob",
		description = "Stops farming once a crop threshold has been met"
	)
	public boolean enableJacobFailsafes = true;
	@Slider(
		name = "Nether Wart Cap", category = FAILSAFE, subcategory = "Jacob",
		description = "The nether wart cap",
		min = 10000, max = 2000000, step = 10000
	)
	public int jacobNetherWartCap = 800000;
	@Slider(
		name = "Potato Cap", category = FAILSAFE, subcategory = "Jacob",
		description = "The potato cap",
		min = 10000, max = 2000000, step = 10000
	)
	public int jacobPotatoCap = 830000;
	@Slider(
		name = "Carrot Cap", category = FAILSAFE, subcategory = "Jacob",
		description = "The carrot cap",
		min = 10000, max = 2000000, step = 10000
	)
	public int jacobCarrotCap = 860000;
	@Slider(
		name = "Wheat Cap", category = FAILSAFE, subcategory = "Jacob",
		description = "The wheat cap",
		min = 10000, max = 2000000
	)
	public int jacobWheatCap = 265000;
	@Slider(
		name = "Sugar Cane Cap", category = FAILSAFE, subcategory = "Jacob",
		description = "The sugar cane cap",
		min = 10000, max = 2000000, step = 10000
	)
	public int jacobSugarCaneCap = 575000;
	@Slider(
		name = "Mushroom Cap", category = FAILSAFE, subcategory = "Jacob",
		description = "The mushroom cap",
		min = 10000, max = 2000000, step = 10000
	)
	public int jacobMushroomCap = 250000;
	@Slider(
		name = "Melon Cap", category = FAILSAFE, subcategory = "Jacob",
		description = "The melon cap",
		min = 10000, max = 2000000, step = 10000
	)
	public int jacobMelonCap = 1234000;
	@Slider(
			name = "Pumpkin Cap", category = FAILSAFE, subcategory = "Jacob",
			description = "The pumpkin cap",
			min = 10000, max = 2000000, step = 10000
	)
	public int jacobPumpkinCap = 240000;
	@Slider(
			name = "Cocoa Beans Cap", category = FAILSAFE, subcategory = "Jacob",
			description = "The cocoa beans cap",
			min = 10000, max = 2000000, step = 10000
	)
	public int jacobCocoaBeansCap = 725000;
	@Slider(
			name = "Cactus Beans Cap", category = FAILSAFE, subcategory = "Jacob",
			description = "The cactus cap",
			min = 10000, max = 2000000, step = 10000
	)
	public int jacobCactusCap = 470000;

	// END JACOB

	// START DEBUG

//	@KeyBind(
//			name = "Debug Keybind", category = DEBUG, subcategory = "Debug"
//	)
//	public OneKeyBind debugKeybind = new OneKeyBind(Keyboard.KEY_H);
//
//	@KeyBind(
//			name = "Debug Keybind 2", category = DEBUG, subcategory = "Debug"
//	)
//	public OneKeyBind debugKeybind2 = new OneKeyBind(Keyboard.KEY_J);

	@HUD(
		name = "Farm Helper Status", category = DEBUG, subcategory = "HUD"
	)
	public StatusHUD statusHUD = new StatusHUD();
	@HUD(
		name = "Farm Helper Profit Calculator", category = DEBUG, subcategory = "HUD"
	)
	public ProfitCalculatorHUD profitHUD = new ProfitCalculatorHUD();

	@Number(
		name = "SpawnPos X", category = DEBUG, subcategory = "SpawnPos",
		description = "The X coordinate of the spawn",
		min = -30000000, max = 30000000

	)
	public int spawnPosX = 0;
	@Number(
		name = "SpawnPos Y", category = DEBUG, subcategory = "SpawnPos",
		description = "The Y coordinate of the spawn",
		min = -30000000, max = 30000000
	)
	public int spawnPosY = 0;
	@Number(
		name = "SpawnPos Z", category = DEBUG, subcategory = "SpawnPos",
		description = "The Z coordinate of the spawn",
		min = -30000000, max = 30000000,
		size = OptionSize.DUAL
	)
	public int spawnPosZ = 0;

	@Switch(name = "Is Spawnpoint set", category = DEBUG, subcategory = "SpawnPos")
	public boolean isSpawnpointSet = false;

	@Button(
		name = "Set SpawnPos", category = DEBUG, subcategory = "SpawnPos",
		description = "Sets the spawn position to your current position",
		text = "Set SpawnPos"
	)
	Runnable _setSpawnPos = () -> {
		BlockPos pos = BlockUtils.getRelativeBlockPos(0, 0, 0);
		spawnPosX = pos.getX();
		spawnPosY = pos.getY() + 1;
		spawnPosZ = pos.getZ();
		isSpawnpointSet = true;
		save();
		LogUtils.scriptLog("Spawn position has been set to " + pos.getX() + ", " + pos.getY() + ", " + pos.getZ());
	};
	@Button(
		name = "Reset SpawnPos", category = DEBUG, subcategory = "SpawnPos",
		description = "Resets the spawn position",
		text = "Reset SpawnPos"
	)
	Runnable _resetSpawnPos = () -> {
		spawnPosX = 0;
		spawnPosY = 0;
		spawnPosZ = 0;
		isSpawnpointSet = false;
		save();
		LogUtils.scriptLog("Spawn position has been reset");
	};

	// END DEBUG

	// START EXPERIMENTAL
	@Switch(
			name = "Enable Fast Break (DANGEROUS)", category = EXPERIMENTAL, subcategory = "Experimental",
			description = "Fast break is very risky and most likely will result in a ban"
	)
	public boolean fastBreak = false;
	@Info(
			text = "Fastbreak will most likely ban you. Proceed with caution.",
			type = InfoType.ERROR,
			category = EXPERIMENTAL,
			subcategory = "Experimental"
	)
	public static boolean ignored3;
	@Slider(
			name = "Fast Break Speed", category = EXPERIMENTAL, subcategory = "Experimental",
			description = "Fast break speed",
			min = 1, max = 3
	)
	public int fastBreakSpeed = 1;

	@Dropdown(
			name = "Alt-tab mode", category = EXPERIMENTAL, subcategory = "Experimental",
			description = "The mode to use when alt-tabbing. Using keys is more reliable, but it's slower. Using WINAPI is faster, but it's less reliable and Windows only. It also maximizes the game window.",
			options = {"Using keys", "Using WINAPI (Windows only)"}
	)
	public int autoAltTabMode = 0;

	// END EXPERIMENTAL

	public Config() {
		super(new Mod("Farm Helper", ModType.HYPIXEL), "/farmhelper/config.json");
		initialize();

		this.addDependency("macroType", "Macro Type", () -> !MacroHandler.isMacroing);
		this.addDependency("VerticalMacroType", "Macro Type", () -> (!this.macroType && !MacroHandler.isMacroing));
		this.addDependency("SShapeMacroType", "Macro Type", () -> (this.macroType && !MacroHandler.isMacroing));

		this.addDependency("rotateAfterDrop", "Vertical Macro Type", () -> (this.macroType && !MacroHandler.isMacroing));
		this.addDependency("ladderDesign", "S Shape Macro Type", () -> (!this.macroType && !MacroHandler.isMacroing));

		this.addDependency("holdLeftClickWhenChangingRow", "macroType");
		this.addDependency("fastBreakSpeed", "fastBreak");

		this.addDependency("sellToNPC", "enableAutoSell");
		this.addDependency("inventoryFullTime", "enableAutoSell");
		this.addDependency("inventoryFullRatio", "enableAutoSell");

		this.addDependency("petSwapperName", "enablePetSwapper");
		this.addDependency("petSwapperDelay", "enablePetSwapper");

		this.addDependency("failsafeSoundSelected", "enableFailsafeSound");
		this.addDependency("_playFailsafeSoundButton", "enableFailsafeSound");
		this.addDependency("failsafeSoundVolume", "enableFailsafeSound");

		this.addDependency("schedulerFarmingTime", "enableScheduler");
		this.addDependency("schedulerFarmingTimeRandomness", "enableScheduler");
		this.addDependency("schedulerBreakTime", "enableScheduler");
		this.addDependency("schedulerBreakTimeRandomness", "enableScheduler");

		this.addDependency("jacobNetherWartCap", "enableJacobFailsafes");
		this.addDependency("jacobPotatoCap", "enableJacobFailsafes");
		this.addDependency("jacobCarrotCap", "enableJacobFailsafes");
		this.addDependency("jacobWheatCap", "enableJacobFailsafes");
		this.addDependency("jacobSugarCaneCap", "enableJacobFailsafes");
		this.addDependency("jacobMushroomCap", "enableJacobFailsafes");
		this.addDependency("jacobMelonCap", "enableJacobFailsafes");
		this.addDependency("jacobPumpkinCap", "enableJacobFailsafes");
		this.addDependency("jacobCocoaBeansCap", "enableJacobFailsafes");
		this.addDependency("jacobCactusCap", "enableJacobFailsafes");

		this.addDependency("onlyAcceptProfitableVisitors", "visitorsMacro");
		this.addDependency("visitorsMacroCoinsThreshold", "visitorsMacro");
		this.addDependency("pauseWhenInContests", "visitorsMacro");

		this.addDependency("sendLogs", "enableWebHook");
		this.addDependency("sendStatusUpdates", "enableWebHook");
		this.addDependency("statusUpdateInterval", "enableWebHook");
		this.addDependency("webHookURL", "enableWebHook");
		this.addDependency("_applyWebhook", "enableWebHook");

		this.addDependency("webSocketIP", "enableRemoteControl");
		this.addDependency("webSocketPassword", "enableRemoteControl");

		this.addDependency("restartAfterFailSafeDelay", "enableRestartAfterFailSafe");
		this.addDependency("enableLeaveOnBanwave", "banwaveCheckerEnabled");
		this.addDependency("banwaveThreshold", "enableLeaveOnBanwave");
		this.addDependency("delayBeforeReconnecting", "enableLeaveOnBanwave");

		this.addDependency("setSpawnBeforeEvacuate", "enableAutoSetSpawn");
		this.addDependency("autoSetSpawnMinDelay", "enableAutoSetSpawn");
		this.addDependency("autoSetSpawnMaxDelay", "enableAutoSetSpawn");

		this.addDependency("SpawnPosX", "debugMode");
		this.addDependency("SpawnPosY", "debugMode");
		this.addDependency("SpawnPosZ", "debugMode");
		this.addDependency("_setSpawnPos", "debugMode");
		this.addDependency("_resetSpawnPos", "debugMode");
		this.addDependency("isSpawnpointSet", "debugMode");
		this.addDependency("customPitchLevel", "customPitch");
		registerKeyBind(openGuiKeybind, () -> FarmHelper.config.openGui());
//		registerKeyBind(debugKeybind, () -> FarmHelper.petSwapper.startMacro(false));
//		registerKeyBind(debugKeybind2, () -> FarmHelper.petSwapper.startMacro(true));
		save();
	}
}
