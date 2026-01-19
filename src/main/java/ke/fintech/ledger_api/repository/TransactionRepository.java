package ke.fintech.ledger_api.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class TransactionRepository {

    @Autowired
    private JdbcTemplate jdbc;

    public boolean TransactionKeyExists(String trxKey) {
        String sql = "SELECT COUNT(*) FROM transactions WHERE trx_key = ?";
        Integer count = jdbc.queryForObject(sql, Integer.class, trxKey);
        return count > 0;
    }


}
