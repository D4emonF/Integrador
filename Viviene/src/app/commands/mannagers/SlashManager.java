package app.commands.mannagers;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.ArrayList;
import java.util.List;

public class SlashManager extends ListenerAdapter {
    private List<SCommand> commands = new ArrayList<>();

    @Override
    public void onReady(ReadyEvent event) {
        for (Guild guild : event.getJDA().getGuilds()) {
            for (SCommand command : commands) {
                guild.upsertCommand(command.getName(), command.getDescription()).addOptions(command.getOptions()).queue();
            }
        }
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        for (SCommand command : commands) {
            if (command.getName().equals(event.getName())) {
                command.execute(event);
                return;
            }
        }
    }

    public void add(SCommand command) {
        commands.add(command);
    }

    protected String getComamnds(){
        StringBuilder sb = new StringBuilder();

        for (SCommand command: commands
             ) {
            if (command.equals(commands.get(0))) {
                sb.append("ComandoSlash - ").append(command.getName());
            }
            else {
                sb.append("\n").append("ComandoSlash - ").append(command.getName());
            }
        }
        if (commands.isEmpty()){

        }
        return sb.toString();
    }

    @Override
    public String toString() {
        return getComamnds();
    }
}
