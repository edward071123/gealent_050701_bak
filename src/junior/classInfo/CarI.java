package junior.classInfo;

// 這是一個類別的建立流程, 想像成一個施作藍圖, 先把藍圖畫出來, 再來實體化物件
// 類別 (Class) : 屬性(Fields) + 方法(Methods)
public class CarI {
    // 屬性 (Fields) - 用來描述物件的特徵
    // 這個類別內的變數
    public String brand;
    public String color;
    public int year;
    public String numberPlate;

    // 方法 (Methods) - 用來描述物件的行為, 或是想像成窗口,手續 流程, 讓外界可以透過這些方法來操作物件
    // 不用static 是因為這些方法是屬於物件的, 不是屬於類別的
    public void start() {
        System.out.println("車子啟動了");
    }

    public void stop() {
        System.out.println("車子停止了");
    }
}
