package io.github.ajurasz.infrastructure;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import software.amazon.awssdk.auth.credentials.EnvironmentVariableCredentialsProvider;
import software.amazon.awssdk.http.urlconnection.UrlConnectionHttpClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueRequest;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueResponse;

class AWSSecrets implements Secrets {
    private static final Logger log = LogManager.getLogger(AWSSecrets.class);
    private final SecretsManagerClient client;

    AWSSecrets() {
        client = SecretsManagerClient.builder()
                .httpClient(UrlConnectionHttpClient.create())
                .credentialsProvider(EnvironmentVariableCredentialsProvider.create())
                .region(Region.of(System.getenv("AWS_REGION")))
                .build();
    }

    @Override
    public RdsSecret byName(String secretName) {
        log.info("Get secret value for {}", secretName);
        var response = client.getSecretValue(buildRequest(secretName));
        return convert(response);
    }

    private GetSecretValueRequest buildRequest(String secretName) {
        return GetSecretValueRequest.builder()
                .secretId(secretName)
                .build();
    }

    private RdsSecret convert(GetSecretValueResponse response) {
        var secret = Json.from(response.secretString());
        return new RdsSecret(
                secret.get("username").asText(),
                secret.get("password").asText(),
                secret.get("host").asText(),
                secret.get("port").asInt()
        );
    }
}
