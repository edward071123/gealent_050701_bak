package senior.borrowGiveBackSystem.carRentalSystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import senior.borrowGiveBackSystem.carRentalSystem.models.Car;

public class CarRespository {
    // 查詢所有車輛
    public List<Car> queryAllCars() throws Exception {
        List<Car> cars = new ArrayList<>();

        String sql = "SELECT * FROM cars";
        try(
            // 1. 建立資料庫連線
            Connection connection = new DBConnection().getConnection();
            // 2. 建立PreparedStatement物件
            PreparedStatement statement = connection.prepareStatement(sql);
            // 3. 執行查詢
            ResultSet resultSet = statement.executeQuery();
        ) {

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String plate = resultSet.getString("plate");
                String brand = resultSet.getString("brand");
                boolean available = resultSet.getBoolean("available");
                String borrowUser = resultSet.getString("borrow_user");
                int categoryId = resultSet.getInt("category_id");

                Car car = new Car(id, plate, brand, available, borrowUser, categoryId);
                cars.add(car);
                
            }

        } catch (Exception e) {
            throw new Exception("=====查詢所有車輛失敗====" + e.getMessage());
        }

        return cars;
    }

    // 用編號查詢單一車輛
    public Car queryOneCar(int number) throws Exception {
        Car car = null;
        String sql = "SELECT * FROM cars WHERE id = ?";
        try(
            // 1. 建立資料庫連線
            Connection connection = new DBConnection().getConnection();
            // 2. 建立PreparedStatement物件
            PreparedStatement ps = connection.prepareStatement(sql);
        ) {

            ps.setInt(1, number);
            try (ResultSet rs = ps.executeQuery();) {
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String plate = rs.getString("plate");
                    String brand = rs.getString("brand");
                    boolean available = rs.getBoolean("available");
                    String borrowUser = rs.getString("borrow_user");
                    int categoryId = rs.getInt("category_id");

                    car = new Car(id, plate, brand, available, borrowUser, categoryId);
                }
            }

        } catch (Exception e) {
           throw new Exception("=====查詢單一車輛失敗====" + e.getMessage());
        }

        return car;
    }


    // 更改車輛的狀態
    public void updateCarStatus(int number, boolean available, String borrowUser) throws Exception {
        String sql = "UPDATE cars SET available = ?, borrow_user = ? WHERE id = ?";
        try(
            // 1. 建立資料庫連線
            Connection connection = new DBConnection().getConnection();
            // 2. 建立PreparedStatement物件
            PreparedStatement ps = connection.prepareStatement(sql);
        ) {

            ps.setBoolean(1, available);
            ps.setString(2, borrowUser);
            ps.setInt(3, number);

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected == 0) {
                throw new Exception("=====更新車輛狀態失敗==== 沒有找到對應的車輛");
            }

        } catch (Exception e) {
           throw new Exception("=====更新車輛狀態失敗====" + e.getMessage());
        }
    }
}
