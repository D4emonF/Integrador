package app.events.functions;

import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.concurrent.TimeUnit;

import static app.statics.Basics.prefixo;
import static app.statics.Basics.prefixos;
import static app.statics.canais.Geral.canalComandos;
import static app.statics.canais.Geral.canalKosame;

public class AutoClear extends ListenerAdapter {


    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        String[] mensagem = event.getMessage().getContentRaw().split(" ");
        if (!event.getMessage().getContentRaw().startsWith(prefixo) || prefixos.stream().anyMatch(mensagem[0]::startsWith)) {
            autoClear(event, canalComandos, 30, TimeUnit.SECONDS);
            autoClear(event, canalKosame, 5, TimeUnit.MINUTES);
        }

    }
    public static void autoClear(MessageReceivedEvent event, TextChannel canal, int delay, TimeUnit timeUnit){
        if (event.getMessage().getChannel().equals(canal)){
            event.getMessage().delete().queueAfter(delay, timeUnit);
        }
    }
}
