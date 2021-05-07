import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PayRollDataBaseIO {


    private static PayRollDataBaseIO payRollDataBaseIO;
    private PreparedStatement employeeInformationStatement;

    private PayRollDataBaseIO() {
    }

    public static PayRollDataBaseIO getInstance() {
        if (payRollDataBaseIO == null)
            payRollDataBaseIO = new PayRollDataBaseIO();
        return payRollDataBaseIO;
    }



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
        return this.updateEmployeeDataUsingPreparedStatement(name, salary);
    }


    /**
     * Updating employee details with query,
     * here, the database is segregated owing to the
     * EER Diagram so, used joins to retrieve basic_pay
     *
     * @param name
     * @param salary
     * @return
     * @throws Exceptions
     */

    private int updateEmployeeDataUsingPreparedStatement(String name, int salary) throws Exceptions {

        String sqlQuery = String.format("UPDATE pay_info p" +
                "INNER JOIN " +
                "employee_details e " +
                "ON p.id = e.pay_info_id" +
                "SET p.basic_pay = %d" +
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
     * this method is responsible to prepare
     * the statement to retrieve the employee pay details
     */

    private void prepareStatementForSalaryRetrieval() {

        try {
            Connection mySqlConnection = this.getConnection();
            String sqlQuery = "SELECT * FROM employee_details e " +
                    "JOIN pay_info p ON e.pay_info_id = p.id " +
                    "WHERE NAME = ?";

            employeeInformationStatement = mySqlConnection.prepareStatement(sqlQuery);
        } catch (SQLException e) {
            /*      an unknown error within intellij forced me no to use custom exception       */
            e.printStackTrace();
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
     * The parent method which calls the below method for
     * details on pay Information
     *
     * @param name
     * @return
     * @throws SQLException
     */

    public List<PayRoll> getPayRollInformation(String name) throws SQLException {
        List<PayRoll> employeePayRollList = null;

        if (this.employeeInformationStatement == null)
            this.prepareStatementForSalaryRetrieval();

        try {

            employeeInformationStatement.setString(1, name);
            ResultSet resultSet = employeeInformationStatement.executeQuery();

            employeePayRollList = this.getPayRollInformation(resultSet);

        } catch (SQLException | Exceptions e) {
            e.printStackTrace();
        }

        return employeePayRollList;
    }


    /**
     * Method to get the employee pay information
     * to be later used at synchronization checking
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
            throw new Exceptions(Exceptions.exceptionType.SQL_EXCEPTION, "THERE WAS AN ERROR WITH RETRIEVAL, PLEASE TRY AGAIN");
        }

        return payRollList;
    }

    /**
     * method takes in the dates as input and finds the employees
     * in that date range
     *
     * @param fromDate
     * @param toDate
     * @return
     * @throws Exceptions
     */

    public List<PayRoll> getEmployeeForDateRange(LocalDate fromDate, LocalDate toDate) throws Exceptions {

        String sqlQuery = String.format("SELECT * FROM employee_details WHERE start BETWEEN %s AND %s;", Date.valueOf(fromDate), Date.valueOf(toDate));
        return this.getEmployeeInformation(sqlQuery);
    }

    /**
     * this method gets the employee details according to the query
     *
     * @param sqlQuery
     * @return
     * @throws Exceptions
     */

    private List<PayRoll> getEmployeeInformation(String sqlQuery) throws Exceptions {

        List<PayRoll> payRollList;

        try (Connection sqlConnection = this.getConnection()) {

            Statement queryStatement = sqlConnection.createStatement();

            ResultSet queryResult = queryStatement.executeQuery(sqlQuery);

            payRollList = this.getPayRollInformation(queryResult);

            //Catching Sql Exception
        } catch (SQLException e) {
            throw new Exceptions(Exceptions.exceptionType.SQL_EXCEPTION, "YOUR SQL CONNECTION WAS NOT CORRECT, CHECK AGAIN");
            //Catching Null pointer exception
        } catch (NullPointerException e) {
            throw new Exceptions(Exceptions.exceptionType.NULL_INPUT, "YOUR INPUT WAS NULL, PLEASE CHECK AGAIN");
        }

        return payRollList;

    }

    /**
     *
     * using this method retrieve various sql methods by gender
     * and mapping that with the gender
     *
     * @return
     * @throws Exceptions
     * @param function
     */

    public Map<String, Integer> getArithmeticOperationalFunctionsOfSalaryByGender(String function) throws Exceptions {
        String sqlQuery = String.format("SELECT e.gender , %s(p.basic_pay) as avg_basic_pay FROM pay_info p " +
                "JOIN employee_details e WHERE e.pay_info_id = p.id AND p.basic_pay IN " +
                "(" +
                "SELECT max(p2.basic_pay) FROM pay_info p2 " +
                "JOIN employee_details e2 WHERE e2.pay_info_id = p2.id" +
                "GROUP BY e2.gender" +
                ")" +
                "GROUP BY e.gender;",function);
        Map<String, Integer> genderBySalaryMap = new HashMap<>();

        try (Connection mySqlConnection = this.getConnection()) {

            Statement sqlStatement = mySqlConnection.createStatement();
            ResultSet resultSet = sqlStatement.executeQuery(sqlQuery);

            while (resultSet.next()) {

                String gender = resultSet.getString("gender");
                Integer salary = resultSet.getInt("avg_basic_pay");

                genderBySalaryMap.put(gender, salary);

            }

        } catch (SQLException e) {
            throw new Exceptions(Exceptions.exceptionType.SQL_EXCEPTION, "YOUR SQL CONNECTION WAS NOT CORRECT, CHECK AGAIN");
            //Catching Null pointer exception
        } catch (NullPointerException e) {
            throw new Exceptions(Exceptions.exceptionType.NULL_INPUT, "YOUR INPUT WAS NULL, PLEASE CHECK AGAIN");

        }

        return genderBySalaryMap;
    }

}
