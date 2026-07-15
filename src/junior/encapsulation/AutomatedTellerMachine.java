package junior.encapsulation;

public class AutomatedTellerMachine {
    // public(公開的) 變成 private(私有的)，封裝資料
    private String account;
    private String password;
    private double balance;
    private String name;

    public String getName() {
        return name;
    }

    // 自定義方法 讀取或寫入屬性的值，來達到存取和修改資料的目的
    // 不一定要寫setter方法 也可以寫其他方法 來達到存取和修改資料的目的
    // 裡面會多一些防呆的邏輯 來保護資料的安全性 才可以寫入資料
    public void saveName(String name) {
        if (name.length() > 0) {
            if (name.equals("admin")) {
                System.out.println("名字不能為 admin");
            } else {
                this.name = name;
            }

        } else {
            System.out.println("名字不能為空");
        }
    }

    // getter(讀取) 和 setter(寫入) 方法，提供外界存取和修改資料的方式(窗口)

    // 讀取帳號的窗口(查詢帳戶資訊)
    public String getAccount() {
        // 可以寫驗證手續，例如驗證密碼是否正確，或者驗證使用者的身分等等...
        return this.account;
    }

    // 修改帳號的窗口(辦理開戶)
    public void setAccount(String account) {
        this.account = account;

        // if(大於20歲才能開戶) {
        // this.account = account;
        // } else {
        // System.out.println("未滿20歲，無法開戶");
        // }

    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public double getBalance() {
        return this.balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
}
