package app.commands.prefix.mod.cargos;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.awt.*;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static app.configs.Intialize.prefixo;
import static app.statics.Functions.*;

public class RemoveCargo extends ListenerAdapter {

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        String[] mensagem = event.getMessage().getContentRaw().split(" ");


        if (mensagem[0].toLowerCase().startsWith(prefixo + "removecargo")) {
            try {
                if (possuiPerm(event.getMember(), Permission.MANAGE_ROLES)) {

                    EmbedBuilder menosQue3 = new EmbedBuilder();
                    menosQue3
                            .setColor(Color.red)
                            .setDescription("Você deve mencionar um membro e um cargo");

                    if (mensagem.length >= 3) {
                        Member membro = obterMembro(event);
                        Role cargo = obterCargo(event);

                        List<Role> cargosModerador = event.getMember().getRoles();

                        Role maiorCargoModerador = null;

                        EmbedBuilder naoAchouMembro = new EmbedBuilder();
                        naoAchouMembro
                                .setColor(Color.red)
                                .setDescription("O membro " + mensagem[1] + " não foi encontrado.");

                        EmbedBuilder naoAchouCargo = new EmbedBuilder();
                        naoAchouCargo
                                .setColor(Color.red)
                                .setDescription("O cargo " + mensagem[2] + " não foi encontrado.");

                        for (Role role : cargosModerador) {
                            if (maiorCargoModerador == null || role.getPositionRaw() < maiorCargoModerador.getPositionRaw()) {
                                maiorCargoModerador = role;
                            }
                        }

                        if (membro != null) {
                            if (cargo != null) {
                                if (maiorCargoModerador.getPositionRaw() > cargo.getPositionRaw() || event.getMember().isOwner()) {
                                    event.getGuild().removeRoleFromMember(membro, cargo).reason("Cargo removido por: " + event.getMember().getEffectiveName()).queue();

                                    EmbedBuilder embedBuilder = new EmbedBuilder();

                                    embedBuilder
                                            .setColor(Color.red)
                                            .setDescription("O cargo " + cargo.getAsMention() + " foi removido de " + membro.getAsMention());

                                    event.getChannel().sendMessage("").setEmbeds(embedBuilder.build()).queue(message -> message.delete().queueAfter(15, TimeUnit.SECONDS));
                                }
                            } else {
                                event.getMessage().reply("").setEmbeds(naoAchouCargo.build()).queue(message -> message.delete().queueAfter(7, TimeUnit.SECONDS));
                            }
                        } else {
                            event.getMessage().reply("").setEmbeds(naoAchouMembro.build()).queue(message -> message.delete().queueAfter(7, TimeUnit.SECONDS));
                        }
                    } else {
                        event.getMessage().reply("").setEmbeds(menosQue3.build()).queue(message -> message.delete().queueAfter(7, TimeUnit.SECONDS));
                    }
                }
            } catch (SQLException ignored) {            }
        }
    }

    @Override
    public String toString() {
        return "Comando - RemoveCargo";
    }
}
