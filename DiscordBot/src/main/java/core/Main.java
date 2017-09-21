package core;

import commands.CommandManager;
import commands.client.ClientCommandManager;
import core.listeners.*;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import tools.logging.Log;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class Main {
    private static final String TOKEN_FILE_PATH = "data/token.txt";


    public static void main(String[] args) {
        // Getting bot token from argument if available.
        String botToken = args.length > 0 ? args[0] : null;

        // Getting bot token from TOKEN_FILE_PATH otherwise.
        if (botToken == null) {
            File file = new File(TOKEN_FILE_PATH);
            if (file.exists()) {
                try (Scanner scanner = new Scanner(new FileReader(file))) {
                    if (scanner.hasNextLine())
                        botToken = scanner.nextLine();
                } catch (IOException ioe) {
                    Log.getInstance().warn("Unable to find bot token at " + TOKEN_FILE_PATH, "Main");
                    return;
                }
            }
        }

        try {
            // Initializing the bot with all the event listeners needed.
            JDA jda = new JDABuilder(AccountType.BOT)
                    .setToken(botToken)
                    .addEventListener(new GuildAvailableListener())
                    .addEventListener(new GuildUnavailableListener())
                    .addEventListener(new GuildJoinListener())
                    .addEventListener(new GuildLeaveListener())
                    .addEventListener(new GuildMemberJoinListener())
                    .addEventListener(new GuildMemberLeaveListener())
                    .addEventListener(new GuildVoiceJoinListener())
                    .addEventListener(new GuildVoiceLeaveListener())
                    .addEventListener(new MessageReceivedListener())
                    .addEventListener(new ReadyListener())
                    .addEventListener(new DisconnectListener())
                    .addEventListener(new TextChannelCreateListener())
                    .addEventListener(new TextChannelDeleteListener())
                    .addEventListener(new VoiceChannelCreateListener())
                    .addEventListener(new VoiceChannelDeleteListener())
                    .addEventListener(new ShutdownListener())
                    .buildBlocking();


            // Get user input for client commands.
            Scanner scanner = new Scanner(System.in);
            System.out.print("> ");
            String line = scanner.nextLine().toLowerCase();

            while (!line.startsWith(CommandManager.DEFAULT_COMMAND_PREFIX + "exit")) {
                // TODO: load and get prefix
                ClientCommandManager.getInstance().run(line, CommandManager.DEFAULT_COMMAND_PREFIX);

                System.out.println();
                System.out.print("> ");
                line = scanner.nextLine().toLowerCase();
            }

            jda.shutdownNow();
        } catch (Exception ex) {
            Log.getInstance().error(ex, "Main");
        }

        System.out.println("Exiting...");
    }
}
