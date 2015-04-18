package edu.uab.cis.cs302;

// data classes
import java.util.Date;
import java.util.Iterator;

// GUI classes
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowAdapter;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JTextArea;

import edu.uab.cis.cs302.layout.FormLayout;


// Text UI classes
import java.util.Scanner;
import java.util.StringTokenizer;
import java.text.SimpleDateFormat;


// Library classes
import edu.uab.cis.cs302.library.*;
import edu.uab.cis.cs302.library.observer.LibraryObserver;

class InvalidCommandExcpetion extends Exception {}

public class LibraryUI
{
  /**
   * internal method that converts a student to a string
   */
  static private String asString(StudentInfo student)
  {
    String res = "" + student.onLoanCount() + "  #  " + student.getId();

    res = res + "  #  " + student.getName() + "  #  " + student.getEmail() + "  #  " + student.getstudentClass();
    return res;
  }

  /**
   * internal method that converts a book to a string
   */
  static private String asString(BookInfo book)
  {
    String res = (book.getLoanRecord() == null) ? "*  " : "   ";

    res = res + book.getId() + "  #  " + book.getTitle() + "  #  " + book.getAuthor();
    return res;
  }
  /**
   * internal method that converts a book to a string
   */
  static private String asString(DVDInfo dvd)
  {
    String res =(dvd.getLoanRecord() == null) ? "*  " : "   ";

    res = res + dvd.getId() + "  #  " + dvd.getTitle() + "  #  " + dvd.getDirector() + "  # Rating: " + dvd.getRating() + " DVD";
    return res;
  }


  /**
   * internal method that converts a student to a string
   */
  static private String asString(Date day)
  {
    SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");

    return df.format(day);
  }

  static private String listLoans(StudentInfo student)
  {
    String result = "";

    // iterate through the loans and convert them to a string
    Iterator<LoanInfo> it = student.loanIterator();

    while (it.hasNext())
    {
      LoanInfo loan = it.next();
      BookInfo book = loan.getBook();
      DVDInfo   dvd = loan.getDVD();
      Date     date = loan.getSince();
      if(dvd == null)
      result += asString(book) + " " + asString(date) + "\n";
      else 
    	  result += asString(dvd) + " " + asString(date) + "\n";
    }

    return result;
  }


  /**
   * reads a next token from a StringTokenizer
   * @precondition  tok.hasMoreTokens()
   */
  static String nextToken(StringTokenizer tok) throws InvalidCommandExcpetion
  {
    if (!tok.hasMoreTokens()) throw new InvalidCommandExcpetion();

    return tok.nextToken();
  }

  /**
   * checks that there is no more unprocessed input
   */
  static void endOfCommand(StringTokenizer tok) throws InvalidCommandExcpetion
  {
    if (tok.hasMoreTokens()) throw new InvalidCommandExcpetion();
  }

  static void openNewStudentDialog(final Library lib)
  {
    final JFrame    dlg      = new JFrame();
    final JPanel    pnText   = new JPanel();
    final JTextArea fldUid   = new JTextArea(1, 40);
    final JTextArea fldName  = new JTextArea(1, 40);
    final JTextArea fldEmail = new JTextArea(1, 40);
    final JTextArea fldClass = new JTextArea(1, 40);

    final JPanel    pnButton = new JPanel();
    final JButton   btOK     = new JButton("OK");
    final JButton   btClose  = new JButton("Close");

    pnText.setLayout(new FormLayout());
    pnText.add(new JLabel("Uid:"));
    pnText.add(fldUid);
    pnText.add(new JLabel("Name:"));
    pnText.add(fldName);
    pnText.add(new JLabel("Email:"));
    pnText.add(fldEmail);
    pnText.add(new JLabel("graduate or undergraduate:"));
    pnText.add(fldClass);

    
    pnButton.setLayout(new FlowLayout());
    pnButton.add(btOK);
    pnButton.add(btClose);

    btOK.addActionListener(
      new ActionListener()
      {
        public void actionPerformed(ActionEvent ev)
        {
          // add student
          lib.addStudent(fldUid.getText(), fldName.getText(), fldEmail.getText(), fldClass.getText());

          // reset text
          fldUid.setText("");
          fldName.setText("");
          fldEmail.setText("");
          fldClass.setText("");
          
        }
      }
    );

    btClose.addActionListener(
      new ActionListener()
      {
        public void actionPerformed(ActionEvent ev)
        {
          dlg.dispatchEvent(new WindowEvent(dlg, WindowEvent.WINDOW_CLOSING));
        }
      }
    );

    dlg.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    dlg.setLayout(new FlowLayout());
    dlg.getContentPane().setLayout(new BoxLayout(dlg.getContentPane(), BoxLayout.PAGE_AXIS));
    dlg.add(pnText);
    dlg.add(pnButton);

    dlg.pack();
    dlg.setVisible(true);
  }

