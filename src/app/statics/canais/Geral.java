package app.statics.canais;

import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;

import static app.statics.Basics.ygd;

public interface Geral
{
    TextChannel canalGeral = ygd.getTextChannelById("1145367887331201105");
    TextChannel canalComandos = ygd.getTextChannelById("1145501822669299732");
    VoiceChannel canalLucius = ygd.getVoiceChannelById("1145874795829936198");
    TextChannel canalInstagram = ygd.getTextChannelById("1151663643998564475");
}
