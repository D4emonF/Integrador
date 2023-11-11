package app.commands.mannagers;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.time.LocalDateTime;

import static app.configs.Intialize.prefixo;
import static app.db.update.Update.salvaMensagem;
import static app.statics.Functions.gerarTimestamp;

public class MessageReceived extends ListenerAdapter {
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (!event.getAuthor().isBot()) {
            salvaMensagem(event.getMessageId(), event.getMessage().getContentRaw(), event.getMember(), gerarTimestamp(LocalDateTime.now()), event.getChannel().getId());
            String mensagem[] = event.getMessage().getContentRaw().split(" ");
            String mensagemInteira = event.getMessage().getContentRaw();
            if (mensagem[0].startsWith(prefixo)) {
                String comando = mensagem[0].substring(3);
                if (!comando.equals("clear")) {
                    excluirMensagem(event.getMessage());

                }
                System.out.println(event.getMember().getEffectiveName() + " usou o comando: " + comando);

            }
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

    @Override
    public String toString() {
        return "Manager - MessageReceived";
    }
}
