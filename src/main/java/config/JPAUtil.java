package config;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public final class JPAUtil {

    private static final EntityManagerFactory EMF = Persistence.createEntityManagerFactory("livrosPU");

    private JPAUtil() {
    }

    public static EntityManagerFactory emf() {
        return EMF;
    }

    public static void close() {
        if (EMF.isOpen()) {
            EMF.close();
        }
    }
}
