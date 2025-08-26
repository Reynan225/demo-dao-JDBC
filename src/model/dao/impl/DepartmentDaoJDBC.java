package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import db.DB;
import db.DBException;
import db.DbIntegrityException;
import model.dao.DepartmentDao;
import model.entities.Department;

public class DepartmentDaoJDBC implements DepartmentDao {

	private Connection conn;

	// Construtor padrão (não recebe conexão)
	public DepartmentDaoJDBC() {
	}

	// Construtor que recebe a conexão com o banco
	public DepartmentDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	// Insere um novo departamento no banco
	@Override
	public void insert(Department obj) {

		PreparedStatement st = null;

		try {
			// Prepara a instrução SQL de INSERT, pedindo o retorno da chave gerada
			st = conn.prepareStatement(
					"INSERT INTO department (Name) VALUES (?)",
					Statement.RETURN_GENERATED_KEYS);

			// Define o valor do parâmetro (nome do departamento)
			st.setString(1, obj.getName());

			// Executa o comando e guarda o número de linhas afetadas
			int rowsAffected = st.executeUpdate();

			// Se pelo menos uma linha foi inserida
			if (rowsAffected > 0) {
				// Recupera a chave primária gerada pelo banco (auto_increment)
				ResultSet rs = st.getGeneratedKeys();

				if (rs.next()) {
					int id = rs.getInt(1); // pega o id gerado
					obj.setId(id);         // atualiza o objeto com o id
				}

				DB.closeResultSet(rs);
			} else {
				throw new DBException("Unexpected error! No rows Affected!");
			}

		} catch (SQLException e) {
			// Se der qualquer erro de SQL, lança exceção personalizada
			throw new DBException("Error: " + e.getMessage());

		} finally {
			// Fecha o PreparedStatement
			DB.closeStatement(st);
		}
	}

	// Atualiza um departamento existente no banco
	@Override
	public void update(Department obj) {

		PreparedStatement st = null;

		try {
			// Prepara a instrução SQL de UPDATE
			st = conn.prepareStatement(
					"UPDATE department SET Name = ? WHERE Id = ?");

			// Define os valores (nome e id do departamento)
			st.setString(1, obj.getName());
			st.setInt(2, obj.getId());

			// Executa o comando
			st.executeUpdate();

		} catch (SQLException e) {
			System.out.println("Error: " + e.getMessage());
		} finally {
			DB.closeStatement(st);
		}
	}

	// Deleta um departamento pelo id
	@Override
	public void deleteById(Integer id) {

		PreparedStatement st = null;

		try {
			// Prepara a instrução SQL de DELETE
			st = conn.prepareStatement(
					"DELETE FROM department WHERE Id = ?");

			// Define o id a ser deletado
			st.setInt(1, id);

			// Executa o comando
			st.executeUpdate();

		} catch (SQLException e) {
			// Se o departamento estiver relacionado com vendedores,
			// o banco lança erro de integridade -> tratamos aqui
			throw new DbIntegrityException(e.getMessage());
			
		} finally {
			DB.closeStatement(st);
		}
	}

	// Busca um departamento pelo id
	@Override
	public Department findById(Integer id) {

		PreparedStatement st = null;
		ResultSet rs = null;

		try {
			// Prepara a instrução SQL de SELECT
			st = conn.prepareStatement(
					"SELECT Name, Id FROM department WHERE Id = ?");

			// Define o id a ser buscado
			st.setInt(1, id);

			// Executa a consulta
			rs = st.executeQuery();

			// Se encontrar resultado, instancia e retorna o objeto
			if (rs.next()) {
				Department dep = new Department();
				dep.setName(rs.getString("Name"));
				dep.setId(rs.getInt("Id"));
				return dep;
			}

		} catch (SQLException e) {
			System.out.println("Error: " + e.getMessage());
		}

		// Se não encontrar, retorna null
		return null;
	}

	// Busca todos os departamentos
	@Override
	public List<Department> findAll() {

		PreparedStatement st = null;
		ResultSet rs = null;

		List<Department> list = new ArrayList<Department>();

		try {
			// Prepara a instrução SQL de SELECT (ordenando por nome)
			st = conn.prepareStatement(
					"SELECT * FROM department ORDER BY Name");

			// Executa a consulta
			rs = st.executeQuery();

			// Percorre os resultados e adiciona na lista
			while (rs.next()) {
				list.add(new Department(rs.getInt("Id"), rs.getString("Name")));
			}
			return list;

		} catch (SQLException e) {
			throw new DBException("Error " + e.getMessage());

		} finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}
}
