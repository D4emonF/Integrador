package app.commands.prefix.misc;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import java.awt.Color;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static app.configs.Intialize.bot;
import static app.configs.Intialize.prefixo;
import static app.db.read.Read.*;
import static app.db.update.Update.resetaTempo;
import static app.db.update.Update.resetaTempoI;
import static app.statics.Functions.*;

public class Tempo extends ListenerAdapter {
    protected static Member membro;

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        String[] mensagem = event.getMessage().getContentRaw().split(" ");
        try {

            if (mensagem[0].equalsIgnoreCase(prefixo + "tempo") || mensagem[0].equalsIgnoreCase(prefixo + "temp") || mensagem[0].equalsIgnoreCase(prefixo + "time")) {
                int tempoCache, tempoSalvo;

                EmbedBuilder embedTempo = new EmbedBuilder();
                Button ranking = Button.success("rank", "Ver Ranking!");


                if (mensagem.length == 1) {
                    membro = event.getMember();
                } else {
                    membro = obterMembro(event);
                }

                embedTempo
                        .setTitle("Tempo de " + event.getMember().getEffectiveName());


                tempoSalvo = getTempoT(membro.getId());


                if (tempoSalvo != 0) {
                    String tempoFormatado = segundosParaTempo(tempoSalvo);

                    embedTempo.addField("Tempo salvo", tempoFormatado, true);
                } else {
                    embedTempo.addField("Tempo salvo", "Não possui tempo salvo", true);
                }

                if (membro.getVoiceState().inAudioChannel()) {
                    LocalDateTime dateTime = LocalDateTime.now();
                    tempoCache = gerarTimestamp(dateTime) - getTempoI(membro.getId());
                    String tempoFormatado = segundosParaTempo(tempoCache);
                    embedTempo.addField("Tempo em cache", tempoFormatado, true)
                            .addField("<:cinza_chat:1171898778601656320> Canal de voz", membro.getVoiceState().getChannel().getAsMention() + " `" + membro.getVoiceState().getChannel().getName() + "`", false);

                }

                User user = bot.getUserById(membro.getId());
                embedTempo
                        .setThumbnail(user.getEffectiveAvatarUrl())
                        .setColor(Color.decode("#2B2D31"));
                event.getMessage().reply("").setEmbeds(embedTempo.build()).setActionRow(ranking).queue(message -> message.delete().queueAfter(7, TimeUnit.SECONDS));
            }

            if (mensagem[0].equalsIgnoreCase(prefixo + "resetatempo")) {
                if (event.getMember().getPermissions().contains(Permission.ADMINISTRATOR)) {
                    resetaTempo();
                    LocalDateTime dateTime = LocalDateTime.now();
                    for (Member membro: getMembroEmCall(event)
                         ) {
                        System.out.println(getTempoI(membro.getId()));
                        resetaTempoI(membro, gerarTimestamp(dateTime));
                        System.out.println(getTempoI(membro.getId()));
                    }
                    event.getMessage().reply("Todos os tempos resetados!").queue(message -> message.delete().queueAfter(7, TimeUnit.SECONDS));
                }
            }

            if (mensagem[0].equalsIgnoreCase(prefixo + "rank")) {
                int tempoSalvo = 0;

                List<String> ids = new ArrayList<>();
                if (mensagem.length == 1) {
                    ids = obterRankTempo(10);
                    EmbedBuilder embed = new EmbedBuilder();
                    embed
                            .setTitle("Ranking de tempo");
                    StringBuilder sb = new StringBuilder();
                    int i = 0;
                    for (String id : ids) {
                            i++;
                            Member member = event.getGuild().getMemberById(id);
                            tempoSalvo = getTempoT(id);
                            String tempoFormatado = segundosParaTempo(tempoSalvo);

                            if (i<10)
                                sb.append("` ").append(i).append("` - ").append(member.getAsMention()).append(" `").append(member.getId()).append("`\n").append("> ").append(tempoFormatado).append("\n");
                            else
                                sb.append("`").append(i).append("` - ").append(member.getAsMention()).append(" `").append(member.getId()).append("`\n").append("> ").append(tempoFormatado).append("\n");
                    }
                    embed
                            .setDescription(sb + "\n\n> Para um ranking maior que 10, use **" + prefixo + "rank** `quantidade`")
                            .setColor(new Color(43,45,49));

                    event.getMessage().reply("").setEmbeds(embed.build()).queue(message -> message.delete().queueAfter(7, TimeUnit.SECONDS));

                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        if (Objects.equals(event.getButton().getId(), "rank")){
            int tempoSalvo = 0;

            List<String> ids = new ArrayList<>();
            try {
                ids = obterRankTempo(10);
            EmbedBuilder embed = new EmbedBuilder();
                embed
                        .setTitle("Ranking de tempo");
                StringBuilder sb = new StringBuilder();
                int i = 0;
                for (String id : ids) {
                    i++;
                    Member member = event.getGuild().getMemberById(id);
                    tempoSalvo = getTempoT(id);
                    String tempoFormatado = segundosParaTempo(tempoSalvo);

                    if (i<10)
                        sb.append("` ").append(i).append("` - ").append(member.getAsMention()).append(" `").append(member.getId()).append("`\n").append("> ").append(tempoFormatado).append("\n");
                    else
                        sb.append("`").append(i).append("` - ").append(member.getAsMention()).append(" `").append(member.getId()).append("`\n").append("> ").append(tempoFormatado).append("\n");
                }
                embed
                        .setDescription(sb + "\n\n> Para um ranking maior que 10, use **" + prefixo + "rank** `quantidade`")
                        .setColor(new Color(43,45,49));

                event.reply("").setEmbeds(embed.build()).setEphemeral(true).queue();

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public String toString() {
        return "Comando - Tempo";
    }
}
