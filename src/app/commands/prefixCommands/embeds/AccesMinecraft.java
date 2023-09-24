package app.commands.prefixCommands.embeds;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import static app.statics.Basics.prefixo;
import static app.statics.canais.Geral.canalAcessoMine;
import static app.statics.external.ColorPalette.cuttySark;

public class AccesMinecraft extends ListenerAdapter {
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        String mensagem = event.getMessage().getContentRaw();
        if (mensagem.equalsIgnoreCase(prefixo + "acmn")){
            if (event.getMember().isOwner()){
                EmbedBuilder minezim = new EmbedBuilder();

                Button acesso = Button.success("acessomine", "Acesso");

                minezim
                        .setColor(cuttySark)
                        .setTitle("<:verde_minecraft:1155287078108676136> Acesso Mine <:verde_minecraft:1155287078108676136>")
                        .setDescription("> O botão abaixo concederá ou removerá seu acesso aos canais de minecraft para evitar marcações indesejadas");

                canalAcessoMine.sendMessage("").setEmbeds(minezim.build()).setActionRow(acesso).queue();
            }
        }
    }
}
