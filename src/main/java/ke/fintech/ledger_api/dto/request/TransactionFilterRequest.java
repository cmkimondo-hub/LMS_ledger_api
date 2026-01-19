package ke.fintech.ledger_api.dto.request;

import jakarta.validation.constraints.NotNull;

public class TransactionFilterRequest {

    @NotNull(message = "Account Code cannot be null or empty")
   private String accountCode;
    @NotNull(message = "Start date cannot be null or empty")
   private String startDate;
    @NotNull(message = "End date  cannot be null or empty")
   private String endDate;

    public String getAccountCode() {
        return accountCode;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }
}
