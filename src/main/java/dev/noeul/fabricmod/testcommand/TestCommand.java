package dev.noeul.fabricmod.testcommand;

import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;

public class TestCommand implements ModInitializer, CommandRegistrationCallback {
	@Override
	public void onInitialize() {
		CommandRegistrationCallback.EVENT.register(this);
	}

	@Override
	public void register(
			CommandDispatcher<ServerCommandSource> dispatcher,
			CommandRegistryAccess registryAccess,
			CommandManager.RegistrationEnvironment environment
	) {
		dispatcher.register(
				CommandManager.literal("test")
						.requires(source -> source.hasPermissionLevel(CommandManager.field_31841))
						.executes(ctx -> {
							throw new Error("Manually triggered debug crash");
						})
		);
	}
}
