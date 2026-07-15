package senior.borrowGiveBackSystemPlus.models;

public class Book {
    private String number;
    private String title;
    private String author;
    private boolean available;
    private String borrowUser;
    private int borrowMemberId;
    private int categoryId;
    private int itemId;
    private String categoryName;
    private String itemName;

    public Book(String number, String title, String author, boolean available, String borrowUser, int borrowMemberId,
            int categoryId, int itemId, String categoryName, String itemName) {
        this.number = number;
        this.title = title;
        this.author = author;
        this.available = available;
        this.borrowUser = borrowUser;
        this.borrowMemberId = borrowMemberId;
        this.categoryId = categoryId;
        this.itemId = itemId;
        this.categoryName = categoryName;
        this.itemName = itemName;
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

    public int getBorrowMemberId() {
        return this.borrowMemberId;
    }

    public int getCategoryId() {
        return this.categoryId;
    }

    public int getItemId() {
        return this.itemId;
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
        return getNumber() + "," + getTitle() + "," + getAuthor() + "," + isAvailable() + ","
                + getExtraInfo() + "," + getBorrowUser();
    }

    public String getInfo() {
        return "編號: " + getNumber() + "\n"
                + "書名：" + getTitle() + "\n"
                + "作者：" + getAuthor() + "\n"
                + "類型：" + this.categoryName + "\n"
                + getItemLabel(this.itemName) + "：" + getExtraInfo() + "\n"
                + "可借：" + (isAvailable() ? "可" : "已被" + borrowUser + "借走");
    }

    private String getItemLabel(String itemName) {
        if (itemName.equals("Java") || itemName.equals("Python")) {
            return "語言";
        }

        if (itemName.equals("奇幻") || itemName.equals("歷史")) {
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

    public void setBorrowMemberId(int borrowMemberId) {
        this.borrowMemberId = borrowMemberId;
    }
}
