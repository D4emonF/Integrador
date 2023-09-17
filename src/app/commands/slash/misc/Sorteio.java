package app.commands.slash.misc;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;

import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import java.awt.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static app.statics.Functions.*;

public class Sorteio extends ListenerAdapter {
    static Message msg;
    static TextChannel canal;
    static String sorteado;
    static String descricao;
    static TimeUnit tipo;
    static long tempo;
    static Member host = null;
    static Button finalizado = Button.danger("finalizado", "Sorteio Finalizado").asDisabled();

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        LocalDateTime agora = LocalDateTime.now();
        host = event.getMember();

        if (event.getName().equalsIgnoreCase("sorteio")) {
            event.reply("Sorteio iniciado").setEphemeral(true).queue();

            OptionMapping opcao1 = event.getOption("item");
            OptionMapping opcao2 = event.getOption("duração");
            OptionMapping opcao3 = event.getOption("descricao");
            OptionMapping opcao4 = event.getOption("canalsorteio");
            OptionMapping opcao5 = event.getOption("cargosorteio");

            sorteado = opcao1.getAsString();
            String duracao = opcao2.getAsString();

            if (opcao4 != null) {
                canal = opcao4.getAsChannel().asTextChannel();
            } else {
                canal = event.getChannel().asTextChannel();
            }

            char sufixo = duracao.toLowerCase().charAt(duracao.length() - 1);

            tempo = Long.parseLong(duracao.replaceAll("[^0-9]", ""));

            switch (sufixo) {
                case 's' -> tipo = TimeUnit.SECONDS;
                case 'm' -> tipo = TimeUnit.MINUTES;
                case 'h' -> tipo = TimeUnit.HOURS;
                case 'd' -> tipo = TimeUnit.DAYS;
                default -> throw new IllegalStateException("Unexpected value: " + sufixo);
            }

            Button participar = Button.success("participar", "Participar");
            Button participantes = Button.secondary("participantes", "Participantes");

            EmbedBuilder sorteio = new EmbedBuilder();

            if (opcao3 != null) {
                descricao = opcao3.getAsString();
                sorteio.setDescription(descricao);
            }

            sorteio
                    .setTitle(sorteado)
                    .addField("Tempo", "\n<t:" + gerarTimestamp(calcularData(agora, tempo, tipo.toChronoUnit())) + ":R>", false)
                    .setColor(Color.cyan);

            if (opcao5 != null){
                canal.sendMessage(marcarCargo(opcao5.getAsRole())).queue(message -> message.delete().queueAfter(10, TimeUnit.SECONDS));
            }

            canal.sendMessage("").setEmbeds(sorteio.build()).setActionRow(participar, participantes).queue(message -> {
                msg = message;
            });

            ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
            scheduler.schedule(() -> sortear(msg), tempo, tipo);

        }
    }

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        Button sair = Button.danger("sairsorteio", "Sair");
        if (Objects.equals(event.getButton().getId(), "participar")) {
            bancoLocal("sorteio" + event.getMessageId());
            List<String> idsList = new ArrayList<>();
            carregarIdsDosUsuarios("sorteio" + event.getMessageId(), idsList);

            // Converter a lista em um conjunto
            Set<String> ids = new HashSet<>(idsList);

            Member member = event.getMember();

            // Verifique se o usuário já está participando
            if (!ids.contains(member.getId())) {
                salvarIdDoUsuario("sorteio" + event.getMessageId(), event.getUser().getId());
                event.reply("Participando!").setEphemeral(true).queue();
            } else {
                EmbedBuilder embedBuilder = new EmbedBuilder();
                embedBuilder.setColor(Color.green)
                                .setDescription("> Você já está participando!");
                event.reply(event.getMessageId()).setEmbeds(embedBuilder.build()).setActionRow(sair).setEphemeral(true).queue();
            }
        }

        if (Objects.equals(event.getButton().getId(), "sairsorteio")){
            String idMsg = event.getMessage().getContentRaw();
            bancoLocal("sorteio" + idMsg);
            List<String> idsList = new ArrayList<>();
            carregarIdsDosUsuarios("sorteio" + idMsg, idsList);

            // Converter a lista em um conjunto
            Set<String> ids = new HashSet<>(idsList);

            User member = event.getUser();
            ids.remove(member.getId());

            // Remova o ID do usuário da lista de participantes
                limparConteudoArquivo("sorteio" + idMsg);
                for (String id : ids) {
                    salvarIdDoUsuario("sorteio" + idMsg, id);
                }
                event.reply("Você foi removido do sorteio").setEphemeral(true).queue();

        }

        if (Objects.equals(event.getButton().getId(), "participantes")) {
            List<String> ids = new ArrayList<>();
            bancoLocal("sorteio" + event.getMessageId());
            carregarIdsDosUsuarios("sorteio" + event.getMessageId(), ids);
            StringBuilder sb = new StringBuilder();
            sb.append("```Lista de participantes:```\n");
            for (String id : ids) {
                sb.append("<@").append(id).append(">\n");
            }
            event.reply(String.valueOf(sb)).setEphemeral(true).queue();
        }

        if (Objects.equals(event.getButton().getId(), "reroll")) {
            if (Objects.equals(event.getMember(), host) || event.getMember().getPermissions().contains(Permission.ADMINISTRATOR)){
                sortear(event.getMessage());
            }
            event.deferEdit().queue();
        }
    }

    static void sortear(Message msg) {
        Button reroll = Button.secondary("reroll", "Reroll");
        List<String> ids = new ArrayList<>();
        bancoLocal("sorteio" + msg.getId());
        carregarIdsDosUsuarios("sorteio" + msg.getId(), ids);
        if (!ids.isEmpty()) {
            String idSorteado;
            LocalDateTime agora = LocalDateTime.now();
            Random random = new Random();
            int index = random.nextInt(ids.size());
            List<String> idList = new ArrayList<>(ids);
            idSorteado = idList.get(index);

            EmbedBuilder sorteio = new EmbedBuilder();
            sorteio
                    .setTitle(sorteado)
                    .addField("Finalizado há:", "\n<t:" + gerarTimestamp(agora) + ":R>", false)
                    .addField("Ganhador", "<@" + idSorteado + ">", false)
                    .setColor(Color.red);

            if (descricao != null) {
                sorteio.setDescription(descricao);
            }

            canal.sendMessage("<@" + idSorteado + "> ganhou o sorteio de " + sorteado + "!").queue();
            msg.editMessageEmbeds(sorteio.build()).setActionRow(finalizado, reroll).queue();
        } else {
            EmbedBuilder sorteio = new EmbedBuilder();
            sorteio
                    .setTitle(sorteado)
                    .addField("Finalizado há:", "\n<t:" + gerarTimestamp(LocalDateTime.now()) + ":R>", false)
                    .setColor(Color.red);

            if (descricao != null) {
                sorteio.setDescription(descricao);
            }

            msg.editMessageEmbeds(sorteio.build()).setActionRow(finalizado).queue();
            canal.sendMessage("O evento não teve participantes o suficiente").queue();
        }
    }

}
