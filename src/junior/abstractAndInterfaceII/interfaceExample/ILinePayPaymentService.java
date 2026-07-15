package junior.abstractAndInterfaceII.interfaceExample;

public class ILinePayPaymentService implements IPaymentService {
    @Override
    public void pay(int amount) {
        System.out.println("使用LinePay支付了 " + amount + " 元");
        System.out.println("請多多用LinePay支付會有其他優惠");
    }
}
