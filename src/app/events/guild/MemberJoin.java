package app.events.guild;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static app.statics.Basics.ygd;
import static app.statics.Functions.carregarIdsDosUsuarios;
import static app.statics.canais.Entrada.canalVerificacao;
import static app.statics.cargos.Funcionais.cargoVerificado;

public class MemberJoin extends ListenerAdapter {
    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent event) {
        List<String> ids = new ArrayList<>();
        carregarIdsDosUsuarios("verificados", ids);
        if (ids.contains(event.getMember().getId()))
        {
            ygd.addRoleToMember(event.getMember(), cargoVerificado).queue();
        }
        else
        {
            canalVerificacao.sendMessage(event.getMember().getAsMention()).queue(message -> message.delete().queueAfter(1, TimeUnit.SECONDS));
        }
    }
}
