package edu.uab.cis.cs302.library;

import java.util.Date;
import java.util.ArrayList;
import java.util.Iterator;

import java.text.SimpleDateFormat;

import java.io.PrintWriter;
import java.io.StringWriter;

import edu.uab.cis.cs302.library.observer.LibraryObserver;

public class Library
{
  /**
   * sets up a valid Library object
   *
   * Design choices: (1) it is assumed that the student's id is assigned by the
   *                     university, thus we accept that this is unique
   *                 (2) the library introduces a book counter (maxBookId) and maxDVDId to
   *                     generate new ids for books and dvds
   *                 (3) the library system manages all creations of Book, DVD
   *                     Student, and Loan objects. They do not escape to the client.
   *                 (4) This version handles clients communication through print streams
   *                 (5) Validity of arguments are checked in the public methods
   *                 (6) Students are now classified as graduate or undergraduate students.
   */
  public Library()
  {
    bookList    = new ArrayList<Book>();
    dvdList     = new ArrayList<DVD>();
    studentList = new ArrayList<Student>();
    maxBookId   = 0;
    maxDVDId    = 0;
  }

  /**
   * adds a new book to the library systems
   *
   * @precondition title != null && author != null
   */
  public void addBook(String title, String author)
  {
    assert title != null && author != null
           : "Precondition violation : invalid null data";

    String bookid = "" + (++maxBookId);  // generates a new unique book id
    assert findBook(bookid) == null : "Precondition failed : BookId in use";

    bookList.add(new Book(bookid, title, author));
  }
  
  /**
   * adds a new dvd to the library systems
   *
   * @precondition title != null && director != null && rating != null
   */
  public void addDVD(String title, String director, String rating)
  {
    assert title != null && director != null && rating != null
           : "Precondition violation : invalid null data";

    String dvdid = "" + (++maxDVDId);  // generates a new unique dvd id
    assert findDVD(dvdid) == null : "Precondition failed : BookId in use";

    dvdList.add(new DVD(dvdid, title, director, rating));
  }

  /**
   * adds a new student to the library systems
   *
   * @precondition uid != null && name != null && email != null && studentClass != null && "student is not yet in the system"
   */
  public void addStudent(String uid, String name, String email, String studentClass)
  {
    assert uid != null && name != null && email != null
           : "Precondition violation : invalid null data";

    Student student = findStudent(uid);
    assert student == null : "Precondition failed : uid already exists";

    studentList.add(new Student(uid, name, email, studentClass));
  }

  /**
   * student checks out a book
   *
   * @param uid student id
   * @param bookid book id
   *
   * @throws ItemLimitReachedException if the student has already checked out too many books
   * @throws StudentNotFoundException if the student is not in the library system
   *
   * @precondition uid and bookid are valid IDs && book.getLoanRecord() == null
   */
  public void checkOutBook(String uid, String bookid) throws StudentNotFoundException, ItemLimitReachedException
  {
    Book book = findBook(bookid);
    assert book != null : "Precondition failed : book not found";
    assert book.getLoanRecord() == null : "Precondition failed : book checked out";

    Student student = findStudent(uid);
    if (student == null) throw new StudentNotFoundException();
    if (student.getstudentClass()=="graduate" ){
    	if (student.onLoanCount() > 3 ) throw new ItemLimitReachedException();
    }
    else if(student.onLoanCount() > 1) throw new ItemLimitReachedException();

    Loan loan = new Loan(student, book, new Date());

    // we set the book before we set the student
    //   b/c otherwise the student's observers are notified
    //   before the book availability is updated.
    book.checkOutBook(loan);
    student.checkOutBook(loan);
  }


  /**
   * student checks out a dvd
   *
   * @param uid student id
   * @param dvdid dvd id
   *
   * @throws ItemLimitReachedException if the student has already checked out too many books
   * @throws StudentNotFoundException if the student is not in the library system
   *
   * @precondition uid and dvdid are valid IDs && dvd.getLoanRecord() == null
   */
  public void checkOutDVD(String uid, String dvdid) throws StudentNotFoundException, ItemLimitReachedException
  {
    DVD dvd = findDVD(dvdid);
    assert dvd != null : "Precondition failed : dvd not found";
    assert dvd.getLoanRecord() == null : "Precondition failed : dvd checked out";

    Student student = findStudent(uid);
    if (student == null) throw new StudentNotFoundException();
    if (student.getstudentClass()=="graduate"){
    	if (student.onLoanCount() > 3 ) throw new ItemLimitReachedException();
    }
    else 
    	if(student.onLoanCount() > 1) throw new ItemLimitReachedException();

    Loan loan = new Loan(student, dvd, new Date());

    // we set the book before we set the student
    //   b/c otherwise the student's observers are notified
    //   before the book availability is updated.
    dvd.checkOutDVD(loan);
    student.checkOutDVD(loan);
  }
  /**
   * a book is returned to the libary
   *
   * @param bookid id of the book that is returned
   * @precondition bookid is a valid book id
   */
  public void returnBook(String bookid)
  {
    Book    book = findBook(bookid);
    assert book != null : "Precondition failed : book not found";

    Loan    loan = book.getLoanRecord();
    assert loan != null : "No loan record for book " + bookid;

    Student student = loan.getStudent();
    assert student != null && student.holds(loan) : "Inconsistent state";

    student.returnBook(loan);
    book.returnBook(loan);
  }

