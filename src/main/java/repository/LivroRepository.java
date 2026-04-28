package repository;

import config.JPAUtil;
import jakarta.persistence.EntityManager;
import model.Livro;

import java.util.List;

public class LivroRepository {

    public List<Livro> listarTodos() {
        EntityManager em = JPAUtil.emf().createEntityManager();
        try {
            return em.createQuery("SELECT l FROM Livro l ORDER BY l.id", Livro.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public List<Livro> buscarPorAutor(String autor) {
        EntityManager em = JPAUtil.emf().createEntityManager();
        try {
            return em.createQuery("SELECT l FROM Livro l WHERE l.autor = :autor ORDER BY l.id", Livro.class)
                    .setParameter("autor", autor)
                    .getResultList();
        } finally {
            em.close();
        }
    }
}
