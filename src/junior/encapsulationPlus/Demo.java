package junior.encapsulationPlus;

public class Demo {
    public static void main(String[] args) {
        // AutomatedTellerMachine atm1 = new AutomatedTellerMachine("a001", "123",
        // 1000.0);
        // System.out.println("Account: " + atm1.getAccount());
        // System.out.println("Password: " + atm1.getPassword());
        // System.out.println("Balance: " + atm1.getBalance());

        // atm1.transaction();

        // 封裝的課後練習
        // 1. 冷氣機的控制 class: AirConditioner
        // 屬性: temperature(int)
        // 設定溫度防呆: 溫度要在16-30度之間
        // 要有設定溫度的方法, 還要取得溫度的方法
        // 2. Netflix的會員系統 class: NetflixMember
        // 屬性: name(String), vip(boolean)
        // 初始化就設定好會員名稱name, vip是false
        // 方法是升級會員 upgradeToVip()
        // watchMovie() 方法裡面要判斷如果是vip會員就可以看電影, 不是vip會員就不能看電影, 要升級會員才能看電影

        // AirConditioner 測試
        AirConditioner ac = new AirConditioner();
        System.out.println("目前溫度：" + ac.getTemperature());
        ac.setTemperature(25);
        System.out.println("目前溫度：" + ac.getTemperature());
        // 設定超出範圍的溫度
        ac.setTemperature(35);

        // NetflixMember 測試
        NetflixMember member = new NetflixMember("小明");
        member.watchMovie(); // 不能看
        member.upgradeToVip();
        member.watchMovie(); // 可以看
    }
}
