package app.commands.prefix.bot;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.ObjectNode;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;


import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.concurrent.TimeUnit;

import static app.configs.Intialize.prefixo;
import static app.statics.Functions.possuiPerm;

public class Prefix extends ListenerAdapter {
    ObjectMapper objectMapper = new ObjectMapper();
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        String[] mensagem = event.getMessage().getContentRaw().split(" ");

        try {
            ObjectNode config = objectMapper.readValue(new File(("config.json")), ObjectNode.class);
            if (mensagem[0].equalsIgnoreCase(prefixo + "setprefix")){
                int i = 0;
                for (Role cargo: event.getMember().getRoles()
                ) {
                    try {
                        if (possuiPerm(cargo, Permission.MANAGE_SERVER)){
                            config.put("prefixo", mensagem[1]);
                            ObjectWriter objectWriter = objectMapper.writerWithDefaultPrettyPrinter();
                            objectWriter.writeValue(new File("config.json"), config);
                            prefixo = config.get("prefixo").asText();
                            event.getMessage().reply("Novo prefixo definido: " + prefixo).queue(message -> message.delete().queueAfter(7, TimeUnit.SECONDS));
                            break;
                        }

                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    i++;
                    if (i == event.getMember().getRoles().size()){
                        event.getMessage().reply("Você não possui permissão para executar esse comando").queue(message -> message.delete().queueAfter(5, TimeUnit.SECONDS));
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public String toString() {
        return "Comando - Prefix";
    }
}
