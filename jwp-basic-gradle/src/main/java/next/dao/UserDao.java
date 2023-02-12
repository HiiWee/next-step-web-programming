package next.dao;

import core.jdbc.JdbcTemplate;
import java.util.List;
import next.model.User;

public class UserDao {

    public void insert(User user) {
        JdbcTemplate insertJdbcTemplate = new JdbcTemplate();
        insertJdbcTemplate.insert(
                "INSERT INTO USERS VALUES (?, ?, ?, ?)",
                preparedStatement -> {
                    preparedStatement.setString(1, user.getUserId());
                    preparedStatement.setString(2, user.getPassword());
                    preparedStatement.setString(3, user.getName());
                    preparedStatement.setString(4, user.getEmail());
                });
    }

    public void update(final User user) {
        JdbcTemplate updateJdbcTemplate = new JdbcTemplate();
        updateJdbcTemplate.update(
                "UPDATE USERS SET password = ?, name = ?, email = ? WHERE userId = ?",
                preparedStatement -> {
                    preparedStatement.setString(1, user.getPassword());
                    preparedStatement.setString(2, user.getName());
                    preparedStatement.setString(3, user.getEmail());
                    preparedStatement.setString(4, user.getUserId());
                });
    }

    public User findByUserId(String userId) {
        JdbcTemplate selectJdbcTemplate = new JdbcTemplate();
        return selectJdbcTemplate.queryForObject(
                "SELECT userId, password, name, email FROM USERS WHERE userId = ?",
                resultSet -> new User(
                        resultSet.getString("userId"),
                        resultSet.getString("password"),
                        resultSet.getString("name"),
                        resultSet.getString("email")),
                userId
        );
    }

    public List<User> findAll() {
        JdbcTemplate selectJdbcTemplate = new JdbcTemplate();
        return selectJdbcTemplate.query(
                "SELECT userId, password, name, email FROM USERS",
                resultSet -> new User(
                        resultSet.getString("userId"),
                        resultSet.getString("password"),
                        resultSet.getString("name"),
                        resultSet.getString("email"))
        );
    }
}
