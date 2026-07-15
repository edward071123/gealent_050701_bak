package senior.borrowGiveBackSystem.librarySystemMysql.models;

public class Item {
    private int id;
    private int categoryId;
    private String name;

    public Item(int id, int categoryId, String name) {
        this.id = id;
        this.categoryId = categoryId;
        this.name = name;
    }

    public int getId() {
        return this.id;
    }

    public int getCategoryId() {
        return this.categoryId;
    }

    public String getName() {
        return this.name;
    }
}
