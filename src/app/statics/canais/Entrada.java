package app.statics.canais;

import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

import static app.statics.Basics.ygd;

public interface Entrada {
    TextChannel canalVerificacao = ygd.getTextChannelById("1145330993675370566");
    TextChannel canalFichasVerificacao = ygd.getTextChannelById("1145347423695290448");
}
