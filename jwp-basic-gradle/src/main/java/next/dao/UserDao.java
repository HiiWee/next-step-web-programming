package next.dao;

import core.jdbc.JdbcTemplate;
import java.util.List;
import next.model.User;

public class UserDao {
    private final JdbcTemplate jdbcTemplate = new JdbcTemplate();

    public void insert(User user) {
        jdbcTemplate.insert(
                "INSERT INTO USERS VALUES (?, ?, ?, ?)",
                preparedStatement -> {
                    preparedStatement.setString(1, user.getUserId());
                    preparedStatement.setString(2, user.getPassword());
                    preparedStatement.setString(3, user.getName());
                    preparedStatement.setString(4, user.getEmail());
                });
    }

    public void update(final User user) {
        jdbcTemplate.update(
                "UPDATE USERS SET password = ?, name = ?, email = ? WHERE userId = ?",
                preparedStatement -> {
                    preparedStatement.setString(1, user.getPassword());
                    preparedStatement.setString(2, user.getName());
                    preparedStatement.setString(3, user.getEmail());
                    preparedStatement.setString(4, user.getUserId());
                });
    }

    public User findByUserId(String userId) {
        return jdbcTemplate.queryForObject(
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
        return jdbcTemplate.query(
                "SELECT userId, password, name, email FROM USERS",
                resultSet -> new User(
                        resultSet.getString("userId"),
                        resultSet.getString("password"),
                        resultSet.getString("name"),
                        resultSet.getString("email"))
        );
    }
}
