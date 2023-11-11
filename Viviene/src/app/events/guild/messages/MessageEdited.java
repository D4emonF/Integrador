package app.events.guild.messages;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.Channel;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.message.MessageUpdateEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.utils.FileUpload;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;

import static app.db.read.Read.getMessageOld;
import static app.db.update.Update.editaMensagem;
import static app.statics.Functions.*;

public class MessageEdited extends ListenerAdapter {
    ObjectMapper objectMapper = new ObjectMapper();
    @Override
    public void onMessageUpdate(MessageUpdateEvent event) {
        try {
            editaMensagem(event.getMessageId(), event.getMessage().getContentRaw());
            EmbedBuilder edited = new EmbedBuilder();
            Member member = event.getMember();
            Channel canal = event.getChannel();
            String oldValue = getMessageOld(event.getMessageId());
            String newValue = event.getMessage().getContentRaw();
            ObjectNode config = objectMapper.readValue(new File(("config.json")), ObjectNode.class);
            TextChannel log = event.getGuild().getTextChannelById(config.get("logMessageEdited").asText());

            edited
                    .setTitle("Mensagem editada!")
                    .addField("<:preto_membro:1171224894504185897> Membro", member.getAsMention() + " `" + member.getEffectiveName() + "`", true)
                    .addField("<:cinza_chat:1171898778601656320> Canal", canal.getAsMention() + " `" + canal.getName() + "`", true)
                    .addField("<:cinza_hashtag:1171243525090852964> Mensagem", event.getMessage().getJumpUrl() +" `" + event.getMessageId() + "`", true)
                    .addField("<:preto_calendario:1171224917891629056> Horário", "<t:" + gerarTimestamp(LocalDateTime.now()) + ">", true)
                    .setThumbnail(member.getAvatarUrl())
                    .setColor(Color.orange);

            if (oldValue.length() > 1022){
                salvarConteudo("MensagemAntiga.txt", oldValue);
                edited.setDescription("Mensagem muito grande para enviar na embed, verifique os attachments.");
                if (newValue.length() > 1022){
                    salvarConteudo("MensagemNova.txt", newValue);
                    log.sendMessage("").setEmbeds(edited.build()).queue();
                    log.sendFiles(FileUpload.fromData(new File("MensagemAntiga.txt")), FileUpload.fromData(new File("MensagemNova.txt"))).queue();
                    excluirArquivo("MensagemAntiga.txt");
                    excluirArquivo("MensagemNova.txt");
                } else {
                    edited.addField("<:cinza_hashtag:1171243525090852964> Mensagem Nova", "`" +  newValue + "`", false);
                    log.sendMessage("").setEmbeds(edited.build()).addFiles(FileUpload.fromData(new File("MensagemAntiga.txt"))).queue();
                    excluirArquivo("MensagemAntiga.txt");
                }
            } else if (newValue.length() > 1022) {
                edited
                        .setDescription("Mensagem muito grande para enviar na embed, verifique os attachments.")
                        .addField("<:cinza_hashtag:1171243525090852964> Mensagem Antiga", "`" +  oldValue + "`", false);
                salvarConteudo("MensagemNova.txt", newValue);
                log.sendMessageEmbeds(edited.build()).queue();
                log.sendFiles(FileUpload.fromData(new File("MensagemNova.txt"))).queue();

            } else {
                edited
                        .addField("<:cinza_hashtag:1171243525090852964> Mensagem Antiga", "`" +  oldValue + "`", false)
                        .addField("<:cinza_hashtag:1171243525090852964> Mensagem Nova", "`" +  newValue + "`", false);
                log.sendMessageEmbeds(edited.build()).queue();
            }




        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (StreamReadException e) {
            throw new RuntimeException(e);
        } catch (DatabindException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String toString() {
        return "Evento - MessageEdited";
    }
}
