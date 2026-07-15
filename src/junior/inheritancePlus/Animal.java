package junior.inheritancePlus;

public class Animal {
    public String name;
    public int age;

    public Animal(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public void eat() {
        System.out.println("動物類別: " + this.name + " 正在吃東西.");
    }

}
