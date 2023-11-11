package app;




import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

import static app.configs.Intialize.inicializarBot;
import static app.db.DataBaseConnection.*;
import static app.db.create.Create.criaTabelas;

public class App {

    public static void main(String[] args) throws SQLException {
        try {
            inicializarBot();
            conectaNoBanco();
            if (!tabelaExiste(getConnexao(), "participante")) {
                criaTabelas();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        finally {
            desconectaDoBanco();
        }
    }

    private static boolean tabelaExiste(Connection conexao, String tabela) throws SQLException {
        DatabaseMetaData meta = conexao.getMetaData();
        try (ResultSet resultado = meta.getTables(null, null, tabela, null)) {
            return resultado.next();
        }
    }
}
