package junior.classInfo;

public class Demo {
    public static void main(String[] args) {
        // 實體化物件 - 透過類別來創造一個物件
        // class -> object
        CarI car1 = new CarI();

        // System.out.println("車子的品牌(還未寫入): " + car1.brand);
        // // 寫入物件的屬性
        car1.brand = "Toyota";
        car1.color = "Red";
        car1.year = 2020;
        car1.numberPlate = "ABC-1234";

        // // 讀取物件的屬性
        System.out.println("車子的品牌(已經寫入): " + car1.brand);

        // // 呼叫(調用)物件的方法
        car1.start();
        car1.stop();

        // // 課堂練習1 - 建立一個類別 Area 用來計算面積
        // // 屬性: int length, int width
        // // 方法: calculateArea() 回傳型態為int, 計算長方形的面積 length * width
        // // 還要實體化物件來測試方法的功能
        // // Area area1 = new Area();
        // // area1.length = 5;
        // // area1.width = 3;
        // // int areaResult1 = area1.calculateArea();
        // // System.out.println("長方形的面積為: " + areaResult1);

        // Area1 area2 = new Area1();
        // area2.length = 5;
        // area2.width = 3;
        // int areaResult2 = area2.calculateArea();
        // System.out.println("長方形的面積為: " + areaResult2);

        // // 建構子使用
        CarII car11 = new CarII("BMW", "Blue");
        System.out.println("車子的品牌: " + car11.brand);
        System.out.println("車子的顏色: " + car11.color);

        // // 課堂練習2 - 改寫Area類別, 加入建構子來初始化length和width, 並且實體化物件來測試建構子的功能
        // Area area1 = new Area(5, 3);
        // int areaResult1 = area1.calculateArea();
        // System.out.println("長方形的面積為: " + areaResult1);

        // // 多載 (Overloading) - 同一個方法名稱, 但是參數不同
        // // 長方形的面積計算
        // int areaResult3 = area1.calculateArea();
        // System.out.println("長方形的面積為: " + areaResult3);

        // // 圓形的面積計算
        // double areaResult4 = area1.calculateArea(5);
        // System.out.println("圓形的面積為: " + areaResult4);

        // // 三角形的面積計算
        // double areaResult5 = area1.calculateArea(5, 3);
        // System.out.println("三角形的面積為: " + areaResult5);

        // // 課堂練習3 - 用多載改寫Area類別的建構子
        // // 讓一開始不帶參數也可以跑出來, 但是預設length和width都是0
        // Area area3 = new Area();
        // area3.length = 5;
        // area3.width = 3;
        // int areaResult6 = area3.calculateArea();
        // System.out.println("長方形的面積為: " + areaResult6);

        // // 課堂練習4 - 在Area類別中加入一個方法, 用來計算梯形的面積
        // // 梯形的面積計算公式: (上底 + 下底) * 高 / 2
        // // 這個方法的參數為 int topBase, int bottomBase, int height, 回傳型態為double
        // double areaResult7 = area1.calculateArea(5, 7, 3);
        // System.out.println("梯形的面積為: " + areaResult7);

        // 課後練習1 - 在Area類別移除 calculateArea 所有多載方法
        // 改用建構子多載初始化不同形狀的屬性,
        // 例如長方形需要 length 和 width,
        // 圓形需要 radius,
        // 三角形需要 base 和 height,
        // 梯形需要 topBase, bottomBase 和 height
        // 並且直接在建構子內去計算面積
        // 呼叫一個印出面積的方法來顯示結果 getArea() 回傳型態為void
        AreaPlus areaPlus1 = new AreaPlus(5); // 圓形
        areaPlus1.getArea();

        AreaPlus areaPlus2 = new AreaPlus(5, 3); // 長方形
        areaPlus2.getArea();

        AreaPlus areaPlus3 = new AreaPlus(5, 3, true); // 三角形
        areaPlus3.getArea();

        AreaPlus areaPlus4 = new AreaPlus(5, 7, 3); // 梯形
        areaPlus4.getArea();

    }

}

// 可以寫進來一個類別, 但是這個類別不能是public的, 因為一個檔案只能有一個public類別
class Area1 {
    public int length;
    public int width;

    public int calculateArea() {
        int area = length * width;
        return area;
    }
}