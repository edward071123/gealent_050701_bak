package senior.borrowGiveBackSystem.carRentalSystem.models;

public class Car {
    private int id;
    private String plate; // 車牌號碼
    private String brand; // 品牌
    private boolean available; // 是否可借
    private String borrowUser; // 借用者
    private int categoryId; // 類別 ID

    public Car(int id, String plate, String brand, boolean available, String borrowUser, int categoryId) {
        setId(id);
        setPlate(plate);
        setBrand(brand);
        setAvailable(available);
        setBorrowUser(borrowUser);
        setCategoryId(categoryId);
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPlate() {
        return this.plate;
    }

    public void setPlate(String plate) {
        this.plate = plate;
    }

    public String getBrand() {
        return this.brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public boolean getAvailable() {
        return this.available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public String getBorrowUser() {
        return this.borrowUser;
    }

    public void setBorrowUser(String borrowUser) {
        this.borrowUser = borrowUser;
    }
    
    public int getCategoryId() {
        return this.categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }
}
