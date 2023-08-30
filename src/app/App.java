package app;

import app.commands.mannagers.MessageReceived;
import app.commands.mannagers.SlashCommand;
import app.commands.prefixCommands.embeds.VerificationEmbed;
import app.commands.prefixCommands.misc.Roleta;
import app.commands.prefixCommands.mod.Cargo;
import app.commands.prefixCommands.mod.Clear;
import app.commands.slash.guild.EmbedCreator;
import app.events.bot.OnGuildReady;
import app.events.bot.OnReady;
import app.events.functions.AutoClear;
import app.events.guild.*;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;

import java.util.EnumSet;

import static app.statics.Basics.token;

public class App {

        public static JDA jda = JDABuilder.create(token, EnumSet.allOf(GatewayIntent.class)).build();
    public static void main(String[] args) throws InterruptedException {

        jda.addEventListener(new OnReady());
        jda.addEventListener(new OnGuildReady());

        jda.addEventListener(new MessageReceived());
        jda.addEventListener(new VoiceJoin());
        jda.addEventListener(new VoiceLeft());
        jda.addEventListener(new VoiceMove());
        jda.addEventListener(new VerificationEmbed());
        jda.addEventListener(new Clear());
        jda.addEventListener(new Verificacao());
        jda.addEventListener(new MemberJoin());
        jda.addEventListener(new AutoClear());
        jda.addEventListener(new Roleta());
        jda.addEventListener(new SlashCommand());
        jda.addEventListener(new EmbedCreator());
        jda.addEventListener(new Cargo());

        jda.awaitReady();
        jda.getPresence().setPresence(Activity.watching("You got DENIED"), true);
    }
}
