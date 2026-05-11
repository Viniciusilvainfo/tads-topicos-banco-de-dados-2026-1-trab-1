import cache.LivroCache;
import config.JPAUtil;
import jdbc.LivroJDBC;
import model.Livro;
import repository.LivroRepository;
import ui.CatalogoLivrosFrame;

import javax.swing.SwingUtilities;
import java.awt.GraphicsEnvironment;
import java.util.List;
import java.util.function.Supplier;

public class Main {

    public static void main(String[] args) {
        if (GraphicsEnvironment.isHeadless() || temArgumento(args, "--console") || temArgumento(args, "--benchmark")) {
            executarConsole();
            return;
        }

        SwingUtilities.invokeLater(() -> new CatalogoLivrosFrame().setVisible(true));
    }

    private static boolean temArgumento(String[] args, String alvo) {
        for (String argumento : args) {
            if (alvo.equalsIgnoreCase(argumento)) {
                return true;
            }
        }

        return false;
    }

    private static void executarConsole() {
        LivroJDBC livroJDBC = new LivroJDBC();
        LivroRepository livroRepository = new LivroRepository();

        try (LivroCache livroCache = new LivroCache()) {
            livroCache.limparCache();
            livroCache.listarLivros();

            List<Livro> livrosJDBC = livroJDBC.listarLivros();
            System.out.println("Total de livros via JDBC: " + livrosJDBC.size());

            List<Livro> livrosJPA = livroRepository.listarTodos();
            System.out.println("Total de livros via JPA: " + livrosJPA.size());

            long tempoSemCacheMs = medir(() -> livroJDBC.listarLivros());
            long tempoComCacheMs = medir(() -> livroCache.listarLivros());

            System.out.println("Tempo sem cache: " + tempoSemCacheMs + " ms");
            System.out.println("Tempo com cache: " + tempoComCacheMs + " ms");

            List<Livro> livrosAutor = livroRepository.buscarPorAutor("Machado de Assis");
            System.out.println("Livros do autor 'Machado de Assis': " + livrosAutor.size());
        } finally {
            JPAUtil.close();
        }
    }

    private static long medir(Supplier<List<Livro>> tarefa) {
        long inicio = System.nanoTime();
        tarefa.get();
        long fim = System.nanoTime();
        return (fim - inicio) / 1_000_000;
    }
}
