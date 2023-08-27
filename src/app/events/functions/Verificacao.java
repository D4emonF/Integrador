package app.events.functions;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import java.awt.*;
import java.util.concurrent.TimeUnit;

import static app.statics.Functions.getBanner;
import static app.statics.canais.Entrada.canalFichasVerificacao;
import static app.statics.canais.Entrada.canalVerificacao;
import static app.statics.cargos.Funcionais.cargoVerificador;
import static app.statics.cargos.Perms.cargoOp;
import static app.statics.external.ColorPalette.cuttySark;
import static app.statics.users.Pessoas.davi;

public class Verificacao extends ListenerAdapter
{
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getMessage().getChannel().asTextChannel().equals(canalVerificacao)) {

            if (!event.getAuthor().isBot() && !event.getMessage().getContentRaw().contains(".")) {
                event.getMessage().delete().queue();
            }

            if (event.getMessage().getContentRaw().equals(".")) {
                event.getMessage().reply("Sua solicitação de verificação foi enviada para os verificadores.").queue(message -> message.delete().queueAfter(7, TimeUnit.SECONDS));

                Button aceitar = Button.success("aceitarverificacao", "Aceitar");
                Button negar = Button.danger("negarverificacao", "Negar");

                EmbedBuilder verificado = new EmbedBuilder();
                verificado
                        .setTitle(event.getAuthor().getName())
                        .setThumbnail(event.getAuthor().getAvatarUrl())
                        .setDescription("O membro " + event.getMember().getAsMention() + " quer ser verificado.");
                String bannerURL = getBanner(event.getAuthor().getId());
                if (bannerURL != null) {
                    verificado.setImage(bannerURL);
                } else {
                    verificado.addField("Banner","Este usuário não tem banner definido ou não está mais no servidor!",false);
                }
                verificado.setColor(cuttySark);

                canalFichasVerificacao.sendMessage(davi.getAsMention() + cargoVerificador.getAsMention() + cargoOp.getAsMention()).queue(message -> message.delete().queueAfter(1, TimeUnit.SECONDS));
                canalFichasVerificacao.sendMessage(event.getMember().getId()).setEmbeds(verificado.build()).setActionRow(aceitar, negar).queue();

            }

        }
    }
}
