package junior.abstractAndInterfaceII;

import junior.abstractAndInterfaceII.abstractExample.*;
import junior.abstractAndInterfaceII.interfaceExample.*;

public class Demo {
    public static void main(String[] args) {
        // 耦合（Coupling） 指的是：
        // 一個類別對另一個類別的依賴程度。

        // 抽象類別適合處理：
        // 1. 共用欄位：付款名稱、手續費率
        // 2. 共用付款流程：驗證 -> 計算手續費 -> 執行付款 -> 顯示結果
        // 3. 共用方法實作：驗證金額、計算手續費、顯示結果
        // 4. 子類別只實作自己的 processPayment()

         /*
         * 課後練習: 請練習此範例至少三次
         * 1. 先建立抽象類別:APayment
         * 兩個屬性: paymaneName(付款名稱), feeRate(手續費率)
         * 
         * 建構子: public APayment(String paymaneName, double feeRate)
         * 
         * 共同方法: public void pay(int amount)
         * 
         * 抽象方法: public abstract void processPayment(int amount, int fee)
         *
         * 私有方法: private String validateAmount(int amount)
         * private int calculateFee(int amount)
         * private void printPaymentResult(int amount, double fee)
         * 
         * 2. 建立付款方式的一般類別且都要繼承APayment:
         * 信用卡: ACreditCard
         * LinePay: ALinePay
         * 
         * 需要建構子: 記得內容也要用super
         * 
         * 需要實做出繼承的抽象方法: public void processPayment(int amount, int fee)
         *
         * 3. 建立一般類別AOrder
         * 建構子: 付款的物件傳入(物件注入)
         * 
         * 建立公開發方法: createOrderLinePay(int amount)
         * createOrderCreditCard(int amount)
         * 
         * 公開方法的內容為: 印出"建立訂單(付款名稱)"
         * 呼叫付款物件的pay方法
         * 
         * 4. 在程式進入點內 實體化AOrder 類別且呼叫 createOrderLinePay or createOrderCreditCard 方法
         * 
         */

        // 範例互動網站:
        // https://edward071123.github.io/gealent_050701/src/junior/abstractAndInterface2/object-call-visualizer.html

        // 如果上面都練習後已經清楚整個流程的呼叫可多練習以下問題
        // 在interfaceExample內修改 ICreditCard & ILinePay的內容
        // 增加各自的驗證跟手續費計算方法
        // 最後印出明細的內容
        // 在pay()內呼叫

        System.out.println("===============抽象類別使用信用卡付款=================");
        AOrderService creditCardOrderService =
                new AOrderService(new ACreditCardPaymentService());
        creditCardOrderService.createOrder(-1);

        System.out.println("===============抽象類別使用LinePay付款=================");
        AOrderService linePayOrderService =
                new AOrderService(new ALinePayPaymentService());
        linePayOrderService.createOrder(1000);

        // 介面
        // 只要實做出自己的功能就好
        System.out.println("===============介面使用LinePay付款=================");
        IOrderService interfaceLinePayOrderService =
                new IOrderService(new ILinePayPaymentService());
        interfaceLinePayOrderService.createOrder(1000);

        System.out.println("===============介面使用信用卡付款=================");
        IOrderService interfaceCreditCardOrderService =
                new IOrderService(new ICreditCardPaymentService());
        interfaceCreditCardOrderService.createOrder(1000);
    }
}
