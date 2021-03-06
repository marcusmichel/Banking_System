package banking;
import java.sql.*;

// I was very stuck on this project, so I used this class
// from User 6421188's solution and adapted it to my code.
public class AccountDB {
    String url = "jdbc:sqlite:";

    //constructor
    public AccountDB(String input) {
        url = url + input;
        createNewDataBase();
    }

    //method to create the initial database
    public void createNewDataBase() {
        try (Connection conn = DriverManager.getConnection(url)) {
            if (conn != null) {
                DatabaseMetaData meta = conn.getMetaData();
                createNewTable();
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    //method to create the card table
    public void createNewTable() {
        // SQL statement for creating a new table
        String sql = "CREATE TABLE IF NOT EXISTS card (\n"
//                + "  id INTEGER PRIMARY KEY AUTOINCREMENT,\n"
                + " id INTEGER, \n"
                + " number TEXT NOT NULL,\n"
                + " pin TEXT NOT NULL,\n"
                + " balance INTEGER DEFAULT 0\n"
                + ");";

        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()) {
            // create a new table
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        // Was getting an initial row with number and pin = null, couldn't figure
        // out why so I just get rid of it right after creating.
        String bruteForce = "DELETE FROM card";
        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()) {
            // create a new table
            stmt.execute(bruteForce);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void insertData(String num, String pin) {
        String insertSql = "INSERT INTO card (number, pin) VALUES(?,?)";

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(insertSql)) {
            pstmt.setString(1, num);
            pstmt.setString(2, pin);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }


        /*try (Connection conn = DriverManager.getConnection(url);
        Statement stmt = conn.createStatement()) {
            String newAccount = "INSERT INTO card (number, pin) " +
                    "VALUES(" + num + ", " + pin + ")";
            stmt.execute(newAccount);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }*/
    }

    private Connection connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    public boolean containsRecord(String num, String pin) {
        boolean found = false;
        String sql = "SELECT * FROM card WHERE number = ? AND pin = ?";
        ;
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, num);
            pstmt.setString(2, pin);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                found = true;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return found;
    }

    public void printBalance(String num, String pin) {
        String balanceSQL = "SELECT balance FROM card WHERE number = ? AND pin = ?";
        try (Connection conn = this.connect();
             PreparedStatement prep = conn.prepareStatement(balanceSQL)) {
            prep.setString(1, num);
            prep.setString(2, pin);
            ResultSet rs = prep.executeQuery();
            while (rs.next()) {
                System.out.println("Balance: " + rs.getInt("balance"));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public boolean checkMoney(String num, String pin, int transfer) {
        String checkSQL = "SELECT balance FROM card WHERE number = ? and pin = ? and balance >= transfer";
        try (Connection conn = this.connect();
             PreparedStatement prep = conn.prepareStatement(checkSQL)) {
            prep.setString(1, num);
            prep.setString(2, pin);
            prep.setString(3, String.valueOf(transfer));
            ResultSet rs = prep.executeQuery();
            if (rs.next()) {
                return true;
            }
            return false;
        } catch(SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }

    public void depositMoney(String num, String pin, int deposit) {
        String depositSQL = "UPDATE card SET balance = balance + ? WHERE number = ? AND pin = ?";
        try (Connection conn = this.connect();
            PreparedStatement prep = conn.prepareStatement(depositSQL)) {
            prep.setString(1, String.valueOf(deposit));
            prep.setString(2, num);
            prep.setString(3, pin);
            prep.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
    public void depositMoney(String num, int deposit) {
        String depositSQL = "UPDATE card SET balance = balance + ? WHERE number = ? ";
        try (Connection conn = this.connect();
             PreparedStatement prep = conn.prepareStatement(depositSQL)) {
            prep.setString(1, String.valueOf(deposit));
            prep.setString(2, num);
            prep.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void transferMoney(String num, String pin, int transferAmount, String transferNum) {

        depositMoney(transferNum, transferAmount);
        String checkSQL = "UPDATE card SET balance = balance - ? WHERE number = ? AND pin = ?";
        try (Connection conn = this.connect();
             PreparedStatement prep = conn.prepareStatement(checkSQL)) {
            prep.setString(1, Integer.toString(transferAmount));
            prep.setString(2, num);
            prep.setString(3, pin);
            //Use executeUpdate() with INSERT, UPDATE, or DELETE
            //Use executeQuery() for SELECT --> returns ResultSet
            prep.executeUpdate();
        } catch( SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void closeAccount(String num) {
        String closeSQL = "DELETE FROM card WHERE number = ?";
        displayTable();
        try (Connection conn = this.connect();
             PreparedStatement prep = conn.prepareStatement(closeSQL)) {
            prep.setString(1, num);
            prep.executeUpdate();
            System.out.println("The account has been closed!");
        } catch (SQLException throwables) {
            System.out.println(throwables.getMessage());
            System.out.println("There was an issue");
            displayTable();
        }
    }

    public void displayTable() {
        String selectAll = "SELECT * FROM card;";
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(selectAll)) {

            ResultSet rs = pstmt.executeQuery();

            // loop through the result set
            while (rs.next()) {
                System.out.println(rs.getInt("id") + "\t" +
                        rs.getString("number") + "\t" +
                        rs.getString("pin") + "\t" +
                        rs.getInt("balance"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }
}
