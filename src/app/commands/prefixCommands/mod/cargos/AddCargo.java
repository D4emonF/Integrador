package app.commands.prefixCommands.mod.cargos;

import app.statics.cargos.Perms;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.awt.*;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static app.statics.Basics.prefixo;
import static app.statics.Functions.*;

public class AddCargo extends ListenerAdapter implements Perms {



    @Override
    public void onMessageReceived(MessageReceivedEvent event) {

        String[] mensagem = event.getMessage().getContentRaw().split(" ");

        List<Role> perms = Perms.getPermCargo();

        if (mensagem[0].toLowerCase().startsWith(prefixo + "addcargo")) {
            if (possuiPeloMenosUmCargo(event.getMember(), perms) || event.getMember().isOwner()) {

                EmbedBuilder menosQue3 = new EmbedBuilder();
                menosQue3
                        .setColor(Color.red)
                        .setDescription("Você deve mencionar um membro e um cargo");

                if (mensagem.length >= 3) {


                    Member membro = obterMembro(mensagem);
                    Role cargo = obterCargo(mensagem);

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
                                event.getGuild().addRoleToMember(membro, cargo).reason("Cargo adicionado por: "+event.getMember().getEffectiveName()).queue();

                                EmbedBuilder embedBuilder = new EmbedBuilder();

                                embedBuilder
                                        .setColor(Color.green)
                                        .setDescription("O cargo " + cargo.getAsMention() + " foi adicionado à " + membro.getAsMention());

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
        }
    }
}
