package io.github.ajurasz.flyless;

import com.amazonaws.services.lambda.runtime.Context;

public interface CfnResponse {
    void send(Status status, CfnEvent event, Context context);

    enum Status {
        SUCCESS, FAILED
    }
}
