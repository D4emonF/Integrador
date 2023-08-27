package app.events.functions;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.Category;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceUpdateEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.ArrayList;
import java.util.List;

import static app.App.jda;
import static app.statics.Basics.ygd;

public class CriarCall {
    public static void CriarCall(Member membro, Category categoria, VoiceChannel canalCriacao, int userLimit, String prefixo){
        categoria.createVoiceChannel(prefixo + membro.getEffectiveName()).queue(voiceChannel -> {
            List<Permission> permsAllow = new ArrayList<>();
            permsAllow.add(Permission.MANAGE_PERMISSIONS);
            permsAllow.add(Permission.MANAGE_CHANNEL);
            permsAllow.add(Permission.VOICE_MOVE_OTHERS);
            permsAllow.add(Permission.VOICE_MUTE_OTHERS);
            permsAllow.add(Permission.VOICE_DEAF_OTHERS);
            permsAllow.add(Permission.VIEW_CHANNEL);
            permsAllow.add(Permission.VOICE_CONNECT);
                    voiceChannel
                            .upsertPermissionOverride(membro)
                            .grant(permsAllow)
                            .queue();
            voiceChannel.getManager().setUserLimit(userLimit).queue();
                    ygd.moveVoiceMember(membro, voiceChannel).queue();
                }
        );
    }
    public static void CriarCall(Member membro, Category categoria, int userLimit, String prefixo, String prefixoNome){
        User user = membro.getUser();

        categoria.createVoiceChannel(prefixo + user.getEffectiveName()).queue(voiceChannel -> {

                    List<Permission> permsAllow = new ArrayList<>();
                    permsAllow.add(Permission.MANAGE_PERMISSIONS);
                    permsAllow.add(Permission.MANAGE_CHANNEL);
                    permsAllow.add(Permission.VOICE_MOVE_OTHERS);
                    permsAllow.add(Permission.VOICE_MUTE_OTHERS);
                    permsAllow.add(Permission.VOICE_DEAF_OTHERS);
                    permsAllow.add(Permission.VIEW_CHANNEL);
                    permsAllow.add(Permission.VOICE_CONNECT);
                    voiceChannel
                            .upsertPermissionOverride(membro)
                            .grant(permsAllow)
                            .queue();
                    voiceChannel.getManager().setUserLimit(userLimit).queue();
                    if (membro.getVoiceState().inAudioChannel()) {
                        ygd.moveVoiceMember(membro, voiceChannel).queue();
                    }else {
                        user.openPrivateChannel().queue(privateChannel -> privateChannel.sendMessage(voiceChannel.getJumpUrl()).queue());
                    }
                    if (!ygd.getOwner().equals(membro))
                    membro.modifyNickname(prefixoNome + user.getEffectiveName()).queue();

                    jda.addEventListener(new ListenerAdapter() {
                        @Override
                        public void onGuildVoiceUpdate(GuildVoiceUpdateEvent event) {
                            if (event.getChannelLeft() != null){
                                if (event.getChannelLeft().equals(voiceChannel)){
                                    if (event.getChannelLeft().getMembers().isEmpty()){
                                        event.getChannelLeft().delete().queue();
                                        jda.removeEventListener(this);
                                    }
                                }
                            }
                        }
                    });
                }
        );

    }

    public static void CriarCall(Member membro, Category categoria, int userLimit, String prefixo, String prefixoNome, Member membroRecrutado){
        User user = membro.getUser();

        categoria.createVoiceChannel(prefixo + user.getEffectiveName()).queue(voiceChannel -> {

                    List<Permission> permsAllow = new ArrayList<>();
                    permsAllow.add(Permission.MANAGE_PERMISSIONS);
                    permsAllow.add(Permission.MANAGE_CHANNEL);
                    permsAllow.add(Permission.VOICE_MOVE_OTHERS);
                    permsAllow.add(Permission.VOICE_MUTE_OTHERS);
                    permsAllow.add(Permission.VOICE_DEAF_OTHERS);
                    permsAllow.add(Permission.VIEW_CHANNEL);
                    permsAllow.add(Permission.VOICE_CONNECT);
                    voiceChannel
                            .upsertPermissionOverride(membro)
                            .grant(permsAllow)
                            .queue();
                    voiceChannel.upsertPermissionOverride(membroRecrutado)
                            .grant(Permission.VOICE_CONNECT)
                            .queue();
                    voiceChannel.getManager().setUserLimit(userLimit).queue();
                    if (membro.getVoiceState().inAudioChannel()) {
                        ygd.moveVoiceMember(membro, voiceChannel).queue();
                    }else {
                        user.openPrivateChannel().queue(privateChannel -> privateChannel.sendMessage(voiceChannel.getJumpUrl()).queue());
                    }
                    if (!ygd.getOwner().equals(membro))
                        membro.modifyNickname(prefixoNome + user.getEffectiveName()).queue();

                    jda.addEventListener(new ListenerAdapter() {
                        @Override
                        public void onGuildVoiceUpdate(GuildVoiceUpdateEvent event) {
                            if (event.getChannelLeft() != null){
                                if (event.getChannelLeft().equals(voiceChannel)){
                                    if (event.getChannelLeft().getMembers().isEmpty()){
                                        event.getChannelLeft().delete().queue();
                                        jda.removeEventListener(this);
                                    }
                                }
                            }
                        }
                    });
                }
        );

    }
}
