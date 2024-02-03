package dev.noeul.fabricmod.testcommand;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.command.CommandSource;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.Locale;

public class TestCommand implements ModInitializer, ClientModInitializer {
	@Override
	public void onInitialize() {
		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) ->
				TestCommand.register(dispatcher, EnvType.SERVER)
		);
	}

	@Override
	public void onInitializeClient() {
		ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) ->
				TestCommand.register(dispatcher, EnvType.CLIENT)
		);
	}

	private static <S extends CommandSource> void register(CommandDispatcher<S> dispatcher, EnvType envType) {
		dispatcher.register(
				LiteralArgumentBuilder.<S>literal("test")
						.requires(source -> envType == EnvType.CLIENT || source.hasPermissionLevel(CommandManager.field_31840))
						.then(
								LiteralArgumentBuilder.<S>literal(envType.name().toLowerCase(Locale.ROOT))
										.executes(ctx -> TestCommand.test(ctx.getSource(), envType))
						)
		);
	}

	private static int test(CommandSource commandSource, EnvType envType) {
		Text text = Text.literal("This command was defined and has executed on ")
				.append(Text.literal(envType.name()).formatted(Formatting.YELLOW))
				.append(" side");
		if (commandSource instanceof ServerCommandSource source)
			source.sendFeedback(() -> text, false);
		else if (commandSource instanceof FabricClientCommandSource source)
			source.sendFeedback(text);
		return Command.SINGLE_SUCCESS;
	}
}
