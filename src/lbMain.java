import javax.sql.StatementEvent;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Queue;
import java.util.Scanner;


public class lbMain {

    String Url = "jdbc:postgresql://localhost:5432/library Management";
    String uname = "postgres";
    String password = "0000";
    Scanner sc = new Scanner(System.in);
    Scanner in = new Scanner(System.in);
    String stdName;
    int sid;
    public void login() throws Exception {
        System.out.println("Enter  Student Name:");
        stdName = sc.nextLine();
        System.out.println("Enter Student ID :");
        sid = in.nextInt();
       // System.out.println(stdName + " " + sid);
       boolean found = check(sid,stdName);
       if (found){
          // System.out.println("yes found from login ");
           System.out.println("Press 1 to Borrow a Book");
           System.out.println("Press 2 to return a Book");
           int choice = in.nextInt();
           if (choice==1){
               borrowBook(sid,stdName);
           }else{
               returnBook(sid,stdName);
           }
       }else{

           System.out.println(" Account not found");

                System.out.println("Press 1 to create a Account , 2. to Exit");
                int choice = in.nextInt();
                if (choice==1) {
                    createAccount();
                }
        }
    }

    public boolean check(int id,String sname) throws Exception {
        String sql = "select * from studentdetails  ";
        Connection cn = DriverManager.getConnection(Url, uname, password);
        Statement st = cn.createStatement();
        ResultSet rs = st.executeQuery(sql);
        while (rs.next()) {
            if (rs.getInt(1) == id && rs.getString(2).equals(sname)){
//                System.out.print("found - ");
//                System.out.println(rs.getString(2));
                return true;
            }
        }
        cn.close();
        return  false;
    }

    public void createAccount() throws Exception{
        System.out.println("Please Enter Student ID : ");
        int id = in.nextInt();
        System.out.println("Enter Student name : ");
        String stdName = sc.nextLine();
        System.out.println("Enter Student Class  : ");
        int stdclass = in.nextInt();
        System.out.println("Enter phone number : ");
        long phNumber = in.nextLong();
        String sql = "insert into studentdetails values(?,?,?,?)";
        Connection cn = DriverManager.getConnection(Url, uname, password);
        PreparedStatement st = cn.prepareStatement(sql);
        st.setInt(1,id);
        st.setString(2,stdName);
        st.setLong(3,phNumber);
        st.setInt(4,stdclass);
        st.execute();
        //System.out.println(id + stdName + stdclass + phNumber );
        System.out.println("done");
        cn.close();
        System.out.println("Press 1 to Login  , 2. to Exit");
        int choice = in.nextInt();
        if (choice==1){
            login();
        }
    }
     public void borrowBook(int id , String sname) throws Exception {
//        int sid = id;
//        String name =sname;
 String genre="";
         Connection cn = DriverManager.getConnection(Url, uname, password);
         String sql = "select * from books";
         Statement st = cn.createStatement();
         ResultSet rs = st.executeQuery(sql);
         System.out.println("please Choose Genre :");
         System.out.println(" 1.Science fiction 2.Biography 3.History 4.Horror 5.Mystery 6.Science");
          int choice = in.nextInt();
          if (choice==1){
              genre ="Science fiction";
          }else if(choice==2){
              genre="Biography";
          }else if (choice==3){
              genre= "History";
          }else if(choice==4){
              genre="Horror";
          }else if(choice==5){
              genre="Mystery";
          }else{
              genre="Science fiction";
          }
         while (rs.next()) {
//String dbGenre="";
//              if(rs.getString("genre").equals(genre)){
//                  dbGenre= genre;
//                  System.out.println(dbGenre);
//
//              };
//           System.out.print(rs.getString(3)+"-");
//             System.out.println(rs.getInt(2));
             if (rs.getString(3).equals(genre)) {
                 System.out.print(rs.getString(2) + "   of ID - ");
                 System.out.println(rs.getInt(1));

             }

         }
         //System.out.println(genre);
         System.out.println("Enter the Book ID to Continue");

         int bid =in.nextInt();
         String bookName="";
         LocalDate datenow = LocalDate.now();

         String date = String.valueOf(datenow);
         Statement st1 = cn.createStatement();
         ResultSet rs1 = st1.executeQuery(sql);
         while(rs1.next()){
             if (rs1.getInt(1)==bid){
                 System.out.print(rs1.getString(2) + " - ");
                 System.out.println(bookName);
                 bookName = rs1.getString(2);
             }
         }
          System.out.println("  book name  : "+ bookName + " and  id: " + bid + " is borrowed by student :" +sname +" of   student-id : "+sid + " "+ "on "+ date ) ;

         String sql2 = "insert into librarytransactions values(?,?,?,?,?)";

         PreparedStatement ps = cn.prepareStatement(sql2);
         ps.setInt(1,sid);
         ps.setString(2,sname);
         ps.setString(3,bookName);
         ps.setInt(4,bid);
         ps.setString(5,date);
         ps.execute();
         cn.close();

         //System.out.println( sid + sname+ date);
     }
     public  void returnBook(int id,String sname) throws Exception{
         int sid = id;
         String name =sname;
         System.out.println("Please Enter The Book ID : ");
         int bookId = in.nextInt();
         LocalDate date = LocalDate.now();
         String returnDate = String.valueOf(date);
         Connection cn = DriverManager.getConnection(Url, uname, password);
         String sql =  String.format("update librarytransactions set returnon = '%s' where bid = %d",returnDate,bookId);
//         PreparedStatement st = cn.prepareStatement(sql);
//         st.setString(6,returnDate);
//         st.setInt(4,bookId);
//          st.execute( );
         Statement st = cn.createStatement();
         st.execute(sql);
          cn.close();
         System.out.println("student:"+sname +" of Id:" +sid +" retured book of Id:"+bookId+" on " + returnDate);
        // System.out.println( sid + name + returnDate);
     }

    public static void main (String[] args) throws Exception  {
//       Scanner scan = new Scanner(System.in);
//        System.out.println("nnumb");
//        long num = scan.nextLong();
//        System.out.println(num);
        Class.forName("org.postgresql.Driver");

        System.out.println("Connection Established");
        lbMain lb = new lbMain();
         lb.login();
//lb.borrowBook(1212,"asf");


    }
}
