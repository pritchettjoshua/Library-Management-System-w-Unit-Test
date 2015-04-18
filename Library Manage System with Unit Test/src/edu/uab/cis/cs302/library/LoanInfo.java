package edu.uab.cis.cs302.library;

import java.util.Date;

/**
 * represents an immutable interface for an active loan record
 */
public interface LoanInfo
{
  /**
   * returns the student who loaned the book
   */
  StudentInfo getStudent();

  /**
   * returns the book associated with this loan
   */
  
  BookInfo getBook();
  /**
   * returns the dvd associated with this loan
   */
  DVDInfo getDVD();

  /**
   * returns the date when the check out took place
   */
  Date getSince();
}
