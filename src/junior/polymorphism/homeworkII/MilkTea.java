package junior.polymorphism.homeworkII;

public class MilkTea extends Drink {
    private boolean addPearl;

    public MilkTea(int sugarLevel, int iceLevel, boolean addPearl) {
        super("奶茶", 45, sugarLevel, iceLevel);
        setAddPearl(addPearl);
    }

    public void setAddPearl(boolean addPearl) {
        this.addPearl = addPearl;
    }

    public boolean getAddPearl() {
        return this.addPearl;
    }

    @Override
    public double calculatePrice() {
        // 有加珍珠多10塊
        if (getAddPearl()) {
            return getPrice() + 10;
        }

        return getPrice();
    }
}
