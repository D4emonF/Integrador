package app.commands.prefix.misc;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;

import java.awt.*;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.EnumSet;
import java.util.concurrent.TimeUnit;

import static app.configs.Intialize.bot;
import static app.configs.Intialize.prefixo;
import static app.statics.Functions.*;

public class UserInfo extends ListenerAdapter {
    Member member;


    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        String[] mensagem = event.getMessage().getContentRaw().split(" ");






        if (mensagem[0].equalsIgnoreCase(prefixo + "ui") || mensagem[0].equalsIgnoreCase(prefixo + "userinfo") || mensagem[0].equalsIgnoreCase(prefixo + "iu") || mensagem[0].equalsIgnoreCase(prefixo + "infouser")){
            if (mensagem.length > 1 && obterMembro(event) != null) {
                member = obterMembro(event);
            }
            else
            {
                member = event.getMember();
            }
            if (member == null) {
                event.getChannel().sendMessage("O membro escolhido não foi encontrado ou não existe").queue(message -> message.delete().queueAfter(7, TimeUnit.SECONDS));
            }else {
                try {
                    event.getChannel().sendMessage("").setEmbeds(msgEmbed(member).build()).queue(message -> message.delete().queueAfter(30, TimeUnit.SECONDS));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (event.getName().equalsIgnoreCase("infouser")){

            OptionMapping opcao1 = event.getOption("user");

            if (opcao1 != null){
                member = opcao1.getAsMember();
            }else{
                member = event.getMember();
            }

            try {
                event.reply("").setEphemeral(true).setEmbeds(msgEmbed(member).build()).queue();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static EmbedBuilder msgEmbed(Member membro) throws IOException {
        User user = bot.getUserById(membro.getId());


        LocalDateTime tempoCriacao = user.getTimeCreated().toLocalDateTime().atZone(ZoneId.of("America/Sao_Paulo")).toLocalDateTime();
        LocalDateTime tempoEntrada = membro.getTimeJoined().toLocalDateTime().atZone(ZoneId.of("America/Sao_Paulo")).toLocalDateTime();

        EmbedBuilder userinfo = new EmbedBuilder();

        String apelido;
        if (membro.getNickname() != null){
            apelido = membro.getNickname();
        }
        else
        {
            apelido = user.getEffectiveName();
        }

        EnumSet<User.UserFlag> flags = user.getFlags();

        userinfo
                .setColor(Color.decode("#2B2D31"))
                .setTitle(user.getName())
                .addField("<:preto_id:1171224864678477926>  ID:", "`" + user.getId() + "`", true)
                .addField("<:preto_membro:1171224894504185897> Apelido:", apelido, true)
                .addField("<:preto_calendario:1171224917891629056> Criado em:", "<t:" + gerarTimestamp(tempoCriacao) + "> ("+ "<t:" + gerarTimestamp(tempoCriacao) + ":R>)", true);


        if (!flags.isEmpty()){
            for (User.UserFlag flag: flags) {
                flag.getName();
            }
        }

        userinfo
                .addField("<:preto_calendario:1171224917891629056> Entrou no servidor em:", "<t:" + gerarTimestamp(tempoEntrada) + "> ("+ "<t:" + gerarTimestamp(tempoEntrada) + ":R>)", true);


        if (membro.getTimeBoosted() != null){
            LocalDateTime tempoNitro = membro.getTimeBoosted().toLocalDateTime();
            userinfo.addField("<:preto_calendario:1171224917891629056> Deu boost no servidor em:", "<t:" + gerarTimestamp(tempoNitro) + "> ("+ "<t:" + gerarTimestamp(tempoNitro) + ":R>)", true);
        }
        String bannerURL = getBanner(user.getId());

        if (bannerURL != null) {
            userinfo.setImage(bannerURL);
        }
        if (user.getAvatarUrl() != null){
            userinfo.setThumbnail(user.getAvatarUrl());
        }

        return userinfo;
    }

    @Override
    public String toString() {
        return "Comando - UserInfo";
    }
}
