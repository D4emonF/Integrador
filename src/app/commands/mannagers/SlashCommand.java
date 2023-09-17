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

        OptionData emoji = new OptionData(OptionType.STRING, "emoji", "Emoji ou o ID do emoji a ser procurado", true);

        //Sorteio
        OptionData sorteado = new OptionData(OptionType.STRING, "item", "O que será sorteado", true);
        OptionData duracao = new OptionData(OptionType.STRING, "duração", "Tempo de duração. Ex: 15m (\"s\" para segundos, \"m\" para minutos, \"h\" para horas, \"d \"para dias)", true);
        OptionData cSorteio = new OptionData(OptionType.CHANNEL, "canalsorteio", "Canal do sorteio (deve ser canal de texto)");
        OptionData cargoSorteio = new OptionData(OptionType.ROLE, "cargosorteio", "Cargo para marcar");

        // Opções do comando "infouser"
        OptionData infouser = new OptionData(OptionType.USER, "user", "Usuário a ser consultado", false);

        //Adiciona os slashs
        commandData.add(Commands.slash("ping", "Responde com o ping do bot"));
        commandData.add(Commands.slash("sorteio", "Cria um novo sorteio").addOptions(sorteado, duracao, descricao, cSorteio, cargoSorteio));
        commandData.add(Commands.slash("criarembed", "Cria uma mensagem embed.").addOptions(descricao,titulo, cor, footer, imagem, thumbnail, canal));
        commandData.add(Commands.slash("emojiinfo", "Busca informações sobre um emoji").addOptions(emoji));
        commandData.add(Commands.slash("boosters", "Mostra os membros dando boost no servidor"));
        commandData.add(Commands.slash("infouser", "Informações sobre um usuário").addOptions(infouser));



        //Atualiza toda vez que o bot reinicia
        event.getGuild().updateCommands().addCommands(commandData).queue();
    }
}
