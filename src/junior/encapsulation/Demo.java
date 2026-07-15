package junior.encapsulation;

public class Demo {
    public static void main(String[] args) {
        // 封裝基本介紹 用atm1來示範封裝的概念跟 不安全的存取方式
        AutomatedTellerMachine atm1 = new AutomatedTellerMachine();

        // 不一定透過 getter 和 setter 方法來存取和修改資料
        // 但是絕對不能直接存取和修改資料，因為資料已經被封裝了
        // 可以自定義方法 讀取或寫入屬性的值，來達到存取和修改資料的目的

        atm1.setAccount("123");
        atm1.setPassword("password123");
        atm1.setBalance(1000.0);

        System.out.println("Account: " + atm1.getAccount());
        System.out.println("Password: " + atm1.getPassword());
        System.out.println("Balance: " + atm1.getBalance());

        // 以下範例為直接存取屬性 會有危險 資料沒被保護

        // atm1.account = "123456789";
        // atm1.password = "password123";
        // atm1.balance = 1000.0;
        // System.out.println("Account: " + atm1.account);
        // System.out.println("Password: " + atm1.password);
        // System.out.println("Balance: " + atm1.balance);

        // atm1.balance = 10000.0;
        // System.out.println("Updated Balance: " + atm1.balance);
    }
}
