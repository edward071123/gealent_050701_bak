package junior.interfaceInfo;

public class Demo {
    public static void main(String[] args) {
        // 介面: interface 介紹
        // 也是類似書的目錄
        // 不同類別也可以學習相同技能
        Dragon dragon1 = new Dragon();
        dragon1.line();
        dragon1.circle();
        dragon1.fire();

        Bird bird1 = new Bird();
        bird1.line();
        bird1.circle();
        bird1.fire();
    }
}
