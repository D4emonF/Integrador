package app.commands.prefix.misc;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.awt.*;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import static app.configs.Intialize.prefixo;
import static app.statics.Functions.obterMembro;

public class Soco extends ListenerAdapter {
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        String[] mensagem = event.getMessage().getContentRaw().split(" ");

        String onepunch = "https://media.discordapp.net/attachments/1150623253581267066/1150623373844549734/onePunchMan.gif";
        String socoFofo = "https://media.discordapp.net/attachments/1150623253581267066/1150623656859406387/SocoFofo.gif";
        String socoMedio = "https://media.discordapp.net/attachments/1150623253581267066/1150624041082818590/socoMedio.gif";


        if (mensagem[0].equalsIgnoreCase(prefixo + "soco")){

            Random random = new Random();

            int forcaSoco = random.nextInt(101);

            Member socado = obterMembro(event);

            EmbedBuilder embedBuilder = new EmbedBuilder();
            if (socado != null){
                embedBuilder
                        .setTitle("Toma-le soco")
                        .setDescription(event.getMember().getAsMention() + " socou " + socado.getAsMention() + " com " + forcaSoco + "% da sua força.")
                        .setColor(new Color(43,45,49));

                if (forcaSoco <= 20){
                    embedBuilder.addField("Situação do soco", "O membro " + socado.getAsMention() + " saiu ileso, que soco fofo!", false)
                            .setImage(socoFofo);
                }

                if (20 < forcaSoco && forcaSoco < 80){
                    embedBuilder.addField("Situação do soco", "O membro " + socado.getAsMention() + " saiu machucado, que belo soco!", false)
                            .setImage(socoMedio);
                }

                if (forcaSoco > 80){
                    embedBuilder.addField("Situação do soco", "O membro " + socado.getAsMention() + " foi nocauteado, " + event.getMember().getAsMention() + " sabe bem dar um soco!", false)
                            .setImage(onepunch)
                            .setColor(Color.red);
                }

                event.getChannel().sendMessage("").setEmbeds(embedBuilder.build()).queue(message -> message.delete().queueAfter(30, TimeUnit.SECONDS));
            }

        }
    }

    @Override
    public String toString() {
        return "Comando - Soco";
    }
}