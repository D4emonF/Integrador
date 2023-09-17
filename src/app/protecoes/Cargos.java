package app.protecoes;

import net.dv8tion.jda.api.audit.ActionType;
import net.dv8tion.jda.api.audit.AuditLogEntry;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRoleRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static app.statics.Basics.ygd;

public class Cargos extends ListenerAdapter {
    Map<String, ActionCounter> actionCounters = new HashMap<>();


    @Override
    public void onGuildMemberRoleRemove(GuildMemberRoleRemoveEvent event) {
        AuditLogEntry logEntry = null;
        if (ygd.retrieveAuditLogs().complete().get(0).getType().equals(ActionType.MEMBER_ROLE_UPDATE)){
            logEntry = (ygd.retrieveAuditLogs().complete().get(0));
        }

        Member moderator = ygd.getMember(logEntry.getUser()); // O membro que removeu o cargo

        // Verifique se o membro-alvo já está sendo rastreado
        if (actionCounters.containsKey(moderator.getId())) {
            ActionCounter counter = actionCounters.get(moderator.getId());

            // Verifique se a ação está dentro do limite de tempo (2 minutos)
            if (counter.isWithinTimeLimit()) {
                // Incremente o contador de ações
                counter.incrementAction();

                // Verifique se o limite de ações foi atingido (por exemplo, 3 vezes)
                if (counter.getActionCount() >= 3) {
                    // Aplique uma punição ao membro, como remover todos os cargos
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

                        event.getGuild().modifyMemberRoles(moderator, null, cargos).reason("Proteção contra remoção de cargos no dedo.").queue();
                    }
                    // Redefina o contador de ações após a punição
                    counter.reset();
                }
            } else {
                // A ação ocorreu fora do limite de tempo, redefina o contador de ações
                counter.reset();
            }
        } else {
            // O membro-alvo não está sendo rastreado, crie um novo contador de ações para ele
            ActionCounter counter = new ActionCounter();
            counter.incrementAction();
            actionCounters.put(moderator.getId(), counter);
        }
    }
}
