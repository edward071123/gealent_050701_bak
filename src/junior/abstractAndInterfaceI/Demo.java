package junior.abstractAndInterfaceI;

public class Demo {
    public static void main(String[] args) {
        Iphone i14 = new Iphone("iphone14");
        i14.powerOn();
        i14.charge();
        i14.display();

        Android pixel10pro = new Android("pixel 10 pro");
        pixel10pro.powerOn();
        pixel10pro.charge();
        pixel10pro.display();
    }
}
