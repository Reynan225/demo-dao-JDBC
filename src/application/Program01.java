package application;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

import model.dao.SellerDao;
import model.dao.impl.DaoFactory;
import model.entities.Department;
import model.entities.Seller;

public class Program01 {

	public static void main(String[] args) {

		Scanner sc = new Scanner(System.in);
		
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
		
		System.out.println("=== TEST 4: seller Insert =====");
		Seller newSeller = new Seller(null, "Greg", "greg@gmail.com", LocalDate.now(), 4000.0, dep);
		sl.insert(newSeller);
		
		System.out.println("Inserted! New id = " + newSeller.getId());
		
		System.out.println("=== TEST 5: seller Update =====");
		seller = sl.findById(1);
		seller.setName("Martha Waine");
		sl.update(seller);

		System.out.println("Update completed");

		System.out.println("=== TEST 6: seller Delete =====");
		System.out.print("Enter id for delete test: ");
		int id = sc.nextInt();
		
		sl.deleteById(id);
		System.out.println("Delete completed");
		
		sc.close();
	}

}