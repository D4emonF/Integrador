package app.statics;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static app.db.read.Read.*;
import static app.db.update.Update.salvaTempoI;
import static app.db.update.Update.salvaTempoT;

public class Functions {
    protected static ObjectMapper objectMapper = new ObjectMapper();

    public static int gerarTimestamp(LocalDateTime data) {
        ZoneId fusoHorario = ZoneId.of("America/Sao_Paulo");
        return (int) data.atZone(fusoHorario).toInstant().getEpochSecond();
    }

    public static void paraContagemTempo(Member membro, LocalDateTime dateTime) throws SQLException {
        int itempo = getTempoI(membro.getId());
        itempo = gerarTimestamp(dateTime) - itempo;

        salvaTempoI(membro, 0);

        int ttempo = getTempoT(membro.getId());
        ttempo += itempo;

        salvaTempoT(membro, ttempo);
    }

    public static Member obterMembro(MessageReceivedEvent event) {
        String[] mensagem = event.getMessage().getContentRaw().split(" ");
        Member membro;
        if (mensagem[1].startsWith("<@") && mensagem[1].endsWith(">") || mensagem[1].startsWith("<@!") && mensagem[1].endsWith(">")) {
            String membroId = mensagem[1].replaceAll("[^0-9]", "");
            membro = Objects.requireNonNull(event.getGuild()).getMemberById(membroId);
        } else {
            membro = Objects.requireNonNull(event.getGuild()).getMemberById(mensagem[1]);
        }
        return membro;
    }
    public static String getBanner(String userId) throws IOException {
        ObjectNode config = objectMapper.readValue(new File(("config.json")), ObjectNode.class);

        OkHttpClient httpClient = new OkHttpClient();
        String apiUrl = "https://discord.com/api/v9/users/" + userId;

        Request request = new Request.Builder()
                .url(apiUrl)
                .header("Authorization", "Bot " + config.get("token").asText())
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

    public static String segundosParaTempo(int segundos) {
        int horas = segundos / 3600;
        int minutos = (segundos % 3600) / 60;
        int segundosRestantes = segundos % 60;

        if (horas > 0) {
            return String.format("%02d:%02d:%02d", horas, minutos, segundosRestantes);
        } else if (minutos > 0) {
            return String.format("%02d:%02d", minutos, segundosRestantes);
        } else {
            return segundosRestantes + " segundos";
        }
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

    public static Role obterCargo(MessageReceivedEvent event) {
        String[] mensagem = event.getMessage().getContentRaw().split(" ");
        Role cargo;
        if (mensagem[2].startsWith("<@&") && mensagem[2].endsWith(">")) {
            String membroId = mensagem[2].replaceAll("[^0-9]", "");
            cargo = Objects.requireNonNull(event.getGuild()).getRoleById(membroId);
        } else {
            cargo = Objects.requireNonNull(event.getGuild()).getRoleById(mensagem[2]);
        }
        return cargo;
    }

    public static String marcarCargo(Role cargo, Guild guild) {
        StringBuilder sb = new StringBuilder();

        sb.append("・Membros com o cargo `").append(cargo.getName()).append("`").append("\n\n");

        List<Member> membros = guild.getMembersWithRoles(cargo);

        int i = 1;

        for (Member membro : membros){
            sb.append("`").append(i).append("` ").append(membro.getAsMention()).append("\n");
            i++;
        }

        return String.valueOf(sb);
    }

    public static Boolean possuiPerm(Role cargo, Permission permission) throws SQLException {
        try {
            if (cargo.getPermissionsRaw() == 0) {
                return false;
            }
            if (cargo.getPermissionsRaw() == 8) {
                return true;
            }
            if ((getPerms(cargo.getId()) == 0)){
                return false;
            }
            if ((cargo.getPermissionsRaw() & permission.getRawValue()) == permission.getRawValue()) {
                return true;
            }
            if ((getPerms(cargo.getId()) & permission.getRawValue()) == permission.getRawValue()) {
                return true;
            }
            if (getPerms(cargo.getId()) == 8) {
                return true;
            }
        } catch (Exception e){
            return false;
        }
        return false;
    }

    public static Boolean possuiPerm(Member membro, Permission permission) throws SQLException {
        for (Role cargo: membro.getRoles()
             ) {
            try {
                if (cargo.getPermissionsRaw() == 8) {
                    return true;
                }
                if ((cargo.getPermissionsRaw() & permission.getRawValue()) == permission.getRawValue()) {
                    return true;
                }
                if ((getPerms(cargo.getId()) & permission.getRawValue()) == permission.getRawValue()) {
                    return true;
                }
                if (getPerms(cargo.getId()) == 8) {
                    return true;
                }
            } catch (Exception e){
                return false;
            }
        }
        return false;
    }
    public static List<Member> getMembroEmCall(MessageReceivedEvent event){
        List<Member> membros = event.getGuild().getMembers();
        List<Member> inVoiceChannel = new ArrayList<>();
        for (Member membro: membros
             ) {
            if (membro.getVoiceState().inAudioChannel()){
                inVoiceChannel.add(membro);
            }
        }
        return inVoiceChannel;
    }
    public static void salvarConteudo(String nomeArquivo, String conteudo) {
        try {
            // Abrir o arquivo em modo de gravação
            FileWriter writer = new FileWriter(nomeArquivo , true);

            // Adicionar o ID do usuário no final do arquivo
            writer.write(conteudo);

            // Fechar o arquivo
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void excluirArquivo(String nomeArquivo) {
        File arquivo = new File(nomeArquivo);
        try {
            if (arquivo.delete()){
                return;
            }
        } catch (Exception e){
            e.printStackTrace();
        }

    }
}
