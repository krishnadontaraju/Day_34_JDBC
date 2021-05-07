import java.sql.Date;

public class PayRoll {
    public int employeeId;
    public String employeeName;
    public int employeeSalary;
    private int employeeSalaryDeductions;
    private int employeeSalaryNetPay;
    private int employeeSalaryTaxes;
    private Date employeeStartDate;
    private int employeePhoneNumber;
    private String employeeGender;

    public PayRoll(Integer employeeId, String employeeName, int employeeSalary) {
        this.employeeId = employeeId;
        this.employeeName = employeeName;
        this.employeeSalary = employeeSalary;
    }

    /**
     * new Constructor for database IO
     *
     * @param id
     * @param name
     * @param gender
     * @param phoneNumber
     * @param startDate
     * @param salary
     */
    public PayRoll(int id, String name, String gender, int phoneNumber, Date startDate, int salary) {

        employeeId = id;
        employeeName = name;
        employeeStartDate = startDate;
        employeePhoneNumber = phoneNumber;
        employeeGender = gender;
        employeeSalary = salary;


    }

    /**
     * Constructor for the database updation method
     * in database io class
     *
     * @param name
     * @param salary
     * @param deductions
     * @param taxes
     * @param netPay
     */

    public PayRoll(String name, int salary, int deductions, int taxes, int netPay) {

        employeeName = name;
        employeeSalary = salary;
        employeeSalaryDeductions = deductions;
        employeeSalaryTaxes = taxes;
        employeeSalaryNetPay = netPay;
    }

    @Override
    public String toString() {
        return
                "employeeSalaryDeductions=" + employeeSalaryDeductions +
                        ", employeeSalaryNetPay=" + employeeSalaryNetPay +
                        ", employeeSalaryTaxes=" + employeeSalaryTaxes +
                        ", employeeStartDate=" + employeeStartDate +
                        ", employeePhoneNumber=" + employeePhoneNumber +
                        ", employeeGender='" + employeeGender + '\'' +
                        ", employeeId=" + employeeId +
                        ", employeeName='" + employeeName + '\'' +
                        ", employeeSalary=" + employeeSalary;
    }



    /*  overriding the equals method to check for value and class in memory to verify it with the details pulled from the database */

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (this == null || getClass() != o.getClass())
            return false;
        PayRoll that = (PayRoll) o;
        return employeeId == that.employeeId && Integer.compare(that.employeeSalary, employeeSalary) == 0 && employeeName.equals(that.employeeName);

    }
}
