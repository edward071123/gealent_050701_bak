package junior.abstractAndInterfaceII.interfaceExample;

public class ICreditCardPaymentService implements IPaymentService {
    @Override
    public void pay(int amount) {
        System.out.println("使用信用卡支付了 " + amount + " 元");
        System.out.println("有任何問題可以來電聯繫客服");
    }
    
}
