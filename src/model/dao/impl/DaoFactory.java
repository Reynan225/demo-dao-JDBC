package model.dao.impl;

import db.DB;
import model.dao.DepartmentDao;
import model.dao.SellerDao;

public class DaoFactory {
	
	/*Essa classe será responsável por inicializar as
	 * nossas classes DAOJDBC, isso é uma boa prática
	 * para evitar que a implementação da interface
	 * seja exposta e fácil de manuntenção */

	public static SellerDao createSellerDao() {
		return new SellerDaoJDBC(DB.getConnection());
	}
		public static DepartmentDao createDepartmentDao() {
			return new DepartmentDaoJDBC(DB.getConnection());
	}
}