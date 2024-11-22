package org.example.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBTest {
    private static Connection conn;

    public static Connection getConnection() throws SQLException {
        conn = DriverManager.getConnection("jdbc:h2:mem:test;MODE=PostgreSQL;DATABASE_TO_UPPER=false", "sa", "");
        conn.setCatalog("SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO';");
        conn.createStatement().execute("""
            CREATE TABLE IF NOT EXISTS address (
              id SERIAL PRIMARY KEY,
              country varchar(80) NOT NULL,
              state varchar(50) NOT NULL,
              cep char(8) NOT NULL
            );
            
            CREATE TABLE IF NOT EXISTS people (
              id SERIAL PRIMARY KEY,
              email varchar NOT NULL,
              name varchar NOT NULL,
              description text NOT NULL,
              address SERIAL NOT NULL,
              password varchar NOT NULL,
              created_at timestamp,
              updated_at timestamp,
              deleted_at timestamp
            );
            
            CREATE TABLE IF NOT EXISTS naturalpeople (
              idPerson SERIAL PRIMARY KEY,
              cpf varchar(11) UNIQUE NOT NULL,
              age integer NOT NULL
            );
            
            CREATE TABLE IF NOT EXISTS legalpeople (
              idPerson SERIAL PRIMARY KEY,
              cnpj varchar(14) UNIQUE NOT NULL
            );
            
            CREATE TABLE IF NOT EXISTS skills (
              id SERIAL PRIMARY KEY,
              title varchar NOT NULL,
              description text
            );
            
            CREATE TABLE IF NOT EXISTS jobs (
              id SERIAL PRIMARY KEY,
              idLegalPerson SERIAL NOT NULL,
              name varchar NOT NULL,
              description text NOT NULL,
              local SERIAL
            );
            
            CREATE TABLE IF NOT EXISTS match (
              idJob SERIAL PRIMARY KEY,
              likeNaturalPerson integer,
              status integer DEFAULT 0
            );
            
            COMMENT ON COLUMN match.status IS '1: accept
            2: reject
            ';
            
            ALTER TABLE people ADD FOREIGN KEY (address) REFERENCES address (id);
            
            ALTER TABLE naturalpeople ADD FOREIGN KEY (idPerson) REFERENCES people (id);
            
            ALTER TABLE legalpeople ADD FOREIGN KEY (idPerson) REFERENCES people (id);
            
            ALTER TABLE jobs ADD FOREIGN KEY (idLegalPerson) REFERENCES legalpeople (idPerson);
            
            ALTER TABLE jobs ADD FOREIGN KEY (local) REFERENCES address (id);
            
            ALTER TABLE match ADD FOREIGN KEY (idJob) REFERENCES jobs (id);
            
            ALTER TABLE match ADD FOREIGN KEY (likeNaturalPerson) REFERENCES naturalpeople (idPerson);
            
            CREATE TABLE IF NOT EXISTS skills_people (
              skills_id integer,
              people_id integer,
              PRIMARY KEY (skills_id, people_id)
            );
            
            ALTER TABLE skills_people ADD FOREIGN KEY (skills_id) REFERENCES skills (id);
            
            ALTER TABLE skills_people ADD FOREIGN KEY (people_id) REFERENCES people (id);
            
            INSERT INTO address (country, state, cep) VALUES 
            ('Brazil', 'Sao Paulo', '01001000'),
            ('Brazil', 'Rio de Janeiro', '20040000'),
            ('Brazil', 'Brasilia', '70000000'),
            ('Brazil', 'Salvador', '40000000'),
            ('Brazil', 'Fortaleza', '60000000'),
            ('Brasil', 'São Paulo', '01001000'),
            ('Brasil', 'Rio de Janeiro', '20040001'),
            ('Brasil', 'Minas Gerais', '30110010'),
            ('Brasil', 'Bahia', '40010000'),
            ('Brasil', 'Paraná', '80010000'),
            ('Brasil', 'Paraná', '80010000'),
            ('Brasil', 'Paraná', '80010000'),
            ('Brasil', 'Paraná', '80010000'),
            ('Brasil', 'Paraná', '80010000'),
            ('Brasil', 'Paraná', '80010000');
            
            INSERT INTO people (email, name, description, address, password, created_at, updated_at, deleted_at) VALUES 
            ('john.doe@example.com', 'John Doe', 'Description for John Doe', 1, 'password123', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL),
            ('jane.doe@example.com', 'Jane Doe', 'Description for Jane Doe', 2, 'password456', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL),
            ('alice.smith@example.com', 'Alice Smith', 'Description for Alice Smith', 3, 'password789', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL),
            ('bob.johnson@example.com', 'Bob Johnson', 'Description for Bob Johnson', 4, 'passwordabc', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL),
            ('emma.watson@example.com', 'Emma Watson', 'Description for Emma Watson', 5, 'passworddef', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL),
            ('joao@example.com', 'João', 'Description 1', 6, 'senha123', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL),
            ('maria@example.com', 'Maria', 'Description 2', 7, 'senha456', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL),
            ('carlos@example.com', 'Carlos', 'Description 3', 8, 'senha789', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL),
            ('ana@example.com', 'Ana', 'Description 4', 9, 'senha101112', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL),
            ('pedro@example.com', 'Pedro', 'Description 5', 10, 'senha131415', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL),
            ('cooper@example.com', 'Cooper', 'Description 6', 11, 'senha131415', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL),
            ('cooper2@example.com', 'Cooper2', 'Description 6_2', 12, 'senha131415', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL),
            ('cooper1@example.com', 'Cooper1', 'Description 6_1', 13, 'senha131415', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL),
            ('cooper3@example.com', 'Cooper3', 'Description 6_3', 14, 'senha131415', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL),
            ('cooper4@example.com', 'Cooper4', 'Description 6_4', 15, 'senha131415', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL);
            
            INSERT INTO legalpeople (idPerson, cnpj) VALUES 
            (1, '12345678000100'),
            (2, '98765432000101'),
            (3, '24680135000102'),
            (4, '13579246000103'),
            (5, '11223344000104');
            
            INSERT INTO naturalpeople (idPerson, cpf, age) VALUES 
            (6, '12345678900', 30),
            (7, '98765432100', 25),
            (8, '24680135700', 40),
            (9, '13579246800', 35),
            (10, '11223344500', 45);
            
            INSERT INTO skills (title, description) VALUES 
            ('Java', 'Programming language'),
            ('JavaScript', 'Programming language'),
            ('Python', 'Programming language');
            
            INSERT INTO skills_people (skills_id, people_id) VALUES 
            (1, 1),
            (2, 1),
            (3, 2);
            
            INSERT INTO skills (title, description) VALUES 
            ('SQL', 'Programming language'),
            ('C#', 'Programming language'),
            ('Ruby', 'Programming language');
            
            INSERT INTO skills_people (skills_id, people_id) VALUES 
            (4, 6),
            (5, 7),
            (6, 7);
        """);
        return conn;
    }

    public static void closeConnection(Connection conn) throws SQLException {
        if (conn != null && !conn.isClosed()) {
            conn.close();
        }
    }
}