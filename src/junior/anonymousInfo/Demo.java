package junior.anonymousInfo;

public class Demo {
    public static void main(String[] args) {
        // 匿名類別(Anonymous Class)介紹
        // 在使用介面(interface)的前提下
        // 懶得再建立類別去引用

        System.out.println("===============一般物件寫法===============");
        AnimalI dog1 = new Dog();
        dog1.speak();
        dog1.sleep();

        System.out.println("===============匿名類別完整寫法===============");
        AnimalI cat1 = new AnimalI() {
            // 匿名類別直接產生物件時去實作裡面的方法
            @Override
            public void speak() {
                System.out.println("咪喵 (匿名類別完整寫法)");
            }

            @Override
            public void sleep() {
                System.out.println("貓睡覺 (匿名類別完整寫法)");
            }
        };

        cat1.speak();
        cat1.sleep();

        System.out.println("===============匿名類別簡潔寫法===============");

        // 簡潔的寫法 比較適合 介面(interface)內只有一個方法需要被實作
        new AnimalII() {
            @Override
            public void speak() {
                System.out.println("汪汪 (匿名類別簡潔的寫法)");
            }

        }.speak();

        System.out.println("===============Lambda寫法===============");
        // Java 8 開始提供
        // Lambda寫法: 我連匿名類別都懶得寫, 只想實作一個方法
        // 不可以用在抽象類別(abstract)上
        // 一定要 介面(interface)內只有一個方法需要被實作
        AnimalII bird1 = () -> {
            System.out.println("啾啾叫1");
            System.out.println("啾啾叫2");
        };

        bird1.speak();

    }
}

interface AnimalI {
    void speak();

    void sleep();
}

interface AnimalII {
    void speak();
}

class Dog implements AnimalI {

    @Override
    public void speak() {
        System.out.println("汪汪 (一般類別引用)");
    }

    @Override
    public void sleep() {
        System.out.println("狗睡覺 (一般類別引用)");
    }

}