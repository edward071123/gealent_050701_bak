package junior.abstractAndInterfaceII.abstractExample;

public class AOrderService {
    private final APaymentService paymentService;

    public AOrderService(APaymentService paymentService) {
        this.paymentService = paymentService;
    }

    public void createOrder(int amount) {
        System.out.println("建立訂單");
        paymentService.pay(amount);
    }
}
