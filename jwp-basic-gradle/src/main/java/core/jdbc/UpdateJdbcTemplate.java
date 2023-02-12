package core.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import next.exception.DataAccessException;
import next.model.User;

public class UpdateJdbcTemplate {

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

    private static String createQueryForUpdate() {
        return "UPDATE USERS SET password = ?, name = ?, email = ? WHERE userId = ?";
    }

    private static void setValueForUpdate(final User updatedUser, final PreparedStatement pstmt) throws SQLException {
        pstmt.setString(1, updatedUser.getPassword());
        pstmt.setString(2, updatedUser.getName());
        pstmt.setString(3, updatedUser.getEmail());
        pstmt.setString(4, updatedUser.getUserId());
    }
}
