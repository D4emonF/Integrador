package app.statics.canais;

import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

import static app.statics.Basics.ygd;

public interface Geral
{
    TextChannel canalGeral = ygd.getTextChannelById("1145367887331201105");
    TextChannel canalComandos = ygd.getTextChannelById("1145501822669299732");
}
