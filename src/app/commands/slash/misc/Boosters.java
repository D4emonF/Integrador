package app.commands.slash.misc;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.Objects;

public class Boosters extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        String command = event.getName();

        if (command.equals("boosters")){
            event.deferReply().setEphemeral(true).queue();
            String response = "";

            for (Member booster : Objects.requireNonNull(event.getGuild()).getBoosters()){
                response += booster.getAsMention() +"\n";
            }
            event.getHook().sendMessage(response).queue();
        }
    }

}