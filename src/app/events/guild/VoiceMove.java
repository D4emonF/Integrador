package app.events.guild;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.channel.unions.AudioChannelUnion;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceUpdateEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.awt.*;
import java.time.LocalDateTime;
import java.util.Objects;

import static app.statics.Basics.ygd;
import static app.statics.Functions.gerarTimestamp;
import static app.statics.canais.Logs.logTrafego;

public class VoiceMove extends ListenerAdapter
{
    @Override
    public void onGuildVoiceUpdate(GuildVoiceUpdateEvent event) {
        AudioChannelUnion channelJoined = event.getChannelJoined();
        AudioChannelUnion channelLeft = event.getChannelLeft();
        String memberMention = event.getMember().getAsMention();
        String memberEffectiveName = event.getMember().getEffectiveName();
        if (event.getGuild().equals(ygd)) {
            if (event.getChannelLeft() != null && event.getChannelJoined() != null) {
                LocalDateTime dateTime = LocalDateTime.now();
                EmbedBuilder embed = new EmbedBuilder()
                        .setColor(Color.orange)
                        .setTitle("<:preto_audio:1124562092167540787> | " + "Moveu de canal")
                        .setDescription("Moveu do canal " + channelLeft.getAsMention() + " `" + channelLeft.getName() + "` para " + channelJoined.getAsMention() + " `" + channelJoined.getName() + "`")
                        .addField("Hora", "<t:" + gerarTimestamp(dateTime) + ">", false)
                        .setFooter(ygd.getName(), Objects.requireNonNull(ygd).getIconUrl())
                        .addField("**<:preto_membro:1124563263439507538> Membro:**", memberMention + " `" + memberEffectiveName + "`", false);

                Objects.requireNonNull(logTrafego).sendMessage("").setEmbeds(embed.build()).queue();
            }
        }
    }
}
