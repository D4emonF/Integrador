package app.events.bot;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.ArrayList;
import java.util.List;

import static app.statics.Basics.ygd;
import static app.statics.Functions.bancoLocal;
import static app.statics.Functions.salvarIdDoUsuario;
import static app.statics.cargos.Funcionais.cargoVerificado;

public class OnReady extends ListenerAdapter {
    @Override
    public void onReady(ReadyEvent event) {

    }
}
