package ke.fintech.ledger_api.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class ControlAccountRepository {

    @Autowired
    private JdbcTemplate jdbc;

    public boolean AccountCodeExists(String accountCode) {
        String sql = "SELECT COUNT(*) FROM control_account WHERE account_code = ?";
        Integer count = jdbc.queryForObject(sql, Integer.class, accountCode);
        return count > 0;
    }



}