  /**
   * returns a string with student information for a student with a given uid.
   */
  static String asText(Library lib, String uid)
  {
    String result = "";

    try
    {
      StudentInfo student = lib.getStudent(uid);

      result = asString(student) + "\n" + listLoans(student);
    }
    catch (StudentNotFoundException ex)
    {
      result = "Error retrieving student data";
    }

    return result;
  }

  /**
   * convenience function that converts gets the ID from a student and prints
   * the info into a string
   */
  static String asText(Library lib, StudentInfo student)
  {
    return asText(lib, student.getId());
  }

 private static void openStudentView(final Library lib, final StudentInfo student)
  {
    final JFrame    dlg     = new JFrame();
    final JTextArea fldInfo = new JTextArea(asText(lib, student), 3, 60);
    final JButton   btClose = new JButton("Close");

    btClose.addActionListener(
      new ActionListener()
      {
        public void actionPerformed(ActionEvent ev)
        {
          dlg.dispatchEvent(new WindowEvent(dlg, WindowEvent.WINDOW_CLOSING));
        }
      }
    );

    final LibraryObserver observer = new LibraryObserver()
                                     {
                                       public void stateChange(String uid)
                                       {
                                         assert uid == student.getId();

                                         fldInfo.setText(asText(lib, uid));
                                       }
                                     };

    try { lib.attachObserver( student, observer ); } catch (StudentNotFoundException ex) { assert false; }

    dlg.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    dlg.addWindowListener(
      new WindowAdapter()
      {
        public void windowClosing(WindowEvent we)
        {
          try { lib.detachObserver( student, observer ); } catch (StudentNotFoundException ex) { assert false; }
        }
      }
    );

    dlg.setLayout(new FlowLayout());
    dlg.getContentPane().setLayout(new BoxLayout(dlg.getContentPane(), BoxLayout.PAGE_AXIS));
    dlg.add(fldInfo);
    dlg.add(btClose);

    dlg.pack();
    dlg.setVisible(true);
  }

