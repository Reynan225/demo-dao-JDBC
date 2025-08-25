package Program;

import model.dao.SellerDao;
import model.dao.impl.DaoFactory;
import model.entities.Seller;

public class application {

	public static void main(String[] args) {

		SellerDao sl = new DaoFactory().createSellerDao();
		
		System.out.println("=== TEST 1: seller findById =====");
		Seller seller = sl.findById(3);
		
		System.out.println(seller);
	}

}
