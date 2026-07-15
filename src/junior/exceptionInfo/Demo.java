package junior.exceptionInfo;

import java.util.Scanner;

public class Demo {
    public static void main(String[] args) {
        // 例外介紹
        // try catch finally
        System.out.println("========================基本=============================");
        Scanner sc4 = new Scanner(System.in);
        try {

            // 程式執行內容
            int a1 = 10;
            int b1 = 10; // 改0會出錯且跳到catch
            int result1 = a1 / b1;
            System.out.println(result1);

            int a2 = 10;
            int b2 = 2;
            int result2 = a2 * b2;
            System.out.println(result2);

            int[] a3 = { 1, 2, 3, 4, 5 };
            System.out.println(a3[10]); // 改a3[10]會出錯且跳到catch

            int guess = sc4.nextInt(); // 應該要輸入數字的 嘗試輸入"aaa"
            System.out.println("您輸入的數字為:" + guess);

        } catch (Exception e) {
            // 例外處理內容
            System.out.println("您輸入的內容有誤，請重新輸入");
            System.out.println(e.getMessage());
        } finally {
            sc4.close();
        }

        System.out.println("========================進階1=============================");

        // 呼叫範例
        try {
            Atm a1 = new Atm();
            a1.withdraw(-1); // 可以替換 200, 100, -1
        } catch (IllegalArgumentException e1) {
            System.out.println(e1.getMessage());
        } catch (ArithmeticException e2) {
            System.out.println(e2.getMessage());
        } catch (Exception e3) {
            System.out.println(e3.getMessage());
        }

        System.out.println("========================進階2=============================");

        // 呼叫範例
        try {
            Atm a2 = new Atm();
            // a2.withdraw(-1, true); // 可以替換 200, 100, -1
            // a2.withdraw(200, true);
            a2.withdraw(100, true);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }
}
