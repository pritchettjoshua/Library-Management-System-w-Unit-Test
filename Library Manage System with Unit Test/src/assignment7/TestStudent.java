package assignment7;

import java.util.Date;

import edu.uab.cis.cs302.library.Book;
import edu.uab.cis.cs302.library.Loan;
import edu.uab.cis.cs302.library.Student;

public class TestStudent {
	private static Student test;
	private static Loan loan;
	private static Book book;
	private static Date date;
/**
 * Tests the Constructor for the student object
 */
	public void testStudentConstructor(){
		    String uid = "93932";
			String name = "Joshua";
			String email = "email@email.com";
			String classification = "graduate";
			test = new Student(uid ,name,email,classification);
		
	}
	/**
	 * Test the method to see if the UTF actually attempts to test this method.
	 * it should print that it tested successfully as it contains test.
	 */
	public void testRahaul(){
		return;
	}
	/**
	 * Test method to ensure that the UTF is working correctly. To tell that 
	 * is correct, this should not be tested
	 */
	public void pleaseDontWork(){
		System.out.println("Hello I'm Joshua");
		return;
	}
	/**
	 * This method test the checkout method of a student object
	 */
	public void testStudentCheckOut(){
		Book book = new Book ("book1", "The Holy Bible", "Kaixang Wang");
		Date date = new Date();
		Loan loan = new Loan(test, book, date);
		
		test.checkOutBook(loan);
	}

}
