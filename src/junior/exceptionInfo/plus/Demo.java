package junior.exceptionInfo.plus;

import java.util.Scanner;

public class Demo {
    public static void main(String[] args) {

        // 情境1：提款 350
        // 情境2：提款 60000
        // 情境3：提款 40000
        
        Scanner sc = new Scanner(System.in);

        VipSavingAccount account =
                new VipSavingAccount(30000);

        try {

            System.out.print("請輸入提款金額：");

            int money = sc.nextInt();

            account.withdraw(money);

            System.out.println("提款成功");

            account.showBalance();

        } catch (Exception e) {

            System.out.println("提款失敗：" + e.getMessage());

        }

        sc.close();
    }
}
