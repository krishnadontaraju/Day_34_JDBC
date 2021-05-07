import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

public class PayRoll_IOTest {
    @Test
    public void given3EmployeesWhenWrittenToFileShouldMatchEmployeeEntries() {
        PayRoll[] arrayOfEmployees = {
                new PayRoll(1, "Emile", 1200000),
                new PayRoll(2, "Smith", 6461664),
                new PayRoll(3, "Rowe", 979764979)
        };
        PayRollOperation testPayRollOperation;
        testPayRollOperation = new PayRollOperation(Arrays.asList(arrayOfEmployees));
        testPayRollOperation.writePayRollDetails(PayRollOperation.IOService.FILE_IO);
        testPayRollOperation.printData(PayRollOperation.IOService.FILE_IO);
        long entries = testPayRollOperation.countEntries(PayRollOperation.IOService.FILE_IO);
        Assertions.assertEquals(3, entries);
    }

    /*
     * testing the operation of the database IO
     */

    @Test
    public void givenEmployeePayRollInDB_WhenRetrieved_ShouldMatchEmployeeCount() throws Exceptions {

        PayRollOperation dbPayRoll = new PayRollOperation();

        List<PayRoll> dbPayRollData = dbPayRoll.readEmployeeDetails(PayRollOperation.IOService.DB_IO);

        Assertions.assertEquals(8, dbPayRollData.size());

    }


    /**
     * checking if the updated has worked as expected and
     * also checking if the synchronization happened with the database
     *
     * @throws Exceptions
     * @throws SQLException
     */

    @Test
    public void givenNewSalaryForAnEmployee_WhenUpdated_ShouldGiveDesiredOutput() throws Exceptions, SQLException {

        PayRollOperation employeePayRollOperation = new PayRollOperation();

        List<PayRoll> employeePayRollList = employeePayRollOperation.readEmployeeDetails(PayRollOperation.IOService.DB_IO);
        employeePayRollOperation.updateEmployeeSalary("Alicia", 300000000);

        boolean isUpdated = employeePayRollOperation.checkEmployeeSynchronizationWithDatabase("Alicia");
        Assertions.assertTrue(isUpdated);

    }


    /**
     * UC-5 test case to check the retrieval of employees
     * in a date range is correct
     *
     * @throws Exceptions
     */

    @Test
    public void givenDateRange_whenRetrieved_shouldMatchEmployeeCount() throws Exceptions {
        PayRollOperation employeePayRollOperation = new PayRollOperation();
        employeePayRollOperation.readEmployeeDetails(PayRollOperation.IOService.DB_IO);
        LocalDate fromDate = LocalDate.of(2010, 01, 01);
        LocalDate toDate = LocalDate.now();

        List<PayRoll> dateRangeData = employeePayRollOperation.retrievePayRollForDateRange(PayRollOperation.IOService.DB_IO, fromDate, toDate);

        Assertions.assertEquals(3, dateRangeData.size());

    }
}
