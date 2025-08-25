package Program;

import java.util.List;

import model.dao.SellerDao;
import model.dao.impl.DaoFactory;
import model.entities.Department;
import model.entities.Seller;

public class application {

	public static void main(String[] args) {

		SellerDao sl = new DaoFactory().createSellerDao();
		
		System.out.println("=== TEST 1: seller findById =====");
		Seller seller = sl.findById(3);
		
		System.out.println(seller);
		
		System.out.println("=== TEST 2: seller findByDepartment =====");
		Department dep = new Department(2, null);
		List<Seller> list = sl.findByDepartment(dep);
		
		list.forEach(System.out :: println);
		
		System.out.println("=== TEST 3: seller findByAll =====");
		list = sl.findAll();
		
		list.forEach(System.out :: println);
	}

}