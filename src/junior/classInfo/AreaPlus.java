package junior.classInfo;

public class AreaPlus {
    public double circleArea;
    public int rectangleArea;
    public double triangleArea;
    public double trapezoidArea;

    // 圓形的面積計算
    public AreaPlus(int r) {
        this.circleArea = r * r * 3.14;
    }

    // 長方形的面積計算
    public AreaPlus(int length, int width) {
        this.rectangleArea = length * width;
    }

    // 三角形的面積計算
    public AreaPlus(int base, int height, boolean isTriangle) {
        this.triangleArea = (base * height) / 2.0;
    }

    // 梯形的面積計算
    public AreaPlus(int topBase, int bottomBase, int height) {
        this.trapezoidArea = (topBase + bottomBase) * height / 2.0;
    }

    public void getArea() {
        if (this.circleArea != 0) {
            System.out.println("圓形的面積為: " + this.circleArea);
        }
        if (this.rectangleArea != 0) {
            System.out.println("長方形的面積為: " + this.rectangleArea);
        }
        if (this.triangleArea != 0) {
            System.out.println("三角形的面積為: " + this.triangleArea);
        }
        if (this.trapezoidArea != 0) {
            System.out.println("梯形的面積為: " + this.trapezoidArea);
        }
    }
}
