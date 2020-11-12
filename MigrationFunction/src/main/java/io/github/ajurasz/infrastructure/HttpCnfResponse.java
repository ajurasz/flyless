package io.github.ajurasz.infrastructure;

import com.amazonaws.services.lambda.runtime.Context;
import io.github.ajurasz.flyless.CfnEvent;
import io.github.ajurasz.flyless.CfnResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Map;

class HttpCnfResponse implements CfnResponse {
    private static final Logger log = LogManager.getLogger(HttpCnfResponse.class);
    private static final HttpClient client = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(10))
            .build();

    @Override
    public void send(Status status, CfnEvent event, Context context) {
        log.info("Status {}, Response URL {}", status, event.responseURL());
        try {
            var request = HttpRequest.newBuilder()
                    .PUT(HttpRequest.BodyPublishers.ofString(toBody(status, event, context)))
                    .uri(URI.create(event.responseURL()))
                    .build();

            client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            throw new RuntimeException("Failed to send request", e);
        }
    }

    private String toBody(Status status, CfnEvent event, Context context) {
        var body = Map.of(
                "Status", status.name(),
                "PhysicalResourceId", context.getLogStreamName(),
                "StackId", event.stackId(),
                "RequestId", event.requestId(),
                "LogicalResourceId", event.logicalResourceId()
        );
        return Json.toString(body);
    }
}
