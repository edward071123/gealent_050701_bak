package junior.interfaceInfo;

// 一般類別引用介面(interface)
public class Dragon implements IFlyable, IFireable {

    public void line() {
        System.out.println("龍有直線飛行的技能");
    }

    public void circle() {
        System.out.println("龍有順時鐘盤旋飛行的技能");
    }

    public void fire() {
        System.out.println("龍有噴火溫度2000度的技能");
    }

}
