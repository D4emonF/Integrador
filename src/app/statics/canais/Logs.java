package app.statics.canais;

import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

import static app.statics.Basics.ygd;

public interface Logs {
    TextChannel logTrafego = ygd.getTextChannelById("1145146842506592370");
}
