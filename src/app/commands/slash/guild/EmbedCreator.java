package app.commands.slash.guild;

import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import java.awt.*;

public class EmbedCreator extends ListenerAdapter {
    net.dv8tion.jda.api.interactions.components.buttons.Button confirma = Button.success("confirmarembed", "Enviar");
    TextChannel canalEmbed = null;
    private static final net.dv8tion.jda.api.EmbedBuilder embed = new net.dv8tion.jda.api.EmbedBuilder();

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (event.getName().equals("criarembed")) {
            OptionMapping titulo = event.getOption("titulo");
            OptionMapping descricao = event.getOption("descricao");
            OptionMapping cor = event.getOption("cor");
            OptionMapping footer = event.getOption("footer");
            OptionMapping imagem = event.getOption("imagem");
            OptionMapping thumbnail = event.getOption("thumb");
            OptionMapping canal = event.getOption("canal");

            String descricaoText = descricao.getAsString().replace("\\n", "\n");

            embed.setDescription(descricaoText);
            if (titulo != null) {
                embed.setTitle(titulo.getAsString());
            }
            if (cor != null) {
                embed.setColor(Color.decode(cor.getAsString()));
            }
            if (footer != null) {
                embed.setFooter(footer.getAsString());
            }
            if (imagem != null) {
                embed.setImage(imagem.getAsAttachment().getUrl());
            }
            if (thumbnail != null) {
                embed.setThumbnail(thumbnail.getAsAttachment().getUrl());
            }

            if (canal != null) {
                canalEmbed = canal.getAsChannel().asTextChannel();
                event.reply("Deseja enviar em " + canal.getAsChannel().getAsMention() + " a seguinte mensagem?").setEmbeds(embed.build()).addActionRow(confirma).setEphemeral(true).queue();
            } else {
                canalEmbed = event.getChannel().asTextChannel();

                event.reply("Deseja enviar a seguinte mensagem?").setEmbeds(embed.build()).setActionRow(confirma).setEphemeral(true).queue();
            }


        }
    }

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        if (event.getButton().getId().equals(confirma.getId())) {
            canalEmbed.sendMessage("").setEmbeds(embed.build()).queue();
            event.deferEdit().queue();
        }
    }
}
