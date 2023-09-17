package app.events.functions.social;

import java.io.*;
import java.util.*;
import kotlin.Pair;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;


import static app.statics.Functions.*;
import static app.App.jda;
import static app.statics.canais.Geral.canalInstagram;
import static app.statics.cargos.Perms.getPermVerificador;
import static app.statics.external.ColorPalette.cuttySark;

public class Instagram extends ListenerAdapter {

    private final Map<String, Integer> commentCounts = new HashMap<>(); // Para rastrear o número de comentários
    final Button likesB = Button.secondary("likes", "🤍");
    Button comentario = Button.secondary("comentario", "💬" + "0");

    final Button comentariosB = Button.secondary("comentarios", "📑");
    final Button lixo = Button.secondary("lixo", "❌");

    List<Pair<String, String>> comentarios = new ArrayList<>();


    @Override
    public void onMessageReceived(MessageReceivedEvent event) {

        String url;

        if (event.getMessage().getChannel().equals(canalInstagram)) {
            if (!event.getMessage().getAttachments().isEmpty()) {
                url = event.getMessage().getAttachments().get(0).getUrl();

                event.getMessage().delete().queue();
                Button like = Button.secondary("instagram", "💖 " + "0");
                Button likes = Button.secondary("likes", "💌");
                Button comentario = Button.secondary("comentario", "💬" + "0");
                Button comentariosB = Button.secondary("comentarios", "🔍"); // Botão para ver os comentários
                EmbedBuilder embedBuilder = new EmbedBuilder();
                embedBuilder.setDescription(event.getMessage().getContentRaw())
                        .setImage(url);
                canalInstagram.sendMessage("> " + event.getMember().getAsMention()).setEmbeds(embedBuilder.build()).setActionRow(like, likes, comentario, comentariosB, lixo).queue();

                // Inicialize o contador de comentários
                commentCounts.put(event.getMessage().getId(), 0);
            }
            else {
                if (!possuiPeloMenosUmCargo(event.getMember(), getPermVerificador()) || !event.getMember().isOwner()){
                    event.getMessage().delete().queue();
                }
            }

        }
    }

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        String idMensagem = event.getMessage().getId();
        String arquivoLikes = "likes" + idMensagem;
        String arquivoComentarios = "comentarios" + idMensagem;



        if (event.getButton().getId().equals("instagram")) {


            List<String> idsLikes = new ArrayList<>();
            comentarios = new ArrayList<>();

            bancoLocal(arquivoLikes);
            bancoLocal(arquivoComentarios);

            idsLikes = lerIds(arquivoLikes);
            comentarios = lerComentarios(arquivoComentarios);

            if (!idsLikes.contains(event.getMember().getId())) {
                idsLikes.add(event.getMember().getId());
                salvarIds(arquivoLikes, idsLikes);
            }

            bancoLocal(arquivoLikes);
            bancoLocal(arquivoComentarios);

            idsLikes = lerIds(arquivoLikes);
            comentarios = lerComentarios(arquivoComentarios);
            int likes = idsLikes.size();

            comentario = Button.secondary("comentario", "💬" + comentarios.size());

            Button insta = Button.secondary("instagram", "💖 " + likes);
            event.getMessage().editMessage(event.getMessage().getContentRaw()).setActionRow(insta, likesB, comentario, comentariosB, lixo).queue();
            event.deferEdit().queue();
        }
        if (event.getButton().getId().equals("likes")) {

            List<String> idsLikes = new ArrayList<>();

            bancoLocal(arquivoLikes);

            idsLikes = lerIds(arquivoLikes);

            StringBuilder sb = new StringBuilder();
            for (String id : idsLikes) {
                sb.append("\n<@").append(id).append(">");
            }
            EmbedBuilder likes = new EmbedBuilder();

            likes
                    .setColor(cuttySark)
                    .setTitle("Likes da publicação")
                    .setDescription(sb);
            event.reply("").setEmbeds(likes.build()).setEphemeral(true).queue();
        }

