package junior.abstractAndInterfaceII.abstractExample;

public class ALinePayPaymentService extends APaymentService {

    public ALinePayPaymentService() {
        super("Line Pay", 0.01);
    }

    @Override
    public void processPayment(int amount, int fee) {
        System.out.println("Line Pay 帳戶扣款成功，總扣款：" + (amount + fee) + " 元");
    }
}
