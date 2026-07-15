package junior.encapsulationPlus;

import java.util.Scanner;

public class AutomatedTellerMachine {
    private String account;
    private String password;
    private double balance;

    public AutomatedTellerMachine(String account, String password, double balance) {
        setAccount(account);
        setPassword(password);
        setBalance(balance);
    }

    public String getAccount() {
        return this.account;
    }

    public void setAccount(String account) {
        if (account.length() > 0) {
            this.account = account;
        } else {
            System.out.println("帳號不能為空");
        }
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        if (password.length() > 0) {
            this.password = password;
        } else {
            System.out.println("密碼不能為空");
        }
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        if (balance >= 0) {
            this.balance = balance;
        } else {
            System.out.println("餘額不能為負數");
        }
    }

    public void transaction() {
        // 交易邏輯
        // 輸入帳號密碼
        // 驗證帳號密碼 輸入三次就鎖帳號
        // while loop 讓使用者循環選擇交易項目
        // 1:提領,2:存款,3:餘額查詢,4:結束
        // 提領:防呆 總存款 金額 要大於等於 提領金額
        // 存款:防呆 存款金額要大於0

        Scanner sc = new Scanner(System.in);
        // 錯誤的輸入次數
        int errorTimes = 0;
        // 容許輸入三次錯誤的帳號密碼 所以用for loop
        for (int i = 1; i <= 3; i++) {
            System.out.println("請輸入帳號:");
            String inputAccount = sc.nextLine();
            System.out.println("請輸入密碼:");
            String inputPassword = sc.nextLine();
            // 驗證帳號密碼
            if (inputAccount.equals(getAccount()) && inputPassword.equals(getPassword())) {
                System.out.println("登入成功");
                // 比對成功 就應該中斷此迴圈 進入交易流程
                break;
            } else {
                System.out.println("帳號或密碼錯誤");
                errorTimes++;
            }
        }

        // 檢查輸入錯誤次數是否超過三次 如果超過三次 就鎖帳號
        if (errorTimes >= 3) {
            System.out.println("帳號密碼輸入錯誤已超過三次,帳號已鎖定,請洽銀行");
        } else {
            // 開始交易流程
            // 設定結束的起始條件: true:真的結束, false:還沒結束
            boolean end = false;
            while (end == false) {
                System.out.println("請輸入要辦理項目,1:提領,2:存款,3:餘額查詢,4:結束");
                // 偵測輸入存到變數(getInputNum)裡
                int getInputNum1 = sc.nextInt();

                if (getInputNum1 == 1) {
                    System.out.println("請輸入提領金額:");
                    int inputMoney = sc.nextInt();
                    // 取得目前存款金額
                    double currentBalance = getBalance();
                    // 防呆 總存款金額 要大於等於 提領金額
                    if (currentBalance >= inputMoney) {
                        // 更新餘額
                        double newBalance = currentBalance - inputMoney;
                        setBalance(newBalance);
                        System.out.println("您已提領" + inputMoney + "元成功");
                    } else {
                        System.out.println("餘額不足，無法提領");
                    }

                } else if (getInputNum1 == 2) {
                    System.out.println("請輸入存款金額:");
                    int inputMoney = sc.nextInt();
                    // 取得目前存款金額
                    double currentBalance = getBalance();
                    // 更新餘額
                    double newBalance = currentBalance + inputMoney;
                    setBalance(newBalance);
                    System.out.println("您已存入" + inputMoney + "元成功");
                } else if (getInputNum1 == 3) {
                    System.out.println("您的餘額為" + getBalance() + "元");
                } else if (getInputNum1 == 4) {
                    System.out.println("謝謝您的使用");
                    end = true;
                } else {
                    System.out.println("您輸入的數字錯誤,請重新輸入");
                }

                System.out.println("這回合已經結束");
            }

        }

        sc.close();
    }
}
