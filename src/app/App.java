package app;

import app.commands.mannagers.MessageReceived;
import app.commands.mannagers.SlashCommand;
import app.commands.prefixCommands.embeds.VerificacaoInstagram;
import app.commands.prefixCommands.embeds.VerificationEmbed;
import app.commands.prefixCommands.misc.Afk;
import app.commands.prefixCommands.misc.Roleta;
import app.commands.prefixCommands.misc.Soco;
import app.commands.prefixCommands.misc.UserInfo;
import app.commands.prefixCommands.mod.*;
import app.commands.prefixCommands.mod.ban.Ban;
import app.commands.prefixCommands.mod.ban.GifBan;
import app.commands.prefixCommands.mod.ban.UnBan;
import app.commands.prefixCommands.mod.cargos.AddCargo;
import app.commands.prefixCommands.mod.cargos.Cargo;
import app.commands.prefixCommands.mod.cargos.RemoveCargo;
import app.commands.prefixCommands.mod.social.TicketVerificacao;
import app.commands.prefixCommands.mod.social.Verificar;
import app.commands.prefixCommands.vips.Addvip;
import app.commands.prefixCommands.vips.RemoveVip;
import app.commands.prefixCommands.vips.Vip;
import app.commands.slash.guild.EmbedCreator;
import app.commands.slash.misc.Boosters;
import app.commands.slash.misc.EmojiInfo;
import app.commands.slash.misc.Ping;
import app.commands.slash.misc.Sorteio;
import app.events.bot.OnGuildReady;
import app.events.bot.OnReady;
import app.events.functions.AutoClear;
import app.events.functions.social.Instagram;
import app.events.guild.*;
import app.protecoes.BanNoDedo;
import app.protecoes.Cargos;
import app.protecoes.UrlSaver;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;

import java.util.EnumSet;

import static app.statics.Basics.token;
import static app.statics.cargos.Funcionais.cargoVerificado;

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
        jda.addEventListener(new AddCargo());
        jda.addEventListener(new RemoveCargo());
        jda.addEventListener(new Afk());
        jda.addEventListener(new Instagram());
        jda.addEventListener(new Soco());
        jda.addEventListener(new Verificar());
        jda.addEventListener(new VerificacaoInstagram());
        jda.addEventListener(new TicketVerificacao());
        jda.addEventListener(new BanNoDedo());
        jda.addEventListener(new Cargos());
        jda.addEventListener(new UrlSaver());
        jda.addEventListener(new Sorteio());
        jda.addEventListener(new Ping());
        jda.addEventListener(new Boosters());
        jda.addEventListener(new UserInfo());
        jda.addEventListener(new Vip());
        jda.addEventListener(new Addvip());
        jda.addEventListener(new RemoveVip());


        jda.awaitReady();
        jda.getPresence().setPresence(Activity.watching("You got DENIED"), true);
    }
}
