import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Main {
  private static final String url="jdbc:mysql://localhost:3306/library";
  private static final String username="root";
  private static final String password= "Sinjini17@";

  public static void main(String[] args) throws ClassNotFoundException ,SQLException {

    try{
      Class.forName("com.mysql.jdbc.Driver");
    }catch(ClassNotFoundException e){
      System.out.println(e.getMessage());
    }

    try{
        Connection con = DriverManager.getConnection(url, username, password);
        while(true){
          System.out.println();
          System.out.println("LIBRARY MANAGEMENT SYSTEM");
          Scanner sc = new Scanner(System.in);
          System.out.println("1. View Books Stock");
          System.out.println("2. Search For A book");
          System.out.println("3. View Members");
          System.out.println("4. Search For a Member Details");
          System.out.println("5. Delete Stock");
          System.out.println("6. Delete Member");
          System.out.println("7. Update Member");
          System.out.println("8. Update Stock");
          System.out.println("9. Add New Member");
          System.out.println("10. Add New Stock");
          System.out.println("11. Update The Lend List");
          System.out.println("0. Exit");
          System.out.println("Choose an option: ");
          int choice = sc.nextInt();
          
          switch(choice){
            case 1:
                fullStock(con);
                break;
            case 2:
                bookSearch(con,sc);
                break;
            case 3:
                 viewMembers(con);
                 break;
            case 4:
                 memDetails(con,sc);
                 break;
            case 5:
                  stockDelete(con,sc);
                  break;
            case 6:
                  memDelete(con,sc);
                  break;      
            case 7:
                  updateMem(con,sc);
                  break;
            case 8:
                  updateStock(con,sc);
                  break;
            case 9:
                  addMem(con,sc);
                  break;
            case 10:
                  addStock(con,sc);
                  break;
            case 11:
                  updateLoan(con,sc);
                  break;      
            case 0:
                  exit();
                  sc.close(); 
            default:
                  System.out.println("Invalid choice. Try Again!!");                                                   
          }

        }
    }catch (SQLException e){
      System.out.println(e.getMessage());
    }catch(InterruptedException e){
      throw new RuntimeException(e);
    }
  }


private static void fullStock(Connection con) throws SQLException{
  String sql = "SELECT * FROM books";

  try(PreparedStatement pstmt= con.prepareStatement(sql);
      ResultSet res=pstmt.executeQuery()){
        System.out.println("Current Books : ");
        while(res.next()) {
          int book_Id = res.getInt("ISBN");
          String Title = res.getString("Title");
          String Author = res.getString("Author");
          String Genre = res.getString("Genre");
          String Pub_Date = res.getTimestamp("Publication_date").toString();
          int Stock_Quantity = res.getInt("Copies_available");

          System.out.println("book_Id: " +book_Id);
          System.out.println("Book name: " +Title);
          System.out.println("Author name: " +Author);
          System.out.println("Genre is: "+Genre);
          System.out.println("Date of Publication: "+Pub_Date);
          System.out.println("Book Count: "+Stock_Quantity);
          System.out.println("*-----------------------------------------------------------------------*");
        }
        System.out.println("*-----------------------------------------------------------------------*");
        
      }
}


private static void viewMembers(Connection con) throws SQLException{
  String sql = "SELECT * FROM members";

  try(PreparedStatement pstmt= con.prepareStatement(sql);
      ResultSet res=pstmt.executeQuery()){
        System.out.println("Current Members : ");
        while(res.next()) {
          int Member_Id = res.getInt("Mem_id");
          String Name = res.getString("Name");
          int ContactNum = res.getInt("Con_Info");
          String Membership = res.getString("Mem_type");
          int book_Id = res.getInt("book_id");
          String Isuue_date = res.getTimestamp("loan_date").toString();
          String Due_date = res.getTimestamp("due_date").toString();
          String return_date = res.getTimestamp("return_date").toString();

          System.out.println("Id: "+Member_Id);
          System.out.println("Meember Name: "+Name);
          System.out.println("Contact Number: "+ContactNum);
          System.out.println("Membership: "+Membership);
          System.out.println("Book_ID: "+book_Id);
          System.out.println("Issued on: "+Isuue_date);
          System.out.println("Due on: "+Due_date);
          System.out.println("Returned on: "+return_date);
        }
        System.out.println("*-----------------------------------------------------------------------*");
      }
}

private static void bookSearch(Connection con,Scanner sc) throws SQLException{
  System.out.println("Give book ISBN: ");
  int id = sc.nextInt();

  String sql = "SELECT * FROM books WHERE ISBN = ?";

  try{
    PreparedStatement pstmt = con.prepareStatement(sql);
    pstmt.setInt(1, id);
    ResultSet res = pstmt.executeQuery();
    if(res.next()){
      int book_Id = res.getInt("ISBN");
      String Title = res.getString("Title");
      String Author = res.getString("Author");
      String Genre = res.getString("Genre");
      String Pub_Date = res.getTimestamp("Publication_date").toString();
      int Stock_Quantity = res.getInt("Copies_available");

      System.out.println();
      System.out.println("book_Id: " +book_Id);
      System.out.println("Book name: " +Title);
      System.out.println("Author name: " +Author);
      System.out.println("Genre is: "+Genre);
      System.out.println("Date of Publication: "+Pub_Date);
      System.out.println("Book Count: "+Stock_Quantity);

    }
    else{
      System.out.println("Enter correct ISBN !! ");
    }
    res.close();
    pstmt.close();
  }catch (SQLException e){
    e.printStackTrace();
  }
}

private static void memDetails(Connection con,Scanner sc){
  System.out.println("Enter Member Id: ");
  int Id = sc.nextInt();
  String sql = "SELECT * FROM members WHERE Mem_id=? LEFT JOIN loans ON members.Mem_id = loans.Mem_id ";

  try{
    PreparedStatement pstmt= con.prepareStatement(sql);
      pstmt.setInt(1, Id);
      ResultSet res= pstmt.executeQuery();
        System.out.println("Here's The Details : ");
        System.out.println("*------------------------------------------------------------------------------------*");
        if(res.next()) {
          int Member_Id = res.getInt("Mem_id");
          String Name = res.getString("Name");
          int ContactNum = res.getInt("Con_Info");
          String Membership = res.getString("Mem_type");
          int book_Id = res.getInt("book_id");
          String Isuue_date = res.getTimestamp("loan_date").toString();
          String Due_date = res.getTimestamp("due_date").toString();
          String return_date = res.getTimestamp("return_date").toString();

         System.out.println();
         System.out.println("Id: "+Member_Id);
         System.out.println("Meember Name: "+Name);
         System.out.println("Contact Number: "+ContactNum);
         System.out.println("Membership: "+Membership);
         System.out.println("Book_ID: "+book_Id);
         System.out.println("Issued on: "+Isuue_date);
         System.out.println("Due on: "+Due_date);
         System.out.println("Returned on: "+return_date);
        }
        else{
        System.out.println("Invalid Id");
        }
        res.close();
        pstmt.close();
      }catch(SQLException e){
        e.printStackTrace();
      }
  
}

private static void stockDelete(Connection con,Scanner sc){
  System.out.println("Enter the book ID of the book you want to delete: ");
  int id= sc.nextInt();

  String sql="DELETE FROM books WHERE ISBN= ?";

  try{
    PreparedStatement pstmt =con.prepareStatement(sql);
    pstmt.setInt(1, id);
    int af=pstmt.executeUpdate();
    if(af>0){
      System.out.println("Deletion Successfull!!");
    }
    else{
      System.out.println("Deletion failed.");
    }
    pstmt.close();
  }catch(SQLException e){
    e.printStackTrace();
  }
}

private static void updateMem(Connection con,Scanner sc){
  System.out.println("Enter the ID of the Member you want to Update: ");
  int memid = sc.nextInt();
  System.out.println("Update contact number: ");
  int new_contact=sc.nextInt();
  System.out.println("Update Membership");
  String Membership=sc.nextLine();

  String sql = "UPDATE members SET Con_Info=?,Mem_type=? WHERE Mem_id=?";

  try{
    PreparedStatement pstmt=con.prepareStatement(sql);
    pstmt.setInt(3, memid);
    pstmt.setInt(1,new_contact);
    pstmt.setString(2, Membership);
    int aff = pstmt.executeUpdate();

    if(aff > 0){
      System.out.println("Update is successfull!!!");
    }
    else{
      System.out.println("Failed.");
    }
    pstmt.close();
  }catch(SQLException e){
    e.printStackTrace();
  }
}

private static void memDelete(Connection con,Scanner sc){
  System.out.println("Enter the Member ID of the member you want to delete: ");
  int id= sc.nextInt();

  String sql="DELETE FROM members WHERE Mem_id= ?";

  try{
    PreparedStatement pstmt =con.prepareStatement(sql);
    pstmt.setInt(1, id);
    int affe=pstmt.executeUpdate();
    if(affe>0){
      System.out.println("Deletion Successfull!!");
    }
    else{
      System.out.println("Deletion failed.");
    }
    pstmt.close();
  }catch(SQLException e){
    e.printStackTrace();
  }
}

private static void updateStock(Connection con,Scanner sc){
  System.out.println("Enter the ID of the book you want to Update: ");
  int id = sc.nextInt();
  System.out.println("Update The stock: ");
  int new_stock=sc.nextInt();

  String sql = "UPDATE books SET Copies_available=? WHERE ISBN=?";

  try{
    PreparedStatement pstmt=con.prepareStatement(sql);
    pstmt.setInt(2, id);
    pstmt.setInt(1,new_stock);
    int affec = pstmt.executeUpdate();

    if(affec > 0){
      System.out.println("Update is successfull!!!");
    }
    else{
      System.out.println("Failed.");
    }
    pstmt.close();
  }catch(SQLException e){
    e.printStackTrace();
  }
}

private static void addMem(Connection con,Scanner sc){
  System.out.println("Enter Member Name: ");
  String name= sc.nextLine();
  System.out.println("Enter Contact Number");
  sc.nextInt();
  int contact=sc.nextInt();
  sc.nextLine();
  System.out.println("Enter Membership Type: ");
  String membership=sc.nextLine();

  String sql = "INSERT INTO members(Name,Con_Info,Mem_type) VALUES(?,?,?)";
  try{
    PreparedStatement pstmt= con.prepareStatement(sql);
    pstmt.setString(1, name);
    pstmt.setInt(2, contact);
    pstmt.setString(3, membership);
    int affectedRows=pstmt.executeUpdate();

    if(affectedRows>0){
      System.out.println("Insertion Successfull!!");
    }
    else{
      System.out.println("Failed.Try Again....");
    }
    pstmt.close();
  }catch(SQLException e){
    e.printStackTrace();
  }
}


private static void addStock(Connection con, Scanner sc){
  System.out.println("Enter The Book ID: ");
  int book_id=sc.nextInt();
  sc.nextLine();
  System.out.println("Enter the name of the book: ");
  String name=sc.nextLine();
  System.out.println("Enter Author Name: ");
  String author=sc.nextLine();
  System.out.println("Enter The Genre: ");
  String gen=sc.nextLine();
  System.out.println("Enter the date of Publication in format: YYYY-MM-DD : ");
  String publication=sc.nextLine();
  System.out.println("Give the count: ");
  int stockc=sc.nextInt();

  String sql="INSERT INTO books(ISBN,Title,Author,Genre,Publication_date,Copies_available) VALUES (?,?,?,?,?,?)";

  try{
    PreparedStatement pstmt= con.prepareStatement(sql);
    pstmt.setInt(1, book_id);
    pstmt.setString(2, name);
    pstmt.setString(3, author);
    pstmt.setString(4, gen);
    pstmt.setString(5, publication);
    pstmt.setInt(6, stockc);
    int affect=pstmt.executeUpdate();

    if(affect>0){
      System.out.println("Inserted Successfully!!");
    }
    else{
      System.out.println("Insertion failed...");
    }
    pstmt.close();
  }catch(SQLException e){
    e.printStackTrace();
  }
}

private static void updateLoan(Connection con, Scanner sc){
  System.out.println("Enetr the ID of the Member you want to Update details of: ");
  int id= sc.nextInt();
  System.out.println("Enter the ID of the book : ");
  int book_id = sc.nextInt();
  System.out.println("Update Issue_date in format (YYYY-MM-DD HH:MI:SS) : ");
  String issue=sc.nextLine();
  System.out.println("Update Due_date in format (YYYY-MM-DD) : ");
  String dues=sc.nextLine();
  System.out.println("Update Returning_date in format (YYYY-MM-DD) : ");
  String returns=sc.nextLine();


  String sql = "UPDATE loans SET book_id=?,loan_date=?,due_date=?,return_date=?  WHERE Mem_id=?";

  try{
    PreparedStatement pstmt=con.prepareStatement(sql);
    pstmt.setInt(5, id);
    pstmt.setInt(1,book_id);
    pstmt.setString(2, issue);
    pstmt.setString(3,dues);
    pstmt.setString(4,returns);
    int affected = pstmt.executeUpdate();

    if(affected > 0){
      System.out.println("Update is successfull!!!");
    }
    else{
      System.out.println("Failed.");
    }
    pstmt.close();
  }catch(SQLException e){
    e.printStackTrace();
  }
}

public static void exit() throws InterruptedException{
  System.out.println("Exiting System");
  int i=12;
  while(i!=0){
    System.out.print(".");
    Thread.sleep(450);
    i--;
  }
  System.out.println();
  System.out.println("Thankyou For Visiting!!!!!");
}
}