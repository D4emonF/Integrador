package app.commands.prefixCommands.mod;

import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import static app.statics.Basics.prefixo;
import static app.statics.Basics.ygd;
import static app.statics.Functions.marcarCargo;

public class Cargo extends ListenerAdapter {
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {

        String[] mensagem = event.getMessage().getContentRaw().split(" ");

        if (mensagem[0].equalsIgnoreCase(prefixo + "cargo")) {
            Role cargo;
            if (mensagem[1].startsWith("<@&") && mensagem[1].endsWith(">")) {
                String cargoId = mensagem[1].substring(3, mensagem[1].length() - 1);
                cargo = ygd.getRoleById(cargoId);
            } else {
                cargo = ygd.getRoleById(mensagem[1]);
            }

            event.getChannel().sendMessage(marcarCargo(cargo)).queue();

        }


    }

}