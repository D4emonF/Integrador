package app.commands.mannagers;

import app.commands.prefixCommands.embeds.AccesMinecraft;
import app.commands.prefixCommands.embeds.VerificacaoInstagram;
import app.commands.prefixCommands.embeds.VerificationEmbed;
import app.commands.prefixCommands.misc.Afk;
import app.commands.prefixCommands.misc.Roleta;
import app.commands.prefixCommands.misc.Soco;
import app.commands.prefixCommands.misc.UserInfo;
import app.commands.prefixCommands.mod.Clear;
import app.commands.prefixCommands.mod.ban.Ban;
import app.commands.prefixCommands.mod.ban.GifBan;
import app.commands.prefixCommands.mod.ban.UnBan;
import app.commands.prefixCommands.mod.cargos.AddCargo;
import app.commands.prefixCommands.mod.cargos.Cargo;
import app.commands.prefixCommands.mod.cargos.RemoveCargo;
import app.commands.prefixCommands.mod.social.TicketVerificacao;
import app.commands.prefixCommands.mod.social.Verificar;
import app.commands.prefixCommands.vips.Addvip;
import app.commands.prefixCommands.vips.CallView;
import app.commands.prefixCommands.vips.RemoveVip;
import app.commands.prefixCommands.vips.Vip;
import app.commands.slash.guild.EmbedCreator;
import app.commands.slash.misc.Boosters;
import app.commands.slash.misc.EmojiInfo;
import app.commands.slash.misc.Ping;
import app.commands.slash.misc.Sorteio;
import app.events.bot.Buttons.Minecraft;
import app.events.bot.OnGuildReady;
import app.events.bot.OnReady;
import app.events.functions.AutoClear;
import app.events.functions.social.Instagram;
import app.events.guild.*;
import app.protecoes.BanNoDedo;
import app.protecoes.Cargos;
import app.protecoes.UrlSaver;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.Channel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.time.LocalDateTime;

import static app.App.jda;
import static app.statics.Basics.prefixo;
import static app.statics.Basics.prefixos;
import static app.statics.Functions.gerarTimestamp;
import static app.statics.canais.Logs.logComandos;
import static app.statics.cargos.Hierarquia.getHierarquia;
import static app.statics.external.ColorPalette.monteCarlo;


public class MessageReceived extends ListenerAdapter {

    public static void iniciaComandos(){
        jda.addEventListener(new VerificationEmbed());
        jda.addEventListener(new Clear());
        jda.addEventListener(new Verificacao());
        jda.addEventListener(new Roleta());
        jda.addEventListener(new EmbedCreator());
        jda.addEventListener(new Cargo());
        jda.addEventListener(new Ban());
        jda.addEventListener(new UnBan());
        jda.addEventListener(new GifBan());
        jda.addEventListener(new EmojiInfo());
        jda.addEventListener(new AddCargo());
        jda.addEventListener(new RemoveCargo());
        jda.addEventListener(new Afk());
        jda.addEventListener(new Soco());
        jda.addEventListener(new Verificar());
        jda.addEventListener(new Sorteio());
        jda.addEventListener(new Ping());
        jda.addEventListener(new Boosters());
        jda.addEventListener(new UserInfo());
        jda.addEventListener(new Vip());
        jda.addEventListener(new Addvip());
        jda.addEventListener(new RemoveVip());
        jda.addEventListener(new AccesMinecraft());
    }
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        String mensagem[] = event.getMessage().getContentRaw().split(" ");
        String mensagemInteira = event.getMessage().getContentRaw();
        if (mensagem[0].startsWith(prefixo)) {
            String comando = mensagem[0].substring(3);
            if (!comando.equals("clear")) {
                excluirMensagem(event.getMessage());
                enviarLog(event.getMember(), mensagemInteira, event.getChannel());

            }
            System.out.println(event.getMember().getEffectiveName() + " usou o comando: " + comando);

        }
        for (String prefixo: prefixos) {
            if (mensagem[0].contains(prefixo)){
                enviarLog(event.getMember(), mensagemInteira, event.getChannel());
            }

        }
        if (prefixos.stream().anyMatch(mensagem[0]::startsWith)){
            excluirMensagem(event.getMessage());
        }


    }
    private void excluirMensagem(Message message) {
        synchronized (this) {
            try {
                wait(1); // Espera 1 milissegundo para garantir que a mensagem esteja visível no log antes de ser excluída
                message.delete().queue();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private void enviarLog(Member member, String comando, Channel canal)
    {
        EmbedBuilder log = new EmbedBuilder();
        log
                .setColor(monteCarlo)
                .setTitle("<:cinza_chat:1154769616543961189> | Comando utilizado")
                .addField("<:preto_membro:1154843741891338282> Membro", member.getAsMention() + " | `" + member.getId() + "`", true)
                .addField("<:cinza_hashtag:1154769641336479814> Comando","`" + comando+ "`", true)
                .addField("<:cinza_chat:1154769616543961189> Canal", canal.getAsMention() + " | `" + canal.getName()+ "`", true)
                .addField("<:preto_calendario:1154843611020677151> Horario", "<t:" + gerarTimestamp(LocalDateTime.now()) + ">", true)
                .setThumbnail(member.getEffectiveAvatar().getUrl());


        logComandos.sendMessage("").setEmbeds(log.build()).queue();
    }
}
