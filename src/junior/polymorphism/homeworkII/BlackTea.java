package junior.polymorphism.homeworkII;

public class BlackTea extends Drink {
    public BlackTea(int sugarLevel, int iceLevel) {
        super("紅茶", 30, sugarLevel, iceLevel);
    }

    @Override
    public double calculatePrice() {
        return getPrice();
    }
}
