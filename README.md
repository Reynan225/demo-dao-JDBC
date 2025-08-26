# Projeto JDBC - Exemplo de DAO com Java

## Descrição do Projeto
Este projeto tem como objetivo demonstrar o uso do **JDBC (Java Database Connectivity)** para realizar operações de banco de dados utilizando Java puro.  
O projeto implementa os padrões **DAO (Data Access Object)** para gerenciar entidades como `Department` e `Seller`, incluindo operações de CRUD: inserir, atualizar, deletar e buscar registros.

O foco principal é **aplicar JDBC de forma prática**, incluindo:  
- Conexão com banco de dados.  
- Uso de `PreparedStatement` para consultas parametrizadas.  
- Tratamento de exceções personalizadas (`DBException`, `DbIntegrityException`).  
- Uso de `ResultSet` para recuperar dados do banco.  
- Gerenciamento de chaves geradas automaticamente (`getGeneratedKeys`).  
- Implementação de cache simples com `Map` para evitar criação duplicada de objetos relacionados (`Department`).

## Tecnologias Utilizadas
- Java 17 (ou superior)  
- JDBC API  
- Banco de dados MySQL ou outro compatível com JDBC  
- Estrutura DAO para separação de camadas de acesso a dados

## Estrutura do Projeto
- `model.entities` → classes de entidades (`Department`, `Seller`)  
- `model.dao` → interfaces DAO (`DepartmentDao`, `SellerDao`)  
- `model.dao.impl` → implementações DAO usando JDBC e DaoFactory responsável por chamar essas implementações (`DepartmentDaoJDBC`, `SellerDaoJDBC`, `DaoFactory`)   
- `db` → classes de suporte para conexão e tratamento de exceções (`DB`, `DBException`, `DbIntegrityException`)

## 🗄 Estrutura do Banco de Dados

### Tabela `department`
| Campo | Tipo        | Restrições     |
|-------|------------|----------------|
| Id    | INT        | PRIMARY KEY, AUTO_INCREMENT |
| Name  | VARCHAR(60)| NOT NULL       |

### Tabela `seller`
| Campo       | Tipo         | Restrições                         |
|-------------|-------------|------------------------------------|
| Id          | INT         | PRIMARY KEY, AUTO_INCREMENT        |
| Name        | VARCHAR(60) | NOT NULL                           |
| Email       | VARCHAR(100)| NOT NULL, UNIQUE                   |
| BirthDate   | DATE        | NOT NULL                           |
| BaseSalary  | DOUBLE      | NOT NULL                           |
| DepartmentId| INT         | FOREIGN KEY → Department(Id)       |

---

## 📑 Script SQL de Criação
```sql
CREATE DATABASE jdbc_demo;
USE jdbc_demo;

CREATE TABLE department (
  Id INT PRIMARY KEY AUTO_INCREMENT,
  Name VARCHAR(60) NOT NULL
);

CREATE TABLE seller (
  Id INT PRIMARY KEY AUTO_INCREMENT,
  Name VARCHAR(60) NOT NULL,
  Email VARCHAR(100) NOT NULL UNIQUE,
  BirthDate DATE NOT NULL,
  BaseSalary DOUBLE NOT NULL,
  DepartmentId INT,
  FOREIGN KEY (DepartmentId) REFERENCES department(Id)
);

```
## Principais Funcionalidades
- Inserir, atualizar, deletar e buscar departamentos e vendedores.  
- Garantia de integridade referencial ao deletar registros com dependências.  
- Recuperação de todas as entidades ou por filtros específicos (ex.: todos os sellers de um departamento).  
- Uso de prepared statements para evitar SQL injection.  

## Links Úteis
- [Documentação oficial JDBC](https://docs.oracle.com/javase/8/docs/technotes/guides/jdbc/)  
- [Tutorial JDBC Oracle](https://docs.oracle.com/javase/tutorial/jdbc/basics/index.html)  
- [PreparedStatement e ResultSet](https://www.baeldung.com/java-jdbc)  

## Observações
Este projeto é didático e foi desenvolvido para fins de aprendizado, com foco em **práticas de acesso a banco de dados utilizando Java puro** e aplicação de boas práticas de programação orientada a objetos e tratamento de exceções.
