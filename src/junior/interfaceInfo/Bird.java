package junior.interfaceInfo;

public class Bird implements IFlyable, IFireable {

    public void line() {
        System.out.println("鳥有直線飛行的技能");
    }

    public void circle() {
        System.out.println("鳥有順時鐘盤旋飛行的技能");
        System.out.println("鳥有逆時鐘盤旋飛行的技能");
    }

    public void fire() {
        System.out.println("鳥有噴火溫度約80度的技能");
    }

}
