package edu.uab.cis.cs302.library;
/**
 * represents an immutable interface for the DVDS in the library
 */

public interface DVDInfo {

	  /**
	   * returns unique DVD id
	   */
	  String getId();

	  /**
	   * returns DVD title
	   */
	  String getTitle();
	  /**
	   * returns DVD rating
	   */
	  String getRating();

	  /**
	   * returns DVD director
	   */
	  String getDirector();

	  /**
	   * returns current loan record
	   * @return null, if the book is available
	   */
	  LoanInfo getLoanRecord();
	

}
