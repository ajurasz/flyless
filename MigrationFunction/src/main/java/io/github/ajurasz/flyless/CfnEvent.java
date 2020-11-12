package io.github.ajurasz.flyless;

import java.util.Map;

public class CfnEvent {
    private String requestType;
    private String responseURL;
    private String stackId;
    private String requestId;
    private String logicalResourceId;

    private CfnEvent(String requestType, String responseURL, String stackId, String requestId, String logicalResourceId) {
        this.requestType = requestType;
        this.responseURL = responseURL;
        this.stackId = stackId;
        this.requestId = requestId;
        this.logicalResourceId = logicalResourceId;
    }

    static CfnEvent from(Map<String, Object> map) {
        return new CfnEvent(
                (String) map.get("RequestType"),
                (String) map.get("ResponseURL"),
                (String) map.get("StackId"),
                (String) map.get("RequestId"),
                (String) map.get("LogicalResourceId")
        );
    }

    boolean isDeleteEvent() {
        return "Delete".equals(requestType);
    }

    public String responseURL() {
        return responseURL;
    }

    public String stackId() {
        return stackId;
    }

    public String requestId() {
        return requestId;
    }

    public String logicalResourceId() {
        return logicalResourceId;
    }

    @Override
    public String toString() {
        return "CfnEvent{" +
                "requestType='" + requestType + '\'' +
                ", responseURL='" + responseURL + '\'' +
                ", stackId='" + stackId + '\'' +
                ", requestId='" + requestId + '\'' +
                ", logicalResourceId='" + logicalResourceId + '\'' +
                '}';
    }
}
