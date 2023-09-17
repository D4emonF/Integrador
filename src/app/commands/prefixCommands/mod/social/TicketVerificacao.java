package app.commands.prefixCommands.mod.social;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.concrete.Category;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import java.awt.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static app.statics.Basics.ygd;
import static app.statics.Functions.*;
import static app.statics.cargos.Funcionais.*;
import static app.statics.cargos.Perms.*;
import static app.statics.external.ColorPalette.bunker;

public class TicketVerificacao extends ListenerAdapter {



    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        TextChannel ticketAberto = null;
        EmbedBuilder embedVerifiquese = new EmbedBuilder();
        if (event.getButton().getId().equals("ticketInstagram")){
            List<TextChannel> canaisServidor = event.getGuild().getTextChannels();
            for (TextChannel canal : canaisServidor) {
                String id = canal.getName().substring(3);
                if (id.equals(event.getMember().getId())) {
                    ticketAberto = canal;
                }
            }

            if (ticketAberto == null){
                event.getChannel().asTextChannel().getParentCategory().createTextChannel("🔎・" + event.getMember().getId()).queue(
                    textChannel -> {
                        textChannel.upsertPermissionOverride(event.getMember())
                                .grant(Permission.VIEW_CHANNEL)
                                .grant(Permission.MESSAGE_SEND)
                                .grant(Permission.MESSAGE_ATTACH_FILES)
                                .queue();
                        textChannel.upsertPermissionOverride(event.getGuild().getPublicRole())
                                .deny(Permission.VIEW_CHANNEL)
                                .queue();
                        textChannel.upsertPermissionOverride(cargoTemporario)
                                .deny(Permission.VIEW_CHANNEL)
                                .queue();
                        textChannel.upsertPermissionOverride(cargoMembro)
                                .deny(Permission.VIEW_CHANNEL)
                                .queue();
                        textChannel.upsertPermissionOverride(cargoVerificador)
                                .grant(Permission.VIEW_CHANNEL)
                                .grant(Permission.MANAGE_PERMISSIONS)
                                .grant(Permission.MANAGE_THREADS)
                                .queue();
                        textChannel.upsertPermissionOverride(cargoOp)
                                .grant(Permission.VIEW_CHANNEL)
                                .grant(Permission.MANAGE_PERMISSIONS)
                                .grant(Permission.MANAGE_THREADS)
                                .queue();


                        embedVerifiquese
                                .setTitle("`🔎` Verificação ・ " + event.getGuild().getName())
                                .setDescription("> Bem vindo à verificação da YGD, um verificador já vai te atender.")
                                .setColor(bunker);

                        Button finalizarSuporte = Button.danger("finalizarticket", "Fechar ticket");
                        Button atribuir = Button.success("atribuiratendimento", "Atribuir Atendimento");

                        textChannel.sendMessage(cargoVerificador.getAsMention() + event.getMember().getAsMention()).queue(message -> message.delete().queueAfter(1, TimeUnit.SECONDS));
                        textChannel.sendMessage("").setEmbeds(embedVerifiquese.build()).setActionRow(atribuir).queue();
                        event.reply("Seu ticket foi aberto em " + textChannel.getAsMention()).setEphemeral(true).queue();
                    }

                );
            }
            else
            {
                event.reply("Você já tem um ticket aberto em " + ticketAberto.getAsMention()).setEphemeral(true).queue();
            }
        }


