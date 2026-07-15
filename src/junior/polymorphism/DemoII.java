package junior.polymorphism;

public class DemoII {
    public static void main(String[] args) {
        Delivery[] deliveries = {
                new FoodPanda(),
                new UberEats(),
                new Delivery(),
        };

        for (int i = 0; i < deliveries.length; i++) {
            deliveries[i].deliver();
        }
    }
}

class Delivery {
    public void deliver() {
        System.out.println("開始配送");
    }
}

class FoodPanda extends Delivery {
    @Override
    public void deliver() {
        super.deliver();
        System.out.println("FoodPanda 配送中");
    }
}

class UberEats extends Delivery {
    @Override
    public void deliver() {
        System.out.println("UberEats 配送中");
        super.deliver();
    }
}
