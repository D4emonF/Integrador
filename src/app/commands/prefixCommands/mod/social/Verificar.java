package app.commands.prefixCommands.mod.social;

import app.statics.cargos.Perms;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.awt.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static app.statics.Basics.prefixo;
import static app.statics.Basics.ygd;
import static app.statics.Functions.*;
import static app.statics.canais.Geral.canalComandos;
import static app.statics.canais.Logs.logVerificacoes;
import static app.statics.cargos.Funcionais.cargoVerificado;
import static app.statics.external.ColorPalette.monteCarlo;

public class Verificar extends ListenerAdapter {
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        String[] mensagem = event.getMessage().getContentRaw().split(" ");
        Member moderador = event.getMember();

        if (mensagem[0].equalsIgnoreCase(prefixo+"verificar")) {
            Member verificado = obterMembro(mensagem);
            List<Role> perms = Perms.getPermVerificador();

            if (possuiPeloMenosUmCargo(moderador, perms) || moderador.isOwner()) {
                if (verificado != null) {
                    if (moderador.getVoiceState().inAudioChannel()) {
                        if (verificado.getVoiceState().inAudioChannel()) {
                            if (Objects.equals(moderador.getVoiceState().getChannel(), verificado.getVoiceState().getChannel())) {
                                ygd.addRoleToMember(verificado, cargoVerificado).reason("Verificação feita por + " + moderador.getUser().getName()).queue();
                                bancoLocal("verificadosInstagram");
                                salvarIdDoUsuario("verificadosInstagram", verificado.getId());

                                EmbedBuilder novoVerificado = new EmbedBuilder();

                                novoVerificado
                                        .setColor(monteCarlo)
                                        .setTitle("Nova verificação")
                                        .addField("<:preto_membro:1124563263439507538> Verificador", moderador.getAsMention() + " `" + moderador.getId() + "`", true)
                                        .addField("<:preto_membro:1124563263439507538> Verificado", verificado.getAsMention() + " `" + verificado.getId() + "`", true)
                                        .setThumbnail(verificado.getUser().getAvatarUrl())
                                        .setImage(getBanner(verificado.getId()));


                                if (event.getChannel().equals(canalComandos)){
                                    event.getChannel().asTextChannel().sendMessage("").setEmbeds(novoVerificado.build()).queue();
                                }
                                else
                                {
                                    event.getChannel().asTextChannel().sendMessage("").setEmbeds(novoVerificado.build()).queue(message -> message.delete().queueAfter(7, TimeUnit.SECONDS));
                                }
                                novoVerificado.addField("<:preto_calendario:1141067399790088353> Horario", "<t:" + gerarTimestamp(LocalDateTime.now()) + ">" , true);

                                logVerificacoes.sendMessage("").setEmbeds(novoVerificado.build()).queue();
                            } else {
                                EmbedBuilder canalDeVoz = new EmbedBuilder();
                                canalDeVoz.setDescription("> Você deve estar no mesmo canal de voz que o membro a ser verificado!")
                                        .setColor(Color.red);
                                event.getChannel().sendMessage("").setEmbeds(canalDeVoz.build()).queue();
                            }

                        } else {
                            EmbedBuilder canalDeVoz = new EmbedBuilder();
                            canalDeVoz.setDescription("> O membro a ser verificado deve estar em um canal de voz para ser verificado!")
                                    .setColor(Color.red);
                            event.getChannel().sendMessage("").setEmbeds(canalDeVoz.build()).queue();

                        }


                    }else {
                        EmbedBuilder canalDeVoz = new EmbedBuilder();
                        canalDeVoz.setDescription("> Você deve estar em um canal de voz para executar esse comando!")
                                .setColor(Color.red);

                        event.getChannel().sendMessage("").setEmbeds(canalDeVoz.build()).queue();

                    }

                } else {
                    EmbedBuilder membroNaoEncontrado = new EmbedBuilder();
                    membroNaoEncontrado.setDescription("> O membro " + mensagem[1] + " não foi encontrado!")
                            .setColor(Color.red);
                    if (!event.getMessage().getChannel().equals(canalComandos)) {
                        event.getMessage().reply("").setEmbeds(membroNaoEncontrado.build()).queue(message -> message.delete().queueAfter(7, TimeUnit.SECONDS));
                    } else {
                        event.getMessage().reply("").setEmbeds(membroNaoEncontrado.build()).queue();

                    }
                }
            }


        }
    }
}
