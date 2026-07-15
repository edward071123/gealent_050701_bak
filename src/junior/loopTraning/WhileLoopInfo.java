package junior.loopTraning;

import java.util.Random;
import java.util.Scanner;

public class WhileLoopInfo {
    public static void main(String[] args) {
        // 先實體化一個Scanner物件
        Scanner sc = new Scanner(System.in);

        System.out.println("請輸入一個數字");
        int getInputNum = sc.nextInt();
        System.out.println("您輸入的數字為:" + getInputNum);

        // 專門用來吃掉殘留的 Enter 鍵！
        sc.nextLine();

        System.out.println("請輸入一組文字");
        String getInputText = sc.nextLine();
        System.out.println("您輸入的文字為:" + getInputText);

        sc.close();

        // while(執行條件) {

        // }

        System.out.println("for 迴圈開始");
        for (int i = 0; i <= 10; i++) {
            System.out.println(i);
        }

        System.out.println("for 迴圈結束");
        System.out.println("while 迴圈開始");
        int z = 0;
        while (z <= 10) {
            System.out.println(z);
            z++;
        }
        System.out.println("while 迴圈結束");

        // // 設定結束的起始條件: true:真的結束, false:還沒結束
        boolean end = false;

        Scanner sc1 = new Scanner(System.in);
        // 總存款金額
        int totalMoney = 0;
        while (end == false) {
            System.out.println("請輸入要辦理項目,1:提領,2:存款,3:餘額查詢,4:結束");
            // 偵測輸入存到變數(getInputNum)裡
            int getInputNum1 = sc1.nextInt();

            if (getInputNum1 == 1) {
                // 防呆 總存款金額 要大於等於 提領金額
                if (totalMoney >= 1000) {
                    totalMoney -= 1000;
                    System.out.println("您已提領1000元成功");
                } else {
                    System.out.println("餘額不足，無法提領");
                }
            } else if (getInputNum1 == 2) {
                totalMoney += 1000;
                System.out.println("您已存款1000元成功");
            } else if (getInputNum1 == 3) {
                System.out.println("您的餘額為" + totalMoney + "元");
            } else if (getInputNum1 == 4) {
                System.out.println("謝謝您的使用");
                end = true;
            } else {
                System.out.println("您輸入的數字錯誤,請重新輸入");
            }

            System.out.println("這回合已經結束");
        }

        sc1.close();

        // 課堂練習1 用while 迴圈 讓使用者 輸入七個不重複的數 (1 ~ 100) 存到陣列 再用for 迴圈印出這七個數字
        Scanner sc2 = new Scanner(System.in);
        // while的起始條件
        int count = 0;

        int[] numbers = new int[7];

        while (count < 7) {
            System.out.println("請輸入第" + (count + 1) + "個數字");
            int getInputNum2 = sc2.nextInt();
            // 防呆 檢查一：檢查數字有沒有在 1 ~ 100 的範圍內
            if (getInputNum2 >= 1 && getInputNum2 <= 100) {
                // 檢查二 : 不能重複
                boolean isDuplicate = false; // false:是沒有重複的, true:有重複的
                for (int i = 0; i < count; i++) {
                    if (numbers[i] == getInputNum2) {
                        isDuplicate = true;
                        // 中斷for迴圈
                        break;
                    }
                }
                if (isDuplicate == false) {
                    // 判斷條件成功 才把數字存到陣列裡 並且讓count+1
                    System.out.println("您輸入的數字為:" + getInputNum2);
                    numbers[count] = getInputNum2;
                    count++;
                } else {
                    System.out.println("您輸入的數字已經存在,請重新輸入");
                }
            } else {
                System.out.println("您輸入的數字不在1 ~ 100 的範圍內,請重新輸入");
            }

        }

        for (int z2 = 0; z < numbers.length; z++) {
            System.out.print(numbers[z2] + ",");
        }
        sc2.close();

        // 課堂練習2
        // 情境： 用while寫一個程式模擬登入系統，只要使用者輸入的密碼錯誤，程式就會一直要求重新輸入，直到輸入正確為止
        // 請輸入密碼：
        // 1234
        // 密碼錯誤，請重新輸入：
        // abcd
        // 密碼錯誤，請重新輸入：
        // java123
        // 登入成功！歡迎系統。

        Scanner sc3 = new Scanner(System.in);
        String correctPassword = "java123";
        boolean isLogin1 = false; // false: 還沒登入成功(跑回圈), true: 已經登入成功(結束回圈)
        // int isLogin2 = 0; // 0: 還沒登入成功(跑回圈), 1: 已經登入成功(結束回圈)
        System.out.println("請輸入密碼:");
        while (isLogin1 == false) {
            String inputPassword = sc3.nextLine();
            // String的比較要用equals方法, 不能用==
            // int a == 10; // true
            if (inputPassword.equals(correctPassword)) {
                System.out.println("登入成功！歡迎系統。");
                isLogin1 = true;
            } else {
                System.out.println("密碼錯誤，請重新輸入：");
            }
        }
        sc3.close();

        // 課堂練習3
        // 終極密碼猜數字遊戲
        // 程式會隨機產生一個1 ~ 100的數
        // 玩家開始猜數字，如果猜錯了，程式要提示「太大了」或「太小了」，並縮小提示範圍，直到玩家猜中為止。
        // 預期輸出畫面：
        // 遊戲開始！請猜一個 1 ~ 100 的數字：50
        // 太小了！範圍是 50 ~ 100，請再猜一次：75
        // 太大了！範圍是 50 ~ 75，請再猜一次：60
        // 恭喜猜中！答案就是 60，你總共猜了 3 次。

        // tip:
        // Random rand = new Random();
        // int answer = rand.nextInt(100) + 1);
        // 用課堂練習2的方式改成猜數字遊戲
        // while裡面要判斷 輸入的數字大於答案 印出 「太大了」 並且把上限改成輸入的數字
        // 輸入的數字小於答案 印出 「太小了」 並且把下限改成輸入的數字
        // 輸入的數字等於答案 印出 「恭喜猜中！答案就是 60，你總共猜了 3 次。」 並且結束回圈

        // 答案是 60
        // 你猜 49 -> 太小了！範圍是 49 ~ 100，請再猜一次：
        // 你猜 75 -> 太大了！範圍是 49 ~ 75，請再猜一次：

        // // 掃描輸入的物件
        Scanner sc4 = new Scanner(System.in);
        // 產生隨機數字的物件
        Random rand = new Random();
        // 產生1 ~ 100的隨機數字
        int answer = rand.nextInt(100) + 1;
        // 猜的次數
        int guessCount = 0;
        // 猜的範圍下限
        int min = 1;
        // 猜的範圍上限
        int max = 100;
        boolean isGuessCorrect = false; // false: 還沒猜中(跑回圈), true: 已經猜中(結束回圈)
        System.out.println("遊戲開始！請猜一個 " + min + " ~ " + max + " 的數字：");

        while (isGuessCorrect == false) {
            if (!sc4.hasNextInt()) {
                System.out.print("⚠️ 錯誤：只能輸入整數喔！請再猜一次：");
                sc4.next(); // 重要：把剛剛輸入錯誤的「爛資料」從緩衝區清除掉
                continue;       // 重新進入迴圈，等待下一次輸入
            }
            int guess = sc4.nextInt();
            sc4.nextLine();
           
            // 不管猜大猜小或是猜成功，猜的次數+1
            guessCount++;
            
            if (guess <= min || guess >= max) {
                System.out.print("⚠️ 警告：請輸入範圍內的數字啦！請再猜一次：");
                continue;
            }

            if (guess > answer) { // 我猜的數字比電腦隨機產生的數值大
                max = guess; // 把上限改成輸入的數字
                System.out.println("太大了！範圍是 " + min + " ~ " + max + "，請再猜一次：");
            } else if (guess < answer) { // 我猜的數字比電腦隨機產生的數值小
                min = guess; // 把下限改成輸入的數字
                System.out.println("太小了！範圍是 " + min + " ~ " + max + "，請再猜一次：");
            } else { // 我猜的數字等於電腦隨機產生的數值
                System.out.println("恭喜猜中！答案就是 " + answer + "，你總共猜了 " + guessCount + " 次。");
                isGuessCorrect = true;
            }
        }
        sc4.close();
    }
}
