package io.github.ajurasz.infrastructure;

import io.github.ajurasz.flyless.Migrations;
import org.flywaydb.core.Flyway;

import static io.github.ajurasz.infrastructure.EnvVariables.*;

class FlywayMigrations implements Migrations {
    private final Flyway flyway;

    FlywayMigrations(Secrets secrets) {
        var secret = secrets.byName(dbSecret());
        flyway = Flyway.configure()
                .dataSource(toJdbcUrl(secret), secret.username(), secret.password())
                .locations("db/migration", "db/data", "db/stage/" + dbStage())
                .load();
    }

    private String toJdbcUrl(Secrets.RdsSecret secret) {
        return String.format("jdbc:mysql://%s:%d/%s", secret.host(), secret.port(), dbName());
    }

    @Override
    public int migrate() {
        return flyway.migrate().migrationsExecuted;
    }
}
