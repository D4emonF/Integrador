package app.commands.prefixCommands.misc;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.awt.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.concurrent.TimeUnit;

import static app.App.jda;
import static app.statics.Basics.prefixo;
import static app.statics.Functions.*;
import static app.statics.canais.Geral.canalComandos;

public class UserInfo extends ListenerAdapter {
    Member member;


    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        String[] mensagem = event.getMessage().getContentRaw().split(" ");


        if (mensagem.length > 1 && obterMembro(mensagem) != null) {
            member = obterMembro(mensagem);
        }
        else
        {
            member = event.getMember();
        }



        if (mensagem[0].equalsIgnoreCase(prefixo + "ui") || mensagem[0].equalsIgnoreCase(prefixo + "userinfo") || mensagem[0].equalsIgnoreCase(prefixo + "iu") || mensagem[0].equalsIgnoreCase(prefixo + "infouser")){
            if (member == null) {
                event.getChannel().sendMessage("O membro escolhido não foi encontrado ou não existe").queue(message -> message.delete().queueAfter(7, TimeUnit.SECONDS));
            }else {
                if (!event.getChannel().asTextChannel().equals(canalComandos)) {
                    event.getChannel().sendMessage("").setEmbeds(msgEmbed(member).build()).queue(message -> message.delete().queueAfter(15, TimeUnit.SECONDS));
                }
                else {
                    event.getChannel().sendMessage("").setEmbeds(msgEmbed(member).build()).queue();
                }
            }
        }
    }

    private static EmbedBuilder msgEmbed(Member membro){
        User user = jda.getUserById(membro.getId());


        LocalDateTime tempoCriacao = user.getTimeCreated().toLocalDateTime().atZone(ZoneId.of("America/Sao_Paulo")).toLocalDateTime();
        LocalDateTime tempoEntrada = membro.getTimeJoined().toLocalDateTime().atZone(ZoneId.of("America/Sao_Paulo")).toLocalDateTime();

        EmbedBuilder userinfo = new EmbedBuilder();

        String apelido;
        if (membro.getNickname() != null){
            apelido = membro.getNickname();
        }
        else
        {
            apelido = user.getName();
        }

        userinfo
                .setColor(Color.decode("#2B2D31"))
                .setTitle(user.getName())
                .addField("<:preto_id:1141064862273917028>  ID:", "`" + user.getId() + "`", true)
                .addField("<:preto_membro:1124563263439507538> Apelido:", apelido, true)
                .addField("<:preto_calendario:1141067399790088353> Criado em:", "<t:" + gerarTimestamp(tempoCriacao) + "> ("+ "<t:" + gerarTimestamp(tempoCriacao) + ":R>)", true)
                .addField("<:preto_calendario:1141067399790088353> Entrou no servidor em:", "<t:" + gerarTimestamp(tempoEntrada) + "> ("+ "<t:" + gerarTimestamp(tempoEntrada) + ":R>)", true);


        if (membro.getTimeBoosted() != null){
            LocalDateTime tempoNitro = membro.getTimeBoosted().toLocalDateTime();
            userinfo.addField("<:preto_calendario:1141067399790088353> Deu boost no servidor em:", "<t:" + gerarTimestamp(tempoNitro) + "> ("+ "<t:" + gerarTimestamp(tempoNitro) + ":R>)", true);
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
}