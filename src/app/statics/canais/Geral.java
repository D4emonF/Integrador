package app.statics.canais;

import net.dv8tion.jda.api.entities.channel.concrete.Category;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;

import static app.statics.Basics.ygd;

public interface Geral
{
    TextChannel canalGeral = ygd.getTextChannelById("1145367887331201105");
    TextChannel canalComandos = ygd.getTextChannelById("1145501822669299732");
    TextChannel canalComandosVip = ygd.getTextChannelById("1153291856013168650");
    TextChannel canalKosame = ygd.getTextChannelById("1153421462003253341");
    VoiceChannel canalLucius = ygd.getVoiceChannelById("1145874795829936198");
    TextChannel canalInstagram = ygd.getTextChannelById("1151663643998564475");
    TextChannel canalAcessoMine = ygd.getTextChannelById("1155283120690561125");
    Category categoriaVips = ygd.getCategoryById("1152993108879999137");

}
