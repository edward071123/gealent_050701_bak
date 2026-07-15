package junior.exceptionInfo.plus;

public class BankAccount {
    
    protected int balance;

    public BankAccount(int balance) {
        this.balance = balance;
    }

    public void withdraw(int money) throws Exception {

        if (money > balance) {
            throw new Exception("餘額不足");
        }

        balance -= money;
    }

    public void showBalance() {
        System.out.println("目前餘額：" + balance);
    }
}
