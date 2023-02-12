package next.dao;


import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import next.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class UserDaoTest {

    private final UserDao userDao = new UserDao();

    @Test
    @Disabled
    public void crud() throws Exception {
        User expected = new User("userId", "password", "name", "javajigi@email.com");
        userDao.insert(expected);

        User actual = userDao.findByUserId(expected.getUserId());
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void update() {
        // when
        userDao.update(new User("userId", "password", "hoseok", "javajigi@email.com"));
        User user = userDao.findByUserId("userId");

        // then
        assertThat(user.getName()).isEqualTo("hoseok");
    }

    @Test
    void findByUserId() {
        // given
        // when
        User user = userDao.findByUserId("admin");

        // then
        assertThat(user).isEqualTo(new User("admin", "password", "자바지기", "admin@slipp.net"));
    }

    @Test
    void findAll() {
        // when
        List<User> users = userDao.findAll();

        // then
        Assertions.assertAll(() -> {
            assertThat(users.size()).isEqualTo(2);
            assertThat(users.contains(new User("userId", "password", "name", "javajigi@email.com"))).isTrue();
        });
    }
}
