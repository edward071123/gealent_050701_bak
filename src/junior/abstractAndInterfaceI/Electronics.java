package junior.abstractAndInterfaceI;

public abstract class Electronics {
    private String model;

    public Electronics(String model) {
        setModel(model);
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getModel() {
        return this.model;
    }

    public void powerOn() {
        System.out.println(getModel() + "正在開機...");
    }

    public abstract void display();
}
