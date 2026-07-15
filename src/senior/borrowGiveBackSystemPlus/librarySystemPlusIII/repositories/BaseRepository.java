package senior.borrowGiveBackSystemPlus.librarySystemPlusIII.repositories;

import java.sql.Connection;
import java.sql.PreparedStatement;

import senior.borrowGiveBackSystemPlus.db.DBConnection;

public abstract class BaseRepository {

    protected void executeUpdate(String sql, Object... values) throws Exception {
        try (
                Connection connection = new DBConnection().getConnection();
                PreparedStatement ps = connection.prepareStatement(sql);
            ) {
            for (int i = 0; i < values.length; i++) {
                ps.setObject(i + 1, values[i]);
            }

            int count = ps.executeUpdate();
            if (count == 0) {
                throw new Exception("沒有資料被更新，請確認 ID 是否存在");
            }
        } catch (Exception e) {
            throw new Exception("更新資料表失敗: " + e.getMessage());
        }
    }
}
