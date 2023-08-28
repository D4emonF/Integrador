package app.commands.mannagers;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import static app.statics.Basics.prefixo;
import static app.statics.Basics.prefixos;


public class MessageReceived extends ListenerAdapter {
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        String mensagem[] = event.getMessage().getContentRaw().split(" ");
        if (mensagem[0].startsWith(prefixo)) {
            String comando = mensagem[0].substring(3);
            if (!comando.equals("clear")) {
                excluirMensagem(event.getMessage());
            }
            System.out.println(event.getMember().getEffectiveName() + " usou o comando: " + comando);

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
}
