package junior.abstractInfo;

public class SmsNotification extends Notification {

    public SmsNotification(String recipint) {
        super(recipint);
    }

    @Override // 實作抽象方法
    public void send() {
        // 實作Sms 串接 可能串三竹簡訊
        System.out.println("寄送Sms通知給: " + getRecipint());
    }

}
