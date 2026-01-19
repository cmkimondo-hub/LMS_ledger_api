package ke.fintech.ledger_api.service;

import ke.fintech.ledger_api.dto.TransactionDTO;
import ke.fintech.ledger_api.dto.request.TransactionEntryRequest;
import ke.fintech.ledger_api.dto.request.TransactionRequest;
import ke.fintech.ledger_api.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Service
public class TransactionService {

    @Autowired
    private JdbcTemplate jdbc;
    @Autowired
    TransactionRepository transactionRepository;
    @Autowired
    ControlAccountRepository controlAccountRepository;



    @Transactional
    public TransactionDTO addTransaction(TransactionRequest req) {

        double totalDebit = 0;
        double totalCredit = 0;

        for (TransactionEntryRequest e : req.getEntries()) {
            totalDebit += e.getDebit();
            totalCredit += e.getCredit();

            if (!controlAccountRepository.AccountCodeExists(e.getAccountCode())) {
                throw new RuntimeException("Invalid account code: " + e.getAccountCode());
            }

            if (transactionRepository.TransactionKeyExists(e.getTrx_key())) {
                throw new RuntimeException("Duplicate trx_key: " + e.getTrx_key());
            }
        }


        if (Double.compare(totalDebit, totalCredit) != 0) {
            throw new RuntimeException("Debit and Credit totals must balance");
        }

        String sql = """
        INSERT INTO transactions
        (transaction_date, doc_no, trx_key, description,
         account_code, debit, credit, currency_code)
        VALUES (?, ?, ?, ?, ?, ?, ?, ?)
    """;

        for (TransactionEntryRequest e : req.getEntries()) {
            jdbc.update(sql,
                    req.getTransactiondate(),
                    req.getDoc_no(),
                    e.getTrx_key(),
                    req.getDescription(),
                    e.getAccountCode(),
                    e.getDebit(),
                    e.getCredit(),
                    req.getCurrencyCode()
            );
        }

        TransactionDTO dto = new TransactionDTO();
        dto.setDoc_no(req.getDoc_no());
        dto.setTransactiondate(req.getTransactiondate());
        dto.setDescription(req.getDescription());
        dto.setAmount(totalDebit);

        return dto;
    }



    public List<TransactionDTO> getTransactionHistory(String accountCode, Date startDate, Date endDate) {



        String sql = """
    SELECT transaction_date, doc_no, trx_key, description, amount, currency_code
    FROM transactions
    WHERE account_code = ?
      AND transaction_date BETWEEN ? AND ?
""";

        return jdbc.query(sql, new Object[]{accountCode, startDate, endDate}, (rs, rowNum) -> {
            TransactionDTO dto = new TransactionDTO();
            dto.setTransactiondate(rs.getString("transaction_date"));
            dto.setDoc_no(rs.getString("doc_no"));
            dto.setTrx_key(rs.getString("trx_key"));
            dto.setDescription(rs.getString("description"));
            dto.setAmount(rs.getDouble("amount"));
            dto.setCurrencyCode(rs.getString("currency_code"));
            return dto;
        });




    }


    private String nullIfEmpty(String value) {
        if (value == null) return null;
        return value.trim().isEmpty() ? null : value.trim();
    }
}
