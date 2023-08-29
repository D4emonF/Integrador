package app.events.bot;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.List;

import static app.statics.Basics.ygd;
import static app.statics.Functions.bancoLocal;
import static app.statics.Functions.salvarIdDoUsuario;
import static app.statics.canais.Geral.canalLucius;
import static app.statics.cargos.Funcionais.cargoVerificado;

public class OnGuildReady extends ListenerAdapter {
    @Override
    public void onGuildReady(GuildReadyEvent event) {
        if (event.getGuild().equals(ygd)){
            ygd.getAudioManager().openAudioConnection(canalLucius);
            bancoLocal("verificados");
            List<Member> verificados = ygd.getMembersWithRoles(cargoVerificado);
            for (Member verificado: verificados) {
                salvarIdDoUsuario("verificados" ,verificado.getId());
            }
        }

    }
}
