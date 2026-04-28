package model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "livro")
public class Livro {

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "titulo", nullable = false)
    private String titulo;

    @Column(name = "autor", nullable = false)
    private String autor;

    @Column(name = "ano_publicacao", nullable = false)
    private Integer anoPublicacao;

    @Column(name = "categoria", nullable = false)
    private String categoria;

    public Livro() {
    }

    public Livro(Long id, String titulo, String autor, Integer anoPublicacao, String categoria) {
        this.id = id;
        this.titulo = titulo;
        this.autor = autor;
        this.anoPublicacao = anoPublicacao;
        this.categoria = categoria;
    }

    public Long getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getAutor() {
        return autor;
    }

    public Integer getAnoPublicacao() {
        return anoPublicacao;
    }

    public String getCategoria() {
        return categoria;
    }

    @Override
    public String toString() {
        return "Livro{" +
                "id=" + id +
                ", titulo='" + titulo + '\'' +
                ", autor='" + autor + '\'' +
                ", anoPublicacao=" + anoPublicacao +
                ", categoria='" + categoria + '\'' +
                '}';
    }
}
