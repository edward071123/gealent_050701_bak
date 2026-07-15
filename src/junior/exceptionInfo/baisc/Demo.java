package junior.exceptionInfo.baisc;

import java.util.List;

public class Demo {
    public static void main(String[] args) {
        /*
         * try-catch 與 throws Exception 的差別：
         *
         * 1. try-catch
         *    在發生例外的方法裡直接處理。
         *    AtmService.withdraw() 就是這種寫法。
         *
         * 2. throws Exception
         *    方法只宣告可能發生例外，不在方法裡處理。
         *    呼叫端必須使用 try-catch，或繼續使用 throws 往外傳。
         *
         * 3. throw 與 throws
         *    throw：真的拋出一個例外物件。
         *    throws：寫在方法後面，宣告方法可能發生例外。
         */

        System.out.println("======== 方法內使用 try-catch ========");
        runExample("正常提款", "300");
        runExample("輸入非數字", "三百");
        runExample("輸入錯誤金額", "-100");
        runExample("提款金額超過餘額", "1500");

        System.out.println("======== 方法使用 throws Exception ========");
        runThrowsExample("正常提款", "-1");
        runThrowsExample("提款金額超過餘額", "1500");
    }

    private static void runExample(String title, String amountText) {
        System.out.println("================ " + title + " ================");

        AtmService atm = new AtmService(1000);
        List<String> messages = atm.withdraw(amountText);

        for (String message : messages) {
            System.out.println(message);
        }
    }

    private static void runThrowsExample(String title, String amountText) {
        System.out.println("================ " + title + " ================");
        AtmService atm = new AtmService(1000);

        /*
         * withdrawWithThrows() 沒有在方法內處理例外，
         * 所以呼叫端必須使用 try-catch 接住例外。
         */
        try {
            String message = atm.withdrawWithThrows(amountText);
            System.out.println(message);
        } catch (Exception exception) {
            System.out.println("呼叫端收到例外：" + exception.getMessage());
        }
    }
}
