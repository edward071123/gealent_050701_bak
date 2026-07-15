package junior.exceptionInfo;

public class Atm {

    // 提款
    public void withdraw(int amount) {
        int balance = 100;
        if (amount <= 0) {
            // System.out.println("金額不能小於0");
            // return;
            throw new IllegalArgumentException("金額不能小於0");
        }

        if (amount > balance) {
            // System.out.println("餘額不足");
            // return;
            throw new ArithmeticException("餘額不足");
        }

        System.out.println("可以提領");
    }

    /*
     * 提款(throws 與 throw)
     * throws Exception 表示：
     * 這個方法可能發生例外，但不在方法內使用 try-catch 處理，
     * 而是把例外交給呼叫這個方法的人處理。
     */
    public void withdraw(int amount, boolean exceptions) throws Exception {
        int balance = 100;
        if (amount <= 0) {
            throw new Exception("進階2-金額不能小於0");
        }

        if (amount > balance) {
            throw new Exception("進階2-餘額不足");
        }

        System.out.println("進階2-可以提領");
    }

}
