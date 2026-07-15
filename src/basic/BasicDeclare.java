package basic;

// 變數的基本型別
// 快速輸出(印出):      sout + tab => System.out.println();
// 快速輸出(程式進入點): main + tab => public static void main(String[] args) {};
public class BasicDeclare {
    public static void main(String[] args) throws Exception {
        // int: 整數, 範圍: -2,147,483,648 ~ 2,147,483,647
        int x1 = 2147483647;
        System.out.println("整數(int): " + x1);

        // byte: 整數, 範圍: -128 ~ 127
        byte x2 = 127;
        System.out.println("整數(byte): " + x2);

        // 溢位（Overflow）
        x2++; // +1的寫法
        System.out.println("溢位(Overflow): 整數(byte) + 1 後: " + x2);
        // ... → -2 → -1 → 0 → 1 → ... → 126 → 127
        // ↓
        // -128

        // long: 整數, 範圍: -9,223,372,036,854,775,808 ~ 9,223,372,036,854,775,807
        long x3 = 9223372036854775807L;
        System.out.println("整數(long): " + x3);

        // float: 32位元浮點數, 需要加上F
        float weight1 = 49.4F;
        System.out.println("浮點數(float): " + weight1);

        // double: 64位元浮點數, 預設型別
        double weight2 = 49.49;
        System.out.println("浮點數(double): " + weight2);

        // char: 字元, 1個字元, 單引號
        char c1 = 'Y';
        System.out.println("字元(char): " + c1);

        // String: 字串, 多個字元, 雙引號
        String name = "Edward";
        System.out.println("字串(String): " + name);

        // boolean: 布林, true/false
        boolean isStudent = false;
        System.out.println("布林(boolean): " + isStudent);

        // 陣列(Array)宣告
        // 陣列的長度是固定的, 不能改變
        // 陣列的長度是從0開始計算
        // 陣列的最大索引值: 陣列長度 - 1

        // 空陣列 無法列印
        // int[] xx1 = {};

        // 有數值的陣列
        int[] xx2 = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };
        System.out.println("陣列(int)-xx2: " + xx2[0]);
        System.out.println("陣列(int)-xx2: " + xx2[1]);

        System.out.println("陣列(int)-xx2: " + xx2[8]);

        // 請同學建立一個 String的array 裡面要有三個字串
        String[] xx3 = { "Hello", "hi", "How are you" };

        System.out.println("陣列(int)-xx3: " + xx3[0]);
        System.out.println("陣列(int)-xx3: " + xx3[1]);
        System.out.println("陣列(int)-xx3: " + xx3[2]);

        // 建立一個boolean的array 裡面要有三個boolean值
        boolean[] xx4 = { true, false, true };
        System.out.println("陣列(boolean)-xx4: " + xx4[0]);
        System.out.println("陣列(boolean)-xx4: " + xx4[1]);
        System.out.println("陣列(boolean)-xx4: " + xx4[2]);

    }
}
