
package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Classe utilitária responsável por gerenciar a conexão com o banco de dados MySQL.
 * <p>
 * Centraliza as configurações de conexão (URL, usuário, senha) e fornece um
 * método estático para obter uma nova conexão. Isso promove o reuso de código
 * e facilita a manutenção das credenciais do banco.
 *
 * @author Gemini
 */
public class ConexaoMySQL {

    // --- ATENÇÃO: Configure suas credenciais aqui ---
    // URL de conexão: jdbc:mysql://<servidor>:<porta>/<nome_do_banco>
    private static final String URL = "jdbc:mysql://localhost:3306/supermercado_db";

    // Usuário do banco de dados
    private static final String USER = "root"; // <-- SUBSTITUA PELO SEU USUÁRIO

    // Senha do banco de dados
    private static final String PASSWORD = "123456"; //<-- SUBSTITUA PELO SUA SENHA

    /**
     * Tenta estabelecer e retornar uma conexão com o banco de dados.
     * <p>
     * Carrega o driver JDBC do MySQL e utiliza o DriverManager para criar a conexão.
     * Em caso de falha, imprime o erro no console e lança uma RuntimeException
     * para interromper a operação que dependia da conexão.
     *
     * @return um objeto {@link Connection} pronto para ser usado.
     * @throws RuntimeException se a conexão com o banco de dados falhar.
     */
    public static Connection getConexao() {
        try {
            // Garante que a classe do driver MySQL seja carregada na memória.
            Class.forName("com.mysql.cj.jdbc.Driver");
            // Retorna a conexão estabelecida com sucesso.
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("ERRO DE CONEXÃO: Não foi possível conectar ao banco de dados MySQL.");
            e.printStackTrace();
            // Lança uma exceção não verificada para sinalizar uma falha grave na aplicação.
            throw new RuntimeException("Erro ao conectar ao banco de dados", e);
        }
    }
}
