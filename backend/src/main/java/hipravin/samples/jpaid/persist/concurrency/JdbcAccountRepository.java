package hipravin.samples.jpaid.persist.concurrency;

import jakarta.persistence.EntityManager;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Repository
@Transactional
public class JdbcAccountRepository implements AccountRepository {
    final JdbcTemplate jdbcTemplate;

    private final static String QUERY_LOCK = "select * from account where id in (?,?) FOR NO KEY UPDATE";
    private final static String QUERY_TRANSFER = "update account set amount = amount + ? where id = ?";

    public JdbcAccountRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void transfer(long accountIdFrom, long accountIdTo, long amount) {
        final long[] ids = {accountIdFrom, accountIdTo};
        final long[] amounts = {-amount, amount};

        lockForUpdate(accountIdFrom, accountIdTo);//otherwise deadlock sometimes if updates not ordered

        jdbcTemplate.batchUpdate(QUERY_TRANSFER, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setLong(1, amounts[i]);
                ps.setLong(2, ids[i]);
            }

            @Override
            public int getBatchSize() {
                return 2;
            }
        });
    }

    private void lockForUpdate(long id1, long id2) {
        jdbcTemplate.query(QUERY_LOCK, ps -> {
            ps.setLong(1, id1);
            ps.setLong(2, id2);
        }, rs -> {
            //ignore
        });
    }
}
