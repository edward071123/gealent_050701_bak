package junior.exceptionInfo.plus;

public class VipSavingAccount extends SavingAccount {
    
    public VipSavingAccount(int balance) {
        super(balance);
    }

    @Override
    public void withdraw(int money) throws Exception {

        if (money > 50000) {
            throw new Exception("VIP帳戶單次提款不可超過50000");
        }

        super.withdraw(money);
    }
}
