package app.db.update;

import app.commands.prefix.misc.Tempo;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static app.db.DataBaseConnection.*;

public class Update {


    public static void salvaTempoI(Member membro, Integer timestamp) throws SQLException {
        try {
            conectaNoBanco();
            String selectQuery = "SELECT COUNT(*) as count FROM membro WHERE id = ?";
            String updateQuery = "UPDATE membro SET tempo_inicial = ? WHERE id = ?";
            String insertQuery = "INSERT INTO membro (id, tempo_inicial) VALUES (?, ?)";

            try (PreparedStatement selectStatement = getConnexao().prepareStatement(selectQuery)) {
                selectStatement.setString(1, membro.getId());
                ResultSet resultSet = selectStatement.executeQuery();

                resultSet.next();
                int count = resultSet.getInt("count");

                if (count > 0) {
                    try (PreparedStatement updateStatement = getConnexao().prepareStatement(updateQuery)) {
                        updateStatement.setInt(1, timestamp);
                        updateStatement.setString(2, membro.getId());
                        updateStatement.executeUpdate();
                    }
                } else {
                    try (PreparedStatement insertStatement = getConnexao().prepareStatement(insertQuery)) {
                        insertStatement.setString(1, membro.getId());
                        insertStatement.setInt(2, timestamp);
                        insertStatement.executeUpdate();
                    }
                }
            }
        } catch (Exception ignore) {
        } finally {
            desconectaDoBanco();
        }
    }

    public static void salvaTempoT(Member membro, Integer timestamp) throws SQLException {
        try {
            conectaNoBanco();

            String selectQuery = "SELECT COUNT(*) as count FROM membro WHERE id = ?";
            String updateQuery = "UPDATE membro SET tempo_total = ? WHERE id = ?";
            String insertQuery = "INSERT INTO membro (id, tempo_total) VALUES (?, ?)";

            try (PreparedStatement selectStatement = getConnexao().prepareStatement(selectQuery)) {
                selectStatement.setString(1, membro.getId());
                ResultSet resultSet = selectStatement.executeQuery();

                resultSet.next();
                int count = resultSet.getInt("count");

                if (count > 0) {
                    try (PreparedStatement updateStatement = getConnexao().prepareStatement(updateQuery)) {
                        updateStatement.setInt(1, timestamp);
                        updateStatement.setString(2, membro.getId());
                        updateStatement.executeUpdate();
                    }
                } else {
                    try (PreparedStatement insertStatement = getConnexao().prepareStatement(insertQuery)) {
                        insertStatement.setString(1, membro.getId());
                        insertStatement.setInt(2, timestamp);
                        insertStatement.executeUpdate();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            desconectaDoBanco();
        }
    }

    public static void resetaTempo() throws SQLException {
        try {
            conectaNoBanco();

            String updateQuery = "UPDATE membro SET tempo_total = ?";


            try (PreparedStatement updateStatement = getConnexao().prepareStatement(updateQuery)) {
                updateStatement.setInt(1, 0);
                updateStatement.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            desconectaDoBanco();
        }

    }
    public static void resetaTempoI(Member member,int dateTime) throws SQLException {
        try {
            conectaNoBanco();

            String updateQuery = "UPDATE membro SET tempo_inicial = ? where id = ?";


            try (PreparedStatement updateStatement = getConnexao().prepareStatement(updateQuery)) {
                updateStatement.setInt(1, dateTime);
                updateStatement.setString(2, member.getId());
                updateStatement.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            desconectaDoBanco();
        }

    }

    public static void salvaAfk(Member membro, String motivo) throws SQLException {
        try {
            conectaNoBanco();
            String selectQuery = "SELECT COUNT(*) as count FROM membro WHERE id = ?";
            String updateQuery = "UPDATE membro SET afk = ? WHERE id = ?";
            String insertQuery = "INSERT INTO membro (id, afk) VALUES (?, ?)";

            try (PreparedStatement selectStatement = getConnexao().prepareStatement(selectQuery)) {
                selectStatement.setString(1, membro.getId());
                ResultSet resultSet = selectStatement.executeQuery();

                resultSet.next();
                int count = resultSet.getInt("count");

                if (count > 0) {
                    try (PreparedStatement updateStatement = getConnexao().prepareStatement(updateQuery)) {
                        updateStatement.setString(1, motivo);
                        updateStatement.setString(2, membro.getId());
                        updateStatement.executeUpdate();
                    }
                } else {
                    try (PreparedStatement insertStatement = getConnexao().prepareStatement(insertQuery)) {
                        insertStatement.setString(1, membro.getId());
                        insertStatement.setString(2, motivo);
                        insertStatement.executeUpdate();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            desconectaDoBanco();
        }
    }

    public static void salvaCargo(Role cargo, Integer permLevel) throws SQLException {
        try {


            conectaNoBanco();
            String selectQuery = "SELECT COUNT(*) as count FROM membro WHERE id = ?";
            String updateQuery = "UPDATE cargo SET perm_Level = ? WHERE id = ?";
            String insertQuery = "INSERT INTO cargo (id, perm_level, nome) VALUES (?, ?, ?)";

            try (PreparedStatement selectStatement = getConnexao().prepareStatement(selectQuery)) {
                selectStatement.setString(1, cargo.getId());
                ResultSet resultSet = selectStatement.executeQuery();

                resultSet.next();
                int count = resultSet.getInt("count");

                if (count > 0) {
                    try (PreparedStatement updateStatement = getConnexao().prepareStatement(updateQuery)) {
                        updateStatement.setString(1, cargo.getId());
                        updateStatement.setInt(2, permLevel);
                        updateStatement.executeUpdate();
                    }
                } else {
                    try (PreparedStatement insertStatement = getConnexao().prepareStatement(insertQuery)) {
                        insertStatement.setString(1, cargo.getId());
                        insertStatement.setInt(2, permLevel);
                        insertStatement.setString(3, cargo.getName());
                        insertStatement.executeUpdate();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            desconectaDoBanco();
        }
    }

    public static void salvaMensagem(String id, String mensagem, Member membro, int timestamp, String channelId){
        try {
            conectaNoBanco();
            String selectQuery = "SELECT COUNT(*) as count FROM membro WHERE id = ?";
            String insertQuery = "INSERT INTO mensagem (id, new_content, fk_member_id, mtimestamp, channel_id) VALUES (?, ?, ?, ?, ?)";

            try (PreparedStatement selectStatement = getConnexao().prepareStatement(selectQuery)) {
                selectStatement.setString(1, id);
                ResultSet resultSet = selectStatement.executeQuery();

                resultSet.next();
                int count = resultSet.getInt("count");

                if (count == 0) {
                    {
                        try (PreparedStatement insertStatement = getConnexao().prepareStatement(insertQuery)) {
                            insertStatement.setString(1, id);
                            insertStatement.setString(2, mensagem);
                            insertStatement.setString(3, membro.getId());
                            insertStatement.setInt(4, timestamp);
                            insertStatement.setString(5, channelId);
                            insertStatement.executeUpdate();
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            desconectaDoBanco();
        }
    }
    public static void editaMensagem(String id, String mensagem) throws SQLException {
        try {
            conectaNoBanco();
            String updateQuery = "UPDATE mensagem SET old_content = new_content, new_content = ? WHERE id = ?";

            try (PreparedStatement updateStatement = getConnexao().prepareStatement(updateQuery)) {
                updateStatement.setString(1, mensagem);
                updateStatement.setString(2, id);
                updateStatement.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            desconectaDoBanco();
        }
    }



}
