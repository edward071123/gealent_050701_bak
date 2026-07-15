package junior.abstractInfo;

public class Demo {
    public static void main(String[] args) {
        // 抽象: abstract 介紹
        // 想像是書的目錄
        // 也有屬性 跟 方法 但是方法需要被實作
        // 方便專案維護, 制定抽象類別跟抽象方法的名稱, 讓大家可以去直接去實做此方法不用再另外取方法名稱
        //
        // 舉例1: 我先制定一個功能是通知: 所以分別實作出 sms通知 跟 email通知, APP推播通知....
        //
        // 舉例2: 我先制定一個功能是付款: 所以分別實作出 信用卡付款 跟 虛擬帳戶轉帳付款, 現金付款....
        //
        // 舉例3: 我先制定一個功能是煮魚: 所以分別實作出 生吃 跟 煎, 烤, 炸, 清蒸......

        Notification email = new EmailNotification("aa@gmail.com");
        email.send();
        email.log();

        Notification sms = new SmsNotification("0911222333");
        sms.send();
        sms.log();

        // 課後練習:
        // 改寫junior.polymorphism.employee 為 抽象類別
        // 裡面的calculateSalary() 也改成抽象方法
        // 要有月薪員工 , 銷售員, 工讀生 的計算薪水方式
    }
}
