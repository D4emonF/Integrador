package app.commands.prefix.misc;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static app.configs.Intialize.prefixo;

public class Roleta extends ListenerAdapter
{

    List<Member> participantes = new ArrayList<>();
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        String[] mensagem = event.getMessage().getContentRaw().split(" ");
        if (mensagem[0].startsWith(prefixo + "rola") || mensagem[0].startsWith(prefixo + "roleta") || mensagem[0].startsWith(prefixo + "role") || mensagem[0].startsWith(prefixo + "roll" ) || mensagem[0].startsWith(prefixo + "rolar"))
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
            desafios.add("Conte um date/encontro estranho");
            desafios.add("Morda um pedaço de cebola com a camera ligada");
            desafios.add("Conte alguém que você já ficou e ninguém sabe");
            desafios.add("Diga a maior mentira que já contou na sua vida");
            desafios.add("Ligue para alguém e comece a pedir desculpas desperadamente, sem explicar o porque");
            desafios.add("Finja ser um dinossauro");
            desafios.add("Mande \"Ainda amo meu ex\" para algum amigo");
            desafios.add("Conte duas mentiras e uma verdade sobre sua vida amorosa e deixe o grupo na dúvida sobre qual é qual");
            desafios.add("Conte uma história de quando se arrependeu de ficar com alguém e o motivo");
            desafios.add("Transmita a tela e mostre seu histórico de pesquisas mais recentes no Google");
            desafios.add("Escolha alguém da call e diga na cara dela o maior defeito dela");
            desafios.add("Imite uma peak me girl");
            desafios.add("Quem você levaria dessa call para uma ilha deserta?");


            if (event.getMember().getVoiceState().inAudioChannel()) {
                if (event.getMember().getVoiceState().getChannel().getMembers().size() > 1) {
                    participantes = event.getMember().getVoiceState().getChannel().getMembers();


                    Random random = new Random();

                    Member desafiado = participantes.get(random.nextInt(participantes.size()));
                    EmbedBuilder desafio = new EmbedBuilder();
                    desafio
                            .setTitle("A roleta foi girada!")
                            .addField("Desafiador", event.getMember().getAsMention(), false)
                            .addField("Desafio", "`" + desafios.get(random.nextInt(desafios.size())) + "`", false)
                            .addField("Desafiado", "Entre " + participantes.size() + " participantes, o escolido foi: " + desafiado.getAsMention(), false)
                            .setColor(new Color(43,45,49))
                            .setThumbnail(desafiado.getEffectiveAvatar().getUrl());

                    event.getChannel().sendMessage(desafiado.getAsMention()).setEmbeds(desafio.build()).queue();
                }else
                {
                        event.getMessage().reply("Você não pode brincar sozinho!").queue();
                }
            } else
            {
                    event.getMessage().reply("Você não está em uma chamada.").queue();
            }

        }
    }

    @Override
    public String toString() {
        return "Comando - Roleta";
    }
}
