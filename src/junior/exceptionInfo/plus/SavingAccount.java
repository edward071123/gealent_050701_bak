package junior.exceptionInfo.plus;

public class SavingAccount extends BankAccount {
    
    public SavingAccount(int balance) {
        super(balance);
    }

    @Override
    public void withdraw(int money) throws Exception {

        if (money % 100 != 0) {
            throw new Exception("提款金額必須為100的倍數");
        }

        super.withdraw(money);
    }
}
