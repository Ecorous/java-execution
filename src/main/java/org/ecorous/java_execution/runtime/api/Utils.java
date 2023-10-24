package org.ecorous.java_execution.runtime.api;

import net.minecraft.client.MinecraftClient;
import net.minecraft.server.MinecraftServer;
import net.minecraft.text.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class Utils {
	public static Logger LOGGER = LoggerFactory.getLogger("JavaExecution - Runtime Utils");
	public static Map<String, Object> data = new HashMap<>();
	public static Text textOf(String string) {
		return Text.of(string);
	}
	public static void sendSystemMessage(Text text) {
		MinecraftClient.getInstance().getServer().getPlayerManager().broadcastSystemMessage(text, false);
	}
}
