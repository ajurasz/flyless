package io.github.ajurasz.flyless;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import io.github.ajurasz.infrastructure.Application;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;

import static io.github.ajurasz.flyless.CfnResponse.Status.FAILED;
import static io.github.ajurasz.flyless.CfnResponse.Status.SUCCESS;

public class MigrationHandler implements RequestHandler<Map<String, Object>, Integer> {
    private static final int NOT_EXECUTED = -1;
    private static final Logger log = LogManager.getLogger(MigrationHandler.class);
    private final CfnResponse cfnResponse;
    private final Migrations migrations;

    public MigrationHandler() {
        this(Application.cfnResponse());
    }

    MigrationHandler(CfnResponse cfnResponse) {
        this.cfnResponse = cfnResponse;
        this.migrations = Application.migrations();
    }

    @Override
    public Integer handleRequest(Map<String, Object> input, Context context) {
        var event = CfnEvent.from(input);
        log.info(event.toString());

        if (event.isDeleteEvent()) {
            cfnResponse.send(SUCCESS, event, context);
            return NOT_EXECUTED;
        }

        try {
            var result = migrations.migrate();
            cfnResponse.send(SUCCESS, event, context);
            return result;
        } catch (Exception e) {
            log.error("Migration failed", e);
            cfnResponse.send(FAILED, event, context);
            return NOT_EXECUTED;
        }
    }
}
