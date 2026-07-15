package junior.methodInfo;

public class Demo {
    public static void main(String[] args) {

        for (int i = 0; i < 5; i++) {
            printHello1();
        }

        printHello2(4);
        printHello2(5);
        printHello2(6);
        printHello2(20);

        printHello3("Edward");
        printHello3("David");

        printHello4("Allen", 7);
        printHello4("Ben", 8);

        String[] names = { "Edward1", "David1", "Allen1", "Ben1" };
        // for(int z = 0; z < names.length; z++) {
        // System.out.println("Hello, " + names[z]);
        // }
        printHello5(names);

        String[] names2 = { "Edward2", "David2", "Allen2" };
        printHello5(names2);

        int nums[] = { 1, 2, 3, 4, 5 };
        printHello6(names2, nums);

        int result = add(3, 5);
        System.out.println("3 + 5 = " + result);

        String strResult = stringAdd("Hello1, ", "World1!");
        System.out.println(strResult);

    }

    // 方法(Method)的定義 其他語言或許是function
    // void : 沒有回傳值
    // static : 靜態的, 不需要實體化物件就可以使用
    // 前綴字 靜態 回傳型態 方法名稱(輸入型別 輸入參數) {
    // 方法內容
    // }

    public static void printHello1() {
        System.out.println("Hello, World1!");
        System.out.println("Hello, World2!");
        System.out.println("Hello, World3!");
    }

    public static void printHello2(int num) {
        System.out.println("Hello, World" + num + "!");
    }

    // 課堂練習1: 寫一個方法printHello3, 輸入一個字串, 印出 "Hello, " + 輸入的字串 + "!"
    public static void printHello3(String name) {
        System.out.println("Hello, World " + name + "!");
    }

    public static void printHello4(String name, int num) {
        System.out.println("Hello, World " + name + " " + num + "!");
    }

    // 課堂練習2: 寫一個方法printHello5, 輸入一個字串的陣列, 印出 "Hello, " + 陣列內的每個字串 + "!"
    // 字串的陣列: String[] names = {"Edward", "David", "Allen"};
    public static void printHello5(String[] names) {
        for (int i = 0; i < names.length; i++) {
            System.out.println("Hello, " + names[i] + "!");
        }
    }

    // 理想長度的陣列:兩個長度都相等
    // String[] names = {"Edward", "David", "Allen", "Ben", "Cathy"};
    // int[] nums = {1, 2, 3, 4, 5};

    // String[] names = {"Edward", "David", "Allen"};
    // int[] nums = {1, 2, 3, 4, 5};

    // String[] names = {"Edward", "David", "Allen", "Ben", "Cathy"};
    // int[] nums = {1, 2, 3};
    // 如何印出以下結果:
    // Hello, Edward, 1!
    // Hello, David, 2!
    // Hello, Allen, 3!
    // Hello, empty, 4!
    // Hello, empty, 5!

    public static void printHello6(String[] names, int[] nums) {
        // 取得陣列的長度
        int namesLength = names.length;
        int numsLength = nums.length;

        // 如果names的長度比較大 就以names的長度為主
        if (namesLength > numsLength) {
            for (int i = 0; i < namesLength; i++) {
                // 預期印出, 但是因為nums的長度比較短, 所以系統會彈出例外的錯誤並且終止程式
                // System.out.println("Hello, " + names[i] + ", " + nums[i] + "!");

                // i 就要判斷超過nums的長度了嗎? 超過了就印出0, 沒有超過就預期印出
                if (i < numsLength) {
                    System.out.println("Hello, " + names[i] + ", " + nums[i] + "!");
                } else {
                    System.out.println("Hello, " + names[i] + ", 0!");
                }

            }
        } else { // 如果nums的長度比較大 就以nums的長度為主
            for (int i = 0; i < numsLength; i++) {
                // i 就要判斷超過names的長度了嗎? 超過了就印出empty, 沒有超過就預期印出
                if (i < namesLength) {
                    System.out.println("Hello, " + names[i] + ", " + nums[i] + "!");
                } else {
                    System.out.println("Hello, empty, " + nums[i] + "!");
                }
            }
        }

    }

    public static int add(int num1, int num2) {
        int sum = num1 + num2;
        return sum;
    }

    public static String stringAdd(String str1, String str2) {
        String strResult = str1 + str2;
        return strResult;
    }

}
