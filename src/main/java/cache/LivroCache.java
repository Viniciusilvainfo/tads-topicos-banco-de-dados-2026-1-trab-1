package cache;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import config.DatabaseConfig;
import jdbc.LivroJDBC;
import model.Livro;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.lang.reflect.Type;
import java.util.List;

public class LivroCache implements AutoCloseable {

    private static final Type LISTA_DE_LIVRO = new TypeToken<List<Livro>>() { } .getType();

    private final JedisPool jedisPool;
    private final LivroJDBC livroJDBC;
    private final Gson gson;

    public LivroCache() {
        this.jedisPool = new JedisPool(DatabaseConfig.redisHost(), DatabaseConfig.redisPort());
        this.livroJDBC = new LivroJDBC();
        this.gson = new Gson();
    }

    public List<Livro> listarLivros() {
        String key = DatabaseConfig.cacheKey();

        try (Jedis jedis = jedisPool.getResource()) {
            String cached = jedis.get(key);
            if (cached != null && !cached.isBlank()) {
                return gson.fromJson(cached, LISTA_DE_LIVRO);
            }

            List<Livro> livrosBanco = livroJDBC.listarLivros();
            jedis.set(key, gson.toJson(livrosBanco));
            return livrosBanco;
        }
    }

    public void limparCache() {
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.del(DatabaseConfig.cacheKey());
        }
    }

    @Override
    public void close() {
        jedisPool.close();
    }
}
