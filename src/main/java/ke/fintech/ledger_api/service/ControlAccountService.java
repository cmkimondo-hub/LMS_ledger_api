package ke.fintech.ledger_api.service;

import ke.fintech.ledger_api.dto.ControlAccountDTO;
import ke.fintech.ledger_api.dto.request.ControlAccountRequest;
import ke.fintech.ledger_api.repository.ControlAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class ControlAccountService {

    @Autowired
    private JdbcTemplate jdbc;

    @Autowired
    ControlAccountRepository controlAccountRepository;


    public ControlAccountDTO addAccount(ControlAccountRequest req) {



        if (controlAccountRepository.AccountCodeExists(req.getAccountCode())) {
            throw new RuntimeException("Code Exists Enter a Different Account Code");
        }



        String sql = """
              INSERT INTO control_account (account_code, account_name, category, description, sub_account_id, currency_id,balance
              ) 
              VALUES(?,?,?,?,?,?,?)
                """;

        jdbc.update(sql,
                req.getAccountCode(),
                req.getAccountName(),
                req.getAccountCategory(),
                req.getAccountDescription(),
                req.getSubaccountId(),
                req.getCurrencyId(),
                req.getAccountBalance()
        );

        // 4. Prepare response
        ControlAccountDTO dto = new ControlAccountDTO();



       dto.setAccountCode(req.getAccountCode());
       dto.setAccountName(req.getAccountName());
       dto.setAccountCategory( req.getAccountCategory());
       dto.setAccountDescription(req.getAccountDescription());
       dto.setSubaccountId(req.getSubaccountId());
       dto.setAccountBalance(req.getAccountBalance());


        return dto;
    }



}
