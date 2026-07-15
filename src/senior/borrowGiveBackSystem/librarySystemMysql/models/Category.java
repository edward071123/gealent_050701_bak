package senior.borrowGiveBackSystem.librarySystemMysql.models;

public class Category {
    private int id;
    private String code;
    private String name;

    public Category(int id, String code, String name) {
        this.id = id;
        this.code = code;
        this.name = name;
    }

    public int getId() {
        return this.id;
    }

    public String getCode() {
        return this.code;
    }

    public String getName() {
        return this.name;
    }
}
