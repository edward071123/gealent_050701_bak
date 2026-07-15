package junior.polymorphism.homeworkII;

public class Coffee extends Drink {
    private boolean largeSize;

    public Coffee(int sugarLevel, int iceLevel, boolean largeSize) {
        super("咖啡", 55, sugarLevel, iceLevel);
        setLargeSize(largeSize);
    }

    public boolean getLargeSize() {
        return this.largeSize;
    }

    public void setLargeSize(boolean largeSize) {
        this.largeSize = largeSize;
    }

    @Override
    public double calculatePrice() {
        // 大杯加15塊
        if (getLargeSize()) {
            return getPrice() + 15;
        }

        return getPrice();
    }

}
