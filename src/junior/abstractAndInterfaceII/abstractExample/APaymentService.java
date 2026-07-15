package junior.abstractAndInterfaceII.abstractExample;

public abstract class APaymentService {
    // 共用欄位: 所有付款方式都有名稱與手續費率。
    // 屬性用上final關鍵字: 只能設定一次，通常在宣告時或建構子中賦值。
    private final String paymentName;
    private final double feeRate;

    protected APaymentService(String paymentName, double feeRate) {
        this.paymentName = paymentName;
        this.feeRate = feeRate;
    }

    // 共用付款流程（Template Method），final 避免子類別改變執行順序。
    public final void pay(int amount) {
        // 驗證後會回傳錯誤訊息, 若是空的表示沒錯誤
        String validationMessage = validateAmount(amount);
        // 驗證失敗, 有錯誤訊息直接顯示
        if (!validationMessage.isEmpty()) {
            System.out.println(validationMessage);
            System.out.println(paymentName + "付款失敗");
            return;
        }

        // 手續費計算
        int fee = calculateFee(amount);
        System.out.println("開始使用" + paymentName + "付款");
        processPayment(amount, fee);
        printPaymentResult(amount, fee);
    }

    // protected 抽象方法：保留給子類別實作不同付款邏輯。
    protected abstract void processPayment(int amount, int fee);

    // 共用方法實作：子類別不需要重複驗證與計算手續費。

    private String validateAmount(int amount) {
        if (amount <= 0) {
            return "付款金額必須大於0";
        } else {
            return "";
        }
    }

    // 計算手續費
    private int calculateFee(int amount) {
        return (int) Math.round(amount * feeRate);
    }

    private void printPaymentResult(int amount, int fee) {
        System.out.println("付款金額：" + amount + " 元");
        System.out.println("手續費：" + fee + " 元");
        System.out.println(paymentName + "付款完成");
    }
}
