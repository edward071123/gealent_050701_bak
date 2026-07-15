package junior.inheritanceInfo;

public class Animal {

    public Animal() {
        // 建構子
        System.out.println("Animal 初始化");
    }

    public void eat() {
        System.out.println("Animal1 is eating.");
    }

    // final 關鍵字用於表示這個方法不能被覆寫(Override)
    public final void sleep() {
        System.out.println("Animal1 is sleeping.");
    }
}
