import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PayRollDataBaseIO {


    /**
     * read the entries from database and store it to payroll list
     *
     * @return
     */

    public List<PayRoll> readData() throws Exceptions {
        String sqlQuery = "SELECT * FROM employee_details e JOIN pay_info p ON e.pay_info_id = p.id;";//Using join because, two related tales
        List<PayRoll> payRollList = new ArrayList<>();

        try (Connection sqlConnection = this.getConnection()) {

            Statement queryStatement = sqlConnection.createStatement();

            ResultSet queryResult = queryStatement.executeQuery(sqlQuery);

            while (queryResult.next()) {
                int id = queryResult.getInt("id");
                String name = queryResult.getString("name");
                String gender = queryResult.getString("gender");
                int phoneNumber = queryResult.getInt("phone");
                Date startDate = queryResult.getDate("start");
                int salary = queryResult.getInt("basic_pay");

                payRollList.add(new PayRoll(id, name, gender, phoneNumber, startDate, salary));
            }
            //Catching Sql Exception
        } catch (SQLException e) {
            throw new Exceptions(Exceptions.exceptionType.SQL_EXCEPTION, "YOUR SQL CONNECTION WAS NOT CORRECT, CHECK AGAIN");
            //Catching Null pointer exception
        } catch (NullPointerException e) {
            throw new Exceptions(Exceptions.exceptionType.NULL_INPUT, "YOUR INPUT WAS NULL, PLEASE CHECK AGAIN");
        }

        return payRollList;
    }


    /*   The method here calls the method below   */

    public int updateEmployeeData(String name, int salary) throws Exceptions {
        return this.updateEmployeeDataUsingStatement(name, salary);
    }


    /**
     *
     * Updating employee details with query,
     * here, the database is segregated owing to the
     * EER Diagram so, used joins to retrieve basic_pay
     *
     *
     * @param name
     * @param salary
     * @return
     * @throws Exceptions
     */

    private int updateEmployeeDataUsingStatement(String name, int salary) throws Exceptions {

        String sqlQuery = String.format("UPDATE pay_info p\n" +
                "INNER JOIN \n" +
                "employee_details e \n" +
                "ON p.id = e.pay_info_id\n" +
                "SET p.basic_pay = %d\n" +
                "WHERE e.name = '%s';", salary, name);

        try (Connection mySqlConnection = this.getConnection()) {
            Statement sqlStatement = mySqlConnection.createStatement();
            return sqlStatement.executeUpdate(sqlQuery);
        } catch (SQLException e) {
            throw new Exceptions(Exceptions.exceptionType.SQL_EXCEPTION, "THERE WAS A PROBLEM IN CONNECTING TO THE DATABASE PLEASE TRY AGAIN");
        } catch (NullPointerException e) {
            throw new Exceptions(Exceptions.exceptionType.NULL_INPUT, "YOU HAVE GIVEN A NULL INPUT, PLEASE TRY AGAIN !");
        }
    }


    /**
     * to setup communicable connection with the database
     *
     * @return
     * @throws SQLException
     */
    private Connection getConnection() throws SQLException {

        String mySqlUrl = "jdbc:mysql://localhost:3306/payroll_operations";
        String mySqlUsername = "root";
        String mySqlPassword = "Polt@1234";

        Connection databaseConnection;

        databaseConnection = DriverManager.getConnection(mySqlUrl, mySqlUsername, mySqlPassword);

        System.out.println("Connected ");

        return databaseConnection;
    }


    /**
     *
     * The parent method which calls the below method for
     * details on pay Information
     *
     *
     * @param name
     * @return
     * @throws SQLException
     */

    public List<PayRoll> getPayRollInformation(String name) throws SQLException {
        List<PayRoll> employeePayRollList = null;

        Connection mySqlConnection = this.getConnection();
        String payInformationRetrievalQuery = "SELECT e.name , p.basic_pay , p.deductions , p.taxable_pay , p.net_pay FROM employee_details e JOIN pay_info p ON e.pay_info_id = p.id;";
        try {

            Statement retrievalStatement = mySqlConnection.createStatement();
            ResultSet resultSet = retrievalStatement.executeQuery(payInformationRetrievalQuery);

            employeePayRollList = this.getPayRollInformation(resultSet);

        } catch (SQLException | Exceptions e) {
            e.printStackTrace();
        }

        return employeePayRollList;
    }

    /**
     *
     * Method to get the employee pay information
     * to be later used at synchronization checking
     *
     *
     * @param resultSet
     * @return
     * @throws Exceptions
     */

    private List<PayRoll> getPayRollInformation(ResultSet resultSet) throws Exceptions {

        List<PayRoll> payRollList = new ArrayList<>();


        try {
            while (resultSet.next()) {
                String name = resultSet.getString("name");
                int salary = resultSet.getInt("basic_pay");
                int deductions = resultSet.getInt("deductions");
                int taxes = resultSet.getInt("taxable_pay");
                int net_pay = resultSet.getInt("net_pay");

                payRollList.add(new PayRoll(name, salary, deductions, taxes, net_pay));
            }
        } catch (SQLException e) {
            throw new Exceptions(Exceptions.exceptionType.SQL_EXCEPTION,"THERE WAS AN ERROR WITH RETRIEVAL, PLEASE TRY AGAIN");
        }

        return payRollList;
    }
}
