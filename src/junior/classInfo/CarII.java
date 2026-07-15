package junior.classInfo;

public class CarII {
    public String brand;
    public String color;
    public int year;
    public String numberPlate;

    // 建構子 (Constructor) - 用來初始化物件的屬性, 讓物件在被創造出來的同時就有一些初始值
    // 建構子名稱必須和類別名稱一樣, 沒有回傳型態, 也不需要寫void
    public CarII(String brand, String color) {
        // this關鍵字 - 此類別內的變數(屬性)
        this.brand = brand;
        this.color = color;
    }

    public void start() {
        System.out.println("車子啟動了");
    }

    public void stop() {
        System.out.println("車子停止了");
    }
}
