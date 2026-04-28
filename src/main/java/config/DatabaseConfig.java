package config;

public final class DatabaseConfig {

    private DatabaseConfig() {
    }

    public static String dbUrl() {
        return env("DB_URL", "jdbc:postgresql://localhost:5432/livrosdb");
    }

    public static String dbUser() {
        return env("DB_USER", "postgres");
    }

    public static String dbPassword() {
        return env("DB_PASSWORD", "postgres");
    }

    public static String redisHost() {
        return env("REDIS_HOST", "localhost");
    }

    public static int redisPort() {
        return Integer.parseInt(env("REDIS_PORT", "6379"));
    }

    public static String cacheKey() {
        return env("REDIS_CACHE_KEY", "livros:listarTodos");
    }

    private static String env(String key, String defaultValue) {
        String value = System.getenv(key);
        return value == null || value.isBlank() ? defaultValue : value;
    }
}
