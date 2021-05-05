import java.sql.Date;

public class PayRoll {
    private Date employeeStartDate;
    private int employeePhoneNumber;
    private String employeeGender;
    public int employeeId;
    public String employeeName;
    public double employeeSalary;

    public PayRoll(Integer employeeId,String employeeName,Double employeeSalary){
        this.employeeId = employeeId;
        this.employeeName = employeeName;
        this.employeeSalary = employeeSalary;
    }

    /**
     * new Constructor for database IO
     * @param id
     * @param name
     * @param gender
     * @param phoneNumber
     * @param startDate
     */
    public PayRoll(int id, String name, String gender, int phoneNumber, Date startDate) {

        employeeId = id;
        employeeName = name;
        employeeStartDate = startDate;
        employeePhoneNumber = phoneNumber;
        employeeGender = gender;

    }

    @Override
    public String toString() {
        return
                "employeeId=" + employeeId +
                ", employeeName=" + employeeName + '\'' +
                ", employeeSalary=" + employeeSalary ;
    }
}
