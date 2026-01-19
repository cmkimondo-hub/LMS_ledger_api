package ke.fintech.ledger_api.dto;

public class CurrencyDTO {

    private String currency_code;
    private String currencyDetail;

    public String getCurrency_code() {
        return currency_code;
    }

    public void setCurrency_code(String currency_code) {
        this.currency_code = currency_code;
    }

    public String getCurrencyDetail() {
        return currencyDetail;
    }

    public void setCurrencyDetail(String currencyDetail) {
        this.currencyDetail = currencyDetail;
    }
}
