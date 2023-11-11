package app.db.read;

import net.dv8tion.jda.api.entities.Member;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static app.db.DataBaseConnection.*;

public class Read {


    public static Integer getTempoI(String memberId) throws SQLException {
        try {
            conectaNoBanco();
            String query = "SELECT tempo_inicial FROM membro WHERE id = ?";
            try (PreparedStatement preparedStatement = getConnexao().prepareStatement(query)) {
                preparedStatement.setString(1, memberId);
                ResultSet resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {
                    return resultSet.getInt("tempo_inicial");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            desconectaDoBanco();
        }
        return null;
    }

    public static Integer getTempoT(String memberId) throws SQLException {
        try {
            conectaNoBanco();
            String query = "SELECT tempo_total FROM membro WHERE id = ?";
            try (PreparedStatement preparedStatement = getConnexao().prepareStatement(query)) {
                preparedStatement.setString(1, memberId);
                ResultSet resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {
                    return resultSet.getInt("tempo_total");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            desconectaDoBanco();
        }
        return null;
    }

    public static String getAfk(String memberId) throws SQLException {
        try {
            conectaNoBanco();
            String query = "SELECT afk FROM membro WHERE id = ?";
            try (PreparedStatement preparedStatement = getConnexao().prepareStatement(query)) {
                preparedStatement.setString(1, memberId);
                ResultSet resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {
                    return resultSet.getString("afk");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            desconectaDoBanco();
        }
        return null;
    }

    public static List<String> obterMembrosAfk() throws SQLException {
        try {
            conectaNoBanco();
            List<String> afkUsers = new ArrayList<>();
            String query = "SELECT id FROM membro WHERE afk IS NOT NULL AND afk <> ''";

            try (PreparedStatement preparedStatement = getConnexao().prepareStatement(query)) {
                ResultSet resultSet = preparedStatement.executeQuery();

                while (resultSet.next()) {
                    String id = resultSet.getString("id");
                    afkUsers.add(id);
                }
            }
            return afkUsers;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            desconectaDoBanco();
        }
        return new ArrayList<>();
    }

    public static long getPerms(String roleId) throws SQLException {
        try {
            conectaNoBanco();
            String query = "SELECT perm_level FROM cargo WHERE id = ?";
            try (PreparedStatement preparedStatement = getConnexao().prepareStatement(query)) {
                preparedStatement.setString(1, roleId);
                ResultSet resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {
                    return resultSet.getLong("perm_level");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            desconectaDoBanco();
        }
        return 0;
    }


    public static Boolean verificaExiste(Connection connection, String tabela, String campo, String valor) {
        try {
            conectaNoBanco();
            Boolean existe = false;

            try {
                // Consulta SQL para verificar a existência do valor na tabela
                String sql = "SELECT COUNT(*) AS count FROM " + tabela + " WHERE " + campo + " = ?";

                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setString(1, valor);

                ResultSet resultSet = statement.executeQuery();

                if (resultSet.next()) {
                    int count = resultSet.getInt("count");
                    existe = count > 0; // Se o count for maior que 0, o valor existe na tabela
                }

                resultSet.close();
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
                // Trate a exceção conforme necessário
            }
            return existe;

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            desconectaDoBanco();
        }
        return false;
    }

    public static List<String> obterRankTempo(int limite) throws SQLException {
        try {
            conectaNoBanco();
            List<String> ids = new ArrayList<>();
            String query = "SELECT id FROM membro ORDER BY tempo_total DESC LIMIT ?";

            try (PreparedStatement preparedStatement = getConnexao().prepareStatement(query)) {
                preparedStatement.setInt(1, limite);
                ResultSet resultSet = preparedStatement.executeQuery();

                while (resultSet.next()) {
                    String id = resultSet.getString("id");
                    ids.add(id);
                }
            }
            return ids;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            desconectaDoBanco();
        }
        return new ArrayList<>();
    }
    public static String getMessageOld(String id){
        try {
            conectaNoBanco();
            String query = "SELECT old_content FROM mensagem WHERE id = ?";

            try (PreparedStatement preparedStatement = getConnexao().prepareStatement(query)) {
                preparedStatement.setString(1, id);
                ResultSet resultSet = preparedStatement.executeQuery();
                return resultSet.getString("old_content");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            desconectaDoBanco();
        }
        return null;
    }
    public static String getMessageNew(String id){
        try {
            conectaNoBanco();
            String query = "SELECT new_content FROM mensagem WHERE id = ?";

            try (PreparedStatement preparedStatement = getConnexao().prepareStatement(query)) {
                preparedStatement.setString(1, id);
                ResultSet resultSet = preparedStatement.executeQuery();
                return resultSet.getString("new_content");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            desconectaDoBanco();
        }
        return null;
    }

    public static int getMessageTimestamp(String id){
        try {
            conectaNoBanco();
            String query = "SELECT mtimestamp FROM mensagem WHERE id = ?";

            try (PreparedStatement preparedStatement = getConnexao().prepareStatement(query)) {
                preparedStatement.setString(1, id);
                ResultSet resultSet = preparedStatement.executeQuery();
                return resultSet.getInt("mtimestamp");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            desconectaDoBanco();
        }
        return 0;
    }
    public static String getMessageAuthorId(String id) {
        try {
            conectaNoBanco();
            String query = "SELECT fk_member_id FROM mensagem WHERE id = ?";

            try (PreparedStatement preparedStatement = getConnexao().prepareStatement(query)) {
                preparedStatement.setString(1, id);
                ResultSet resultSet = preparedStatement.executeQuery();
                return resultSet.getString("fk_member_id");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            desconectaDoBanco();
        }
        return null;
    }
}

