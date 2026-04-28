# Trabalho 1 - Catalogo de Livros (simples)

Projeto Java simples e separado para cumprir os requisitos:

- JDBC
- JPA/Hibernate
- Redis (cache)
- Comparacao de desempenho no console

## Estrutura

```text
src/main/java/
  model/Livro.java
  jdbc/LivroJDBC.java
  repository/LivroRepository.java
  cache/LivroCache.java
  config/DatabaseConfig.java
  config/JPAUtil.java
  Main.java
```

## Requisitos

- Java 17+
- Maven 3.9+
- PostgreSQL ativo
- Redis ativo

## 1) Criar banco e tabela

Use o script em `sql/init.sql`.

## 2) Ajustar configuracao

Padroes usados no codigo:

- DB_URL=jdbc:postgresql://localhost:5432/livrosdb
- DB_USER=postgres
- DB_PASSWORD=postgres
- REDIS_HOST=localhost
- REDIS_PORT=6379
- REDIS_CACHE_KEY=livros:listarTodos

Se quiser, exporte variaveis de ambiente antes de executar.

## 3) Executar

```bash
mvn clean compile
mvn exec:java
```

## Saida esperada (exemplo)

```text
Total de livros via JDBC: 5
Total de livros via JPA: 5
Tempo sem cache: 15 ms
Tempo com cache: 1 ms
Livros do autor 'Machado de Assis': 1
```
