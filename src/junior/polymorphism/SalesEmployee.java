package junior.polymorphism;

public class SalesEmployee extends Employee {
    // 銷售員: 領底薪 + 業績獎金
    // 銷售業績獎金
    private double commission;

    public SalesEmployee(String name, double salary, double commission) {
        // 呼叫父類別的建構子
        super(name, salary);
        setCommission(commission);
    }

    public double getCommission() {
        return this.commission;
    }

    public void setCommission(double commission) {
        this.commission = commission;
    }

    // 覆寫父類別的方法 (Override)
    // calculateSalary() 的方式內容不同 必須用Override 去改寫
    @Override
    public void calculateSalary() {
        double totalSalary = getSalary() + getCommission();
        System.out.println("銷售員: " + getName() + " 的薪水為: " + totalSalary);
    }

}
