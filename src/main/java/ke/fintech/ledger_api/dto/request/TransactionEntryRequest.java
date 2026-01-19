package ke.fintech.ledger_api.dto.request;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class TransactionEntryRequest {

    @NotBlank
    private String trx_key;

    @NotBlank
    private String accountCode;

    @NotNull
    private Double debit;

    @NotNull
    private Double credit;

    public String getTrx_key() {
        return trx_key;
    }

    public void setTrx_key(String trx_key) {
        this.trx_key = trx_key;
    }

    public String getAccountCode() {
        return accountCode;
    }

    public void setAccountCode(String accountCode) {
        this.accountCode = accountCode;
    }

    public Double getDebit() {
        return debit;
    }

    public void setDebit(Double debit) {
        this.debit = debit;
    }

    public Double getCredit() {
        return credit;
    }

    public void setCredit(Double credit) {
        this.credit = credit;
    }
}
