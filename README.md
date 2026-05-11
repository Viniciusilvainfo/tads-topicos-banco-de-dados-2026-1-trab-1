# Trabalho 1 - Catalogo de Livros

Projeto Java com acesso a dados por JDBC, JPA/Hibernate e Redis, com interface grafica em Swing.

## Estrutura

```text
src/main/java/
  model/Livro.java
  jdbc/LivroJDBC.java
  repository/LivroRepository.java
  cache/LivroCache.java
  config/DatabaseConfig.java
  config/JPAUtil.java
  ui/CatalogoLivrosFrame.java
  Main.java
```

## Requisitos

- Java 17+
- Maven 3+
- PostgreSQL ativo
- Redis ativo

## Banco

Execute o script em `sql/init.sql` para criar a tabela e inserir os dados iniciais.

## Configuracao

Valores padrao:

- DB_URL=jdbc:postgresql://localhost:5432/livrosdb
- DB_USER=postgres
- DB_PASSWORD=postgres
- REDIS_HOST=localhost
- REDIS_PORT=6379
- REDIS_CACHE_KEY=livros:listarTodos

## Como executar

```bash
mvn clean compile
mvn exec:java
```

Se quiser testar sem abrir a interface, rode:

```bash
mvn exec:java -Dexec.args=--console
```

## O que a tela faz

- Listar livros via JDBC
- Listar livros via JPA
- Listar livros via Redis
- Buscar livros por autor
- Comparar tempo sem cache e com cache
