package ke.fintech.ledger_api.utilities;

import ke.fintech.ledger_api.model.ApiResponse;

import java.util.List;

public class ResponseWrapper {
    private List<ApiResponse> response;

    // Constructor
    public ResponseWrapper(List<ApiResponse> response) {
        this.response = response;
    }

    // Getter and Setter
    public List<ApiResponse> getResponse() {
        return response;
    }

    public void setResponse(List<ApiResponse> response) {
        this.response = response;
    }
}

