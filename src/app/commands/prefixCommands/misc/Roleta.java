package app.commands.prefixCommands.misc;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static app.statics.Basics.prefixo;
import static app.statics.external.ColorPalette.bunker;
import static app.statics.users.Pessoas.davi;

public class Roleta extends ListenerAdapter
{

    List<Member> participantes = new ArrayList<>();
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        String[] mensagem = event.getMessage().getContentRaw().split(" ");
        if (mensagem[0].startsWith(prefixo + "rola") || mensagem[0].startsWith(prefixo + "roleta") || mensagem[0].startsWith(prefixo + "role") || mensagem[0].startsWith(prefixo + "roll"))
        {
            List<String> desafios = new ArrayList<>();
            desafios.add("PPP");
            desafios.add("Alguém que você acha forçado");
            desafios.add("Eu nunca fiquei com ex minha");
            desafios.add("Eu nunca talariquei");
            desafios.add("Eu nunca peguei ex de amigo");
            desafios.add("Eu nunca quis pegar alguém da call");
            desafios.add("Top 3 Soca/Senta Fofo");
            desafios.add("Top 3 Soca/Senta Forte");
            desafios.add("Uma pessoa com quem ficaria da call");
            desafios.add("Uma pessoa que nunca ficaria");
            desafios.add("Opinião sobre o desafiador");
            desafios.add("Mande mensagem para seu ex e fale \"Oi sumido, sdds\" ou \"Volta vida, errei fui Neymar\"");
            desafios.add("Convite uma pessoa aleatória pra call e quando ela entrar a peça em namoro");
            desafios.add("O Desafiador tem uma pergunta livre para o desafiado");
            desafios.add("O jogo virou, lance um desafio para o desafiador");
            desafios.add("Faça voz de criança e chame o desafiador de papai/mamãe");
            desafios.add("Deu sorte, passe o desafio pro próximo");
            desafios.add("Toda frase que falar deve finalizar com miau até o fim da brincadeira");
            desafios.add("GRITE yametekudasai");
            desafios.add("Uma pessoa que você considera Maria/João carguinho");
            desafios.add("Qual a pessoa mais tóxica do servidor?");
            desafios.add("Cite 2 pessoas que você pegaria e o por quê");
            desafios.add("Diga uma musica, pra quem dedicaria a mesma e o por quê");
            desafios.add("Envie uma declaração para quem o desafiador escolher");
            desafios.add("O desafiado agora é uma MAID, e sempre deve falar coisas como Arigato nya ou moe moe cute");
            desafios.add("Imite uma Loli");
            desafios.add("Fale com voz de gf até o fim da brincadeira");
            desafios.add("Fale um defeito de cada um da call");
            desafios.add("Mande uma mensagem para a pessoa que você gosta");
            desafios.add("A pessoa que o desafiado mais tem afinidade vai ter que dar em cima de quem o desafiado está gostando ou em cima do ex do desafiado");
            desafios.add("Conte uma história vergonhosa sobre o desafiador, se não souber conte sobre você");


            if (event.getMember().getVoiceState().inAudioChannel()){
                participantes = event.getMember().getVoiceState().getChannel().getMembers();
            }

            System.out.println("Quantidade de desafios:" + desafios.size());
            System.out.println("Participantes: ");
            for (Member participante: participantes) {
                System.out.println(participante.getEffectiveName());
            }

            Random random = new Random();

            EmbedBuilder desafio = new EmbedBuilder();
            desafio
                    .setTitle("A roleta foi girada!")
                    .addField("Desafiador", event.getMember().getAsMention(), false)
                    .addField("Desafio", "`"+ desafios.get(random.nextInt(desafios.size())) +"`", false)
                    .addField("Desafiado","Entre " + participantes.size() + " participantes, o escolido foi: " + participantes.get(random.nextInt(participantes.size())).getAsMention(), false)
                    .setColor(bunker)
                    .setFooter("Sistema desenvolvido por: " + davi.getEffectiveName());

            event.getChannel().sendMessage("").setEmbeds(desafio.build()).queue();

        }
    }
}
