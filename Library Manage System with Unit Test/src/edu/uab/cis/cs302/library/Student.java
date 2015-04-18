package edu.uab.cis.cs302.library;

import java.util.ArrayList;
import java.util.Iterator;

import edu.uab.cis.cs302.library.observer.LibraryObserver;

/**
 * represents a student in the library
 */
public class Student implements StudentInfo
{
  /**
   * sets up a valid Student object
   *
   * @precondition uid != null && studentname != null && emailaddr != null && studentclass != null
   */
  public Student(String uid, String studentname, String emailaddr, String studentclass)
  {
    assert uid != null && studentname != null && emailaddr != null && studentclass != null
           : "Precondition violation : invalid null data";

    id        = uid;
    name      = studentname;
    email     = emailaddr;
    studentClass = studentclass;
    loans     = new ArrayList<Loan>();
    observers = new ArrayList<LibraryObserver>();
  }

  /**
   * returns student id
   */
  public String getId() { return id; }

  /**
   * returns student name
   */
  public String getName()  { return name;  }

  /**
   * returns email address
   */
  public String getEmail() { return email; }
  /**
   * returns student class
   */
  public String getstudentClass() { return studentClass; }

  /**
   * student checks out a book from the library
   * @param loanRecord record of the new transaction
   * @postcondition loanRecord != null && onLoanCount() > 0
   */
  public void checkOutBook(Loan loanRecord)
  {
    assert loanRecord != null : "Precondition failure: null record";
    if(studentClass =="graduate" || studentClass =="Graduate" || studentClass =="GRADUATE")
    assert onLoanCount() < 4 : "Precondition failure: too may loans";
    else assert onLoanCount() < 2 : "Precondition failure: too may loans";

    loans.add(loanRecord);
    stateChange();
  }
  /**
   * student checks out a dvd from the library
   * @param loanRecord record of the new transaction
   * @postcondition loanRecord != null && onLoanCount() > 0
   */
  public void checkOutDVD(Loan loanRecord)
  {
    assert loanRecord != null : "Precondition failure: null record";
    if(studentClass =="graduate" || studentClass =="Graduate" || studentClass =="GRADUATE")
    assert onLoanCount() < 4 : "Precondition failure: too may loans";
    else assert onLoanCount() < 2 : "Precondition failure: too may loans";

    loans.add(loanRecord);
    stateChange();
  }

  /**
   * student returns a book to the library
   *
   * @param loanRecord loan record for the book to be returned
   * @precondition loanRecord != null && getLoans.contains(loanRecord)
   */
  public void returnBook(Loan loanRecord)
  {
    assert loanRecord != null : "Precondition failure: null record";
    boolean found = loans.remove(loanRecord);

    assert found : "Precondition failure: Loan record not found";
    stateChange();
  }
  
  /**
   * student returns a dvd to the library
   *
   * @param loanRecord loan record for the dvd to be returned
   * @precondition loanRecord != null && getLoans.contains(loanRecord)
   */
  public void returnDVD(Loan loanRecord)
  {
    assert loanRecord != null : "Precondition failure: null record";
    boolean found = loans.remove(loanRecord);

    assert found : "Precondition failure: Loan record not found";
    stateChange();
  }
  /**
   * returns the number of books a student holds
   */
  public int onLoanCount()
  {
    return loans.size();
  }

  /**
   * register Observer
   */
  public void attach(LibraryObserver obs)
  {
    observers.add(obs);
  }

  /**
   * detach Observer
   */
  public void detach(LibraryObserver obs)
  {
    boolean success = observers.remove(obs);

    assert success : "Observer was not registered";
  }

  /**
   * internal function that notifies all registered observers
   */
  private void stateChange()
  {
    for (LibraryObserver obs : observers)
    {
      obs.stateChange(getId());
    }
  }

  /**
   * returns an iterator to the active loan records
   *
   * @return an iterator to all loan records for this student
   */
  public Iterator<LoanInfo> loanIterator()
  {
    // see comment in Library.studentIterator.

    final Iterator<Loan> iter = loans.iterator();

    return new Iterator<LoanInfo>() {
              public boolean hasNext() { return iter.hasNext(); }
              public LoanInfo next() { return iter.next(); }
              public void remove() { throw new UnsupportedOperationException(); }
           };
  }

  /**
   * tests whether a student holds the item currently
   *
   * @return an iterator to all loan records for this student
   */
  public boolean holds(Loan loan)
  {
    return loans.contains(loan);
  }

  private String                     id;
  private String                     name;
  private String                     email;
  private String                     studentClass;
  private ArrayList<Loan>            loans;
  private ArrayList<LibraryObserver> observers;
}
