package junior.inheritancePlus;

public class Cat extends Animal {
    public String color;

    public Cat(String name, int age, String color) {
        super(name, age);
        this.color = color;
    }

    @Override
    public void eat() {
        System.out.println("Cat-this.name:  " + this.name + " is eating.");
        System.out.println("Cat-this.color:  " + this.color + " is eating.");
    }

}
