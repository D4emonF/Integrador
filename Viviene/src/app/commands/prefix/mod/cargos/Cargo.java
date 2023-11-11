package app.commands.prefix.mod.cargos;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.sql.SQLException;
import java.util.concurrent.TimeUnit;

import static app.configs.Intialize.prefixo;
import static app.db.update.Update.salvaCargo;
import static app.statics.Functions.*;

public class Cargo extends ListenerAdapter {
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {

        String[] mensagem = event.getMessage().getContentRaw().split(" ");

        if (mensagem[0].equalsIgnoreCase(prefixo + "listacargo")) {
            event.getChannel().sendMessage(marcarCargo(obterCargo(event), event.getGuild())).queue();
        }

        if (mensagem[0].equalsIgnoreCase(prefixo+ "configcargo") && (mensagem.length > 1)){
            try {
                if (possuiPerm(event.getMember(), Permission.ADMINISTRATOR))
                {

                    try {
                        salvaCargo(obterCargo(event), Integer.valueOf(mensagem[1]));
                        event.getMessage().reply("Cargo salvo no banco de dados!").queue(message -> message.delete().queueAfter(7, TimeUnit.SECONDS));
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

    }

    @Override
    public String toString() {
        return "Comandos - Cargo";
    }
}
