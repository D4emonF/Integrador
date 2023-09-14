package app.statics.canais;

import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

import static app.statics.Basics.ygd;

public interface Logs {
    TextChannel logTrafego = ygd.getTextChannelById("1145146842506592370");
    TextChannel logComandos = ygd.getTextChannelById("1145338924043083776");
    TextChannel logBanimentos = ygd.getTextChannelById("1147954234764042260");
    TextChannel logVerificacoes = ygd.getTextChannelById("1151888090848297131");
}
