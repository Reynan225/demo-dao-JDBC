package model.dao;

import java.util.List;

import model.entities.Department;
import model.entities.Seller;

public interface SellerDao {

	void insert(Seller obj); // inserir um vendedor
	void update(Seller obj); // atualizar dados dos vendedores
	void deleteById(Integer id); // deletar um vendedor
	Seller findById(Integer id); // selecionar um vendedor por Id
	List<Seller> findAll(); // Pegar todos os registros dos vendedores
	List<Seller> findByDepartment(Department department); // pegar todos os registros dos vendedores com os departamentos
													      // respectivos	 
}
