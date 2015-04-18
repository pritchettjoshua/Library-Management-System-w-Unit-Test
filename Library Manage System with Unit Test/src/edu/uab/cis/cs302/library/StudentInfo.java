package edu.uab.cis.cs302.library;

import java.util.ArrayList;
import java.util.Iterator;

import edu.uab.cis.cs302.library.observer.LibraryObserver;

/**
 * Represents an interface to access the student on the GUI side.
 *
 * The interface only exposes an immutable interface; any updates will
 * be performed through the Library Object. This allows us to send to
 * share the student objects with the GUI side without exposing them to
 * the risk that they are manipulated on the client side.
 */
public interface StudentInfo
{
  /**
   * returns student id
   */
  String getId();

  /**
   * returns student name
   */
  String getName();

  /**
   * returns email address
   */
  String getEmail();
  /**
   * returns student classification 
   */
  String getstudentClass();

  /**
   * returns the number of books a student holds
   */
  int onLoanCount();

  /**
   * returns an iterator to the active loan records
   *
   * @return an iterator to all loan records for this student
   */
  Iterator<LoanInfo> loanIterator();
}
