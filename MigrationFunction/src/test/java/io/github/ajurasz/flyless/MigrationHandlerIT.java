package io.github.ajurasz.flyless;

import com.amazonaws.services.lambda.runtime.Context;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.util.Map;

import static io.github.ajurasz.flyless.CfnResponse.Status.SUCCESS;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

@Testcontainers
@ExtendWith(MockitoExtension.class)
class MigrationHandlerIT {
    @Container
    private MySQLContainer mysql = new MySQLContainer(DockerImageName.parse("mysql:5.7.31"))
            .withDatabaseName(System.getenv("RDS_DB_NAME"))
            .withUsername(System.getenv("RDS_USERNAME"))
            .withPassword(System.getenv("RDS_PASSWORD"));

    @Mock
    private CfnResponse cfnResponse;

    @BeforeEach
    void setUp() {
        EnvironmentVariables.getenv().put("RDS_PORT", "" + mysql.getFirstMappedPort());
    }

    @Test
    @DisplayName("should run all migration scripts")
    void test() {
        // given
        var migrationHandler = new MigrationHandler(cfnResponse);
        var input = Map.<String, Object>of(
                "RequestType", "Create",
                "RequestId", "RequestId",
                "ResponseURL", "http://pre-signed-S3-url-for-response",
                "StackId", "arn:aws:cloudformation:us-east-1:123456789012:stack/MyStack/guid",
                "LogicalResourceId", "MyTestResource");

        // when
        var response = migrationHandler.handleRequest(input, new DummyContext());

        // then
        Assert.assertEquals(3, (int) response);
        verify(cfnResponse).send(eq(SUCCESS), any(CfnEvent.class), any(Context.class));
    }
}
