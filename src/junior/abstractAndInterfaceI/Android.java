package junior.abstractAndInterfaceI;

public class Android extends Electronics implements IChargeable{

    public Android(String model) {
        super(model);
    }

    @Override
    public void display() {
        System.out.println(getModel() + "畫面大小為: 6.7 吋");
    }

    @Override
    public void charge() {
        System.out.println(getModel() + " 手機正在通過 USB-C 充電...");
    }
}