        if (event.getButton().getId().equals("comentario")) {


            Message mensagemInicial = event.getMessage();
            idMensagem = event.getMessage().getId();
            arquivoComentarios = "comentarios" + idMensagem;
            String idMembro = event.getMember().getId();
            List<String> idsLikes = new ArrayList<>();

            bancoLocal(arquivoLikes);
            bancoLocal(arquivoComentarios);

            idsLikes = lerIds(arquivoLikes);
            comentarios = lerComentarios(arquivoComentarios);
            int likes = idsLikes.size();

            Button insta = Button.secondary("instagram", "💖 " + likes);


            event.reply("Envie seu comentário no privado do bot.").setEphemeral(true).queue();

            String finalArquivoComentarios = arquivoComentarios;
            event.getMember().getUser().openPrivateChannel().queue(privateChannel -> {
                privateChannel.sendMessage("Envie seu comentário aqui:").queue();

                jda.addEventListener(new ListenerAdapter() {
                    @Override
                    public void onMessageReceived(MessageReceivedEvent event) {
                        if (event.getAuthor().getId().equals(idMembro) && event.getMessage().getContentRaw().length() > 0) {
                            salvarComentario(finalArquivoComentarios, idMembro, event.getMessage().getContentRaw());
                            comentarios = lerComentarios(finalArquivoComentarios);
                            String msg = mensagemInicial.getContentRaw();

                            Button novoComentario = Button.secondary("comentario", "💬 " + comentarios.size());
                            mensagemInicial.editMessage(msg).setActionRow(insta ,likesB, novoComentario, comentariosB, lixo).queue();
                            jda.removeEventListener(this);
                        }
                    }
                });
            });
        }

        if (event.getButton().getId().equals("comentarios")) {
            idMensagem = event.getMessage().getId();
            arquivoComentarios = "comentarios" + idMensagem;

            comentarios = lerComentarios(arquivoComentarios);

            if (!comentarios.isEmpty()) {
                StringBuilder sb = new StringBuilder();
                sb.append("Comentários da publicação: \n");
                for (Pair<String, String> comentarioS : comentarios) {
                    sb.append("<@").append(comentarioS.getFirst()).append(">: ").append(comentarioS.getSecond()).append("\n");
                }
                EmbedBuilder comentarios = new EmbedBuilder();

                comentarios
                        .setColor(cuttySark)
                        .setTitle("Comentários da publicação")
                        .setDescription(sb);
                event.reply("").setEmbeds(comentarios.build()).setEphemeral(true).queue();
            } else {
                event.reply("Não há comentários nesta publicação.").setEphemeral(true).queue();
            }
        }
        if (event.getButton().getId().equals("lixo")) {
            Member membro = event.getGuild().getMemberById(event.getMessage().getContentRaw().replaceAll("[^0-9]", ""));
            if (event.getMember().equals(membro)){
                event.getMessage().delete().queue();
                event.deferEdit().queue();
            }else{
                event.reply("Você não tem permição de excluir essa postagem").setEphemeral(true).queue();
            }
        }


    }

    void salvarComentario(String nomeArquivo, String idDoMembro, String comentario) {
        try {
            FileWriter writer = new FileWriter(nomeArquivo + ".txt", true);
            writer.write(idDoMembro + ":" + comentario + "\n");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    List<String> lerIds(String nomeArquivo) {
        List<String> ids = new ArrayList<>();

        try {
            BufferedReader reader = new BufferedReader(new FileReader(nomeArquivo + ".txt"));
            String linha;

            while ((linha = reader.readLine()) != null) {
                ids.add(linha);
            }

            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return ids;
    }

    List<Pair<String, String>> lerComentarios(String nomeArquivo) {
        List<Pair<String, String>> comentarios = new ArrayList<>();

        try {
            BufferedReader reader = new BufferedReader(new FileReader(nomeArquivo + ".txt"));
            String linha;

            while ((linha = reader.readLine()) != null) {
                String[] partes = linha.split(":");
                if (partes.length == 2) {
                    comentarios.add(new Pair<>(partes[0], partes[1]));
                }
            }

            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return comentarios;
    }

    void salvarIds(String nomeArquivo, List<String> ids) {
        try {
            FileWriter writer = new FileWriter(nomeArquivo + ".txt");
            for (String id : ids) {
                writer.write(id + "\n");
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
