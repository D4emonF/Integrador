package app.events.bot;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import java.awt.*;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static app.configs.Intialize.bot;
import static app.configs.Intialize.prefixo;

public class SelfMention extends ListenerAdapter {
    MessageEmbed embed = null;
    Button voltar = Button.danger("voltarHelper", "Voltar");
    Button cmds = Button.primary("cmdHelp", "Comandos");
    Button info = Button.secondary("infoHelp", "Informação");
    Button configuracao = Button.danger("configHelp", "Configurações");
    int lastPressed = 0;

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        Message mensagem = event.getMessage();
        if (mensagem.getMentions().isMentioned(bot.getSelfUser()) && !event.getAuthor().isBot()){
            EmbedBuilder helpEmbed = new EmbedBuilder();
            Button comandos = Button.primary("cmdHelp", "Comandos");
            Button info = Button.secondary("infoHelp", "Informação");
            Button configuracao = Button.danger("configHelp", "Configurações");


            helpEmbed
                    .setColor(new Color(43,45,49))
                    .setDescription("Selecione o módulo que deseja ajuda");

            embed = helpEmbed.build();
            event.getMessage().reply("").setEmbeds(helpEmbed.build()).setActionRow(comandos, info, configuracao).queue(message -> message.delete().queueAfter(120, TimeUnit.SECONDS));
        }
    }

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {




        if (Objects.equals(event.getButton().getId(), "cmdHelp")) {
            EmbedBuilder comandos = new EmbedBuilder();

            comandos.setDescription("# Comandos de diversão: \n" +
                    "<:branco_ponto:1171243772630282300> **" + prefixo + "roleta**\n" +
                    "> Roleta de verdade ou desafio!\n" +
                    "<:branco_ponto:1171243772630282300> **" + prefixo + "afk** `motivo`\n" +
                    "> Se define como afk e responde automaticamente se alguém te marcar.\n" +
                    "<:branco_ponto:1171243772630282300> **" + prefixo + "soco**\n" +
                    "> Dê um soco em alguém com uma força aleatória.\n" +
                    "<:branco_ponto:1171243772630282300> **" + prefixo + "ui**\n" +
                    "> Mostra algumas informações sobre um usuário.\n\n" +
                    
                    "# Comandos de moderação:\n" +
                    "<:branco_ponto:1171243772630282300> **" + prefixo + "clear** `quantidade`\n " +
                    "> Limpa a quantidade escolhida de mensagens, acima de 100 é recomendado usar **" + prefixo + "clearall**.\n"+
                    "<:branco_ponto:1171243772630282300> **" + prefixo + "clearall**\n"+
                    "> Limpa todas as mensagens de um chat.\n"+
                    "<:branco_ponto:1171243772630282300> **" + prefixo + "addcargo** `membro (@ ou id)` `cargo (@ ou id)`\n"+
                    "> Adiciona um cargo à um membro.\n"+
                    "<:branco_ponto:1171243772630282300> **" + prefixo + "removecargo** `membro (@ ou id)` `cargo (@ ou id)`\n"+
                    "> Remove um cargo de um membro.\n" +
                    "<:branco_ponto:1171243772630282300> **" + prefixo + "listacargo** `cargo @ ou id` \n" +
                    "> Menciona todos os membros de um cargo.\n" +
                    "<:branco_ponto:1171243772630282300> **" + prefixo + "rank**\n" +
                    "> Mostra o ranking de tempo em chamada.");
            comandos.setColor(new Color(43,45,49));

            event.getMessage().editMessageEmbeds().setEmbeds(comandos.build()).setActionRow(voltar).queue();
            event.deferEdit().queue();

        }
        if (Objects.equals(event.getButton().getId(), "infoHelp")) {
            EmbedBuilder infoe = new EmbedBuilder();

            infoe
                    .setTitle("Informação")
                    .setDescription("> Bot Desenvolvido para servir como vitrine de nossos bots, mostrando algumas funcionalidades que podem estar nos nossos bots.\n\n" +
                            "Prefixo: **`" + prefixo + "`**");


            infoe.setColor(new Color(43,45,49));

            event.editMessageEmbeds().setEmbeds(infoe.build()).setActionRow(voltar).queue();
            event.deferEdit().queue();
        }
        if (Objects.equals(event.getButton().getId(), "configHelp")){
            EmbedBuilder confige = new EmbedBuilder();

            confige
                    .setDescription("""
                            # Comandos Configurações:
                            <:branco_ponto:1171243772630282300> **vv!configcargo** `nivel de permissão` `@ ou id do cargo`
                            > Configura permissões de um cargo pelo bot (as permições serão apenas no bot). Para calcular o nível de permissão (clique aqui)[https://finitereality.github.io/permissions-calculator/?v=0].
                             
                            """);
            event.deferEdit().queue();
        }

        if (Objects.equals(event.getButton().getId(), "voltarHelper")) {

            event.editMessageEmbeds().setEmbeds(embed).setActionRow(cmds, info, configuracao).queue();
            event.deferEdit().queue();
        }


    }



    @Override
    public String toString() {
        return "Evento - SelfMention";
    }
}