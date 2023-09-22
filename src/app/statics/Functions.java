package app.statics;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.awt.*;
import java.io.*;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static app.App.jda;
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

    public static Role obterCargo(String[] mensagem) {
        Role cargo;
        if (mensagem[1].startsWith("<@") && mensagem[1].endsWith(">") || mensagem[1].startsWith("<@!") && mensagem[1].endsWith(">")) {
            String membroId = mensagem[2].replaceAll("[^0-9]", "");
            cargo = Objects.requireNonNull(ygd).getRoleById(membroId);
        } else {
            cargo = Objects.requireNonNull(ygd).getRoleById(mensagem[2]);
        }
        return cargo;
    }

    public static User obterUser(String[] mensagem) {
        User user;
        System.out.println("id adquirido: " + mensagem[1]);
        if (mensagem[1].startsWith("<@") && mensagem[1].endsWith(">") || mensagem[1].startsWith("<@!") && mensagem[1].endsWith(">")) {
            String membroId = mensagem[1].replaceAll("[^0-9]", "");
            user = jda.getUserById(membroId);
            System.out.println("Usuário adquirido= " + user);
        } else {
            user = jda.getUserById(mensagem[1]);
            System.out.println("Usuário adquirido= " + user);
        }
        return user;
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

    public static Boolean existeArquivo(String nomeArquivo) {
        File file = new File(nomeArquivo + ".txt");
        return file.exists();
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
    public static void salvarConteudo(String nomeArquivo, String conteudo) {
        limparConteudoArquivo(nomeArquivo);
            try {
                // Abrir o arquivo em modo de gravação
                FileWriter writer = new FileWriter(nomeArquivo + ".txt", true);

                // Adicionar o ID do usuário no final do arquivo
                writer.write(conteudo);

                // Fechar o arquivo
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    // Método para esvaziar um arquivo local
    public static void limparConteudoArquivo(String nomeArquivo) {
        try {
            // Verifica se o nome do arquivo termina com ".txt" e, se não, adiciona essa extensão
            if (!nomeArquivo.endsWith(".txt")) {
                nomeArquivo += ".txt";
            }

            File arquivo = new File(nomeArquivo);

            // Cria um FileWriter para escrever no arquivo (isso apagará o conteúdo existente)
            FileWriter writer = new FileWriter(arquivo);
            BufferedWriter buffer = new BufferedWriter(writer);

            // Fecha o BufferedWriter para salvar as alterações
            buffer.close();

            System.out.println("Conteúdo do arquivo '" + nomeArquivo + "' foi apagado com sucesso.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String lerConteudoArquivo(String nomeArquivo) {
        StringBuilder conteudo = new StringBuilder();

        try {
            // Verifica se o nome do arquivo termina com ".txt" e, se não, adiciona essa extensão
            if (!nomeArquivo.endsWith(".txt")) {
                nomeArquivo += ".txt";
            }

            BufferedReader leitor = new BufferedReader(new FileReader(nomeArquivo));
            String linha;
            int i=0;

            while ((linha = leitor.readLine()) != null) {
                if (i == 0) {
                    conteudo.append(linha);
                }else {
                    conteudo.append("\n").append(linha);
                }
                i++;
            }

            leitor.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return conteudo.toString();
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

        sb.append("・Membros com o cargo `").append(cargo.getName()).append("`").append("\n\n");

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
                .setTitle("<:preto_audio:1154843570528862248> | " + title)
                .setDescription(description)
                .addField("Hora", "<t:" + gerarTimestamp(dateTime) + ">", false)
                .setFooter(ygd.getName(), Objects.requireNonNull(ygd).getIconUrl())
                .addField("**<:preto_membro:1154843741891338282> Membro:**", memberMention + " `" + memberEffectiveName + "`", false);

        Objects.requireNonNull(logTrafego).sendMessage("").setEmbeds(embed.build()).queue();
    }

    public static boolean temCargoMaisAlto(Member moderador, Member membro) {
        return moderador.getRoles().stream().anyMatch(r -> membro.getRoles().stream().noneMatch(mr -> mr.getPositionRaw() >= r.getPositionRaw()));
    }

    public static boolean possuiPeloMenosUmCargo(Member membro, List<Role> cargosAlvo) {
        List<Role> cargosDoMembro = membro.getRoles();

        for (Role cargoAlvo : cargosAlvo) {
            if (cargosDoMembro.contains(cargoAlvo)) {
                return true; // O membro possui pelo menos um dos cargos alvo
            }
        }

        return false; // O membro não possui nenhum dos cargos alvo
    }

    public static File salvaImagem(String link){
        String sufixo = "";
        if (link.endsWith(".png?size=2048")){
            sufixo = ".png";
        }
        if (link.endsWith(".gif?size=2048")){
            sufixo = ".gif";
        }



        try {
            // Crie uma URL com o link do emoji
            URL url = new URL(link);

            // Abra uma conexão para a URL
            InputStream inputStream = url.openStream();

            // Crie um arquivo local onde a imagem será salva
            File outputFile = new File("NovaImagem" + sufixo);

            // Crie um fluxo de saída para escrever os dados no arquivo local
            FileOutputStream outputStream = new FileOutputStream(outputFile);

            // Leia os dados da URL e escreva no arquivo local
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            // Feche os fluxos
            inputStream.close();
            outputStream.close();
            return outputFile;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
