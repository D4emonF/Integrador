package app.protecoes;

import net.dv8tion.jda.api.audit.AuditLogEntry;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.guild.GuildBanEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.List;

import static app.statics.Basics.ygd;

public class BanNoDedo extends ListenerAdapter {
    @Override
    public void onGuildBan(GuildBanEvent event) {
        Member moderador = ygd.getMember(event.getGuild().retrieveAuditLogs().complete().get(0).getUser());
        if (!event.getGuild().retrieveAuditLogs().complete().get(0).getUser().isBot() || moderador.isOwner()){
            List<Role> cargos = moderador.getRoles();
            for (Role cargo: cargos) {
                ygd.removeRoleFromMember(moderador, cargo).reason("Proteção contra ban no dedo.").queue();
            }
        }
    }
}
