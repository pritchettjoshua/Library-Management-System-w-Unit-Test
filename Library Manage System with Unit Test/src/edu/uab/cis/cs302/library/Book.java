package edu.uab.cis.cs302.library;

/**
 * represents a book in the library
 */
public class Book implements BookInfo
{
  /**
   * sets up a valid Book object
   *
   * @precondition bookid != null && booktitle != null && bookauthor != null
   */
  public Book(String bookid, String booktitle, String bookauthor)
  {
    assert bookid != null && booktitle != null && bookauthor != null
           : "Precondition violation : invalid null data";

    id         = bookid;
    title      = booktitle;
    author     = bookauthor;
    loanRecord = null;
  }

  /**
   * returns unique book id
   */
  public String getId() { return id; }

  /**
   * returns book title
   */
  public String getTitle() { return title; }

  /**
   * returns book author
   */
  public String getAuthor()     { return author;     }

  /**
   * returns current loan record
   * @return null, if the book is available
   */
  public Loan getLoanRecord()
  {
    return loanRecord;
  }

  /**
   * sets current loan record
   * @param checkoutRecord check out data
   * @precondition checkoutRecord must not be null
   */
  public void checkOutBook(Loan checkoutRecord)
  {
    assert loanRecord == null : "Precondition failed : Book already checked out";

    loanRecord = checkoutRecord;
  }

  /**
   * clears check out record
   * @param loan checkout data for the book returned
   * @precondition @loan must be the same as the stored loan record
   */
  public void returnBook(Loan loan)
  {
    assert loanRecord == loan : "Precondition failed : Book not checked out";

    loanRecord = null;
  }

  private String id;
  private String title;
  private String author;
  private Loan   loanRecord;
}
