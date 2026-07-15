package junior.abstractAndInterfaceII.abstractExample;

public class ACreditCardPaymentService extends APaymentService {
    
    public ACreditCardPaymentService() {
        super("信用卡", 0.03);
    }

    @Override
    public void processPayment(int amount, int fee) {
        System.out.println("信用卡授權成功，總扣款：" + (amount + fee) + " 元");
    }
}
