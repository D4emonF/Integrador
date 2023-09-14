package app.events.bot;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.List;

import static app.statics.Basics.ygd;
import static app.statics.Functions.bancoLocal;
import static app.statics.Functions.salvarIdDoUsuario;
import static app.statics.canais.Geral.canalLucius;
import static app.statics.cargos.Funcionais.cargoMembro;

public class OnReady extends ListenerAdapter {
    @Override
    public void onReady(ReadyEvent event) {
            ygd.getAudioManager().openAudioConnection(canalLucius);
            bancoLocal("verificados");
            List<Member> verificados = ygd.getMembersWithRoles(cargoMembro);
            for (Member verificado: verificados) {
                salvarIdDoUsuario("verificados" ,verificado.getId());
            }
    }
}
