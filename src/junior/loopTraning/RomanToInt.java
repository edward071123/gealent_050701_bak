package junior.loopTraning;

public class RomanToInt {
    public static void main(String[] args) {
        // 練習作業
        // Symbol Value
        // I        1
        // V        5
        // X        10
        // L        50
        // C        100
        // D        500
        // M        1000

        // I , II , III ,  IV ,  V  ,  VI ,  VII ,  VIII , IX , X  , XI
        // 1 , 2  , 3   ,  4  ,  5  ,  6  ,  7   ,  8    , 9  , 10 , 11

        // 有有六個地方用到了減法
        // I 在 Ｖ(5) 和 X(10) 左邊就會變成 4 跟 9
        // X 在 L(50) 和 C(100) 左邊就會變成 40 跟 90
        // C 在 D(500) 和 M(1000) 左邊就會變成 400 跟 900

        // 輸入: III
        // 輸出: 3
        // 過程: 1 + 1 + 1 = 3

        // 輸入: LVIII
        // 輸出: 58
        // 過程: 50 + 5 + 1 + 1 + 1 = 58

        // 輸入: MCMXCIV
        // 輸出: 1994
        // 過程: 1000 + 100 + 1000 + 10 + 100 + 1 + 5 = 2216 (x)

        // 輸入: M CM XC IV
        // 過程: 1000 + (100 - 1000) + (10 - 100) + (1 - 5) = 1994(o)
        // 發現規律 左邊數值比右邊數值小不能+

        String input = "MCMXCIV";

        // 對應羅馬文字的阿拉伯數字的數值
        char symbol[] = { 'I', 'V', 'X', 'L', 'C', 'D', 'M' };
        int value[] = { 1, 5, 10, 50, 100, 500, 1000 };

        // 轉型 把symbol轉乘String: 為了用indexOf這個方法才需要的轉型
        String str = new String(symbol);

        int result = 0;
        // 上一個數(左邊的數)
        int lastValue = 0;
        // length(): 找出字串長度的方法
        // 這個迴圈是為了切割此字串變成單一字元
        for (int i = 0; i < input.length(); i++) {
            // 取得單一字元
            char getOne = input.charAt(i);
            // System.out.println(getOne);

            // 搜尋字元在symbol這個array內的索引值(index)
            int getIndex = str.indexOf(getOne);

            // 根據相同的index 取得對應的數值
            int getValue = value[getIndex];
            System.out.println(getOne + "的index:" + getIndex + "的value:" + getValue);

            if (lastValue >= getValue) {
                // 如果左邊的數值(上一個數) 大於 右邊數值(現在的數) 可相加
                result += getValue;
            } else {
                // 由於上一次的result先去判斷左邊大於右邊故而先加一次, 這次必須扣回來
                result = result - lastValue;
                // 這是左邊小於右邊必須用的減法
                int sub = getValue - lastValue;
                result = result + sub;
            }

            lastValue = getValue;
        }

        System.out.println("轉換結果:" + result);
    }
}
