package ke.fintech.ledger_api.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ke.fintech.ledger_api.model.ApiResponse;
import ke.fintech.ledger_api.utilities.AppLogger;
import ke.fintech.ledger_api.utilities.ResponseWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @Autowired
    private AppLogger appLogger;
    @Autowired
    private ObjectMapper objectMapper;


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseWrapper> handleValidationExceptions(MethodArgumentNotValidException ex) {


        appLogger.debug("Error Message Invoked");

        // Collect validation error messages
        Map<String, String> errors = new HashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }

        // Construct response map
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("status", HttpStatus.BAD_REQUEST.value());
        response.put("error", "Validation Error");
        response.put("errors", errors);

        // Create ApiResponse
        ApiResponse apiResponse = new ApiResponse(false,"Invalid Values found", response.get("errors"));

        // Wrap ApiResponse in ResponseWrapper
        ResponseWrapper responseWrapper = new ResponseWrapper(Collections.singletonList(apiResponse));

        String jsonResponse = null;
        try {
            jsonResponse = objectMapper.writer().writeValueAsString(responseWrapper);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        appLogger.debug("Response: {}"+ jsonResponse);

        // Return the response
        return ResponseEntity.ok(responseWrapper);
    }




    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<?> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        Map<String, Object> response = new HashMap<>();

        response.put("status", HttpStatus.BAD_REQUEST.value());
        response.put("error", "Malformed JSON Request");
        response.put("message", "Invalid or empty JSON structure");
//
//        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);

        appLogger.debug("HttpMessageNotReadableException Handler Invoked: " + ex.getMessage());

        // Construct response
        ApiResponse apiResponse = new ApiResponse(false,response.get("message").toString() , null);
        ResponseWrapper responseWrapper = new ResponseWrapper(Collections.singletonList(apiResponse));

        logResponse(responseWrapper);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseWrapper);
    }


    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ResponseWrapper> handleIllegalStateException(IllegalStateException ex) {
        appLogger.debug("IllegalStateException Handler Invoked: " + ex.getMessage());

        // Construct response
        ApiResponse apiResponse = new ApiResponse(false, ex.getMessage(), null);
        ResponseWrapper responseWrapper = new ResponseWrapper(Collections.singletonList(apiResponse));

        logResponse(responseWrapper);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(responseWrapper);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ResponseWrapper> handleRuntimeException(RuntimeException ex) {
        appLogger.debug("RuntimeException Handler Invoked: " + ex.getMessage());

        // Construct response
        ApiResponse apiResponse = new ApiResponse(false, ex.getMessage(), null);
        ResponseWrapper responseWrapper = new ResponseWrapper(Collections.singletonList(apiResponse));

        logResponse(responseWrapper);
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(responseWrapper);
    }


    private void logResponse(ResponseWrapper responseWrapper) {
        try {
            String jsonResponse = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(responseWrapper);
            appLogger.debug("Response: {}"+ jsonResponse);
        } catch (JsonProcessingException e) {
            appLogger.error("Error serializing response", e);
        }
    }
}

