package app.events.guild;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import java.awt.*;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static app.statics.Basics.ygd;
import static app.statics.Functions.*;
import static app.statics.canais.Entrada.canalFichasVerificacao;
import static app.statics.canais.Entrada.canalVerificacao;
import static app.statics.cargos.Funcionais.*;
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
                Button tempo = Button.secondary("aceitartempo", "Temporário");

                EmbedBuilder verificado = new EmbedBuilder();
                verificado
                        .setTitle(event.getAuthor().getName())
                        .setThumbnail(event.getAuthor().getAvatarUrl())
                        .setDescription("O membro " + event.getMember().getAsMention() + " quer ser verificado.");
                String bannerURL = getBanner(event.getAuthor().getId());
                if (bannerURL != null) {
                    verificado.setImage(bannerURL);
                }
                verificado.setColor(cuttySark);

                canalFichasVerificacao.sendMessage(davi.getAsMention() + cargoVerificador.getAsMention() + cargoOp.getAsMention()).queue(message -> message.delete().queueAfter(1, TimeUnit.SECONDS));
                canalFichasVerificacao.sendMessage(event.getMember().getId()).setEmbeds(verificado.build()).setActionRow(aceitar, negar, tempo).queue();

            }

        }
    }

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        Member membro = ygd.getMemberById(event.getMessage().getContentRaw());
        Button verificcado = Button.success("verificacaoaceita", "Verificação aceita").asDisabled();
        Button negado = Button.success("verificacaonegada", "Verificação negada").asDisabled();
        Button tempo = Button.secondary("verificacaotempo", "Verificação temporária").asDisabled();

        List<Message> mensagens = canalVerificacao.getHistory().retrievePast(100).complete();

        if (event.getButton().getId().equals("aceitarverificacao"))
        {
            EmbedBuilder verificado = new EmbedBuilder();
            verificado
                    .setTitle(membro.getUser().getName())
                    .setThumbnail(membro.getUser().getAvatarUrl())
                    .setDescription("O membro " +membro.getUser().getAsMention() + " quer ser verificado.");
            String bannerURL = getBanner(membro.getUser().getId());
            if (bannerURL != null) {
                verificado.setImage(bannerURL);
            }
            verificado.setColor(Color.green);
            verificado.setFooter(event.getMember().getEffectiveName() + " | " + event.getMember().getId() + " aceitou a verificação");

            event.getMessage().editMessage(" ").queue();
            event.getMessage().editMessageEmbeds(verificado.build()).setActionRow(verificcado).queue();
            event.deferEdit().queue();

            for (Message mensagem : mensagens) {
                if (Objects.requireNonNull(mensagem.getMember()).equals(membro)) {
                    mensagem.delete().queue();
                }
            }
            ygd.addRoleToMember(membro, cargoVerificado).queue();
            bancoLocal("verificados");
            salvarIdDoUsuario("verificados", membro.getId());
        }
        if (event.getButton().getId().equals("negarverificacao"))
        {
            EmbedBuilder verificado = new EmbedBuilder();
            verificado
                    .setTitle(membro.getUser().getName())
                    .setThumbnail(membro.getUser().getAvatarUrl())
                    .setDescription("O membro " + membro.getUser().getAsMention() + " quer ser verificado.");
            String bannerURL = getBanner(membro.getUser().getId());
            if (bannerURL != null) {
                verificado.setImage(bannerURL);
            }
            verificado.setColor(Color.red);
            verificado.setFooter(event.getMember().getEffectiveName() + " | " + event.getMember().getId() + " negou a verificação");

            for (Message mensagem : mensagens) {
                if (Objects.requireNonNull(mensagem.getMember()).equals(membro)) {
                    mensagem.delete().queue();
                }
            }
            event.getMessage().editMessage(" ").queue();
            event.getMessage().editMessageEmbeds(verificado.build()).setActionRow(negado).queue();

            event.deferEdit().queue();

        }
        if (event.getButton().getId().equals("aceitartempo"))
        {
            event.deferEdit().queue();

            EmbedBuilder verificado = new EmbedBuilder();
            verificado
                    .setTitle(membro.getUser().getName())
                    .setThumbnail(membro.getUser().getAvatarUrl())
                    .setDescription("O membro " + membro.getUser().getAsMention() + " quer ser verificado.");
            String bannerURL = getBanner(membro.getUser().getId());
            if (bannerURL != null) {
                verificado.setImage(bannerURL);
            }
            verificado.setColor(Color.gray);
            verificado.setFooter(event.getMember().getEffectiveName() + " | " + event.getMember().getId() + " aceitou temporariamente o membro");

            event.getMessage().editMessage(" ").queue();
            event.getMessage().editMessageEmbeds(verificado.build()).setActionRow(tempo).queue();

            for (Message mensagem : mensagens) {
                if (Objects.requireNonNull(mensagem.getMember()).equals(membro)) {
                    mensagem.delete().queue();
                }
            }

            ygd.addRoleToMember(membro, cargoTemporario).queue();
        }


    }
}
