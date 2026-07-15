package junior.polymorphism.homeworkII;

public class Demo {
    public static void main(String[] args) {
        // 課後練習: 飲料店點餐結帳系統
        // 父類別: Drink
        // 有三個子類別: BlackTea, MilkTea, Coffee
        // 每杯飲料都要有以下屬性皆是private 且用封裝處理(getter/setter)
        // 飲料名稱: name(String)
        // 飲料價格: price(double)
        // 甜度: sugarLevel(int), 0:無糖, 1:半糖, 2:全糖
        // 冰塊: iceLevel(int), 0:去冰, 1:微冰, 2:正常冰
        // Drink 有一個方法: calculatePrice(), 子類別都要覆寫
        // BlackTea: 基本價格 30 元
        // MilkTea: 基本價格 45 元，如果加珍珠 + 10元, ＊所以有多屬性,加珍珠:addPearl(boolean)
        // Coffee: 基本價格 55 元，如果大杯 + 15元, ＊所以有多屬性, 大杯:largeSize(boolean)

        // Main(程式進入點) 內用多型 + array + for 印出
        // 名稱, 甜度, 冰塊, 價格
        // 用飲料的價位 且判斷 飲料超過60元 後面接著輸出"高價飲料", 否則為"一般飲料"
        // 總金額

        // 輸出結果如下:
        // ==========
        // 名稱：紅茶
        // 甜度：無糖
        // 冰塊：去冰
        // 價格：30
        // 一般飲料

        // ==========
        // 名稱：奶茶
        // 甜度：半糖
        // 冰塊：微冰
        // 是否加珍珠：加珍珠
        // 價格：55
        // 一般飲料

        // ==========
        // 名稱：咖啡
        // 甜度：全糖
        // 冰塊：正常冰
        // 是否大杯：大杯
        // 價格：70
        // 高價飲料

        // ==========
        // 總金額：155 元

        Drink[] drinks = {
                new BlackTea(2, 1), // 全糖微冰
                new MilkTea(1, 2, true), // 半糖正常冰加珍珠
                new Coffee(0, 0, true) // 無糖去冰大杯
        };

        int total = 0;

        // foreach loop
        // 前面是單一陣列內的內容
        // 後面是陣列
        for (Drink drink1 : drinks) {
            // Drink drink1 = drinks[i];
            System.out.println("==========");
            System.out.println("名稱：" + drink1.getName());
            System.out.println("甜度：" + drink1.getSugarLevel());
            System.out.println("冰塊：" + drink1.getIceLevel());
            System.out.println("價格：" + drink1.calculatePrice());
            if (drink1.calculatePrice() > 60) {
                System.out.println("高價飲料");
            } else {
                System.out.println("一般飲料");
            }

            total += drink1.calculatePrice();
        }

        System.out.println("==========");

        System.out.println("總金額：" + total + " 元");

        // ==========================================================================================
        // foreach vs for 舉例
        int[] numbers = { 1, 2, 3, 4, 5 };

        // 1 -> 5
        // 所有元素都跑過一輪
        int count = 1;
        for (int n : numbers) {
            System.out.println(n);
            count++;
        }
        System.out.println(count);

        // 1 -> 5
        // 取決於i++ or i=i+2 or i=i+3
        for (int i = 0; i < 3; i++) {
            int n = numbers[i];
            System.out.println(n);
        }

        // 5 -> 1
        for (int i = numbers.length - 1; i >= 0; i--) {
            System.out.println(numbers[i]);
        }
        // ==========================================================================================
    }
}
