package junior.inheritanceInfo;

public class Bird extends Animal {

    public Bird() {
        // 會被默默執行 可以忽略不用寫出來
        // super(); // 呼叫父類別的建構子
        System.out.println("Bird 初始化");
    }

    @Override
    public void eat() {
        System.out.println("Bird1 is eating.");
    }

}
