package app.commands.slash.misc;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;


import static app.App.jda;

public class Ping extends ListenerAdapter {
    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (event.getName().equals("ping")){
            event.reply("Meu ping atual é: " + jda.getGatewayPing() + "ms!").setEphemeral(true).queue();
        }
    }
}
