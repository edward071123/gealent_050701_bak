package junior.encapsulationPlus;

public class AirConditioner {
    private int temperature;

    public AirConditioner() {
        this.temperature = 24; // 預設溫度
    }

    // 設定溫度
    public void setTemperature(int temperature) {
        if (temperature >= 16 && temperature <= 30) {
            this.temperature = temperature;
        } else {
            System.out.println("溫度必須介於 16~30 度之間");
        }
    }

    // 取得溫度
    public int getTemperature() {
        return this.temperature;
    }
}
