package junior.polymorphism;

public class Employee {
    // 基本員工: 領月薪的

    // 員工名稱
    private String name;
    // 員工基本薪水
    private double salary;

    public Employee(String name, double salary) {
        setName(name);
        setSalary(salary);
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getSalary() {
        return this.salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    // 每個月結算時顯示薪水
    public void calculateSalary() {
        System.out.println("基本月薪員工: " + getName() + " 的薪水為: " + getSalary());
    }

}
