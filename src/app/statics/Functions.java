package app.statics;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.awt.*;
import java.io.*;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static app.statics.Basics.token;
import static app.statics.Basics.ygd;
import static app.statics.canais.Logs.logTrafego;

public class Functions
{
    // Método para obter a data e hora atual formatada
    public static String stringData(){
        LocalTime currentTime = LocalTime.now();
        LocalDateTime currentDay = LocalDateTime.now();
        int hour = currentTime.getHour();
        int minute = currentTime.getMinute();
        int day = currentDay.getDayOfMonth();
        int month = currentDay.getMonthValue();
        int year = currentDay.getYear();
        String hora = String.valueOf(hour);
        String minuto = String.valueOf(minute);
        String dia = String.valueOf(day);
        String mes = String.valueOf(month);
        if(hora.length() < 2)
            hora = "0" + hour;
        if(minuto.length() < 2)
            minuto ="0"+minuto;
        if(dia.length() < 2)
            dia ="0"+day;
        if(mes.length() < 2)
            mes ="0"+month;
        return "・" + hora + ":" + minuto + " do dia: " + dia + "/" + mes + "/" + year;
    }

    // Método para calcular uma nova data a partir de uma data inicial, quantidade e unidade de tempo
    public static LocalDateTime calcularData(LocalDateTime dataInicial, long quantidade, ChronoUnit unidade) {
        return dataInicial.plus(quantidade, unidade);
    }

    // Método para gerar um timestamp a partir de uma data
    public static long gerarTimestamp(LocalDateTime data) {
        ZoneId fusoHorario = ZoneId.of("America/Sao_Paulo");
        return data.atZone(fusoHorario).toInstant().getEpochSecond();
    }

    // Método para obter um membro a partir de uma mensagem que contém uma menção ou ID
    public static Member obterMembro(String[] mensagem) {
        Member membro;
        if (mensagem[1].startsWith("<@") && mensagem[1].endsWith(">") || mensagem[1].startsWith("<@!") && mensagem[1].endsWith(">")) {
            String membroId = mensagem[1].replaceAll("[^0-9]", "");
            membro = Objects.requireNonNull(ygd).getMemberById(membroId);
        } else {
            membro = Objects.requireNonNull(ygd).getMemberById(mensagem[1]);
        }
        return membro;
    }

    // Método para criar ou limpar um arquivo local
    public static void bancoLocal(String nomeArquivo) {
        File file = new File(nomeArquivo + ".txt");
        try {
            // Verifique se o arquivo já existe
            if (!file.exists()) {
                // Se o arquivo não existir, crie um novo arquivo
                file.createNewFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Método para salvar o ID de um usuário em um arquivo
    public static void salvarIdDoUsuario(String nomeArquivo, String id) {
        List<String> ids = new ArrayList<>();
        carregarIdsDosUsuarios(nomeArquivo, ids);
        if (!ids.contains(id)) {
            try {
                // Abrir o arquivo em modo de gravação
                FileWriter writer = new FileWriter(nomeArquivo + ".txt", true);

                // Adicionar o ID do usuário no final do arquivo
                writer.write(id + "\n");

                // Fechar o arquivo
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Método para esvaziar um arquivo local
    public static void esvaziarArquivo(String nomeArquivo) {
        try {
            File arquivoOriginal = new File(nomeArquivo + ".txt");
            File arquivoTemporario = new File(nomeArquivo + "_temp.txt");

            FileReader reader = new FileReader(arquivoOriginal);
            BufferedReader bufferedReader = new BufferedReader(reader);

            FileWriter writer = new FileWriter(arquivoTemporario);
            BufferedWriter bufferedWriter = new BufferedWriter(writer);

            String linha;
            while ((linha = bufferedReader.readLine()) != null) {
                bufferedWriter.write(linha);
                bufferedWriter.newLine();
            }

            bufferedReader.close();
            bufferedWriter.close();

            // Deleta o arquivo original
            arquivoOriginal.delete();

            // Renomeia o arquivo temporário para substituir o arquivo original
            arquivoTemporario.renameTo(arquivoOriginal);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Método para carregar IDs de usuários de um arquivo local para uma lista
    public static void carregarIdsDosUsuarios(String nomeArquivo, List<String> ids) {
        try {
            // Abrir o arquivo em modo de leitura
            FileReader reader = new FileReader(nomeArquivo + ".txt");
            BufferedReader bufferedReader = new BufferedReader(reader);

            // Ler os IDs dos usuários e adicionar na lista
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                ids.add(line.trim());
            }

            // Fechar o arquivo
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Método para criar uma lista de membros com um cargo específico e retornar uma string formatada
    public static String marcarCargo(Role cargo) {
        StringBuilder sb = new StringBuilder();

        sb.append("Membros com o cargo `").append(cargo.getName()).append("`").append("\n\n");

        List<Member> membros = ygd.getMembersWithRoles(cargo);

        int i = 1;

        for (Member membro : membros){
            sb.append("`").append(i).append("` ").append(membro.getAsMention()).append("\n");
            i++;
        }

        return String.valueOf(sb);
    }

    // Método para obter o banner de um usuário a partir de seu ID usando a API do Discord
    public static String getBanner(String userId) {
        OkHttpClient httpClient = new OkHttpClient();
        String apiUrl = "https://discord.com/api/v9/users/" + userId;

        Request request = new Request.Builder()
                .url(apiUrl)
                .header("Authorization", "Bot " + token)
                .build();

        try {
            Response response = httpClient.newCall(request).execute();
            String responseBody = response.body().string();

            // Simple parsing of the JSON response without using external libraries
            String bannerKey = "\"banner\":\"";
            int bannerStart = responseBody.indexOf(bannerKey);
            if (bannerStart != -1) {
                int bannerEnd = responseBody.indexOf('"', bannerStart + bannerKey.length());
                if (bannerEnd != -1) {
                    String bannerUrl = "https://cdn.discordapp.com/banners/" + userId + "/" + responseBody.substring(bannerStart + bannerKey.length(), bannerEnd) + "?size=4096";
                    return bannerUrl;
                }
            }

            return null; // Return null if banner is not found
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getEmojiMention(Emoji emoji){

        return "<:" + emoji.getAsReactionCode() + ">";
    }
    public void sendVoiceLog(String title, String description, Color color, String memberMention, String memberEffectiveName) {
        LocalDateTime dateTime = LocalDateTime.now();
        EmbedBuilder embed = new EmbedBuilder()
                .setColor(color)
                .setTitle("<:preto_audio:1124562092167540787> | " + title)
                .setDescription(description)
                .addField("Hora", "<t:" + gerarTimestamp(dateTime) + ">", false)
                .setFooter(ygd.getName(), Objects.requireNonNull(ygd).getIconUrl())
                .addField("**<:preto_membro:1124563263439507538> Membro:**", memberMention + " `" + memberEffectiveName + "`", false);

        Objects.requireNonNull(logTrafego).sendMessage("").setEmbeds(embed.build()).queue();
    }
}
