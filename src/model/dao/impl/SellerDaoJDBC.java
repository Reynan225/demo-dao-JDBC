package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import db.DB;
import db.DBException;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

public class SellerDaoJDBC implements SellerDao {

	private Connection conn;

	// Construtor recebe a conexão e guarda pra usar nos métodos
	public SellerDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	// Insere um novo Seller no banco
	@Override
	public void insert(Seller obj) {
		PreparedStatement st = null;

		try {
			// Prepara o INSERT pedindo as chaves geradas
			st = conn.prepareStatement(
				"INSERT INTO seller "
				+ "(Name, Email, BirthDate, BaseSalary, DepartmentId) "
				+ "VALUES (?, ?, ?, ?, ?)",
				Statement.RETURN_GENERATED_KEYS);

			// Define os valores do Seller a inserir
			st.setString(1, obj.getName());
			st.setString(2, obj.getEmail());
			st.setDate(3, java.sql.Date.valueOf(obj.getBirthDate()));
			st.setDouble(4, obj.getBaseSalary());
			st.setInt(5, obj.getDepartment().getId());

			int rowsAffected = st.executeUpdate();

			if (rowsAffected > 0) {
				// Recupera o ID gerado pelo banco e seta no objeto
				ResultSet rs = st.getGeneratedKeys();
				if (rs.next()) {
					int id = rs.getInt(1);
					obj.setId(id);
				}
				DB.closeResultSet(rs);
			} else {
				throw new DBException("Unexpected error! No rows Affected!");
			}

		} catch (SQLException e) {
			throw new DBException("Error: " + e.getMessage());
		} finally {
			DB.closeStatement(st);
		}
	}

	// Atualiza os dados de um Seller já existente
	@Override
	public void update(Seller obj) {
		PreparedStatement st = null;

		try {
			st = conn.prepareStatement(
				"UPDATE seller "
				+ "SET Name = ?, Email = ?, BirthDate = ?, BaseSalary = ?, DepartmentId = ? "
				+ "WHERE Id = ?");

			st.setString(1, obj.getName());
			st.setString(2, obj.getEmail());
			st.setDate(3, java.sql.Date.valueOf(obj.getBirthDate()));
			st.setDouble(4, obj.getBaseSalary());
			st.setInt(5, obj.getDepartment().getId());
			st.setInt(6, obj.getId());

			st.executeUpdate();

		} catch (SQLException e) {
			throw new DBException("Error: " + e.getMessage());
		} finally {
			DB.closeStatement(st);
		}
	}

	// Deleta um Seller pelo ID
	@Override
	public void deleteById(Integer id) {
		PreparedStatement st = null;
		
		try {
			st = conn.prepareStatement(
				"DELETE FROM seller WHERE Id = ?");
			
			st.setInt(1, id);
			
			int rows = st.executeUpdate();
			
			if (rows == 0) {
				// Nenhum registro deletado → id inexistente
				throw new DBException("Nonexistent id");
			}
			
		} catch (SQLException e) {
			throw new DBException("Error: " + e.getMessage());
		}
	}

	// Busca um Seller pelo ID
	@Override
	public Seller findById(Integer id) {
		PreparedStatement st = null;
		ResultSet rs = null;

		try {
			st = conn.prepareStatement(
				"SELECT seller.*, department.Name as DepName "
				+ "FROM seller INNER JOIN department "
				+ "ON seller.DepartmentId = department.Id "
				+ "WHERE seller.Id = ?");

			st.setInt(1, id);
			rs = st.executeQuery();

			if (rs.next()) {
				// Cria o Department e o Seller a partir dos dados do banco
				Department dep = instantiateDepartment(rs);
				Seller obj = instantiateSeller(rs, dep);
				return obj;
			}

			return null;

		} catch (SQLException e) {
			throw new DBException("Error" + e.getMessage());
		} finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}

	// Método auxiliar para criar um Seller a partir do ResultSet
	private Seller instantiateSeller(ResultSet rs, Department dep) throws SQLException {
		Seller obj = new Seller();
		obj.setId(rs.getInt("Id"));
		obj.setName(rs.getString("Name"));
		obj.setEmail(rs.getString("Email"));
		obj.setBaseSalary(rs.getDouble("BaseSalary"));
		obj.setBirthDate(rs.getDate("BirthDate").toLocalDate());
		obj.setDepartment(dep);
		return obj;
	}

	// Método auxiliar para criar um Department a partir do ResultSet
	private Department instantiateDepartment(ResultSet rs) throws SQLException {
		Department dep = new Department();
		dep.setId(rs.getInt("DepartmentId"));
		dep.setName(rs.getString("DepName"));
		return dep;
	}

	// Busca todos os Sellers (com os respectivos Departments)
	@Override
	public List<Seller> findAll() {
		PreparedStatement st = null;
		ResultSet rs = null;

		try {
			st = conn.prepareStatement(
				"SELECT seller.*, department.Name as DepName " 
				+ "FROM seller "
				+ "INNER JOIN department "
				+ "ON seller.DepartmentId = department.Id "
				+ "ORDER BY Name");

			rs = st.executeQuery();

			List<Seller> list = new ArrayList<>();
			Map<Integer, Department> map = new HashMap<>(); // cache de departamentos

			while (rs.next()) {
				// Busca no cache se já temos o Department carregado
				Department dep = map.get(rs.getInt("DepartmentId"));

				if (dep == null) {
					// Se não, cria e adiciona no cache
					dep = instantiateDepartment(rs);
					map.put(rs.getInt("DepartmentId"), dep);
				}
				// Cria o Seller associando ao Department já cacheado
				Seller obj = instantiateSeller(rs, dep);
				list.add(obj);
			}
 /*
  * Isso evita criar vários objetos Department duplicados para o mesmo ID.
  * Todos os Seller que pertencem ao mesmo departamento vão compartilhar a
  * mesma instância de Department. Ultilizando a mesma técnia no findByDepartment;
  */
			return list;

		} catch (SQLException e) {
			throw new DBException("Error" + e.getMessage());
		} finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}

	// Busca todos os Sellers de um determinado Department
	@Override
	public List<Seller> findByDepartment(Department department) {
		PreparedStatement st = null;
		ResultSet rs = null;

		try {
			st = conn.prepareStatement(
				"SELECT seller.*, department.Name as DepName " 
				+ "FROM seller INNER JOIN department "
				+ "ON seller.DepartmentId = department.Id "
				+ "WHERE DepartmentId = ? "
				+ "ORDER BY Name");

			st.setInt(1, department.getId());
			rs = st.executeQuery();

			List<Seller> list = new ArrayList<>();
			Map<Integer, Department> map = new HashMap<>(); // cache de departamentos

			while (rs.next()) {
				Department dep = map.get(rs.getInt("DepartmentId"));

				if (dep == null) {
					dep = instantiateDepartment(rs);
					map.put(rs.getInt("DepartmentId"), dep);
				}

				Seller obj = instantiateSeller(rs, dep);
				list.add(obj);
			}

			return list;

		} catch (SQLException e) {
			throw new DBException("Error" + e.getMessage());
		} finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}
}