        if (event.getButton().getId().equals("atribuiratendimento")){
            if (possuiPeloMenosUmCargo(event.getMember(), getPermVerificador()) || event.getMember().isOwner()){
                Button criarCall = Button.success("criarcall", "Criar Call");
                Button finalizarTicket = Button.danger("finalizarticket", "Fechar ticket");

                String id = event.getChannel().getName().substring(3);
                Member membro = event.getGuild().getMemberById(id);

                event.getChannel().asTextChannel().upsertPermissionOverride(event.getMember()).grant(Permission.VIEW_CHANNEL).reason("Verificação aceita");
                event.getChannel().asTextChannel().upsertPermissionOverride(membro).grant(Permission.VIEW_CHANNEL).reason("Verificação aceita");

                event.getChannel().asTextChannel().upsertPermissionOverride(verificador).deny(Permission.VIEW_CHANNEL).reason("Verificação aceita").queue();



                embedVerifiquese
                        .setTitle("`🔎` Verificação ・ " + event.getGuild().getName())
                        .setDescription("> Bem vindo à verificação da YGD, seu ticket foi atendido por " + event.getMember().getAsMention() +".")
                        .setColor(Color.green);
                event.getMessage().editMessageEmbeds(embedVerifiquese.build()).setActionRow(criarCall, finalizarTicket).queue();
                event.deferEdit().queue();
            }
            else
            {
                event.deferEdit().queue();
            }
        }
        if (event.getButton().getId().equals("criarcall")){
            VoiceChannel callAberta = null;

            if (possuiPeloMenosUmCargo(event.getMember(), getPermVerificador()) || event.getMember().isOwner()) {
                if (possuiPeloMenosUmCargo(event.getMember(), getPermVerificador()) || event.getMember().isOwner()) {

                    List<VoiceChannel> canaisServidor = event.getGuild().getVoiceChannels();
                    for (VoiceChannel canal : canaisServidor) {
                        String id = canal.getName().substring(3);
                        if (id.equals(event.getMember().getId())) {
                            callAberta = canal;
                        }
                    }
                    if (callAberta == null) {
                        String nome = event.getChannel().getName();
                        Category categoria = event.getChannel().asTextChannel().getParentCategory();

                        String id = event.getChannel().getName().substring(3);
                        Member membro = event.getGuild().getMemberById(id);

                        event.getGuild().createVoiceChannel(nome, categoria).queue(voiceChannel -> {
                            voiceChannel.getManager().setUserLimit(2).queue();
                            voiceChannel.upsertPermissionOverride(event.getMember())
                                    .grant(Permission.VIEW_CHANNEL)
                                    .grant(Permission.VOICE_CONNECT)
                                    .grant(Permission.MANAGE_CHANNEL)
                                    .grant(Permission.MANAGE_PERMISSIONS)
                                    .grant(Permission.VOICE_DEAF_OTHERS)
                                    .grant(Permission.VOICE_MUTE_OTHERS)
                                    .grant(Permission.VOICE_MOVE_OTHERS)
                                    .queue();
                            voiceChannel.upsertPermissionOverride(event.getGuild().getPublicRole())
                                    .deny(Permission.VIEW_CHANNEL)
                                    .queue();
                            voiceChannel.upsertPermissionOverride(cargoTemporario)
                                    .deny(Permission.VIEW_CHANNEL)
                                    .queue();
                            voiceChannel.upsertPermissionOverride(cargoMembro)
                                    .deny(Permission.VIEW_CHANNEL)
                                    .queue();
                            voiceChannel.upsertPermissionOverride(cargoOp)
                                    .grant(Permission.VIEW_CHANNEL)
                                    .grant(Permission.MANAGE_PERMISSIONS)
                                    .grant(Permission.MANAGE_THREADS)
                                    .queue();
                            voiceChannel.upsertPermissionOverride(membro)
                                    .grant(Permission.VIEW_CHANNEL)
                                    .grant(Permission.VOICE_CONNECT)
                                    .grant(Permission.VOICE_STREAM)
                                    .queue();

                            event.reply("Call criada em " + voiceChannel.getAsMention()).queue();
                        });

                    } else {
                        event.reply("Já existe uma call criada em " + callAberta.getAsMention()).setEphemeral(true).queue();
                    }
                }
                else event.deferEdit().queue();
            }


        }

        if (event.getButton().getId().equals("finalizarticket")){
            Button confirma = Button.danger("confirmafinalticket", "Finalizar");
            event.reply("Tem certeza que deseja finalizar o ticket?").addActionRow(confirma).setEphemeral(true).queue();
        }
        if (event.getButton().getId().equals("confirmafinalticket")){
            event.getChannel().delete().queueAfter(15, TimeUnit.SECONDS);
            List<VoiceChannel> canaisDeVoz = event.getGuild().getVoiceChannels();
            for (VoiceChannel canalDeVoz: canaisDeVoz) {
                if (canalDeVoz.getName().equals(event.getChannel().getName())){
                    canalDeVoz.delete().queueAfter(15, TimeUnit.SECONDS);
                }
            }
            event.deferEdit().queue();
            event.getChannel().sendMessage("> Este ticket será finalizado <t:" + gerarTimestamp(calcularData(LocalDateTime.now(),15L, TimeUnit.SECONDS.toChronoUnit())) + ":R> ").queue();
        }


        }
}
