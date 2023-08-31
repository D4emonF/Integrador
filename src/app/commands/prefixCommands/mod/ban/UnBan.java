package app.commands.prefixCommands.mod.ban;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.awt.*;
import java.util.concurrent.TimeUnit;

import static app.App.jda;
import static app.statics.Basics.prefixo;
import static app.statics.Basics.ygd;
import static app.statics.Functions.lerConteudoArquivo;
import static app.statics.Functions.obterUser;
import static app.statics.canais.Logs.logBanimentos;
import static app.statics.cargos.Perms.tresEstrelas;

public class UnBan extends ListenerAdapter {
    String motivo = "";
    User banido;

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        String[] mensagem = event.getMessage().getContentRaw().split(" ");
        String mensagemInteira = event.getMessage().getContentRaw();

        int idLength;
        if (mensagem[0].equalsIgnoreCase(prefixo + "unban")) {
            if (event.getMember().getPermissions().contains(Permission.BAN_MEMBERS) || event.getMember().getRoles().contains(tresEstrelas)) {
                idLength = mensagem[1].length();
                motivo = mensagemInteira.substring(prefixo.length() + 3 + 3 + idLength);
                if (motivo.equals("")){
                    motivo = "sem motivo";
                }


                EmbedBuilder embedUnBan = new EmbedBuilder();

                // Obtém a lista de membros banidos
                jda.getGuildById(event.getGuild().getId()).retrieveBanList().queue(banList -> {
                    for (Guild.Ban ban : banList) {
                        if (ban.getUser().getId().equals(mensagem[1])) {
                            // Encontrou o membro com o ID fornecido, desbane-o
                            banido = ban.getUser();
                            ygd.unban(ban.getUser()).reason(event.getMember().getEffectiveName() + " | " + motivo).queue();
                            return; // Saia do loop
                        }
                    }
                    // Se chegou aqui, significa que o usuário não foi encontrado
                    event.getChannel().sendMessage("Usuário não encontrado na lista de bans.").queue();
                });

                embedUnBan
                        .setColor(Color.green)
                        .setTitle("Membro desbanido")
                        .addField("Moderador", event.getMember().getAsMention() + " `" + event.getMember().getId() + "`", true)
                        .addField("Membro", banido.getAsMention() + " `" + banido.getId() + "`", true)
                        .addField("Motivo", "`" + motivo + "`", false);

                event.getMessage().getChannel().sendMessage("").setEmbeds(embedUnBan.build()).queue(message -> message.delete().queueAfter(7, TimeUnit.SECONDS));
                logBanimentos.sendMessage("").setEmbeds(embedUnBan.build()).queue();
            }
        }
    }
}
