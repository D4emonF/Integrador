package app.commands.prefixCommands.misc;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static app.statics.Basics.prefixo;
import static app.statics.Functions.*;

public class Afk extends ListenerAdapter {
    public static final List<String> afkUsers = new ArrayList<>();
    private static final Map<String, String> afkReasons = new HashMap<>();

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        String[] mensagem = event.getMessage().getContentRaw().split(" ");
        if (mensagem[0].equalsIgnoreCase(prefixo + "afk")) {
            String memberId = event.getAuthor().getId();
            String afkFile = "estadoafk" + memberId;  // Nome do arquivo com "estadoafk" seguido pelo ID do membro
            String motivo = String.join(" ", mensagem).substring((prefixo + "afk").length()).trim();
            bancoLocal(afkFile);

            if (afkUsers.contains(memberId)) {
                event.getMessage().reply("Você já está AFK com o motivo: " + lerConteudoArquivo(afkFile)).queue(message -> message.delete().queueAfter(7, TimeUnit.SECONDS));
            } else {
                motivo = motivo.replace("@everyone", "@\u200Beveryone").replace("@here", "@\u200Bhere");
                salvarIdDoUsuario(afkFile, motivo.isEmpty() ? "Sem motivo" : motivo);
                afkUsers.add(memberId);
                afkReasons.put(memberId, motivo);
                event.getMessage().reply("Você está agora AFK com o motivo: " + lerConteudoArquivo(afkFile)).queue(message -> message.delete().queueAfter(7, TimeUnit.SECONDS));
            }
        } else if (!event.getAuthor().isBot() && afkUsers.contains(event.getAuthor().getId())) {
            String memberId = event.getAuthor().getId();
            String afkFile = "estadoafk" + memberId;  // Nome do arquivo com "estadoafk" seguido pelo ID do membro
            String motivo = lerConteudoArquivo(afkFile);

            if (!motivo.isEmpty()) {
                event.getMessage().reply("Você não está mais AFK.").queue(message -> message.delete().queueAfter(7, TimeUnit.SECONDS));
            } else {
                event.getMessage().reply("Você não está mais AFK.").queue(message -> message.delete().queueAfter(7, TimeUnit.SECONDS));
            }

            // Remove o membro da lista AFK e limpa o motivo
            afkUsers.remove(memberId);
            afkReasons.remove(memberId);
            limparConteudoArquivo(afkFile);
        }

        // Verifique se alguém mencionou um usuário AFK e não seja o bot
        for (Member mentionedMember : event.getMessage().getMentions().getMembers()) {
            String memberId = mentionedMember.getId();
            if (afkUsers.contains(memberId) && !event.getAuthor().isBot()) {
                String motivo = afkReasons.get(memberId);
                event.getMessage().reply(mentionedMember.getEffectiveName() + " está AFK com o motivo: " + motivo).queue(message -> message.delete().queueAfter(7, TimeUnit.SECONDS));
            }
        }
    }

}