  /**
   * a book is returned to the libary
   *
   * @param bookid id of the book that is returned
   * @precondition bookid is a valid book id
   */
  public void returnDVD(String dvdid)
  {
    DVD    dvd = findDVD(dvdid);
    assert dvd != null : "Precondition failed : dvd not found";

    Loan    loan = dvd.getLoanRecord();
    assert loan != null : "No loan record for dvd " + dvdid;

    Student student = loan.getStudent();
    assert student != null && student.holds(loan) : "Inconsistent state";

    student.returnDVD(loan);
    dvd.returnDVD(loan);
  }
  /**
   * prints a list of students to the printwriter
   */
  public Iterator<StudentInfo> studentIterator()
  {
    // the challenge is that we need to return an iterator to immutable objects
    // (i.e., StudentInfo), but we only have an iterator to the mutable type.
    // (i.e., Student). Hence we wrap the original iterator into a new one
    // where return only the immutable object type.

    final Iterator<Student> iter = studentList.iterator();

    return new Iterator<StudentInfo>() {
              public boolean hasNext() { return iter.hasNext(); }
              public StudentInfo next() { return iter.next(); }
              public void remove() { throw new UnsupportedOperationException(); }
           };
  }

  /**
   * prints a list of books where the title starts with a given pattern to the printwriter
   */
  public Iterator<BookInfo> bookIterator()
  {
    // see comment in studentIterator.

    final Iterator<Book> iter = bookList.iterator();

    return new Iterator<BookInfo>() {
              public boolean hasNext() { return iter.hasNext(); }
              public BookInfo next() { return iter.next(); }
              public void remove() { throw new UnsupportedOperationException(); }
           };
  }
  /**
   * prints a list of dvds where the title starts with a given pattern to the printwriter
   */
  public Iterator<DVDInfo> dvdIterator()
  {
    // see comment in studentIterator.

    final Iterator<DVD> iter = dvdList.iterator();

    return new Iterator<DVDInfo>() {
              public boolean hasNext() { return iter.hasNext(); }
              public DVDInfo next() { return iter.next(); }
              public void remove() { throw new UnsupportedOperationException(); }
           };
  }

  /**
   * returns the student with a given UID
   *
   * @return the Student with uid
   * @throws StudentNotFoundException if student does not exist in the library system
   */
  public StudentInfo getStudent(String uid) throws StudentNotFoundException
  {
    Student student = findStudent(uid);
    if (student == null) throw new StudentNotFoundException();

    // should be student.clone() to prevent accidental modification in the outside world
    return student;
  }

  /**
   * register an observer for the student
   */
  public void attachObserver(StudentInfo s, LibraryObserver o) throws StudentNotFoundException
  {
    // Inside of the library, we know that s has to be a Student;
    // Hence we down cast it to Student
    // Alternatively, we could look up the student based on id.
    Student student = (Student)s;

    student.attach(o);
  }

  /**
   * unregister an observer from a student
   */
  public void detachObserver(StudentInfo s, LibraryObserver o) throws StudentNotFoundException
  {
    // see comment in attachObserver
    Student student = (Student)s;

    student.detach(o);
  }


  /**
   * internal method that retrieves a book from the library records
   */
  private Book findBook(String bookid)
  {
    Book           res = null;
    Iterator<Book> it = bookList.iterator();

    while (it.hasNext() && (res == null))
    {
      Book cand = it.next();

      if (cand.getId().equals(bookid)) res = cand;
    }

    return res;
  }
  /**
   * internal method that retrieves a dvd from the library records
   */
  private DVD findDVD(String dvdid)
  {
    DVD           res = null;
    Iterator<DVD> it = dvdList.iterator();

    while (it.hasNext() && (res == null))
    {
      DVD cand = it.next();

      if (cand.getId().equals(dvdid)) res = cand;
    }

    return res;
  }

  /**
   * internal method that retrieves a student from the library records
   */
  private Student findStudent(String studentid)
  {
    Student           res = null;
    Iterator<Student> it = studentList.iterator();

    while (it.hasNext() && (res == null))
    {
      Student cand = it.next();

      if (cand.getId().equals(studentid)) res = cand;
    }

    return res;
  }

  private ArrayList<Book>    bookList;
  private ArrayList<DVD>     dvdList;
  private ArrayList<Student> studentList;
  private int                maxBookId;
  private int                 maxDVDId;
}
