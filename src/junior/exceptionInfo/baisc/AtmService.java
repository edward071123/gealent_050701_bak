package junior.exceptionInfo.baisc;

import java.util.ArrayList;
import java.util.List;

public class AtmService {
    private int balance;

    public AtmService(int balance) {
        this.balance = balance;
    }

    public List<String> withdraw(String amountText) {
        List<String> messages = new ArrayList<>();

        try {
            // 可能發生 NumberFormatException
            int amount = Integer.parseInt(amountText);

            if (amount <= 0) {
                throw new IllegalArgumentException("提款金額必須大於 0");
            }

            if (amount > balance) {
                throw new ArithmeticException("餘額不足");
            }

            balance -= amount;
            messages.add("提款成功：" + amount + " 元");
            messages.add("剩餘餘額：" + balance + " 元");
        } catch (NumberFormatException exception) {
            messages.add("輸入錯誤：提款金額必須是數字");
        } catch (IllegalArgumentException exception) {
            messages.add("金額錯誤：" + exception.getMessage());
        } catch (ArithmeticException exception) {
            messages.add("提款失敗：" + exception.getMessage());
            messages.add("目前餘額：" + balance + " 元");
        } finally {
            // 不論成功或發生例外，finally 都會執行。
            messages.add("交易結束");
        }

        return messages;
    }

    /*
     * throws Exception 表示：
     * 這個方法可能發生例外，但不在方法內使用 try-catch 處理，
     * 而是把例外交給呼叫這個方法的人處理。
     */
    public String withdrawWithThrows(String amountText) throws Exception {
        int amount = Integer.parseInt(amountText);

        if (amount <= 0) {
            // throw 是真的建立並拋出一個例外。
            throw new Exception("提款金額必須大於0");
        }

        if (amount > balance) {
            throw new Exception("餘額不足");
        }

        balance -= amount;
        return "提款成功：" + amount + " 元，剩餘餘額：" + balance + " 元";
    }
}
