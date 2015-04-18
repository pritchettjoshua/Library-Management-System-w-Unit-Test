package edu.uab.cis.cs302.library;

import java.util.Date;

/**
 * represents an active loan record
 */
public class Loan implements LoanInfo
{
  /**
   * sets up a valid Loan object for a dvd
   *
   * @precondition aStudent != null && aBook != null && day != null
   */
  public Loan(Student aStudent, DVD aDVD, Date day)
  {
    assert aStudent != null && aDVD != null && day != null
           : "Precondition violation : invalid null data";

    student    = aStudent;
    dvd       = aDVD;
    dateLoaned = day;
  }
  
  /**
   * sets up a valid Loan object for a book
   *
   * @precondition aStudent != null && aBook != null && day != null
   */
  public Loan(Student aStudent, Book aBook, Date day)
  {
    assert aStudent != null && aBook != null && day != null
           : "Precondition violation : invalid null data";

    student    = aStudent;
    book       = aBook;
    dateLoaned = day;
  }

  /**
   * returns the student who loaned the book
   */
  public Student getStudent() { return student; }

  /**
   * returns the book associated with this loan
   */
  public Book getBook() { return book; }
  /**
   * returns the dvd associated with this loan
   */
  public DVD getDVD() { return dvd; }

  /**
   * returns the date when the check out took place
   */
  public Date getSince() { return dateLoaned; }

  private Student student;
  private Book    book;
  private DVD     dvd;
  private Date    dateLoaned;
}
