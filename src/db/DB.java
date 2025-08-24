package db;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class DB {
	
	// Guarda a conexão com o banco, pra usar sempre a mesma (singleton)
		private static Connection conn = null;

		// Método que retorna a conexão com o banco
		public static Connection getConnection() {

			// Se não existe conexão aberta, cria uma
			if (conn == null) {
				try {
					// Carrega as propriedades do arquivo (user, senha, url)
					Properties props = loadProperties();
					// Pega a URL do banco dentro das propriedades
					String url = props.getProperty("dburl");

					// Cria a conexão usando URL e as propriedades (user, password)
					conn = DriverManager.getConnection(url, props);

				} catch (SQLException e) {
					// Se der erro no SQL, lança uma exceção personalizada (DBException)
					throw new DBException(e.getMessage());
				}
			}

			// Retorna a conexão existente ou recém criada
			return conn;
		}

		// Método para carregar o arquivo db.properties e retornar as propriedades
		private static Properties loadProperties() {
			// Tenta abrir o arquivo db.properties usando FileInputStream
			try (FileInputStream fs = new FileInputStream("db.properties")) {
				// Cria o objeto Properties vazio
				Properties props = new Properties();
				// Carrega as propriedades do arquivo para o objeto props
				props.load(fs);

				// Retorna o objeto com as propriedades carregadas
				return props;

			} catch (IOException e) {
				// Se der erro lendo o arquivo, lança a exceção personalizada
				throw new DBException(e.getMessage());
			}
		}

		// Método para fechar a conexão com o banco
		public static void closeConnection() {
			// Só tenta fechar se a conexão existir
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					throw new DBException(e.getMessage());
				}
			}
		}

		public static void closeStatement(Statement st) {

			if (st != null) {

				try {
					st.close();

				} catch (SQLException e) {

					throw new DBException(e.getMessage());
				}
			}

		}

		public static void closeResultSet(ResultSet rs) {

			if (rs != null) {

				try {
					rs.close();

				} catch (SQLException e) {

					throw new DBException(e.getMessage());
				}
			}

		}
}
