package aplicativo;

import java.io.File;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

import static db.DataBaseConnection.*;
import static db.create.Create.criaTabelas;

public class Principal {
    public static void main(String[] args) {
        try {
            conectaNoBanco();
            try {
                File arquivo = new File("Integrador.db");

                if (!arquivo.exists() || arquivo.length() == 0) {
                    // Verifique se a tabela 'pessoa' não existe antes de tentar criá-la
                    if (!tabelaExiste(getConnexao(), "pessoa")) {
                        criaTabelas();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        finally {
            desconectaDoBanco();
        }
    }

    // Função para verificar se a tabela existe no banco de dados
    private static boolean tabelaExiste(Connection conexao, String tabela) throws SQLException {
        DatabaseMetaData meta = conexao.getMetaData();
        try (var resultado = meta.getTables(null, null, tabela, null)) {
            return resultado.next();
        }
    }
}
