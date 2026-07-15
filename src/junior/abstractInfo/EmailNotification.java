package junior.abstractInfo;

public class EmailNotification extends Notification {

    public EmailNotification(String recipint) {
        super(recipint);
    }

    // 一定要實作出來抽象方法
    @Override
    public void send() {
        // 實作Email 串接 可能串gmail
        System.out.println("寄送Email通知給: " + getRecipint());
    }
}
