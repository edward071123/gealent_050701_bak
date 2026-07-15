package junior.polymorphism;

public class PartimeEmployee extends Employee {
    // 兼職員工: 領時薪的

    // 工作時數
    private int hours;

    public PartimeEmployee(String name, double salary, int hours) {
        // 呼叫父類別的建構子
        super(name, salary);
        setHours(hours);
    }

    public int getHours() {
        return this.hours;
    }

    public void setHours(int hours) {
        this.hours = hours;
    }

    // 覆寫父類別的方法 (Override)
    // calculateSalary() 的方式內容不同 必須用Override 去改寫
    @Override
    public void calculateSalary() {
        double totalSalary = getSalary() * getHours();
        System.out.println("兼職員工: " + getName() + " 的薪水為: " + totalSalary);
    }

}
