package app.commands.mannagers;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.Channel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import static app.statics.Basics.prefixo;
import static app.statics.Basics.prefixos;
import static app.statics.canais.Logs.logComandos;
import static app.statics.external.Emojis.preto_SetaC;


public class MessageReceived extends ListenerAdapter {
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        String mensagem[] = event.getMessage().getContentRaw().split(" ");
        String mensagemInteira = event.getMessage().getContentRaw();
        if (mensagem[0].startsWith(prefixo)) {
            String comando = mensagem[0].substring(3);
            if (!comando.equals("clear")) {
                excluirMensagem(event.getMessage());
                enviarLog(event.getMember(), mensagemInteira, event.getChannel());

            }
            System.out.println(event.getMember().getEffectiveName() + " usou o comando: " + comando);

        }
        for (String prefixo: prefixos) {
            if (mensagem[0].contains(prefixo)){
                enviarLog(event.getMember(), mensagemInteira, event.getChannel());
            }

        }
        if (prefixos.stream().anyMatch(mensagem[0]::startsWith)){
            excluirMensagem(event.getMessage());
        }


    }
    private void excluirMensagem(Message message) {
        synchronized (this) {
            try {
                wait(1); // Espera 1 milissegundo para garantir que a mensagem esteja visível no log antes de ser excluída
                message.delete().queue();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private void enviarLog(Member member, String comando, Channel canal)
    {
        logComandos.sendMessage(preto_SetaC.getFormatted() + " " + member.getEffectiveName() + " `" + member.getId() + "` utilizou o comando: ```" + comando + "```Em: " + canal.getAsMention()).queue();
    }
}
