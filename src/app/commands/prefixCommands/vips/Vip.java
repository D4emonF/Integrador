package app.commands.prefixCommands.vips;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import java.awt.*;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import static app.App.jda;
import static app.statics.Basics.prefixo;
import static app.statics.Basics.ygd;
import static app.statics.Functions.*;
import static app.statics.canais.Geral.*;
import static app.statics.cargos.Funcionais.cargoTemporario;
import static app.statics.cargos.Funcionais.cargoVerificado;
import static app.statics.cargos.Hierarquia.getHierarquia;
import static app.statics.external.ColorPalette.cuttySark;

public class Vip extends ListenerAdapter {
    Button criarCanal = Button.primary("callvip", "Canal");
    Button criarCargo = Button.primary("cargovip", "Cargo");
    Button fechar = Button.danger("fecharmensagem", "Fechar");
    Button voltar = Button.secondary("voltarmsgvip", "Voltar");
    EmbedBuilder embedVip = new EmbedBuilder();


    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        String mensagem = event.getMessage().getContentRaw();
        Member membro = event.getMember();
        String cargoVip = "cargoV" + membro.getId();
        String canalVip = "canalV" + membro.getId();
        if (mensagem.equalsIgnoreCase(prefixo + "vip")) {
            if (possuiPeloMenosUmCargo(membro, getHierarquia())) {
                if (event.getChannel().equals(canalComandos) || event.getChannel().equals(canalComandosVip)) {
                    if (!existeArquivo(canalVip) && !existeArquivo(cargoVip)) {
                        EmbedBuilder embedVipInicial = new EmbedBuilder();

                        embedVipInicial
                                .setColor(cuttySark)
                                .setTitle("Vip " + membro.getEffectiveName())
                                .setDescription("**Cargo:** não criado\n**Canal:** não criado");
                        event.getChannel().sendMessage("").setEmbeds(embedVipInicial.build()).setActionRow(criarCanal, criarCargo, fechar).queue();
                    }
                    if (existeArquivo(canalVip) || existeArquivo(cargoVip)) {
                        Role cargo;
                        VoiceChannel canal;
                        embedVip.setColor(cuttySark).setTitle("Vip " + membro.getEffectiveName());
                        StringBuilder sb = new StringBuilder();

                        if (existeArquivo(cargoVip)) {
                            cargo = event.getGuild().getRoleById(lerConteudoArquivo(cargoVip));
                            sb.append("**Cargo:** ").append(cargo.getAsMention());
                        } else {
                            sb.append("**Cargo:** não criado");
                        }
                        if (existeArquivo(canalVip)) {
                            canal = ygd.getVoiceChannelById(lerConteudoArquivo(canalVip));
                            sb.append("\n**Canal:** ").append(canal.getAsMention());
                        } else {
                            sb.append("\n**Canal:** não criado");
                        }
                        embedVip.setDescription(sb);
                        event.getChannel().sendMessage("").setEmbeds(embedVip.build()).setActionRow(criarCanal, criarCargo, fechar).queue(message -> message.delete().queueAfter(5, TimeUnit.MINUTES));

                    }

                    jda.addEventListener(new ListenerAdapter() {
                        @Override
                        public void onButtonInteraction(ButtonInteractionEvent event) {
                            if (event.getMember().equals(membro)) {
                                if (Objects.equals(event.getButton().getId(), "callvip")) {
                                    VoiceChannel canal = null;
                                    if (existeArquivo(canalVip)) {
                                        String temp = lerConteudoArquivo(canalVip);
                                        if (!temp.isEmpty()) {
                                            canal = ygd.getVoiceChannelById(temp);
                                        }
                                    } else {
                                        bancoLocal(canalVip);

                                        ygd.createVoiceChannel("Vip - " + membro.getUser().getName(), categoriaVips).queue(voiceChannel -> {
                                            voiceChannel.upsertPermissionOverride(membro)
                                                    .grant(Permission.VIEW_CHANNEL)
                                                    .grant(Permission.VOICE_CONNECT)
                                                    .grant(Permission.MANAGE_CHANNEL)
                                                    .grant(Permission.MANAGE_PERMISSIONS)
                                                    .queue();
                                            voiceChannel.upsertPermissionOverride(ygd.getPublicRole())
                                                    .deny(Permission.VOICE_CONNECT)
                                                    .queue();
                                            salvarConteudo(canalVip, voiceChannel.getId());
                                            if (existeArquivo(cargoVip) && !lerConteudoArquivo(cargoVip).isEmpty()) {
                                                Role cargov = ygd.getRoleById(lerConteudoArquivo(cargoVip));
                                                voiceChannel.upsertPermissionOverride(cargov).grant(Permission.VIEW_CHANNEL).grant(Permission.VOICE_CONNECT).queue();
                                            }
                                        });


                                        event.reply("Canal criado, clique novamente para editá-lo").setEphemeral(true).queue();
                                    }

                                    event.deferEdit().queue();

                                    MessageEmbed msgEmbed = event.getMessage().getEmbeds().get(0);
                                    EmbedBuilder embedBuilder = new EmbedBuilder();
                                    embedBuilder
                                            .setTitle(msgEmbed.getTitle())
                                            .setDescription("**Canal:** " + canal.getAsMention())
                                            .setColor(msgEmbed.getColor());

                                    Button editarNome = Button.primary("editarnomecanal", "Editar nome");
                                    Button editarLimite = Button.primary("editarlimite", "Editar limite");

                                    event.getMessage().editMessageEmbeds(embedBuilder.build()).setActionRow(editarNome, editarLimite, voltar).queue();

                                    jda.addEventListener(new ListenerAdapter() {
                                        @Override
                                        public void onButtonInteraction(ButtonInteractionEvent event) {
                                            if (event.getMember().equals(membro)) {


                                                if (Objects.equals(event.getButton().getId(), "voltarmsgvip")) {

                                                    StringBuilder sb = new StringBuilder();

                                                    if (existeArquivo(cargoVip)) {
                                                        Role cargov = event.getGuild().getRoleById(lerConteudoArquivo(cargoVip));
                                                        sb.append("**Cargo:** ").append(cargov.getAsMention());
                                                    } else {
                                                        sb.append("**Cargo:** não criado");
                                                    }
                                                    if (existeArquivo(canalVip)) {
                                                        VoiceChannel canal = ygd.getVoiceChannelById(lerConteudoArquivo(canalVip));
                                                        sb.append("\n**Canal:** ").append(canal.getAsMention());
                                                    } else {
                                                        sb.append("\n**Canal:** não criado");
                                                    }
                                                    embedVip.setColor(cuttySark).setTitle("Vip " + membro.getEffectiveName());
                                                    embedVip.setDescription(sb);

                                                    event.getMessage().editMessageEmbeds(embedVip.build()).setActionRow(criarCanal, criarCargo, fechar).queue();
                                                    event.deferEdit().queue();
                                                    jda.removeEventListener(this);
                                                }
                                            }
                                        }
                                    });
                                }
                                if (Objects.equals(event.getButton().getId(), "cargovip")) {
                                    Role cargo = null;
                                    if (existeArquivo(cargoVip)) {
                                        String temp = lerConteudoArquivo(cargoVip);
                                        if (!temp.isEmpty()) {
                                            cargo = ygd.getRoleById(temp);
                                        }
                                    } else {
                                        bancoLocal(cargoVip);
                                        ygd.createRole().setName("Vip - " + membro.getUser().getName()).queue(role -> {
                                            salvarConteudo(cargoVip, role.getId());
                                            if (existeArquivo(canalVip) && !lerConteudoArquivo(canalVip).isEmpty()) {
                                                VoiceChannel canalv = ygd.getVoiceChannelById(lerConteudoArquivo(canalVip));
                                                canalv.upsertPermissionOverride(role).grant(Permission.VIEW_CHANNEL).grant(Permission.VOICE_CONNECT).queue();
                                                moverCargoParaCima(role, cargoVerificado);
                                            }
                                            event.getGuild().addRoleToMember(event.getMember(), role).queue();
                                        });
                                        event.reply("Cargo criado, clique novamente para editá-lo").setEphemeral(true).queue();
                                    }


                                    MessageEmbed msgEmbed = event.getMessage().getEmbeds().get(0);
                                    EmbedBuilder embedBuilder = new EmbedBuilder();
                                    embedBuilder
                                            .setTitle(msgEmbed.getTitle())
                                            .setDescription("**Cargo:** " + cargo.getAsMention())
                                            .setColor(msgEmbed.getColor());

                                    Button editarNome = Button.primary("editarnomecargo", "Editar nome");
                                    Button editarCor = Button.primary("editarcor", "Editar cor");
                                    Button editarEmoji = Button.primary("editaremoji", "Editar emoji");

                                    event.getMessage().editMessageEmbeds(embedBuilder.build()).setActionRow(editarNome, editarCor, editarEmoji, voltar).queue();
                                    event.deferEdit().queue();

                                    jda.addEventListener(new ListenerAdapter() {
                                        @Override
                                        public void onButtonInteraction(ButtonInteractionEvent event) {
                                            if (event.getMember().equals(membro)) {

                                                if (Objects.equals(event.getButton().getId(), "voltarmsgvip")) {

                                                    StringBuilder sb = new StringBuilder();

                                                    if (existeArquivo(cargoVip)) {
                                                        Role cargov = event.getGuild().getRoleById(lerConteudoArquivo(cargoVip));
                                                        sb.append("**Cargo:** ").append(cargov.getAsMention());
                                                    } else {
                                                        sb.append("**Cargo:** não criado");
                                                    }
                                                    if (existeArquivo(canalVip)) {
                                                        VoiceChannel canal = ygd.getVoiceChannelById(lerConteudoArquivo(canalVip));
                                                        sb.append("\n**Canal:** ").append(canal.getAsMention());
                                                    } else {
                                                        sb.append("\n**Canal:** não criado");
                                                    }
                                                    embedVip.setColor(cuttySark).setTitle("Vip " + membro.getEffectiveName());
                                                    embedVip.setDescription(sb);

                                                    event.getMessage().editMessageEmbeds(embedVip.build()).setActionRow(criarCanal, criarCargo, fechar).queue();
                                                    event.deferEdit().queue();
                                                    jda.removeEventListener(this);
                                                }
                                            }
                                        }
                                    });
                                }

                                if (Objects.equals(event.getButton().getId(), "editarnomecanal")) {
                                    Message mensagem = event.getMessage();
                                    TextChannel canalEvento = event.getChannel().asTextChannel();
                                    event.reply("> Envie o novo nome do canal").setEphemeral(true).queue();
                                    jda.addEventListener(new ListenerAdapter() {
                                        @Override
                                        public void onMessageReceived(MessageReceivedEvent event) {
                                            if (event.getChannel().equals(canalEvento) && event.getMember().equals(membro)) {
                                                String conteudo = event.getMessage().getContentRaw();
                                                event.getMessage().delete().queue();
                                                VoiceChannel canalvip = ygd.getVoiceChannelById(lerConteudoArquivo(canalVip));
                                                canalvip.getManager().setName(conteudo).queue();
                                                MessageEmbed embed = mensagem.getEmbeds().get(0);
                                                mensagem.editMessageEmbeds(embed).queue();
                                                jda.removeEventListener(this);
                                            }
                                        }
                                    });
                                }

                                if (Objects.equals(event.getButton().getId(), "editarlimite")) {
                                    Message mensagem = event.getMessage();
                                    TextChannel canalEvento = event.getChannel().asTextChannel();
                                    event.reply("> Envie o novo limite do canal").setEphemeral(true).queue();
                                    jda.addEventListener(new ListenerAdapter() {
                                        @Override
                                        public void onMessageReceived(MessageReceivedEvent event) {
                                            if (event.getChannel().equals(canalEvento) && event.getMember().equals(membro)) {
                                                String conteudo = event.getMessage().getContentRaw();
                                                conteudo = conteudo.replaceAll("[^0-9]", "");
                                                event.getMessage().delete().queue();
                                                VoiceChannel canalvip = ygd.getVoiceChannelById(lerConteudoArquivo(canalVip));
                                                canalvip.getManager().setUserLimit(Integer.parseInt(conteudo)).queue();
                                                MessageEmbed embed = mensagem.getEmbeds().get(0);
                                                mensagem.editMessageEmbeds(embed).queue();
                                                jda.removeEventListener(this);
                                            }
                                        }
                                    });
                                }


                                if (Objects.equals(event.getButton().getId(), "editarnomecargo")) {
                                    Message mensagem = event.getMessage();
                                    TextChannel canalEvento = event.getChannel().asTextChannel();
                                    event.reply("> Envie o novo nome do cargo").setEphemeral(true).queue();
                                    jda.addEventListener(new ListenerAdapter() {
                                        @Override
                                        public void onMessageReceived(MessageReceivedEvent event) {
                                            if (event.getChannel().equals(canalEvento) && event.getMember().equals(membro)) {
                                                String conteudo = event.getMessage().getContentRaw();
                                                event.getMessage().delete().queue();
                                                Role cargovip = ygd.getRoleById(lerConteudoArquivo(cargoVip));
                                                cargovip.getManager().setName(conteudo).queue();
                                                MessageEmbed embed = mensagem.getEmbeds().get(0);
                                                mensagem.editMessageEmbeds(embed).queue();
                                                jda.removeEventListener(this);
                                            }
                                        }
                                    });
                                }
                                if (Objects.equals(event.getButton().getId(), "editaremoji")) {
                                    Message mensagem = event.getMessage();
                                    TextChannel canalEvento = event.getChannel().asTextChannel();
                                    event.reply("> Envie o novo emoji do cargo").setEphemeral(true).queue();
                                    jda.addEventListener(new ListenerAdapter() {
                                        @Override
                                        public void onMessageReceived(MessageReceivedEvent event) {
                                            if (event.getChannel().equals(canalEvento) && event.getMember().equals(membro)) {
                                                String conteudo = event.getMessage().getContentRaw();
                                                conteudo = conteudo.replaceAll("[^0-9]", "");
                                                event.getMessage().delete().queue();
                                                Role cargovip = ygd.getRoleById(lerConteudoArquivo(cargoVip));
                                                if (conteudo.startsWith("<a:") && conteudo.endsWith(">")) {
                                                    cargovip.getManager().setIcon("https://cdn.discordapp.com/emojis/" + conteudo + ".gif").queue();
                                                }
                                                if (conteudo.startsWith("<:") && conteudo.endsWith(">")) {
                                                    cargovip.getManager().setIcon("https://cdn.discordapp.com/emojis/" + conteudo + ".png").queue();
                                                }
                                                MessageEmbed embed = mensagem.getEmbeds().get(0);
                                                mensagem.editMessageEmbeds(embed).queue();
                                                jda.removeEventListener(this);
                                            }
                                        }
                                    });
                                }

                                if (Objects.equals(event.getButton().getId(), "editarcor")) {
                                    Message mensagem = event.getMessage();
                                    TextChannel canalEvento = event.getChannel().asTextChannel();
                                    event.reply("> Envie o código HEX da nova cor do cargo").setEphemeral(true).queue();
                                    jda.addEventListener(new ListenerAdapter() {
                                        @Override
                                        public void onMessageReceived(MessageReceivedEvent event) {
                                            if (event.getChannel().equals(canalEvento) && event.getMember().equals(membro)) {
                                                String conteudo = event.getMessage().getContentRaw();
                                                event.getMessage().delete().queue();
                                                Role cargovip = ygd.getRoleById(lerConteudoArquivo(cargoVip));
                                                cargovip.getManager().setColor(Color.decode(conteudo)).queue();
                                                MessageEmbed embed = mensagem.getEmbeds().get(0);
                                                mensagem.editMessageEmbeds(embed).queue();
                                                jda.removeEventListener(this);
                                            }
                                        }
                                    });
                                }
                                if (Objects.equals(event.getButton().getId(), "fecharmensagem")) {
                                    event.getMessage().delete().queue();
                                    event.deferEdit().queue();
                                    jda.removeEventListener(this);
                                }


                            } else {
                                event.deferEdit().queue();
                            }
                        }
                    });

                } else {
                    EmbedBuilder outroCanal = new EmbedBuilder();
                    outroCanal.setDescription("**Esse comando só pode ser utilizado em**:\n> " + canalComandos.getAsMention() + " ou " + canalComandosVip.getAsMention()).setColor(Color.red);
                    event.getChannel().sendMessage("").setEmbeds(outroCanal.build()).queue(message -> message.delete().queueAfter(7, TimeUnit.SECONDS));
                }
            } else {
                EmbedBuilder semVip = new EmbedBuilder();
                semVip
                        .setColor(Color.red)
                        .setDescription("> Você não tem o direito de call vip ainda.");


                event.getChannel().sendMessage("").setEmbeds(semVip.build()).queue();
            }
        }
    }

    private void moverCargoParaCima(Role cargoMover, Role cargoAlvo) {
        // Obtenha todos os cargos do servidor
        List<Role> roles = cargoMover.getGuild().getRoles();

        // Encontre as posições dos cargos
        int posicaoMover = roles.indexOf(cargoMover);
        int posicaoAlvo = roles.indexOf(cargoAlvo);

        // Verifique se ambos os cargos existem
        if (posicaoMover >= 0 && posicaoAlvo >= 0) {
            // Certifique-se de que a posição de destino seja menor (acima) do que a posição de origem
            if (posicaoMover < posicaoAlvo) {
                // Reordene os cargos
                roles.remove(cargoMover);
                roles.add(posicaoAlvo, cargoMover);

                // Atualize as posições dos cargos
                for (int i = 0; i < roles.size(); i++) {
                    ygd.modifyRolePositions().selectPosition(cargoMover).moveTo(i).queue();
                }
            }
        }
    }
}

