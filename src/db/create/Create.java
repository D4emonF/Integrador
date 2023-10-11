package db.create;


import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import static db.DataBaseConnection.*;


public class Create {

    public static void criaTabelas(){

        try {
            getStatement().execute("""
CREATE TABLE pessoa (
    Identificador   TEXT PRIMARY KEY
                         UNIQUE
                         NOT NULL,
    cnh             BLOB,
    rg              BLOB,
    nome            TEXT NOT NULL,
    numero          TEXT NOT NULL,
    email           TEXT NOT NULL,
    data_nascimento DATE NOT NULL,
    cidade          TEXT NOT NULL,
    estado          TEXT NOT NULL
);""");
            System.out.println("Tabela pessoas criada!");
            try{
                getStatement().execute("""
CREATE TABLE hospede (
    fk_pessoa_Identificador TEXT NOT NULL
                               REFERENCES pessoa (Identificador)\s
);
""");
                System.out.println("Tabela hospede criada!");
                try {
                    getStatement().execute("""
CREATE TABLE proprietario (
    fk_pessoa_Identificador TEXT NOT NULL
                               REFERENCES pessoa (Identificador)
);""");
                    System.out.println("Tabela proprietário criada!");
                    try{
                        getStatement().execute("""

CREATE TABLE imovel (
    rua            TEXT NOT NULL,
    quadra         TEXT NOT NULL,
    lote           TEXT NOT NULL,
    cidade         TEXT NOT NULL,
    estado         TEXT NOT NULL,
    casa_apto      TEXT,
    numero         TEXT,
    Identificador  TEXT PRIMARY KEY
                        NOT NULL,
    email_portaria TEXT,
    vagas          INT  NOT NULL
);


""");
                        System.out.println("Tabela Imóvel criada!");
                        try{
                            getStatement().execute("""
CREATE TABLE posse_imovel (
    fk_proprietario_fk_pessoa_Identificador TEXT NOT NULL
                                                 REFERENCES proprietario (fk_pessoa_Identificador),
    fk_imovel_Identificador                 TEXT REFERENCES imovel (Identificador)
                                                 NOT NULL
);
""");
                            System.out.println("Tabela posse_imovel criada!");
                            try {
                                getStatement().execute("""
CREATE TABLE estadia (
    fk_hospede_fk_pessoa_Identificador TEXT NOT NULL
                                            REFERENCES hospede (fk_pessoa_Identificador),
    fk_imovel_Identificador            TEXT NOT NULL
                                            REFERENCES imovel (Identificador),
    data_inicio                        TEXT NOT NULL,
    data_final                         TEXT NOT NULL,
    motivo_viagem                      TEXT NOT NULL
);
""");
                                System.out.println("Tabela estadia criada!");
                                System.out.println("Todas as tabelas foram criadas!");
                            } catch (SQLException e){
                                e.printStackTrace();
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    } catch (SQLException e){
                        e.printStackTrace();
                    }
                } catch (SQLException e){
                    e.printStackTrace();
                }
            } catch (SQLException e){
                e.printStackTrace();
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        finally {
            try {
              desconectaDoBanco();
            } catch (Exception e){
                e.printStackTrace();
            }
            try {
                fechaStatement();
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
