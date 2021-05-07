import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class PayRollOperation {

    public List<PayRoll> payRollList;

    public PayRollOperation() {
    }

    public PayRollOperation(List<PayRoll> payRollList) {
    }

    public static void main(String[] args) {

        ArrayList<PayRoll> payRollArrayList = new ArrayList<>();
        PayRollOperation newPayRollOperation = new PayRollOperation(payRollArrayList);
        Scanner fetch = new Scanner(System.in);

        newPayRollOperation.readPayRollDetails(fetch);

        newPayRollOperation.writePayRollDetails(IOService.FILE_IO);
    }

    private void readPayRollDetails(Scanner inputFetcher) {
        System.out.println("WHAT IS THE EMPLOYEE ID ?");
        int employeeId = inputFetcher.nextInt();

        System.out.println("WHAT IS THE NAME OF THE EMPLOYEE ?");
        String employeeName = inputFetcher.next();

        System.out.println("WHAT IS THE SALARY OF THE EMPLOYEE ?");
        double employeeSalary = inputFetcher.nextDouble();
    }

    public void writePayRollDetails(IOService ioService) {
        if (ioService.equals(IOService.CONSOLE_IO)) {
            System.out.println("\n Writing Employee Payroll Roster to Console\n" + payRollList);
        } else if (ioService.equals(IOService.FILE_IO)) {
            new PayRollFileIO().writeDataToAFile(payRollList);
        }
    }

    public void printData(IOService ioService) {
        if (ioService.equals(IOService.FILE_IO))
            new PayRollFileIO().printDataFromFile();
    }

    public long countEntries(IOService ioService) {
        if (ioService.equals(IOService.FILE_IO)) ;
        return new PayRollFileIO().countDataOfPayBill();
    }

    public List<PayRoll> readFileEntries(IOService ioService) {
        if (ioService.equals(IOService.FILE_IO)) ;
        return new PayRollFileIO().readData();
    }

    /**
     * use enum to classify the IO stream and use the respective class to perform operations
     *
     * @param ioService
     * @return
     */

    public List<PayRoll> readEmployeeDetails(IOService ioService) throws Exceptions {
        if (ioService.equals(IOService.DB_IO))
            this.payRollList = PayRollDataBaseIO.getInstance().readData();
        return this.payRollList;
    }

    /**
     * update employee's salary with a normal statement checks if
     * update result is successful, if yes returns the
     * employee changed
     *
     * @param name
     * @param salary
     * @throws Exceptions
     */

    public void updateEmployeeSalary(String name, int salary) throws Exceptions {
        int updateResult = PayRollDataBaseIO.getInstance().updateEmployeeData(name, salary);
        if (updateResult == 0)
            return;
        PayRoll employeePayRoll = this.getPayRollData(name);
        if (employeePayRoll != null)
            employeePayRoll.employeeSalary = salary;
    }


    /*  method to get the respective details of the employee */

    public PayRoll getPayRollData(String name) {

        PayRoll employeePayRoll;
        employeePayRoll = this.payRollList.stream()
                .filter(item -> item.employeeName.equals(name))
                .findFirst()
                .orElse(null);
        return employeePayRoll;

    }


    /**
     * Intermediary method which acts as a layer to call
     * update method in the database io class
     *
     * @param name
     * @return
     * @throws SQLException
     */


    public boolean checkEmployeeSynchronizationWithDatabase(String name) throws SQLException {
        List<PayRoll> employeePayRollList = PayRollDataBaseIO.getInstance().getPayRollInformation(name);
        return employeePayRollList.get(0).equals(getPayRollData(name));
    }

    /**
     * Creating A method as layer to call on the respective
     * method to retrieve employees from date range
     *
     * @param ioService
     * @param fromDate
     * @param toDate
     * @return
     * @throws Exceptions
     */

    public List<PayRoll> retrievePayRollForDateRange(IOService ioService, LocalDate fromDate, LocalDate toDate) throws Exceptions {
        if (ioService.equals(IOService.DB_IO))
            return PayRollDataBaseIO.getInstance().getEmployeeForDateRange(fromDate, toDate);
        return null;
    }

    /**
     *
     * Using this method as a layer to call on the
     * DB io class' arithmetic methods
     *
     * @param ioService
     * @param function
     * @return
     * @throws Exceptions
     */
    public Map<String, Integer> getArithmeticOperationalFunctionsOfSalaryByGender(IOService ioService, String function) throws Exceptions {
        if (ioService.equals(IOService.DB_IO))
            return PayRollDataBaseIO.getInstance().getArithmeticOperationalFunctionsOfSalaryByGender(function);
        return null;
    }

    public enum IOService {
        CONSOLE_IO, FILE_IO, DB_IO, REST_IO

    }
}
