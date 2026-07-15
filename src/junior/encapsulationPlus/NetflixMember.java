package junior.encapsulationPlus;

// 2. Netflix 會員系統
public class NetflixMember {
    private String name;
    private boolean vip; // false:VIP未啟動, true:VIP已啟動

    // 建構子
    public NetflixMember(String name) {
        setName(name);
        // 初始化不是vip
        setVip(false);
    }

    // 升級 VIP
    public void upgradeToVip() {
        setVip(true);
        System.out.println(getName() + " 已升級為 VIP 會員");
    }

    // 看電影
    public void watchMovie() {
        if (getVip()) {
            System.out.println(getName() + " 正在觀看電影");
        } else {
            System.out.println("您不是 VIP 會員，請先升級會員才能觀看電影");
        }
    }

    // Setter
    public void setName(String name) {
        this.name = name;
    }

    public void setVip(boolean vip) {
        this.vip = vip;
    }

    // Getter
    public String getName() {
        return this.name;
    }

    public boolean getVip() {
        return this.vip;
    }
}