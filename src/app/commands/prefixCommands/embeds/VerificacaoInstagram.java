package app.commands.prefixCommands.embeds;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import static app.statics.Basics.prefixo;
import static app.statics.canais.Sociais.canalVerifiquese;
import static app.statics.external.ColorPalette.cuttySark;

public class VerificacaoInstagram extends ListenerAdapter {

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getMember().isOwner()) {
            EmbedBuilder verificacao = new EmbedBuilder();
            Button ticket = Button.primary("ticketInstagram", "Iniciar Verificação");
            verificacao
                    .setTitle("<:instagram:1151898547017498715> *Instagram YGD* <:instagram:1151898547017498715>")
                    .setDescription("Para poder postar em nosso <#1151663643998564475>, você deve ser verificado no servidor.\n\n" +
                            "> Caso você deseje se verificar, basta apertar no botão abaixo que um ticket de verificação será aberto.\n\n" +
                            "> Após o ticket aberto, os verificadores do servidor irão te instruir e te verificar.")
                    .setColor(cuttySark);


            if (event.getMessage().getContentRaw().equalsIgnoreCase(prefixo + "evqs"))
            canalVerifiquese.sendMessage("").setEmbeds(verificacao.build()).setActionRow(ticket).queue();
        }
    }
}
