package org.ecorous.java_execution.runtime;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.server.Main;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.WorldLoader;
import net.minecraft.server.integrated.IntegratedServer;
import net.minecraft.server.network.ServerPlayerInteractionManager;
import net.minecraft.server.world.PlayerChunkWatchingManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import org.quiltmc.loader.api.QuiltLoader;

public interface Runtime {

	public static IntegratedServer server = MinecraftClient.getInstance().getServer();
	void run();
}
