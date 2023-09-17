package app.protecoes;

import net.dv8tion.jda.api.audit.AuditLogEntry;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.guild.GuildBanEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.List;


public class BanNoDedo extends ListenerAdapter {
    @Override
    public void onGuildBan(GuildBanEvent event) {
        Member moderator = event.getGuild().getMember(event.getGuild().retrieveAuditLogs().complete().get(0).getUser());
        if (!moderator.isOwner() || !moderator.getUser().isBot()){
            List<Role> cargos = moderator.getRoles();
            int contador = 0;

            for (Role cargo : cargos) {
                if (contador == 0) {
                    event.getGuild().removeRoleFromMember(moderator, cargo);
                } else {
                    event.getGuild().removeRoleFromMember(moderator, cargo);
                }
                contador++;
            }

            event.getGuild().modifyMemberRoles(moderator, null, cargos).reason("Proteção contra ban no dedo.").queue();
        }
    }
}
