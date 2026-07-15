package junior.loopTraning;

public class RomanToIntII {
    public static void main(String[] args) {
        // Symbol Value
        // I 1
        // V 5
        // X 10
        // L 50
        // C 100
        // D 500
        // M 1000

        // if (input.charAt(i) == 'I') {
        //     getValue = 1;
        // } else if (input.charAt(i) == 'V') {
        //     getValue = 5;
        // } else if (input.charAt(i) == 'X') {
        //     getValue = 10;
        // } else if (input.charAt(i) == 'L') {
        //     getValue = 50;
        // } else if (input.charAt(i) == 'C') {
        //     getValue = 100;
        // } else if (input.charAt(i) == 'D') {
        //     getValue = 500;
        // } else {
        //     getValue = 1000;
        // }

        // 有有六個地方用到了減法
        // I 在 Ｖ(5) 和 X(10) 左邊就會變成 4 跟 9
        // X 在 L(50) 和 C(100) 左邊就會變成 40 跟 90
        // C 在 D(500) 和 M(1000) 左邊就會變成 400 跟 900

        // 輸入: III
        // 輸出: 3
        // 過程: 1 + 1 + 1 = 3

        // 輸入: LVIII => 由右到左的話 IIIVL
        // 輸出: 58
        // 過程: 50 + 5 + 1 + 1 + 1 = 58

        // 輸入: MCMXCIV => 由右到左的話 V I C X M X M
        //
        // 推導過程
        // 由左到右輸入: M CM XC IV
        // 由左到右過程: 1000 + (100 - 1000) + (10 - 100) + (1 - 5) = 1994

        // 由右到左輸入: V I C X M C M
        // 由右到左過程: 5 - 1 + 100 - 10 + 1000 - 100 + 1000 = 1994
        // 發現規律1 上一個數 比 自己大 : 減法
        // 發現規律2 上一個數 比 自己小 或等於自己 : 加法

        String input = "MCMXCIV"; // ['M', 'C', 'M', 'X', 'C', 'I', 'V']
        int result = 0;
        int lastValue = 0;
        for (int i = input.length() - 1; i >= 0; i--) {
            // System.out.print(input.charAt(i));
            int getValue = 0;
            // switch case
            switch (input.charAt(i)) {
                case 'I':
                    getValue = 1;
                    break;
                case 'V':
                    getValue = 5;
                    break;
                case 'X':
                    getValue = 10;
                    break;
                case 'L':
                    getValue = 50;
                    break;
                case 'C':
                    getValue = 100;
                    break;
                case 'D':
                    getValue = 500;
                    break;
                default:
                    getValue = 1000;
                    break;
            }

            if (lastValue > getValue) {
                result -= getValue;
            } else {
                result += getValue;
            }

            lastValue = getValue;
        }
        System.out.println("轉換結果:" + result);
    }

}
