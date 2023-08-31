package app.commands.prefixCommands.mod.ban;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import static app.statics.Basics.prefixo;
import static app.statics.Functions.*;
import static app.statics.cargos.Perms.tresEstrelas;

public class GifBan extends ListenerAdapter {
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        String url = "";
        String[] mensagem = event.getMessage().getContentRaw().split(" ");
        if (mensagem[0].equalsIgnoreCase(prefixo+"gifban")) {
            if (event.getMember().getPermissions().contains(Permission.BAN_MEMBERS) || event.getMember().getRoles().contains(tresEstrelas)) {

                if (event.getMessage().getAttachments().get(0) != null){
                    url = event.getMessage().getAttachments().get(0).getUrl();
                } else {
                    url = mensagem[1];
                }

                bancoLocal(event.getMember().getId()+"gifban");
                limparConteudoArquivo(event.getMember().getId()+"gifban");
                System.out.println("Antes: " + lerConteudoArquivo(event.getMember().getId()+"gifban"));
                salvarIdDoUsuario(event.getMember().getId()+"gifban", url);
                System.out.println("Depois: " + lerConteudoArquivo(event.getMember().getId()+"gifban"));
            }
        }
    }
}
