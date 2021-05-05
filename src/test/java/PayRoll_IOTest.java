import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

public class PayRoll_IOTest {
    @Test
    public void given3EmployeesWhenWrittenToFileShouldMatchEmployeeEntries() {
        PayRoll[] arrayOfEmployees = {
                new PayRoll(1,"Emile",1200000.265),
                new PayRoll(2,"Smith",6461664.6164),
                new PayRoll(3,"Rowe",9797649797.2615)
        };
        PayRollOperation testPayRollOperation;
        testPayRollOperation = new PayRollOperation(Arrays.asList(arrayOfEmployees));
        testPayRollOperation.writePayRollDetails(PayRollOperation.IOService.FILE_IO);
        testPayRollOperation.printData(PayRollOperation.IOService.FILE_IO);
        long entries = testPayRollOperation.countEntries(PayRollOperation.IOService.FILE_IO);
        Assertions.assertEquals(3,entries);
    }

    /*
    * testing the operation of the database IO
    */

    @Test
    public void givenEmployeePayRollInDB_WhenRetrieved_ShouldMatchEmployeeCount(){

        PayRollOperation dbPayRoll =  new PayRollOperation();

        List<PayRoll>  dbPayRollData = dbPayRoll.readEmployeeDetails(PayRollOperation.IOService.DB_IO);

        Assertions.assertEquals(8,dbPayRollData.size());

    }
}
