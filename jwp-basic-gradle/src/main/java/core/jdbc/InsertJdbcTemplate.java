package core.jdbc;

import static next.dao.UserDao.createQueryForInsert;
import static next.dao.UserDao.setValueForInsert;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import next.exception.DataAccessException;
import next.model.User;

public class InsertJdbcTemplate {

    public void insert(final User user) {
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = ConnectionManager.getConnection();
            String sql = createQueryForInsert();
            pstmt = con.prepareStatement(sql);
            setValueForInsert(user, pstmt);

            pstmt.executeUpdate();
            if (pstmt != null) {
                pstmt.close();
            }

            if (con != null) {
                con.close();
            }
        } catch (SQLException exception) {
            throw new DataAccessException(exception.getMessage());
        }
    }
}
