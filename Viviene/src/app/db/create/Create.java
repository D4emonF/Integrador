package app.db.create;

import java.sql.SQLException;

import static app.db.DataBaseConnection.*;

public class Create {

    public static void criaTabelas() {
        try {
            conectaNoBanco();
            createMembroTable();
            createCargoVipTable();
            createCanalTable();
            createFotoInstagramTable();
            createSorteioTable();
            createCurtidaTable();
            createComentarioTable();
            createParticipanteTable();
            createCargoTable();
            createMessageTable();
            System.out.println("Tabelas criadas!");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                desconectaDoBanco();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                fechaStatement();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static void createMembroTable() {
        try {
            conectaNoBanco();
            getStatement().execute("""
                        CREATE TABLE membro (
                              id            TEXT    PRIMARY KEY,
                              tempo_inicial INTEGER,
                              tempo_total   INTEGER,
                              afk           TEXT,
                              bangif        BLOB
                          );
                          
                    """);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            desconectaDoBanco();
        }
    }

    private static void createCargoVipTable() {
        try {
            conectaNoBanco();
            getStatement().execute("""
                        CREATE TABLE cargo_vip (
                            id TEXT PRIMARY KEY,
                            nome TEXT,
                            fk_membro_id TEXT,
                            fk_canal_id TEXT,
                            FOREIGN KEY (fk_membro_id) REFERENCES membro (id),
                            FOREIGN KEY (fk_canal_id) REFERENCES canal (id) ON DELETE CASCADE
                        );
                    """);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            desconectaDoBanco();
        }
    }

    private static void createCanalTable() {
        try {
            conectaNoBanco();
            getStatement().execute("""
                        CREATE TABLE canal (
                            id TEXT PRIMARY KEY,
                            nome TEXT,
                            tipo TEXT,
                            categoria TEXT
                        );
                    """);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            desconectaDoBanco();
        }
    }

    private static void createFotoInstagramTable() {
        try {
            conectaNoBanco();
            getStatement().execute("""
                        CREATE TABLE foto_instagram (
                            id_mensagem TEXT PRIMARY KEY,
                            fk_membro_id TEXT,
                            FOREIGN KEY (fk_membro_id) REFERENCES membro (id) ON DELETE CASCADE
                        );
                    """);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            desconectaDoBanco();
        }
    }

    private static void createSorteioTable() {
        try {
            conectaNoBanco();
            getStatement().execute("""
                        CREATE TABLE sorteio (
                            id_sorteio TEXT PRIMARY KEY,
                            premio TEXT,
                            fk_membro_id TEXT,
                            FOREIGN KEY (fk_membro_id) REFERENCES membro (id) ON DELETE CASCADE
                        );
                    """);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            desconectaDoBanco();
        }
    }

    private static void createCurtidaTable() {
        try {
            conectaNoBanco();
            getStatement().execute("""
                        CREATE TABLE curtida (
                            fk_membro_id TEXT,
                            fk_foto_instagram_id_mensagem TEXT,
                            FOREIGN KEY (fk_membro_id) REFERENCES membro (id) ON DELETE SET NULL,
                            FOREIGN KEY (fk_foto_instagram_id_mensagem) REFERENCES foto_instagram (id_mensagem) ON DELETE SET NULL
                        );
                    """);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            desconectaDoBanco();
        }
    }

    private static void createComentarioTable() {
        try {
            getStatement().execute("""
                        CREATE TABLE comentario (
                            fk_membro_id TEXT,
                            fk_foto_instagram_id_mensagem TEXT,
                            comentario TEXT,
                            FOREIGN KEY (fk_membro_id) REFERENCES membro (id) ON DELETE SET NULL,
                            FOREIGN KEY (fk_foto_instagram_id_mensagem) REFERENCES foto_instagram (id_mensagem) ON DELETE SET NULL
                        );
                    """);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            desconectaDoBanco();
        }
    }

    private static void createParticipanteTable() {
        try {
            conectaNoBanco();
            getStatement().execute("""
                        CREATE TABLE participante (
                            fk_membro_id TEXT,
                            fk_sorteio_id_sorteio TEXT,
                            FOREIGN KEY (fk_membro_id) REFERENCES membro (id) ON DELETE SET NULL,
                            FOREIGN KEY (fk_sorteio_id_sorteio) REFERENCES sorteio (id_sorteio) ON DELETE SET NULL
                        );
                    """);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            desconectaDoBanco();
        }
    }

    private static void createCargoTable() {
        try {
            conectaNoBanco();
            getStatement().execute("""  
                    create table cargo
                    (
                        id         text,
                        perm_level integer default 0 not null,
                        nome       text
                    );
                    """);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            desconectaDoBanco();
        }
    }

    private static void createMessageTable() {
        try {
            conectaNoBanco();
            getStatement().execute("""
                   create table mensagem
                   (
                       id           TEXT    not null
                           constraint mensagem_pk
                               primary key,
                       new_content  TEXT    not null,
                       old_content  text,
                       fk_member_id text
                           constraint fk_membro_id
                               references membro,
                       mtimestamp    integer not null,
                       channel_id   text    not null
                   );
                   ;""");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            desconectaDoBanco();
        }
    }
}