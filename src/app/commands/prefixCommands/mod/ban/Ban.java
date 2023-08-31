package app.commands.prefixCommands.mod.ban;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.awt.*;
import java.util.concurrent.TimeUnit;

import static app.statics.Basics.prefixo;
import static app.statics.Basics.ygd;
import static app.statics.Functions.*;
import static app.statics.canais.Logs.logBanimentos;
import static app.statics.cargos.Perms.tresEstrelas;

public class Ban extends ListenerAdapter {

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        String[] mensagem = event.getMessage().getContentRaw().split(" ");
        String mensagemInteira = event.getMessage().getContentRaw();
        String motivo = " ";
        int idLength;
        if (mensagem[0].equalsIgnoreCase(prefixo + "ban")) {
            if (event.getMember().getPermissions().contains(Permission.BAN_MEMBERS) || event.getMember().getRoles().contains(tresEstrelas)) {
                EmbedBuilder embedBanimento = new EmbedBuilder();

                idLength = mensagem[1].length();
                motivo = mensagemInteira.substring(prefixo.length() + 3 + 1 + idLength);

                Member banido = obterMembro(mensagem);
                Member moderador = event.getMember();

                if (motivo.equals("")) {
                    motivo = "sem motivo";
                }

                // Obtenha os cargos do moderador e do membro
                if (banido != null) {
                    // Verifique se o moderador tem um cargo mais alto do que o membro banido
                    if (temCargoMaisAlto(moderador, banido)) {
                        embedBanimento
                                .setThumbnail(banido.getAvatarUrl())
                                .setColor(Color.red)
                                .setTitle("Membro banido!")
                                .addField("Moderador", moderador.getAsMention() + " `" + moderador.getId() + "`", true)
                                .addField("Membro", banido.getAsMention() + " `" + banido.getId() + "`", true)
                                .setImage(lerConteudoArquivo(event.getMember().getId() + "gifban"))
                                .addField("Motivo", "`" + motivo + "`", false);

                        banido.getUser().openPrivateChannel().queue(privateChannel -> privateChannel.sendMessage("").setEmbeds(embedBanimento.build()).queue());
                        ygd.ban(banido, 1, TimeUnit.MILLISECONDS).reason(moderador.getEffectiveName() + " | " + motivo).queue();
                        event.getChannel().sendMessage("").setEmbeds(embedBanimento.build()).queue(message -> message.delete().queueAfter(7, TimeUnit.SECONDS));
                        logBanimentos.sendMessage("").setEmbeds(embedBanimento.build()).queue();
                    } else {
                        event.getChannel().sendMessage("Você não tem permissão para banir membros com cargos iguais ou superiores ao seu.").queue(message -> message.delete().queueAfter(7, TimeUnit.SECONDS));
                    }
                } else {
                    event.getChannel().sendMessage("Não foi possível identificar o membro").queue(message -> message.delete().queueAfter(7, TimeUnit.SECONDS));
                }
            }
        }
    }

    // Função para verificar se um moderador tem um cargo mais alto do que um membro
    private boolean temCargoMaisAlto(Member moderador, Member membro) {
        return moderador.getRoles().stream().anyMatch(r -> membro.getRoles().stream().noneMatch(mr -> mr.getPositionRaw() >= r.getPositionRaw()));
    }
}
