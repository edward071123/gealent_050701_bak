package junior.polymorphism.homeworkII;

public class Drink {
    private String name;
    private double price;
    private int sugarLevel;
    private int iceLevel;

    public Drink(String name, int price, int sugarLevel, int iceLevel) {
        setName(name);
        setPrice(price);
        setSugarLevel(sugarLevel);
        setIceLevel(iceLevel);
    }

    public String getName() {
        return this.name;
    }

    public double getPrice() {
        return this.price;
    }

    public String getSugarLevel() {
        // 0:無糖, 1:半糖, 2:全糖
        if (this.sugarLevel == 1) {
            return "半糖";
        }

        if (this.sugarLevel == 2) {
            return "全糖";
        }

        return "無糖";
    }

    public String getIceLevel() {
        // 0:去冰, 1:微冰, 2:正常冰
        if (this.iceLevel == 1) {
            return "微冰";
        }

        if (this.iceLevel == 2) {
            return "正常冰";
        }

        return "去冰";
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setSugarLevel(int sugarLevel) {
        this.sugarLevel = sugarLevel;
    }

    public void setIceLevel(int iceLevel) {
        this.iceLevel = iceLevel;
    }

    public double calculatePrice() {
        return this.price;
    }
}
