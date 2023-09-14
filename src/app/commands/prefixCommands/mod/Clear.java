package app.commands.prefixCommands.mod;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.awt.*;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static app.App.jda;
import static app.statics.Basics.prefixo;

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
                                .filter(message -> message.getAuthor().equals(jda.getSelfUser()))
                                .toList();

                        event.getChannel().purgeMessages(userMessages);
                    });
                }

            }
        }

        if (event.isFromGuild()) {

            if (mensagem[0].equalsIgnoreCase(prefixo + "clearall")) {

                if (Objects.requireNonNull(membro).getPermissions().contains(Permission.MANAGE_THREADS)) {
                    event.getMessage().delete().queue();

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
                        deletados.setTitle("**Não encontrei mensagens nes   se canal**!");
                    }

                    event.getChannel().asTextChannel().sendMessage(membro.getAsMention()).setEmbeds(deletados.build()).queue(message -> message.delete().queueAfter(10, TimeUnit.SECONDS));
                }
            }
            if (mensagem[0].equalsIgnoreCase(prefixo + "clear") && mensagem.length == 2) {

                if (mensagem[1].equalsIgnoreCase("self")) {
                    event.getChannel().getHistory().retrievePast(100).queue(messages -> {
                        java.util.List<Message> userMessages = messages.stream()
                                .filter(message -> message.getAuthor().equals(jda.getSelfUser()))
                                .toList();

                        event.getChannel().purgeMessages(userMessages);
                    });
                } else if (Integer.parseInt(mensagem[1]) > 100) {
                    event.getMessage().delete().queue();

                    event.getMessage().reply("""
                            Para apagar mais de 100 mensagens use:

                            > lc!clear

                            isso apagará todas as mensagens do chat

                            Caso apagar todas as mensagens do chat não seja sua escolha, apague de 100 em 100""").queue();

                } else if (Integer.parseInt(mensagem[1]) <= 100) {
                    event.getMessage().delete().queue();

                    if (Objects.requireNonNull(membro).getPermissions().contains(Permission.MANAGE_THREADS)) {

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
                }
            }
        }
    }

}
