package junior.inheritancePlus;

public class Demo {
    public static void main(String[] args) {
        Animal animal1 = new Animal("初始動物1", 5);
        animal1.eat();
        System.out.println("-----------------------------");
        Dog dog1 = new Dog("旺財", 3, "拉布拉多");
        dog1.eat();

        // 課堂練習2: 建立一個Cat類別繼承Animal類別，並新增一個屬性color，
        // 然後覆寫eat方法，最後在main方法中建立一個Cat物件並呼叫eat方法。
        System.out.println("-----------------------------");
        Cat cat1 = new Cat("咪咪", 2, "黑色");
        cat1.eat();
    }
}
