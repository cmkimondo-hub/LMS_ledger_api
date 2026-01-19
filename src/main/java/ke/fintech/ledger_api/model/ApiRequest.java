package ke.fintech.ledger_api.model;


import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class ApiRequest {

    @NotNull(message = "Invalid Request")
    @NotEmpty(message = "Empty Request")
    private String Request;

    public String getRequest() {
        return Request;
    }

    public void setRequest(String request) {
        Request = request;
    }
}
