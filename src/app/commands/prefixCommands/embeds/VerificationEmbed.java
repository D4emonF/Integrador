package app.commands.prefixCommands.embeds;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.List;


import static app.statics.Basics.prefixo;
import static app.statics.canais.Entrada.canalVerificacao;
import static app.statics.cargos.Funcionais.cargoVerificador;
import static app.statics.external.ColorPalette.*;

public class VerificationEmbed extends ListenerAdapter {
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        String[] mensagem = event.getMessage().getContentRaw().split(" ");
        if (mensagem[0].startsWith(prefixo + "vf")) {
            if (event.getMember().hasPermission(Permission.ADMINISTRATOR)) {

                List<Message> historico = canalVerificacao.getHistory().retrievePast(100).complete();
                canalVerificacao.purgeMessages(historico);


                EmbedBuilder v = new EmbedBuilder();
                v.setTitle("<:preto_membro:1124563263439507538> | Verificação");
                v.setDescription("> Envie um \".\" e espere algum "+ cargoVerificador.getAsMention() +" te verificar no servidor");
                v.setColor(monteCarlo);
                canalVerificacao.sendMessage("").setEmbeds(v.build()).queue();
            }
        }
    }
}
