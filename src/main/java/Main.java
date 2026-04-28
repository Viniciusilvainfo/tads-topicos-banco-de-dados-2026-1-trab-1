import cache.LivroCache;
import config.JPAUtil;
import jdbc.LivroJDBC;
import model.Livro;
import repository.LivroRepository;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        LivroJDBC livroJDBC = new LivroJDBC();
        LivroRepository livroRepository = new LivroRepository();

        List<Livro> livrosJDBC = livroJDBC.listarLivros();
        System.out.println("Total de livros via JDBC: " + livrosJDBC.size());

        List<Livro> livrosJPA = livroRepository.listarTodos();
        System.out.println("Total de livros via JPA: " + livrosJPA.size());

        try (LivroCache livroCache = new LivroCache()) {
            long inicioSemCache = System.nanoTime();
            livroJDBC.listarLivros();
            long fimSemCache = System.nanoTime();

            livroCache.limparCache();
            livroCache.listarLivros();

            long inicioComCache = System.nanoTime();
            livroCache.listarLivros();
            long fimComCache = System.nanoTime();

            long tempoSemCacheMs = (fimSemCache - inicioSemCache) / 1_000_000;
            long tempoComCacheMs = (fimComCache - inicioComCache) / 1_000_000;

            System.out.println("Tempo sem cache: " + tempoSemCacheMs + " ms");
            System.out.println("Tempo com cache: " + tempoComCacheMs + " ms");

            List<Livro> livrosAutor = livroRepository.buscarPorAutor("Machado de Assis");
            System.out.println("Livros do autor 'Machado de Assis': " + livrosAutor.size());
        } finally {
            JPAUtil.close();
        }
    }
}
