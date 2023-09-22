package app.commands.slash.guild;

import net.dv8tion.jda.api.entities.Icon;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;

import java.io.IOException;

import static app.App.jda;
import static app.statics.Functions.salvaImagem;

public class AddEmoji extends ListenerAdapter {
    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {


        if (event.getName().equals("addemoji")) {
            OptionMapping emoji = event.getOption("emoji");

            String emojiString = emoji.getAsString();
            if (emojiString.startsWith("<:") && emojiString.endsWith(">")) {
                String[] parts = emojiString.split(":");
                if (parts.length == 3) {
                    String id = parts[2].substring(0, parts[2].length() - 1);
                    String emojiName = parts[1];
                    String emojiUrl = "https://cdn.discordapp.com/emojis/" + id + ".png?size=2048";
                    try {
                        event.getGuild().createEmoji(emojiName, Icon.from(salvaImagem(emojiUrl))).queue();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
            if (emojiString.startsWith("<a:") && emojiString.endsWith(">")) {


                String[] parts = emojiString.split(":");
                if (parts.length == 3) {
                    String id = parts[2].substring(0, parts[2].length() - 1);
                    String emojiName = parts[1];
                    String emojiUrl = "https://cdn.discordapp.com/emojis/" + id + ".gif?size=2048";
                    try {
                        event.getGuild().createEmoji(emojiName, Icon.from(salvaImagem(emojiUrl))).queue();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
    }
}

