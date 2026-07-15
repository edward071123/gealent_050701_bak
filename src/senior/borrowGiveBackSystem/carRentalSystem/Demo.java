package senior.borrowGiveBackSystem.carRentalSystem;

import java.util.Scanner;

public class Demo {
    public static void main(String[] args) {
        // 搬移ATM操作 while loop 到這裡，並且在修改裡面內容變成租車系統的操作

        // 設定結束的起始條件: true:真的結束, false:還沒結束
        boolean end = false;

        // 建立Service物件通道
        RentalService rentalService = new RentalService();

        Scanner sc = new Scanner(System.in);
        while (end == false) {
            System.out.println("請輸入要辦理項目--1:查詢所有的車輛, 2:租車, 3:還車, 4:結束");
            // 偵測輸入存到變數(getInputNum)裡
            String getInputNum = sc.nextLine();
            try {
                if (getInputNum.equals("1")) {
                    // 查詢所有的車輛
                    System.out.println("查詢所有的車輛");

                    // 帶人去辦理查詢所有車輛的業務辦公室
                    rentalService.getAllCars();

                } else if (getInputNum.equals("2")) {
                    // 租車
                    System.out.println("請輸入租車人姓名:");
                    String borrowUser = sc.nextLine();

                    System.out.println("請輸入車輛的系統編號(數字):");
                    // 這裡要把輸入的字串轉成數字
                    int carId = Integer.parseInt(sc.nextLine());

                    // 帶人去辦理租車業務的辦公室
                    String result = rentalService.borrow(carId, borrowUser);
                    System.out.println(result);
                } else if (getInputNum.equals("3")) {
                    // 還車
                    System.out.println("請輸入車輛的系統編號(數字):");
                    // 這裡要把輸入的字串轉成數字
                    int carId = Integer.parseInt(sc.nextLine());

                    // 帶人去辦理還車業務的辦公室
                    String result = rentalService.giveBack(carId);
                    System.out.println(result);
                } else if (getInputNum.equals("4")) {
                    System.out.println("謝謝您的使用");
                    end = true;
                } else {
                    System.out.println("您輸入的數字錯誤,請重新輸入");
                }
            } catch (Exception e) {
                System.out.println("");
                System.out.println("=====操作錯誤====");
                System.out.println(e.getMessage());
            }
        }

        sc.close();
    }
}
