package org.ecorous.java_execution.mixin;

import net.minecraft.client.gui.screen.TitleScreen;
import org.ecorous.java_execution.JavaExecution;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TitleScreen.class)
public class TitleScreenMixin {
	@Inject(method = "init", at = @At("TAIL"))
	public void java_execution$onInit(CallbackInfo ci) {
		JavaExecution.INSTANCE.getLOGGER().info("This line is printed by an example mod mixin!");
	}
}
