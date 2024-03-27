package hipravin.samples.jpaid.persist.sqlinjection;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class JdbcVulnerablePersonRepository implements PersonRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcVulnerablePersonRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<PersonEntity> findByName(String name) {
        String query = "select * from person where name = '" + name + "'";
        List<PersonEntity> persons = jdbcTemplate.query(query, (rs, rowNum) -> {
            Long id = getLongNullable(rs, "id");
            String name1 = rs.getString("name");
            return new PersonEntity(id, name1);
        });
        return persons;
    }

    static Long getLongNullable(ResultSet rs, String columnName) throws SQLException {
        long value = rs.getLong(columnName);
        return (rs.wasNull()) ? null : value;
    }
}
