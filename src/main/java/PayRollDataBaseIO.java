import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PayRollDataBaseIO {
    /**
     * read the entries from database and store it to payroll list
     * @return
     */

    public List<PayRoll> readData() {
        String sqlQuery = "SELECT * FROM employee_details;";
        List<PayRoll> payRollList = new ArrayList<>();

        try(Connection sqlConnection = this.getConnection()) {

            Statement queryStatement = sqlConnection.createStatement();

            ResultSet queryResult = queryStatement.executeQuery(sqlQuery);

            while (queryResult.next()){
                int id = queryResult.getInt("id");
                String name = queryResult.getString("name");
                String gender = queryResult.getString("gender");
                int phoneNumber = queryResult.getInt("phone");
                Date startDate = queryResult.getDate("start");

                payRollList.add(new PayRoll(id,name,gender,phoneNumber,startDate));
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return payRollList;
    }

    /**
     * to setup communicable connection with the database
     * @return
     * @throws SQLException
     */
    private Connection getConnection() throws SQLException {

        String mySqlUrl = "jdbc:mysql://localhost:3306/payroll_operations";
        String mySqlUsername = "root";
        String mySqlPassword = "Polt@1234";

        Connection databaseConnection;

        databaseConnection = DriverManager.getConnection(mySqlUrl,mySqlUsername,mySqlPassword);

        System.out.println("Connected ");

        return  databaseConnection;

    }
}
