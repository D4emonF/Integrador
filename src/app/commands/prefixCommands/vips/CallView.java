package app.commands.prefixCommands.vips;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceUpdateEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import static app.statics.canais.Geral.categoriaVips;

public class CallView extends ListenerAdapter {
    @Override
    public void onGuildVoiceUpdate(GuildVoiceUpdateEvent event) {
        if (event.getChannelJoined() != null && event.getChannelLeft() == null) {
            if (event.getChannelJoined().getParentCategory().equals(categoriaVips)){
                if (!event.getChannelJoined().getMembers().isEmpty()){
                    event.getChannelJoined().asVoiceChannel().upsertPermissionOverride(event.getGuild().getPublicRole()).grant(Permission.VIEW_CHANNEL).queue();
                }
            }
        }
        if (event.getChannelJoined() == null && event.getChannelLeft() != null) {
            if (event.getChannelLeft().getParentCategory().equals(categoriaVips)) {
                if (event.getChannelLeft().getMembers().isEmpty()) {
                    event.getChannelLeft().asVoiceChannel().upsertPermissionOverride(event.getGuild().getPublicRole()).deny(Permission.VIEW_CHANNEL).queue();
                }
            }
        }
    }
}
