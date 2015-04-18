package edu.uab.cis.cs302.library;

/**
 * represents an immutable interface for the books in the library
 */
public interface BookInfo
{
  /**
   * returns unique book id
   */
  String getId();

  /**
   * returns book title
   */
  String getTitle();

  /**
   * returns book author
   */
  String getAuthor();

  /**
   * returns current loan record
   * @return null, if the book is available
   */
  LoanInfo getLoanRecord();
}
