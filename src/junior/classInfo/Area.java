package junior.classInfo;

public class Area {
    public int length;
    public int width;

    public Area() {

    }

    public Area(int length, int width) {
        // 這裡是建構子, 可以用來初始化物件的屬性
        // 這裡的length和width是區域變數, 不是屬性
        this.length = length;
        this.width = width;
    }

    // 多載 (Overloading) - 同一個方法名稱, 但是參數不同

    // 長方形的面積計算
    public int calculateArea() {
        int area = length * width;
        return area;
    }

    // 圓形的面積計算
    public double calculateArea(int r) {
        double area = r * r * 3.14;
        return area;
    }

    // 三角形的面積計算
    public double calculateArea(int base, int height) {
        double area = (base * height) / 2.0;
        return area;
    }

    // 課堂練習4 - 在Area類別中加入一個方法, 用來計算梯形的面積
    // 梯形的面積計算公式: (上底 + 下底) * 高 / 2
    // 這個方法的參數為 int topBase, int bottomBase, int height, 回傳型態為double
    public double calculateArea(int topBase, int bottomBase, int height) {
        double area = (topBase + bottomBase) * height / 2.0;
        return area;
    }

}
