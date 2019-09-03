package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;

import java.util.*;

@Repository
@Transactional(readOnly = true)
public class JdbcUserRepositoryImpl implements UserRepository {

    private static final BeanPropertyRowMapper<User> ROW_MAPPER = BeanPropertyRowMapper.newInstance(User.class);

    private final JdbcTemplate jdbcTemplate;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final SimpleJdbcInsert insertUser;

    @Autowired
    public JdbcUserRepositoryImpl(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.insertUser = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");

        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Transactional
    @Override
    public User save(User user) {
        BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(user);

        if (user.isNew()) {
            Number newKey = insertUser.executeAndReturnKey(parameterSource);
            user.setId(newKey.intValue());

            insertRoles(user);
        } else {
            namedParameterJdbcTemplate.update(
                    "UPDATE users SET name=:name, email=:email, password=:password, " +
                            "registered=:registered, enabled=:enabled, calories_per_day=:caloriesPerDay WHERE id=:id", parameterSource);

            deleteRoles(user.getId());
            insertRoles(user);
        }
        return user;
    }

    @Transactional
    @Override
    public boolean delete(int id) {
        return deleteRoles(id) != 0 &&
                jdbcTemplate.update("DELETE FROM users WHERE id=?", id) != 0;
    }

    @Override
    public User get(int id) {
        List<User> users = jdbcTemplate.query("SELECT * FROM users WHERE id=?", ROW_MAPPER, id);
        return getRoles(DataAccessUtils.singleResult(users));
    }

    @Override
    public User getByEmail(String email) {
//        return jdbcTemplate.queryForObject("SELECT * FROM users WHERE email=?", ROW_MAPPER, email);
        List<User> users = jdbcTemplate.query("SELECT * FROM users WHERE email=?", ROW_MAPPER, email);
        return getRoles(DataAccessUtils.singleResult(users));
    }

    @Override
    public List<User> getAll() {
        Map<Integer, List<Role>> roles = new HashMap<>();

        jdbcTemplate.query("SELECT * FROM user_roles", resultSet -> {
            roles.computeIfAbsent(resultSet.getInt("user_id"), user_id -> new ArrayList<>())
                    .add(Role.valueOf(resultSet.getString("role")));
        });

        List<User> users = jdbcTemplate.query("SELECT * FROM users ORDER BY name, email", ROW_MAPPER);

        for (User user : users) {
            user.setRoles(roles.get(user.getId()));
        }

        return users;
    }

    public User getRoles(User user) {
        if (user != null) {
            List<Role> roles = new ArrayList<>();

            jdbcTemplate.query("SELECT * FROM user_roles WHERE user_id=?", rs -> {
                roles.add(Role.valueOf(rs.getString("role")));
            }, user.getId());

            user.setRoles(roles);
        }
        return user;
    }

    public void insertRoles(User user) {
        Set<Role> roles = user.getRoles();
        if (!roles.isEmpty()) {
            jdbcTemplate.batchUpdate("INSERT INTO user_roles values (?,?)", roles, roles.size(), (ps, argument) -> {
                ps.setInt(1, user.getId());
                ps.setString(2, argument.name());
            });
        }
    }

    private int deleteRoles(int id) {
        return jdbcTemplate.update("DELETE FROM user_roles WHERE user_id=?", id);
    }
}
