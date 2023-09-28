package app;


import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;

import java.util.EnumSet;

import static app.commands.mannagers.MessageReceived.iniciaComandos;
import static app.events.Mannager.iniciaEvents;
import static app.statics.Basics.token;

public class App {

        public static JDA jda = JDABuilder.create(token, EnumSet.allOf(GatewayIntent.class)).build();
    public static void main(String[] args) throws InterruptedException {

        iniciaComandos();
        iniciaEvents();

        jda.awaitReady();
        jda.getPresence().setPresence(Activity.watching("You got DENIED"), true);
    }
}
