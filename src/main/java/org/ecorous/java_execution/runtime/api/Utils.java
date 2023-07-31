package org.ecorous.java_execution.runtime.api;

import net.minecraft.client.MinecraftClient;
import net.minecraft.server.MinecraftServer;
import net.minecraft.text.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Utils {
	public static Logger LOGGER = LoggerFactory.getLogger("JavaExecution - Runtime Utils");
	public static Text textOf(String string) {
		return Text.of(string);
	}
	public static void sendSystemMessage(Text text) {
		MinecraftClient.getInstance().getServer().sendSystemMessage(text);
	}
}
