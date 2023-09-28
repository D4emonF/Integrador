package app.events;

import app.commands.mannagers.MessageReceived;
import app.commands.mannagers.SlashCommand;
import app.commands.prefixCommands.embeds.VerificacaoInstagram;
import app.commands.prefixCommands.mod.social.TicketVerificacao;
import app.commands.prefixCommands.vips.CallView;
import app.events.bot.Buttons.Minecraft;
import app.events.bot.OnGuildReady;
import app.events.bot.OnReady;
import app.events.functions.AutoClear;
import app.events.functions.social.Instagram;
import app.events.guild.MemberJoin;
import app.events.guild.VoiceJoin;
import app.events.guild.VoiceLeft;
import app.events.guild.VoiceMove;
import app.protecoes.BanNoDedo;
import app.protecoes.Cargos;
import app.protecoes.UrlSaver;

import static app.App.jda;

public class Mannager {
    public static void iniciaEvents(){
        jda.addEventListener(new OnReady());
        jda.addEventListener(new OnGuildReady());
        jda.addEventListener(new MessageReceived());
        jda.addEventListener(new VoiceJoin());
        jda.addEventListener(new VoiceLeft());
        jda.addEventListener(new VoiceMove());

        jda.addEventListener(new MemberJoin());
        jda.addEventListener(new AutoClear());
        jda.addEventListener(new SlashCommand());
        jda.addEventListener(new Instagram());
        jda.addEventListener(new VerificacaoInstagram());
        jda.addEventListener(new TicketVerificacao());
        jda.addEventListener(new BanNoDedo());
        jda.addEventListener(new Cargos());
        jda.addEventListener(new UrlSaver());
        jda.addEventListener(new CallView());
        jda.addEventListener(new Minecraft());
    }
}
