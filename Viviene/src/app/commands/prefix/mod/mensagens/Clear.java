package app.commands.prefix.mod.mensagens;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.awt.*;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static app.configs.Intialize.bot;
import static app.configs.Intialize.prefixo;
import static app.statics.Functions.possuiPerm;

public class Clear extends ListenerAdapter {
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {


        Member membro = event.getMember();
        String[] mensagem = event.getMessage().getContentRaw().split(" ");
        int j = 0;

        EmbedBuilder deletados = new EmbedBuilder();
        deletados.setColor(Color.RED);

        if (!event.isFromGuild()) {


            if (mensagem[0].equalsIgnoreCase(prefixo + "clear") && mensagem.length == 2) {

                if (mensagem[1].equalsIgnoreCase("self")) {
                    event.getChannel().getHistory().retrievePast(100).queue(messages -> {
                        java.util.List<Message> userMessages = messages.stream()
                                .filter(message -> message.getAuthor().equals(bot.getSelfUser()))
                                .toList();

                        event.getChannel().purgeMessages(userMessages);
                    });
                }

            }
        }

        else {

            if (mensagem[0].equalsIgnoreCase(prefixo+ "cl")){
                    event.getChannel().getHistory().retrievePast(100).queue(messages -> {
                        java.util.List<Message> userMessages = messages.stream()
                                .filter(message -> message.getAuthor().equals(event.getAuthor()))
                                .toList();

                        event.getChannel().purgeMessages(userMessages);
                    });

            }

            if (mensagem[0].equalsIgnoreCase(prefixo + "clearall")) {

                try {
                    if (possuiPerm(event.getMember(), Permission.MANAGE_THREADS)) {
                        try {

                            event.getMessage().delete().queue();
                        } catch (Exception e) {
                        }

                        java.util.List<Message> historico = event.getChannel().asTextChannel().getHistory().retrievePast(100).complete();
                        int tamanhoInicial = historico.size();

                        while (!historico.isEmpty()) {

                            event.getChannel().purgeMessages(historico);
                            j += historico.size();
                            historico = event.getChannel().asTextChannel().getHistory().retrievePast(100).complete();

                        }

                        j -= tamanhoInicial; // Subtrair o tamanho inicial do contador j

                        if (j > 0) {
                            deletados.setTitle("**Foram deletadas " + j + " mensagens**!");
                        } else {
                            deletados.setTitle("**Não encontrei mensagens neste canal**!");
                        }
                        try {


                            event.getChannel().asTextChannel().sendMessage(membro.getAsMention()).setEmbeds(deletados.build()).queue(message -> message.delete().queueAfter(10, TimeUnit.SECONDS));
                        } catch (Exception e) {
                        }
                    }
                } catch (SQLException ignored) {
                }
            }
            if (mensagem[0].equalsIgnoreCase(prefixo + "clear") && mensagem.length == 2) {
                try {
                    if (possuiPerm(event.getMember(), Permission.MANAGE_THREADS)) {
                        if (mensagem[1].equalsIgnoreCase("bot")) {
                            event.getMessage().delete().queue();
                            event.getChannel().getHistory().retrievePast(100).queue(messages -> {
                                java.util.List<Message> userMessages = messages.stream()
                                        .filter(message -> message.getAuthor().equals(bot.getSelfUser()))
                                        .toList();

                                event.getChannel().purgeMessages(userMessages);
                            });
                        }
                    }
                      else if (Integer.parseInt(mensagem[1]) > 100) {
                        event.getMessage().delete().queue();

                        event.getMessage().reply("""
                                Para apagar mais de 100 mensagens use:

                                > lc!clear

                                isso apagará todas as mensagens do chat

                                Caso apagar todas as mensagens do chat não seja sua escolha, apague de 100 em 100""").queue();

                    } else if (Integer.parseInt(mensagem[1]) <= 100) {
                        event.getMessage().delete().queue();

                        try {
                            if (possuiPerm(event.getMember(), Permission.MANAGE_THREADS)) {

                                List<Message> historico = event.getChannel().getHistoryBefore(event.getMessageId(), Integer.parseInt(mensagem[1])).complete().getRetrievedHistory();
                                j = historico.size();
                                event.getChannel().purgeMessages(historico);

                                if (j > 0) {
                                    deletados.setTitle("**Foram deletadas " + j + " mensagens**!");
                                } else {
                                    deletados.setTitle("**Não encontrei mensagens nesse canal**!");
                                }

                                event.getChannel().asTextChannel().sendMessage(membro.getAsMention()).setEmbeds(deletados.build()).queue(message -> message.delete().queueAfter(10, TimeUnit.SECONDS));
                            }
                        } catch (Exception ignore) {
                        }
                    }
                } catch (Exception ignore) {
                }
            }

        }
    }

    @Override
    public String toString() {
        return "Comando - Clear";
    }
}
