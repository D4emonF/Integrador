package app.events.guild.messages;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.Channel;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.message.MessageDeleteEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.utils.FileUpload;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;

import static app.configs.Intialize.bot;
import static app.db.read.Read.*;
import static app.statics.Functions.*;

public class MessageDelete extends ListenerAdapter {
    ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void onMessageDelete(MessageDeleteEvent event) {
        ObjectNode config = null;
        try {
            config = objectMapper.readValue(new File(("config.json")), ObjectNode.class);

            TextChannel log = event.getGuild().getTextChannelById(config.get("logMessageDeleted").asText());
            String newValue = getMessageNew(event.getMessageId());
            Member member = event.getGuild().getMemberById(getMessageAuthorId(event.getMessageId()));

            if (getMessageAuthorId(event.getMessageId()) != null) {

                if (!bot.getUserById(getMessageAuthorId(event.getMessageId())).isBot()) {

                    if ((gerarTimestamp(LocalDateTime.now()) - getMessageTimestamp(event.getMessageId()) > 1)) {
                        System.out.println("evento iniciado");
                        Channel canal = event.getChannel();
                        EmbedBuilder deleted = new EmbedBuilder();
                        deleted
                                .setTitle("Mensagem Deletada")
                                .addField("<:cinza_chat:1171898778601656320> Canal", canal.getAsMention() + " | `" + canal.getName() + "`", true)
                                .addField("<:preto_calendario:1171224917891629056> Horário", "<t:" + gerarTimestamp(LocalDateTime.now()) + ":d>", true)
                                .addField("<:preto_membro:1171224894504185897> Membro", member.getAsMention() + " `" + member.getEffectiveName() + "`", true)
                                .setColor(Color.red);

                        if (newValue.length() > 1022) {
                            deleted.setDescription("Mensagem muito grande para enviar na embed, verifique os attachments.");
                            salvarConteudo("MensagemNova.txt", newValue);
                            log.sendMessage("").setEmbeds(deleted.build()).queue();
                            log.sendFiles(FileUpload.fromData(new File("MensagemNova.txt"))).queue();
                            excluirArquivo("MensagemAntiga.txt");
                        } else {
                            deleted.addField("<:cinza_hashtag:1171243525090852964> Mensagem", "`" + newValue + "`", false);
                            log.sendMessageEmbeds(deleted.build()).queue();
                        }
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public String toString() {
        return "Evento - MessageDelete";
    }
}
