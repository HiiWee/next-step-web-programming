package core.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import next.exception.DataAccessException;
import next.model.User;

public abstract class UpdateJdbcTemplate {

    public void update(final User user) {
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = ConnectionManager.getConnection();
            String sql = createQueryForUpdate();
            pstmt = con.prepareStatement(sql);
            setValueForUpdate(user, pstmt);
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

    protected abstract String createQueryForUpdate();

    protected abstract void setValueForUpdate(final User user, final PreparedStatement preparedStatement) throws SQLException;
}
