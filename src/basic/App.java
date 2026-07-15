package basic;
// package: 隸屬於src下面的資料夾
// 類比的概念就是: 想像src下是一間公司, 其他下面的資料夾是其部門
// 部門下面的檔案就是要隸屬於xx部門


// 類別名稱要跟擋案名稱相同
// 類別名稱要大寫開頭
// {} : 左右大括號
// [] : 左右中括號
// () : 左右小括號
// 類別 -> 方法 -> 每一行程式碼

public class App {

    // 程式進入點: 一律找main
    public static void main(String[] args) throws Exception {
        // 系統的輸出的換行印出
        // 每一行結束要有分號
        System.out.println("Hello, World! #1 #2 #3");
        System.out.println("hi Edward1");
        System.out.println("hi Edward2");

        // 變數宣告: 在記憶體規劃出一個櫃子儲存數值
        // = : 右邊數值丟到左邊容器

        // 不用變數
        System.out.println(7 + 4);
        System.out.println(7 * 4);
        System.out.println(7 / 4);
        System.out.println(7 - 4);
        // 使用變數
        int x = 7;
        int z = 4;
        System.out.println(x + z);
        System.out.println(x * z);
        System.out.println(x / z);
        System.out.println(x - z);

        String y1 = "甲方";
        String y2 = "丙方";

        System.out.println(y1 + y2);
    }

}