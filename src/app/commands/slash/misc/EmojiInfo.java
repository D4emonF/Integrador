package app.commands.slash.misc;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import static app.statics.external.ColorPalette.monteCarlo;

public class EmojiInfo extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (event.getName().equals("emojiinfo")) {
            OptionMapping emoji = event.getOption("emoji");

            String emojiString = emoji.getAsString();

            System.out.println(emojiString);

            if (emojiString.startsWith("<:") && emojiString.endsWith(">")) {
                String[] parts = emojiString.split(":");
                if (parts.length == 3) {
                    String id = parts[2].substring(0, parts[2].length() - 1);
                    String emojiName = parts[1];
                    String emojiUrl = "https://cdn.discordapp.com/emojis/" + id + ".png?size=2048";

                    Button botao = Button.link(emojiUrl, "Clique aqui para abrir o emoji no navegador");

                    EmbedBuilder embed = new EmbedBuilder();
                    embed
                            .setTitle(emojiString + " Informações do emoji")
                            .addField("<:cinza_hashtag:1154769641336479814> Nome do emoji", emojiName, true)
                            .addField("<:preto_id:1141064862273917028> Id do emoji", id, true)
                            .addField("<:cinza_chat:1154769616543961189> Menção", "`" + emojiString + "`", false)
                            .setThumbnail(emojiUrl)
                            .setColor(monteCarlo);
                    event.reply("").setEmbeds(embed.build()).setEphemeral(true).setActionRow(botao).queue();
                }
            }
            if (emojiString.startsWith("<a:") && emojiString.endsWith(">")) {


                String[] parts = emojiString.split(":");
                if (parts.length == 3) {
                    String id = parts[2].substring(0, parts[2].length() - 1);
                    String emojiName = parts[1];
                    String emojiUrl = "https://cdn.discordapp.com/emojis/" + id + ".gif?size=2048";


                    Button botao = Button.link(emojiUrl, "Clique aqui para abrir o emoji no navegador");

                    EmbedBuilder embed = new EmbedBuilder();
                    embed
                            .setTitle(emojiString + " Informações do emoji")
                            .addField("<:cinza_hashtag:1154769641336479814> Nome do emoji", emojiName, true)
                            .addField("<:preto_id:1141064862273917028> Id do emoji", id, true)
                            .addField("<:cinza_chat:1154769616543961189> Menção", "`" + emojiString + "`", false)
                            .setThumbnail(emojiUrl)
                            .setColor(monteCarlo);
                    event.reply("").setEmbeds(embed.build()).setEphemeral(true).setActionRow(botao).queue();
                }
            }
        }
    }
}
