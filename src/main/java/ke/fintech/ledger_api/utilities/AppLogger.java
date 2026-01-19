package ke.fintech.ledger_api.utilities;


import ke.fintech.ledger_api.App;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.apache.logging.log4j.LogManager.getLogger;
@Component
public class AppLogger {

    private final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss:SSS");
    private String requestLogString = "%s LEVEL: %s => %s";
    private final Logger logger = getLogger(App.class);
    private final ExecutorService logService = Executors. newFixedThreadPool(10);

    public void info(String logMsg){
        logService.execute(()->{
            String logLevel = "INFO";
            String msg = String.format(requestLogString,getLogTimeString(),logLevel,logMsg);

        });
        logger.info(logMsg);


    }

    public void debug(String logMsg){
        logService.execute(()->{
            String logLevel = "DEBUG";
            String msg = String.format(requestLogString,getLogTimeString(),logLevel,logMsg);

        });
        logger.debug(logMsg);
    }

    public void debug(String message, Object... args) {

        String formatted = org.slf4j.helpers.MessageFormatter.arrayFormat(message, args).getMessage();

        logService.execute(() -> {
            String logLevel = "DEBUG";
            String entry = String.format(
                    requestLogString,           // your saved log format template
                    getLogTimeString(),         // timestamp
                    logLevel,
                    formatted                   // formatted message
            );

        });


        logger.debug(formatted);
    }


    public void error(String errorKey, Exception errorException){
        logService.execute(()->{
            String logLevel = "ERROR";
            String msg = String.format(requestLogString,getLogTimeString(),logLevel,
                    errorKey + errorException.getMessage());

        });
        logger.error(errorKey + errorException.getMessage());
    }

    private String getLogTimeString() {
        return LocalDateTime.now().format(DATE_TIME_FORMATTER);
    }
}
