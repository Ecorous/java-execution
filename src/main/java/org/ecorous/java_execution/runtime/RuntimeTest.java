package org.ecorous.java_execution.runtime;

import net.minecraft.client.MinecraftClient;
import net.minecraft.server.integrated.IntegratedServer;
import net.minecraft.text.Text;

public class RuntimeTest implements Runtime {
	public void run() {
		server.sendSystemMessage(Text.of("uwu :3"));
	}
}
