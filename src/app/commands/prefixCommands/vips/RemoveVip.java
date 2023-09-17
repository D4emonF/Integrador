package app.commands.prefixCommands.vips;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.concurrent.TimeUnit;

import static app.statics.Basics.prefixo;
import static app.statics.Basics.ygd;
import static app.statics.Functions.*;
import static app.statics.Functions.lerConteudoArquivo;
import static app.statics.cargos.Hierarquia.getHierarquia;
import static app.statics.external.ColorPalette.cuttySark;

public class RemoveVip extends ListenerAdapter {

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        String mensagem = event.getMessage().getContentRaw();
        Member vip = event.getMember();
        String cargoVip = "cargoV" + vip.getId();
        Member membro = ygd.getMemberById(mensagem.replaceAll("[^0-9]", ""));;

        if (mensagem.toLowerCase().startsWith(prefixo + "removervip") || mensagem.toLowerCase().startsWith(prefixo + "removevip")) {
            if (possuiPeloMenosUmCargo(vip, getHierarquia())) {
                if (existeArquivo(cargoVip)){
                    ygd.removeRoleFromMember(membro, ygd.getRoleById(lerConteudoArquivo(cargoVip))).queue();
                    EmbedBuilder addvip = new EmbedBuilder();

                    addvip.setColor(cuttySark).setTitle("Vip removido com sucesso")
                            .setDescription("**Cargo:** " + ygd.getRoleById(lerConteudoArquivo(cargoVip)).getAsMention() +"\n" +
                                    "**Membro:** " + membro.getAsMention())
                            .setFooter(event.getGuild().getName(), event.getGuild().getIconUrl())
                            .setThumbnail(event.getGuild().getIconUrl());

                    event.getChannel().sendMessage("").setEmbeds(addvip.build()).queue(message -> message.delete().queueAfter(7, TimeUnit.SECONDS));
                }
            }
        }
    }

}
