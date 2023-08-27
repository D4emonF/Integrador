package app;

import app.commands.mannagers.MessageReceived;
import app.commands.prefixCommands.embeds.VerificationEmbed;
import app.commands.prefixCommands.mod.Clear;
import app.events.guild.VoiceJoin;
import app.events.guild.VoiceLeft;
import app.events.guild.VoiceMove;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;

import java.util.EnumSet;

import static app.statics.Basics.token;

public class App {

        public static JDA jda = JDABuilder.create(token, EnumSet.allOf(GatewayIntent.class)).build();
    public static void main(String[] args) throws InterruptedException {
        jda.addEventListener(new MessageReceived());
        jda.addEventListener(new VoiceJoin());
        jda.addEventListener(new VoiceLeft());
        jda.addEventListener(new VoiceMove());
        jda.addEventListener(new VerificationEmbed());
        jda.addEventListener(new Clear());
    }
}
