package io.github.ajurasz.infrastructure;

import io.github.ajurasz.flyless.CfnResponse;
import io.github.ajurasz.flyless.Migrations;

public class Application {
    private static final CfnResponse cfnResponse;
    private static final Migrations migrations;

    static {
        cfnResponse = new HttpCnfResponse();
        migrations = new FlywayMigrations(resolveSecretProvider());
    }

    private static Secrets resolveSecretProvider() {
        if (isAWSEnv()) {
            return new AWSSecrets();
        } else {
            return new EnvSecrets();
        }
    }

    private static boolean isAWSEnv() {
        return System.getenv("AWS_SAM_LOCAL") == null;
    }

    public static CfnResponse cfnResponse() {
        return cfnResponse;
    }

    public static Migrations migrations() {
        return migrations;
    }
}
