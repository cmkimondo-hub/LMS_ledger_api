package ke.fintech.ledger_api.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import ke.fintech.ledger_api.dto.*;
import ke.fintech.ledger_api.dto.request.ControlAccountRequest;
import ke.fintech.ledger_api.dto.request.TransactionFilterRequest;
import ke.fintech.ledger_api.dto.request.TransactionRequest;
import ke.fintech.ledger_api.model.*;
import ke.fintech.ledger_api.service.*;
import ke.fintech.ledger_api.utilities.ResponseWrapper;

import ke.fintech.ledger_api.utilities.AppLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.sql.Date;
import java.util.Collections;
import java.util.List;


@RestController
@RequestMapping(value= "/ledger/v1")
public class APIController {

    private final AppLogger appLogger;
    private final ObjectMapper objectMapper;
    private final TransactionService transactionService;
    private final ControlAccountService controlAccountService;

    @Autowired
    private HttpServletRequest httpRequest;


    public APIController(AppLogger appLogger, ObjectMapper objectMapper,
                         TransactionService transactionService, ControlAccountService controlAccountService) {
        this.appLogger = appLogger;
        this.objectMapper = objectMapper;
        this.transactionService = transactionService;
        this.controlAccountService = controlAccountService;
    }





    @PostMapping("/accounts")
    public ResponseEntity<?> save_control_account(@Valid @RequestBody ControlAccountRequest request) {
        logRequest(request);
        ControlAccountDTO dto = controlAccountService.addAccount(request);
        return ResponseEntity.ok(new ApiResponse(true, "Account Added Successfully", dto));
    }






    @PostMapping(
            value= "/transaction",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE

    )
    public ResponseEntity<?> save_transaction(@Valid @RequestBody TransactionRequest request) {
        logRequest(request);
        TransactionDTO dto = transactionService.addTransaction(request);
        return ResponseEntity.ok(new ApiResponse(true, "Transaction Posted Successfully", dto));
    }





    @PostMapping(value = "/trxhistory")
    public ResponseEntity<ResponseWrapper> get_transactions(@Valid @RequestBody TransactionFilterRequest transactionFilterRequest) {

        logRequest(transactionFilterRequest);

        String accountCode=transactionFilterRequest.getAccountCode();


        Date startDate = Date.valueOf(transactionFilterRequest.getStartDate());
        Date endDate = Date.valueOf(transactionFilterRequest.getEndDate());



        List<TransactionDTO> transactionHistory = transactionService.getTransactionHistory(accountCode,startDate,endDate);


        if (transactionHistory.isEmpty()) {
            ApiResponse apiResponse = new ApiResponse(false, "No transactions found", Collections.emptyList());
            ResponseWrapper wrapper = new ResponseWrapper(Collections.singletonList(apiResponse));
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(wrapper);
        }


        ApiResponse apiResponse = new ApiResponse(true, "Transactions found: " + transactionHistory.size(), transactionHistory);
        ResponseWrapper wrapper = new ResponseWrapper(Collections.singletonList(apiResponse));

        return ResponseEntity.ok(wrapper);
    }





    private void logRequest(Object request) {
        try {
            String endpoint = httpRequest.getRequestURI();
            String method   = httpRequest.getMethod();
            String jsonRequest = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(request);
            appLogger.debug("=====================Initialize Request====================");
           // ticketLogger.debug("Request Payload: " + jsonRequest);
            appLogger.debug("Incoming Request [{} {}]: {}", method, endpoint, jsonRequest);
        } catch (JsonProcessingException e) {
            appLogger.error("Error serializing request payload", e);
        }
    }




}
