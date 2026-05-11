package ui;

import cache.LivroCache;
import jdbc.LivroJDBC;
import model.Livro;
import repository.LivroRepository;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingWorker;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.List;

public class CatalogoLivrosFrame extends JFrame {

    private final LivroJDBC livroJDBC = new LivroJDBC();
    private final LivroRepository livroRepository = new LivroRepository();
    private final JTextArea saida = new JTextArea();
    private final JTextField autorCampo = new JTextField("Machado de Assis", 20);
    private final JLabel status = new JLabel("Pronto");

    public CatalogoLivrosFrame() {
        super("Catalogo de Livros");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(920, 640);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(12, 12));

        saida.setEditable(false);
        saida.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 14));
        saida.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        add(criarBarraSuperior(), BorderLayout.NORTH);
        add(new JScrollPane(saida), BorderLayout.CENTER);
        add(status, BorderLayout.SOUTH);

        mostrarTextoInicial();
    }

    private JPanel criarBarraSuperior() {
        JPanel painel = new JPanel(new GridLayout(2, 1, 8, 8));
        painel.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));

        JPanel acoes = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));

        JButton listarJdbc = new JButton("Listar JDBC");
        listarJdbc.addActionListener(evento -> executarAsync("Listando via JDBC", this::listarViaJdbc));

        JButton listarJpa = new JButton("Listar JPA");
        listarJpa.addActionListener(evento -> executarAsync("Listando via JPA", this::listarViaJpa));

        JButton listarRedis = new JButton("Listar Redis");
        listarRedis.addActionListener(evento -> executarAsync("Listando via Redis", this::listarViaRedis));

        JButton comparar = new JButton("Comparar tempo");
        comparar.addActionListener(evento -> executarAsync("Comparando desempenho", this::compararDesempenho));

        acoes.add(listarJdbc);
        acoes.add(listarJpa);
        acoes.add(listarRedis);
        acoes.add(comparar);

        JPanel autor = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        autor.add(new JLabel("Autor:"));
        autor.add(autorCampo);

        JButton buscarAutor = new JButton("Buscar autor");
        buscarAutor.addActionListener(evento -> executarAsync("Buscando por autor", this::buscarPorAutor));

        autor.add(buscarAutor);

        painel.add(acoes);
        painel.add(autor);
        return painel;
    }

    private void mostrarTextoInicial() {
        saida.setText("Escolha uma acao acima para listar os livros, consultar por autor ou comparar o tempo de resposta.\n");
    }

    private void executarAsync(String statusTexto, Conteudo tela) {
        status.setText(statusTexto);
        saida.setText("Processando...\n");

        new SwingWorker<String, Void>() {
            @Override
            protected String doInBackground() {
                return tela.executar();
            }

            @Override
            protected void done() {
                try {
                    saida.setText(get());
                    status.setText("Pronto");
                } catch (Exception erro) {
                    status.setText("Falha");
                    String mensagem = erro.getCause() == null ? erro.getMessage() : erro.getCause().getMessage();
                    JOptionPane.showMessageDialog(CatalogoLivrosFrame.this, mensagem, "Erro", JOptionPane.ERROR_MESSAGE);
                    saida.setText("Nao foi possivel executar a acao.\n");
                }
            }
        }.execute();
    }

    private String listarViaJdbc() {
        return formatarLista("JDBC", livroJDBC.listarLivros());
    }

    private String listarViaJpa() {
        return formatarLista("JPA", livroRepository.listarTodos());
    }

    private String listarViaRedis() {
        try (LivroCache livroCache = new LivroCache()) {
            return formatarLista("Redis", livroCache.listarLivros());
        }
    }

    private String buscarPorAutor() {
        String autor = autorCampo.getText().trim();
        if (autor.isBlank()) {
            return "Informe um autor para a busca.\n";
        }

        return formatarLista("Autor: " + autor, livroRepository.buscarPorAutor(autor));
    }

    private String compararDesempenho() {
        try (LivroCache livroCache = new LivroCache()) {
            livroCache.limparCache();
            livroCache.listarLivros();

            long tempoSemCache = medir(() -> livroJDBC.listarLivros());
            long tempoComCache = medir(() -> livroCache.listarLivros());

            return new StringBuilder()
                    .append("Tempo sem cache: ").append(tempoSemCache).append(" ms\n")
                    .append("Tempo com cache: ").append(tempoComCache).append(" ms\n")
                    .toString();
        }
    }

    private long medir(Operacao operacao) {
        long inicio = System.nanoTime();
        operacao.executar();
        long fim = System.nanoTime();
        return (fim - inicio) / 1_000_000;
    }

    private String formatarLista(String origem, List<Livro> livros) {
        StringBuilder texto = new StringBuilder();
        texto.append(origem).append('\n');
        texto.append("Total: ").append(livros.size()).append("\n\n");

        for (Livro livro : livros) {
            texto.append(livro.getId()).append(" | ")
                    .append(livro.getTitulo()).append(" | ")
                    .append(livro.getAutor()).append(" | ")
                    .append(livro.getAnoPublicacao()).append(" | ")
                    .append(livro.getCategoria()).append('\n');
        }

        return texto.toString();
    }

    @FunctionalInterface
    private interface Conteudo {
        String executar();
    }

    @FunctionalInterface
    private interface Operacao {
        void executar();
    }
}