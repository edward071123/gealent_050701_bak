package junior.polymorphism.homeworkI;

public class Demo {
    public static void main(String[] args) {
        // 多型課後練習:
        // 父類別: Vehicle(車輛)
        // 有一個方法 move()
        // 以下子類別請覆寫 父類別的move()
        // 子類別1: Car(汽車) , 覆寫印出: 汽車在道路行駛
        // 子類別2: Bike(腳踏車) , 覆寫印出: 腳踏車在自行車道騎乘
        // 子類別3: Bus(公車) , 覆寫印出: 公車載客前行
        // 請用多型 + array + for 印出20個車輛的 行駛方式
        Vehicle[] vehicles = {
                new Car(),
                new Bike(),
                new Bus(),
                new Car(),
                new Bike(),
                new Bus(),
                new Car(),
                new Bike(),
                new Bus(),
                new Car(),
                new Bike(),
                new Bike(),
                new Bus(),
                new Car(),
                new Bike(),
                new Bus(),
                new Car(),
                new Bike(),
                new Bus(),
                new Car(),
        };

        for (int i = 0; i < vehicles.length; i++) {
            System.out.println("第" + (i + 1) + "車輛:");
            vehicles[i].move();
        }
    }
}
