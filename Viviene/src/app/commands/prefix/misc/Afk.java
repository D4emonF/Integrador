package app.commands.prefix.misc;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static app.configs.Intialize.prefixo;
import static app.db.read.Read.getAfk;
import static app.db.read.Read.obterMembrosAfk;
import static app.db.update.Update.salvaAfk;

public class Afk extends ListenerAdapter {
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        String[] mensagem = event.getMessage().getContentRaw().split(" ");


        try {
            List<String> afkUsers = obterMembrosAfk();
            if (mensagem[0].equalsIgnoreCase(prefixo + "afk")) {
                String motivo = String.join(" ", mensagem).substring((prefixo + "afk").length()).trim();
                try {



                    if (getAfk(event.getMember().getId()) == null || getAfk(event.getMember().getId()).isEmpty()) {
                            motivo = motivo.replace("@everyone", "@\u200Beveryone").replace("@here", "@\u200Bhere");
                            salvaAfk(event.getMember(), motivo);
                            event.getMessage().reply("Você está agora AFK com o motivo: " + getAfk(event.getMember().getId())).queue(message -> message.delete().queueAfter(7, TimeUnit.SECONDS));
                        }
                    else if (!getAfk(event.getMember().getId()).isEmpty()) {
                        event.getMessage().reply("Você já está AFK com o motivo: " + getAfk(event.getMember().getId())).queue(message -> message.delete().queueAfter(7, TimeUnit.SECONDS));
                    }


                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }


            } else if (!event.getAuthor().isBot() && afkUsers.contains(event.getAuthor().getId())) {

                try {
                    event.getMessage().reply("Você não está mais AFK.").queue(message -> message.delete().queueAfter(7, TimeUnit.SECONDS));
                    salvaAfk(event.getMember(), "");
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }


            }

            // Verifique se alguém mencionou um usuário AFK e não seja o bot
            for (Member mentionedMember : event.getMessage().getMentions().getMembers()) {
                String memberId = mentionedMember.getId();
                if (afkUsers.contains(memberId) && !event.getAuthor().isBot()) {
                    String motivo = null;
                    try {
                        motivo = getAfk(memberId);
                        event.getMessage().reply(mentionedMember.getEffectiveName() + " está AFK com o motivo: " + motivo).queue(message -> message.delete().queueAfter(7, TimeUnit.SECONDS));
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }


    @Override
    public String toString() {
        return "Comando - Afk";
    }
}

