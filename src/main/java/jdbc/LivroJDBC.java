package jdbc;

import config.DatabaseConfig;
import model.Livro;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LivroJDBC {

    private static final String SQL_LISTAR = "SELECT id, titulo, autor, ano_publicacao, categoria FROM livro ORDER BY id";

    public List<Livro> listarLivros() {
        List<Livro> livros = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(
                DatabaseConfig.dbUrl(),
                DatabaseConfig.dbUser(),
                DatabaseConfig.dbPassword()
        );
             PreparedStatement statement = connection.prepareStatement(SQL_LISTAR);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Livro livro = new Livro(
                        resultSet.getLong("id"),
                        resultSet.getString("titulo"),
                        resultSet.getString("autor"),
                        resultSet.getInt("ano_publicacao"),
                        resultSet.getString("categoria")
                );
                livros.add(livro);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar livros com JDBC", e);
        }

        return livros;
    }
}
