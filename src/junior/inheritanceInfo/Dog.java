package junior.inheritanceInfo;

// extends 關鍵字用於表示繼承關係，Dog 類別繼承了 Animal 類別
// Dog 是 Animal 的子類別(subclass)
// Animal 是 Dog 的父類別(superclass)
public class Dog extends Animal {

    // 在 Dog 類別中覆寫(Override)了 父類別的eat 方法
    @Override
    public void eat() {
        super.eat(); // 呼叫父類別的 eat 方法
        System.out.println("Dog1 is eating.");
    }

}
