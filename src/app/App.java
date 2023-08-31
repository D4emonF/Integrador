package app;

import app.commands.mannagers.MessageReceived;
import app.commands.mannagers.SlashCommand;
import app.commands.prefixCommands.embeds.VerificationEmbed;
import app.commands.prefixCommands.misc.Roleta;
import app.commands.prefixCommands.mod.*;
import app.commands.prefixCommands.mod.ban.Ban;
import app.commands.prefixCommands.mod.ban.GifBan;
import app.commands.prefixCommands.mod.ban.UnBan;
import app.commands.slash.guild.EmbedCreator;
import app.commands.slash.misc.EmojiInfo;
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
        jda.addEventListener(new Ban());
        jda.addEventListener(new UnBan());
        jda.addEventListener(new GifBan());
        jda.addEventListener(new EmojiInfo());

        jda.awaitReady();
        jda.getPresence().setPresence(Activity.watching("You got DENIED"), true);
    }
}
