package senior.borrowGiveBackSystemPlus.models;

public class Member {
    private int id;
    private String account;
    private String displayName;
    private String passwordHash;
    private boolean admin;

    public Member(int id, String account, String displayName, String passwordHash, boolean admin) {
        this.id = id;
        this.account = account;
        this.displayName = displayName;
        this.passwordHash = passwordHash;
        this.admin = admin;
    }

    public int getId() {
        return this.id;
    }

    public String getAccount() {
        return this.account;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public String getPasswordHash() {
        return this.passwordHash;
    }

    public boolean isAdmin() {
        return this.admin;
    }

    public String getInfo() {
        return "會員ID: " + getId() + "\n"
                + "帳號: " + getAccount() + "\n"
                + "顯示姓名: " + getDisplayName() + "\n"
                + "是否為管理員: " + (isAdmin() ? "是" : "否");
    }
}