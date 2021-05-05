import java.util.ArrayList;
import java.util.List;
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

    public List<PayRoll> readEmployeeDetails(IOService ioService) {
        if (ioService.equals(IOService.DB_IO))
            this.payRollList = new PayRollDataBaseIO().readData();
        return this.payRollList;
    }

    public enum IOService {
        CONSOLE_IO, FILE_IO, DB_IO, REST_IO
    }
}
