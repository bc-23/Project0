import io.StdIn._
import java.io.FileInputStream
import java.sql.{Connection, DriverManager, PreparedStatement, ResultSet, SQLException, Statement}
import scala.util.control.Exception
import java.io.File
import java.util.InputMismatchException

object Main {
  //global scope variables can go here
  private val url = "jdbc:mysql://localhost:3306/bank";
  private val username = "root";
  private val password = "password";
  private val conn: Connection = DriverManager.getConnection(url, username, password);
  private var id: Int = 0;
  private var balance: Int = 0;
  private var previousTransaction: Int = 0;
  private var lastTran = 0;

  //16 and 17 are not needed
  def main(args: Array[String]): Unit = {
    //step 1 connection step 2. create statement. step 3 execute query
    println("Connection success:" + conn);
    println("please enter your user id");
    id = readInt(); //defines id!!!!!!!!!!!!!
    while (!acctExist()) {
      println("Account does not exist");
    }
    showMenu()
    println("Exiting Menu and shutting down connection...")
    conn.close()
  }
  def selectAllAccounts(id: Int): Unit = {
    val statement = conn.createStatement()
    val query = "SELECT * FROM bankaccount;";
    val resultSet = statement.executeQuery(query)
    println(resultSet)
    while (resultSet.next()) { //TRICKY PART
      val acc = resultSet.getInt("CustomerID"); //IMPORTANT PART
      if (acc == id) {
        println("Account found.")
        println(resultSet.getInt(1) + ", " + resultSet.getInt(2) + ", " + resultSet.getInt(3), resultSet.getInt(4), resultSet.getInt(5))
      } else {
        println("Account does not exist.")
      }
    }
  }
  def updateBal(newbal: Int, CustomerID: Int): Unit = {
    val statement = conn.createStatement()
    val sql_1 = "UPDATE bankaccount SET balance = " + newbal + " " + "WHERE CustomerID = " + CustomerID
    val resultSet = statement.executeUpdate(sql_1)
  }
  def acctExist(): Boolean = { //returns whether or not an account exists
    val statement = conn.createStatement()
    val resultSetNew = statement.executeQuery("SELECT CustomerID FROM bankaccount WHERE CustomerID = " + id + ";")
    resultSetNew.next()
    println("account exists")
    if (resultSetNew.getInt(1) == id) {
      println("Welcome to mini-ATM")
      return true
    } else {
      println("No account found")
      return false
    }
  }

  //4th method
  def showMenu() = {
    var option = 0;
    val menu =
      """
        |Welcome to Mini-ATM
        |Select one of the options
        |1. Check Balance
        |2. Make a deposit
        |3. Make a withdraw
        |4. Verify deposit
        |5. Verify withdraw
        |6. Exit
        |""".stripMargin
    //find id from table?
    println(" ===============================  ");
    while (option != 6) {
      try {
        println(menu);
        val option = scala.io.StdIn.readInt(); // this return a user inputted string
        option match {
          case 1 =>
            println("================");
            if (acctExist()) {
              println("Account verified.")
              val statement = conn.createStatement()
              val query = "SELECT balance FROM bankaccount WHERE CustomerID = " + id + ";";
              val resultSet = statement.executeQuery(query);
              resultSet.next;
              balance = resultSet.getInt("balance");
              //remember default index in resultSet is - 1 so you need to move over one using .next method
              println("Balance = $" + balance);
              println("================");
            }
            else {
              println("No account found.")
            }

          case 2 => //user types in amount to deposit. system updates balance
            println("================");
            println("enter an amount to deposit");
            val amount = scala.io.StdIn.readInt();
            val statement = conn.createStatement()
            //line 115 is not needed???????
            var sum = balance + amount;
            //            lastTran = amount;
            val preape1 = conn.prepareStatement(s"UPDATE trans2 SET lastTran = $amount WHERE CustomerID = " + id)
            preape1.execute()
            val preape2 = conn.prepareStatement(s"UPDATE bankaccount SET balance = $sum WHERE CustomerID = " + id)
            preape2.execute()
            println("depositing...")
            println("Transaction successful.")
            println("Deposit Completed.  Balance Updated")
            println("================");

          case 3 =>
            println("================");
            println("enter an amount to withdraw");
            val amount = scala.io.StdIn.readInt();
            val statement = conn.createStatement()
            var sum = balance - amount;
            lastTran = sum;
            //line 133 not needed
            if (amount < balance) {
            val preape = conn.prepareStatement(s"UPDATE bankaccount SET balance = $sum WHERE CustomerID = " + id);
            preape.execute();
            println("Withdraw successful");
            println("==========================");
           } else {
              println("Not enough funds on account.")
            }
          case 4 =>
            val preape = conn.createStatement()
            val newamt = preape.executeQuery(s"SELECT * FROM trans2 WHERE CustomerID = " + id)
            //change the index from -1 to 0
            newamt.next;
            println("Amount deposited = " + newamt.getFloat("lastTran")); //tricky part
            println("success")
            println("================");

          case 5 =>
            val preape = conn.createStatement()
            val newamt2 = preape.executeQuery(s"SELECT * FROM trans2 WHERE CustomerID = " + id)
            //change the index from -1 to 0
            newamt2.next;
            println("Amount withdrawn = " + newamt2.getFloat("lastTran")); //tricky part
            println("success")
            println("================");
          case 6 =>
            println("================");
            println("Thank you for using our services.");
            println("Goodbye");
            println("================");
          case _ =>
            println("Invalid choice - please select again ");
        }
      }
      catch {
         case e: InputMismatchException => println("Non-integer entered. Existing menu");
         case e: Exception => println("Must be a number between 1 and 5. Please try again.");
          };
    }
  }
}


//after a deposit is made it will verify the deposit.
//it will take a deposit
//it will check balance after deposits
//it will verify id


