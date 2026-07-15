package junior.inheritancePlus;

public class Dog extends Animal {
    public String breed;

    public Dog(String name, int age, String breed) {
        // 因為父類別的建構子有參數需要輸入，所以在子類別的建構子中需要呼叫 super() 並傳入對應的參數
        super(name, age);
        this.breed = breed;
    }

    @Override
    public void eat() {
        // super.name 是指父類別的 name 屬性 但是因為已經被繼承所以也是this.name
        System.out.println("Dog-super.name: " + super.name + " is eating.");
        System.out.println("Dog-this.name:  " + this.name + " is eating.");
        System.out.println("Dog-this.breed:  " + this.breed + " is eating.");
    }

}
