package senior.borrowGiveBackSystem.librarySystemMysql.models;

public class Book {
    private String number;
    private String title;
    private String author;
    private boolean available;
    private String borrowUser;
    private String categoryCode;
    private String categoryName;
    private String itemName;

    public Book(String number, String title, String author, boolean available, String borrowUser,
            String categoryCode, String categoryName, String itemName) {
        this.number = number;
        this.title = title;
        this.author = author;
        this.available = available;
        this.borrowUser = borrowUser;
        this.categoryCode = categoryCode;
        this.categoryName = categoryName;
        this.itemName = itemName;
    }

    public String getCode() {
        return this.categoryCode;
    }

    public String getNumber() {
        return this.number;
    }

    public String getTitle() {
        return this.title;
    }

    public String getAuthor() {
        return this.author;
    }

    public boolean isAvailable() {
        return this.available;
    }

    public String getBorrowUser() {
        return this.borrowUser;
    }

    public String getExtraInfo() {
        return this.itemName;
    }

    public String toFileSting() {
        return getCode() + "," + getNumber() + "," + getTitle() + "," + getAuthor() + "," + isAvailable() + ","
                + getExtraInfo() + "," + getBorrowUser();
    }

    public String getInfo() {
        return "編號: " + getNumber() + "\n"
                + "書名：" + getTitle() + "\n"
                + "作者：" + getAuthor() + "\n"
                + "類型：" + this.categoryName + "\n"
                + getItemLabel() + "：" + getExtraInfo() + "\n"
                + "可借：" + (isAvailable() ? "可" : "已被" + borrowUser + "借走");
    }

    private String getItemLabel() {
        if (this.categoryCode.equals("programming")) {
            return "語言";
        }

        if (this.categoryCode.equals("novel")) {
            return "分類";
        }

        return "項目";
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public void setBorrowUser(String borrowUser) {
        this.borrowUser = borrowUser;
    }
}
