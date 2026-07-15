package junior.inheritanceInfo;

public class Demo {
    // 繼承的使用介紹
    public static void main(String[] args) {
        Animal animal1 = new Animal();
        System.out.println("Using Animal class:");
        animal1.eat(); // 呼叫 Animal 類別的方法

        Dog dog1 = new Dog();
        System.out.println("Using Dog class:");
        dog1.eat();
        dog1.sleep(); // 呼叫從父類別繼承來的方法

        // 建立一個 Cat 物件
        Cat cat1 = new Cat();
        System.out.println("Using Cat class:");
        cat1.eat();
        cat1.sleep(); // 呼叫從父類別繼承來的方法
        System.out.println("-----------------------------");
        // 課堂練習1: 創建一個類別叫做 Bird, 繼承自 Animal 類別, 並覆寫 eat 方法
        Bird bird1 = new Bird();
        System.out.println("Using Bird class:");
        bird1.eat();
        bird1.sleep(); // 呼叫從父類別繼承來的方法

    }
}
