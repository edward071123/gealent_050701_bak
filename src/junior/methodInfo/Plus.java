package junior.methodInfo;

import java.util.Arrays;

public class Plus {
    public static void main(String[] args) {
        // 做一個印出金字塔的方法 我要可以輸入金字塔的層數
        // printPyramid(5);

        // 羅馬數字轉阿拉伯數字的方法 回傳型態為int
        // int result = romanToInt("MCMXCIV");
        // System.out.println("羅馬數字 MCMXCIV 轉換為阿拉伯數字為: " + result);

        // findMax 方法 輸入一個整數的陣列 回傳陣列內的最大值
        // findMax(int[] numbers) 回傳型態為int
        // int[] numbers = { 1, 5, 3, 9, 2, 12, 15, 99, 4 };
        // int max = findMax(numbers);
        // System.out.println("陣列內的最大值為: " + max);

        // 課程練習1 寫一個方法 叫做 calScore 輸入一個int的陣列 scores
        // 回傳一個int陣列 result 內容為 {平均分數, 總分數, 最高分數, 最低分數}
        // int[] scores = { 80, 90, 70, 60, 85 };
        int[] result = calScore(new int[] { 80, 90, 70, 60, 85 });
        System.out.println("平均分數: " + result[0]);
        System.out.println("總分數: " + result[1]);
        System.out.println("最高分數: " + result[2]);
        System.out.println("最低分數: " + result[3]);

        // 課程練習2 寫一個方法 叫做 reverseArray 輸入一個int的陣列 arrs
        // 回傳一個int陣列 result 內容為 arrs的反轉
        // int[] arrs = { 1, 2, 3, 4, 5 };
        // int[] reverseArrs = reverseArray(arrs);
        // System.out.println("反轉後的陣列為: " + Arrays.toString(reverseArrs));
        // { 5, 4, 3, 2, 1 }
        // tip: 先在方法內新增一個新的陣列 result 來存放反轉後的結果
        // int[] result = new int[arrs.length];
        int[] arrs = { 1, 2, 3, 4, 5 };
        int[] reverseArrs = reverseArray(arrs);
        // Arrays.toString() 是一個可以把陣列轉成字串的工具方法
        System.out.println("反轉後的陣列為: " + Arrays.toString(reverseArrs));

        // 課程練習3 檢查是否有重複元素
        // 寫一個方法 containsDuplicate 輸入一個int的陣列 nums
        // 回傳一個boolean 如果陣列內有重複元素回傳true 沒有重複元素回傳false
        // int[] nums = { 1, 2, 3, 4, 5, 1 }; => true
        // int[] nums = { 1, 2, 3, 4, 5 }; => false
        int[] nums = { 1, 2, 3, 4, 5, 1 };
        int[] nums1 = { 1, 2, 3, 4, 5 };
        System.out.println("檢查元素是否重複的陣列(nums)結果: " + containsDuplicate(nums));
        System.out.println("檢查元素是否重複的陣列(nums1)結果: " + containsDuplicate(nums1));
    }

    public static void printPyramid(int level) {
        for (int i = 1; i <= level; i++) {
            // 排除偶數
            // (i % 2) => 偶數會得到餘數為0, 奇數會得到餘數為1
            if (i % 2 == 1) {
                System.out.print("第" + i + "層為奇數");

                // 印出空白
                int space = (level - i) / 2;
                for (int s = 1; s <= space; s++) {
                    System.out.print("_");
                }

                // 第i天 每天只能玩i次 (第i層 只能印出i次星星)
                for (int k = 1; k <= i; k++) {
                    System.out.print("*");
                }
                // 換行
                System.out.println("");
            }
        }
    }

    public static int romanToInt(String input) {
        int result = 0;
        int lastValue = 0;
        // 切割 "MCMXCIV" 變成 ['M', 'C', 'M', 'X', 'C', 'I', 'V']
        for (int i = input.length() - 1; i >= 0; i--) {
            // System.out.print(input.charAt(i));
            // charAt(i) 取得單一字元
            char inputChar = input.charAt(i);
            // 呼叫translate方法 輸入單一字元 回傳對應的數值
            int getValue = translate(inputChar);
            if (lastValue > getValue) {
                result -= getValue;
            } else {
                result += getValue;
            }

            lastValue = getValue;
        }
        return result;
    }

    public static int translate(char c) {
        int getValue = 0;
        switch (c) {
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
        return getValue;
    }

    public static int findMax(int[] numbers) {
        int max = 0;
        for (int i = 0; i < numbers.length; i++) {
            if (numbers[i] > max) {
                max = numbers[i];
            }
        }
        // 回傳最大值
        return max;
    }

    // int[] scores = { 80, 90, 70, 60, 85 };
    public static int[] calScore(int[] scores) {
        int reslt[] = new int[4];
        int max = scores[0];
        int min = scores[0];
        int sum = 0;
        int avg = 0;
        for (int i = 0; i < scores.length; i++) {
            sum = sum + scores[i];
            // sum += scores[i]; // sum = sum + scores[i] 的簡寫

            if (scores[i] > max) {
                max = scores[i];
            }

            if (scores[i] < min) {
                min = scores[i];
            }
        }
        // 平均分數 = 總分數 / 分數的個數
        avg = sum / scores.length;

        // 檢查用
        // System.out.println("陣列內的最大值為: " + max);
        // System.out.println("陣列內的最小值為: " + min);
        // System.out.println("陣列內的總分為: " + sum);
        // System.out.println("陣列內的平均分數為: " + avg);

        // {平均分數, 總分數, 最高分數, 最低分數}
        reslt[0] = avg;
        reslt[1] = sum;
        reslt[2] = max;
        reslt[3] = min;
        // 回傳陣列
        return reslt;
    }

    // int[] arrs = { 1, 2, 3, 4, 5 };
    // int[] result = { 5, 4, 3, 2, 1 };
    public static int[] reverseArray(int[] arrs) {
        int[] result = new int[arrs.length];
        for (int i = 0; i < arrs.length; i++) {
            // 推理過程
            // result[0] = arrs[arrs.length - 1];
            // result[1] = arrs[arrs.length - 2];
            // result[2] = arrs[arrs.length - 3];
            // result[3] = arrs[arrs.length - 4];
            // .
            // .
            // .
            // result[n] = arrs[arrs.length - (n + 1)];
            result[i] = arrs[arrs.length - (i + 1)];
        }

        return result;
    }

    // int[] nums = { 1, 2, 3, 4, 5, 1 };
    // i = 0 => nums[0] = 1 .. 外迴圈

    // j = 1 => nums[1] = 2 .. 內迴圈
    // j = 2 => nums[2] = 3 .. 內迴圈
    // j = 3 => nums[3] = 4 .. 內迴圈
    // j = 4 => nums[4] = 5 .. 內迴圈
    // j = 5 => nums[5] = 1 .. 內迴圈
    // =========================================
    // i = 1 => nums[1] = 2 .. 外迴圈

    // j = 2 => nums[2] = 3 .. 內迴圈
    // j = 3 => nums[3] = 4 .. 內迴圈
    // j = 4 => nums[4] = 5 .. 內迴圈
    // j = 5 => nums[5] = 1 .. 內迴圈
    public static boolean containsDuplicate(int[] nums) {
        // false => 沒有重複元素
        // true => 有重複元素
        boolean hasDuplicate = false;
        for (int i = 0; i < nums.length; i++) {
            for (int j = i + 1; j < nums.length; j++) {
                // 跟下一個元素去比對
                if (nums[i] == nums[j]) {
                    hasDuplicate = true;
                    break;
                }
            }
        }
        return hasDuplicate;
    }
}
