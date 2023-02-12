package core.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import next.exception.DataAccessException;

public abstract class JdbcTemplate {
    public void insert() {
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = ConnectionManager.getConnection();
            String sql = createQuery();
            pstmt = con.prepareStatement(sql);
            setValue(pstmt);

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


    public void update() {
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = ConnectionManager.getConnection();
            String sql = createQuery();
            pstmt = con.prepareStatement(sql);
            setValue(pstmt);
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


    public List query() {
        Connection con = null;
        ResultSet rs = null;
        try {
            con = ConnectionManager.getConnection();
            String sql = createQuery();
            Statement st = con.createStatement();

            rs = st.executeQuery(sql);

            List<Object> results = new ArrayList<>();
            Object result;
            while (rs.next()) {
                result = mapRow(rs);
                results.add(result);
            }
            if (rs != null) {
                rs.close();
            }
            if (st != null) {
                st.close();
            }
            if (con != null) {
                con.close();
            }
            return results;
        } catch (SQLException exception) {
            throw new DataAccessException(exception.getMessage());
        }
    }


    public Object queryForObject() {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            con = ConnectionManager.getConnection();
            String sql = createQuery();
            pstmt = con.prepareStatement(sql);
            setValue(pstmt);

            rs = pstmt.executeQuery();

            Object result = null;
            if (rs.next()) {
                result = mapRow(rs);
            }
            if (rs != null) {
                rs.close();
            }
            if (pstmt != null) {
                pstmt.close();
            }
            if (con != null) {
                con.close();
            }

            return result;
        } catch (SQLException exception) {
            throw new DataAccessException(exception.getMessage());
        }
    }

    protected abstract Object mapRow(final ResultSet rs) throws SQLException;

    protected abstract String createQuery();

    protected abstract void setValue(final PreparedStatement preparedStatement) throws SQLException;
}
