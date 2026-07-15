package junior.abstractAndInterfaceII.interfaceExample;

public class IOrderService {
    private final IPaymentService paymentService;

    public IOrderService(IPaymentService paymentService) {
        this.paymentService = paymentService;
    }

    public void createOrder(int amount) {
        System.out.println("建立訂單");
        paymentService.pay(amount);
    }
}
