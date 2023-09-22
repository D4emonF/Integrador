package app.events.guild;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.channel.unions.AudioChannelUnion;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceUpdateEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.awt.*;
import java.time.LocalDateTime;
import java.util.Objects;

import static app.App.jda;
import static app.statics.Basics.ygd;
import static app.statics.Functions.gerarTimestamp;
import static app.statics.canais.Logs.logTrafego;

public class VoiceLeft extends ListenerAdapter
{
    @Override
    public void onGuildVoiceUpdate(GuildVoiceUpdateEvent event) {
        AudioChannelUnion channelLeft = event.getChannelLeft();
        String memberMention = event.getMember().getAsMention();
        String memberEffectiveName = event.getMember().getEffectiveName();
        if (event.getGuild().equals(ygd)) {
            if (event.getChannelLeft() != null && event.getChannelJoined() == null) {
                if (!event.getMember().getUser().equals(jda.getSelfUser())) {

                    LocalDateTime dateTime = LocalDateTime.now();
                    EmbedBuilder embed = new EmbedBuilder()
                            .setColor(Color.red)
                            .addField("**<:preto_membro:1154843741891338282> Membro:**", memberMention + " `" + memberEffectiveName + "`", false)
                            .setTitle("<:preto_audio:1154843570528862248> | " + "Saiu do canal")
                            .addField("<:cinza_chat:1154769616543961189> Canal", event.getChannelLeft().getAsMention() + " | `" + event.getChannelLeft().getName()+ "`", true)
                            .addField("<:preto_calendario:1154843611020677151> Hora", "<t:" + gerarTimestamp(dateTime) + ">", false)
                            .setFooter(ygd.getName(), Objects.requireNonNull(ygd).getIconUrl());

                    Objects.requireNonNull(logTrafego).sendMessage("").setEmbeds(embed.build()).queue();
                }
            }
        }
    }
}
