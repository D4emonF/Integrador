package app.commands.mannagers;

import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.ArrayList;
import java.util.List;

public class SlashCommand extends ListenerAdapter
{
    @Override
    public void onGuildReady(GuildReadyEvent event) {
        List<CommandData> commandData = new ArrayList<>();

        //EmbedCreator
        OptionData titulo = new OptionData(OptionType.STRING, "titulo", "Titulo da mensagem", false);
        OptionData descricao = new OptionData(OptionType.STRING, "descricao", "Descrição da mensagem", true);
        OptionData cor = new OptionData(OptionType.STRING, "cor", "Cor da embed, envie a cor em formato hex. Ex: #FF00FF", false);
        OptionData footer = new OptionData(OptionType.STRING, "footer", "Footer da mensagem", false);
        OptionData imagem = new OptionData(OptionType.ATTACHMENT, "imagem", "Imagem da mensagem", false);
        OptionData thumbnail = new OptionData(OptionType.ATTACHMENT, "thumb", "Imagem da thumbnail", false);
        OptionData canal = new OptionData(OptionType.CHANNEL, "canal", "Canal a ser enviado", false);




        //Adiciona os slashs
        commandData.add(Commands.slash("criarembed", "Cria uma mensagem embed.").addOptions(descricao,titulo, cor, footer, imagem, thumbnail, canal));

        //Atualiza toda vez que o bot reinicia
        event.getGuild().updateCommands().addCommands(commandData).queue();
    }
}
