package junior.abstractInfo;

// 抽象類別
public abstract class Notification {
    // 一樣有屬性
    private String recipint;

    // 一樣有建構子
    public Notification(String recipint) {
        setRecipint(recipint);
    }

    // 抽象方法: 子類別繼承必須實作的方法, 比喻為書的目錄
    // 無大括號{}
    public abstract void send();

    // 一樣可以有一般的方法
    public void log() {
        System.out.println("已發出通知給:" + getRecipint());
    }

    public void setRecipint(String recipint) {
        this.recipint = recipint;
    }

    public String getRecipint() {
        return this.recipint;
    }

}
