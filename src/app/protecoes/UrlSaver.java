package app.protecoes;

import net.dv8tion.jda.api.audit.ActionType;
import net.dv8tion.jda.api.audit.AuditLogEntry;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.guild.update.GuildUpdateVanityCodeEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.concurrent.TimeUnit;


public class UrlSaver extends ListenerAdapter {

    @Override
    public void onGuildUpdateVanityCode(GuildUpdateVanityCodeEvent event) {
        AuditLogEntry logEntry = null;
        if (event.getGuild().retrieveAuditLogs().complete().get(0).getType().equals(ActionType.GUILD_UPDATE)){
            logEntry = (event.getGuild().retrieveAuditLogs().complete().get(0));
        }
        Member membro = event.getGuild().getMember(logEntry.getUser());
        if (!membro.isOwner()){
            event.getGuild().ban(membro, 1, TimeUnit.MILLISECONDS).reason("Alteração de url").queue();
        }
    }
}
