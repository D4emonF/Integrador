package app.events.bot;

import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import static app.configs.Intialize.bot;

public class OnReady extends ListenerAdapter {
    @Override
    public void onReady(ReadyEvent event) {
        System.out.println("Bot online!");
        bot.getPresence().setPresence(Activity.customStatus("Para ajuda, me marque!"), true);

    }

    @Override
    public String toString() {
        return "Evento - OnReady";
    }
}
