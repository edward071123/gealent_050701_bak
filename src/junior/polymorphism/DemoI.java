package junior.polymorphism;

public class DemoI {
    public static void main(String[] args) {
        // 多型的介紹
        // ------------------ 不用多型處理 ------------------
        Employee emp11 = new Employee("Alice", 40000);
        SalesEmployee emp12 = new SalesEmployee("Bob", 30000, 5000);
        PartimeEmployee emp13 = new PartimeEmployee("Charlie", 200, 80);

        emp11.calculateSalary();
        emp12.calculateSalary();
        emp13.calculateSalary();
        // ------------------ 不用多型處理 ------------------
        System.out.println("-----------------------以下用多型處理-------------------------");
        // ------------------ 使用多型處理 ------------------
        // 可使用父類別的型別做變數宣告
        // 白話: 父類別的箱子可以裝子類別的物件
        Employee emp21 = new Employee("David", 45000);
        Employee emp22 = new SalesEmployee("Allen", 30000, 10000);
        Employee emp23 = new PartimeEmployee("Cathy", 300, 100);

        emp21.calculateSalary();
        emp22.calculateSalary();
        emp23.calculateSalary();
        // ------------------ 使用多型處理 ------------------
        System.out.println("---------------------以下用多型+array+for處理---------------------------");
        // ------------------ 使用多型 + array + for 處理 ------------------
        Employee[] employees = {
                new Employee("David", 45000),
                new SalesEmployee("Allen", 30000, 10000),
                new PartimeEmployee("Cathy", 300, 100),
                new Employee("Alice", 40000),
                new SalesEmployee("Bob", 30000, 5000),
                new PartimeEmployee("Charlie", 200, 80),
                new Employee("David1", 50000),
                new SalesEmployee("Allen1", 35000, 10000)
        };

        for (int i = 0; i < employees.length; i++) {
            employees[i].calculateSalary();
        }

    }
}
