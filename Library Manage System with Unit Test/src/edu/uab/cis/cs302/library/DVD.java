package edu.uab.cis.cs302.library;

public class DVD implements DVDInfo {
	/**
	   * sets up a valid Book object
	   *
	   * @precondition dvdid != null && dvdtitle != null && dvdauthor != null  && dvdrating != null
	   */
	  public DVD(String dvdid, String dvdtitle, String dvddirector, String dvdrating )
	  {
	    assert dvdid != null && dvdtitle != null && dvddirector != null && dvdrating != null
	           : "Precondition violation : invalid null data";

	    id         = dvdid;
	    title      = dvdtitle;
	    director   = dvddirector;
	    rating     = dvdrating;
	    loanRecord = null;
	  }
	  /**
	   * returns unique dvd id
	   */
	public String getId() {
		
		return id;
	}

	/**
	   * returns dvd title
	   */
	public String getTitle() {
		
		return title;
	}

	/**
	   * returns dvd director
	   */
	public String getDirector() {
	
		return director;
	}

	/**
	   * returns current loan record
	   * @return null, if the dvd is available
	   */
	public Loan getLoanRecord() {
		
		return loanRecord;
	}
	
	/**
	   * returns dvd rating
	   */
	public String getRating() {
		
		return rating;
	}

	 /**
	   * sets current loan record
	   * @param checkoutRecord check out data
	   * @precondition checkoutRecord must not be null
	   */
	  public void checkOutDVD(Loan checkoutRecord)
	  {
	    assert loanRecord == null : "Precondition failed : DVD already checked out";

	    loanRecord = checkoutRecord;
	  }

	  /**
	   * clears check out record
	   * @param loan checkout data for the dvd returned
	   * @precondition @loan must be the same as the stored loan record
	   */
	  public void returnDVD(Loan loan)
	  {
	    assert loanRecord == loan : "Precondition failed : DVD not checked out";

	    loanRecord = null;
	  }

	  private String id;
	  private String title;
	  private String director;
	  private String rating;
	  private Loan   loanRecord;
}