  /**
   * handles user input and communicates with the Library object
   *
   * @param lib Library object
   * @param s   user input
   */
  static void handleCommand(Library lib, String s) throws InvalidCommandExcpetion, LibraryException
  {
    StringTokenizer tok = new StringTokenizer(s, " ", false);
    String cmd = nextToken(tok);

    if (cmd.equals("new-student"))  
    {
      String uid   = nextToken(tok);
      String name  = nextToken(tok);
      String email = nextToken(tok);
      String studentClass = nextToken(tok);

      endOfCommand(tok);
      lib.addStudent(uid, name, email, studentClass);
    }
    else if (cmd.equals("new-book"))
    {
      String title  = nextToken(tok);
      String author = nextToken(tok);

      endOfCommand(tok);
      lib.addBook(title, author);
    }
    else if (cmd.equals("new-dvd"))
    {
      String title  = nextToken(tok);
      String director = nextToken(tok);
      String rating = nextToken(tok);

      endOfCommand(tok);
      lib.addDVD(title, director, rating);
    }
    else if (cmd.equals("checkout-book"))
    {
      String uid    = nextToken(tok);
      String bookid = nextToken(tok);

      endOfCommand(tok);
      lib.checkOutBook(uid, bookid);
    }
    else if (cmd.equals("checkout-dvd"))
    {
      String uid    = nextToken(tok);
      String dvdid = nextToken(tok);

      endOfCommand(tok);
      lib.checkOutDVD(uid, dvdid);
    }
    else if (cmd.equals("return-book"))
    {
      String bookid = nextToken(tok);

      endOfCommand(tok);
      lib.returnBook(bookid);
    }
    else if (cmd.equals("return-dvd"))
    {
      String dvdid = nextToken(tok);

      endOfCommand(tok);
      lib.returnDVD(dvdid);
    }
    else if (cmd.equals("list-students"))
    {
      endOfCommand(tok);

      Iterator<StudentInfo> it = lib.studentIterator();

      while (it.hasNext())
      {
        System.out.println(asString(it.next()));
      }
    }
    else if (cmd.equals("list-books"))
    {
      String titlepattern = (tok.hasMoreTokens() ? tok.nextToken() : "");

      endOfCommand(tok);
      Iterator<BookInfo> it = lib.bookIterator();

      while (it.hasNext())
      {
        System.out.println(asString(it.next()));
      }
    }
    else if (cmd.equals("list-dvds"))
    {
      String titlepattern = (tok.hasMoreTokens() ? tok.nextToken() : "");

      endOfCommand(tok);
      Iterator<DVDInfo> it = lib.dvdIterator();

      while (it.hasNext())
      {
        System.out.println(asString(it.next()));
      }
    }
    else if (cmd.equals("list-loans"))
    {
      String uid = nextToken(tok);

      endOfCommand(tok);

      System.out.println(listLoans(lib.getStudent(uid)));
    }
    else if (cmd.equals("new-student-dialog"))
    {
      openNewStudentDialog(lib);
    }
    else if (cmd.equals("view-student"))
    {
      String uid = nextToken(tok);

      endOfCommand(tok);
      openStudentView(lib, lib.getStudent(uid));
    }
    else
    {
      throw new InvalidCommandExcpetion();
    }
  }
  public static void main(String[] args) throws StudentNotFoundException, ItemLimitReachedException //main method
  {
	//provided test code
    Library     lib = new Library();
    Scanner     sc  = new Scanner(System.in);
    
    lib.addStudent("1", "Madhuri Dixit", "wow@hot.com", "graduate");
    lib.addStudent("2", "Aishwarya Rai Bachchan", "gp@uab", "undergraduate");
    lib.addStudent("3", "Lorde", "team@uab" ,"graduate");

    lib.addBook("Java", "Horstman");
    lib.addBook("Compiler", "Uhlman");
    lib.addBook("Worldpeace", "Shungai");
    lib.addBook("Champion Programming", "Joshua Pritchett");
    
    lib.addDVD("Avatar", "James Cameron", "10/10");
    lib.addDVD("Cars", "Someone Rich", "5/10");
    lib.addDVD("Avengers", "Joss Whedon", "10/10");
    lib.addDVD("The Internship", "Shawn Levy", "6/10");
    lib.addDVD("Rush Hour 3", "Bret Ratner", "7/10");
   
    System.out.println("##### Students in Library ##### ");
    System.out.println();
    
    Iterator<StudentInfo> it = lib.studentIterator(); //prints all students in library

    while (it.hasNext())
    {
      System.out.println(asString(it.next()));
    }
    
    System.out.println();
    System.out.println("##### Books in Library ##### ");
    System.out.println();
    
    Iterator<BookInfo> it2 = lib.bookIterator(); // prints all books in library

    while (it2.hasNext())                               
    {
      System.out.println(asString(it2.next()));
    }
    
    System.out.println();
    System.out.println("##### DVDs in Library ##### ");
    System.out.println();
    
    Iterator<DVDInfo> it3 = lib.dvdIterator();    //prints all dvds in library

    while (it3.hasNext())
    {
      System.out.println(asString(it3.next()));
    }
    System.out.println();
    //test to show that a graduate student with UID "1" can checkout up to 4 items
    lib.checkOutBook("1", "1");
    lib.checkOutBook("1", "2");
    lib.checkOutDVD("1", "3");
    lib.checkOutDVD("1", "4");
    openStudentView(lib, lib.getStudent("1"));
    //test to show that undergraduate student with UID "2" can checkout up to 2 items
    lib.checkOutBook("2", "4");
    lib.checkOutDVD("2", "1");
    openStudentView(lib, lib.getStudent("2"));
    
    openNewStudentDialog(lib); //opens new student dialog window
    
  //provided test code ends
    
    while (sc.hasNextLine())
    {
      try
      {
        handleCommand(lib, sc.nextLine());
      }
      catch (InvalidCommandExcpetion e)
      {
        System.out.println("invalid command");
      }
      catch (ItemLimitReachedException e)
      {
        System.out.println("Checkout limit reached");
      }
      catch (StudentNotFoundException e)
      {
        System.out.println("student not found");
      }
      catch (LibraryException e)
      {
        // we handle all library exceptions already, thus we should not fall
        // into this catch handler.
        assert false;
      }
    }
  }
}
