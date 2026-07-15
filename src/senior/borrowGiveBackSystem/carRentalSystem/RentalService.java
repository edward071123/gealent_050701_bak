package senior.borrowGiveBackSystem.carRentalSystem;
import java.util.List;

import senior.borrowGiveBackSystem.carRentalSystem.models.Car;
import senior.borrowGiveBackSystem.main.IBorrowGiveBack;

public class RentalService implements IBorrowGiveBack {

    CarRespository carRespository;
    
    public RentalService() {
        // 初始化租車服務
        this.carRespository = new CarRespository();
    }

    // 查詢所有車輛的房間
    public void getAllCars() throws Exception {
        System.out.println("=====查詢所有車輛 開始====");
        List<Car> cars = this.carRespository.queryAllCars();
        for(Car car : cars) {
            System.out.println(
                car.getId() + " " +
                car.getPlate() + " " +
                car.getBrand() + " " +
                car.getAvailable() + " " +
                car.getBorrowUser() + " " +
                car.getCategoryId()
            );
        }
        System.out.println("=====查詢所有車輛 結束====");
    }

    // 辦理租車業務的房間
    @Override
    public String borrow(int number, String borrowUser) throws Exception {
        // 1. 檢查borrowUser是否為空或null
        if(borrowUser.isEmpty() || borrowUser == null) {
            throw new Exception("租車人姓名不能為空");
        }

        // 2. 用number去查詢目標車輛, 並且檢查是否被借走了
        // 針對資料庫做操作 => CarRespository.java => SELECT * FROM cars WHERE id = number
        Car car = this.carRespository.queryOneCar(number);

        // 3. 如果已被借走則回應, 已被借走, 無法租借
        if(car.getAvailable() == false) {
            throw new Exception("車輛編號 " + number + " 已被借走, 無法租借");
        }

        // 4. 如果未被借走則進行租借操作, 記錄借用者, 並將車輛狀態設為已借出
        // 針對資料庫做操作 => CarRespository.java => UPDATE cars SET available = false, borrow_user = borrowUser WHERE id = number
        this.carRespository.updateCarStatus(number, false, borrowUser);
        // 實現租車邏輯
        return "租車成功: 車輛編號 " + number + " 已租給 " + borrowUser;
    }

    // 辦理還車業務的房間
    @Override
    public String giveBack(int number) throws Exception {
        // 1. 用number去查詢目標車輛, 並且檢查是否被借走了
        // 針對資料庫做操作 => CarRespository.java => SELECT * FROM cars WHERE id = number
        Car car = this.carRespository.queryOneCar(number);

        // 2. 如果未被借走則回應, 該車輛未被借出, 無須還車
        if(car.getAvailable() == true) {
            throw new Exception("車輛編號 " + number + " 未被借出, 無需還車");
        }

        // 3. 如果已被借走則進行還車操作, 清除借用者, 並將車輛狀態設為可借
        // 針對資料庫做操作 => CarRespository.java => UPDATE cars SET available = true, borrow_user = null WHERE id = number
        this.carRespository.updateCarStatus(number, true, null);

        // 實現還車邏輯
        return "還車成功: 車輛編號 " + number + " 已歸還";
    }

}
