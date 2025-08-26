package model.dao;

import java.util.List;

import model.entities.Department;

public interface DepartmentDao {

	void insert(Department obj); //inserir um departamento
	void update(Department obj); //atulizar nome do departamento
	void deleteById(Integer id); // deletar um departamento por Id
	Department findById(Integer id); // Selecionar um departamento por Id
	List<Department> findAll(); // Pegar todos os registros de departamento
}
