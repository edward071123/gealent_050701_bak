package junior.abstractAndInterfaceI;

public class Iphone extends Electronics implements IChargeable {
    public Iphone(String model) {
        super(model);
    }

    @Override
    public void display() {
        System.out.println(getModel() + "畫面大小為: 6.1 吋");
    }

    @Override
    public void charge() {
        System.out.println(getModel() + " 手機正在通過 Lightning 充電...");
    }
}
