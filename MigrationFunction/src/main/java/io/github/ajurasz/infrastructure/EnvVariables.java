package io.github.ajurasz.infrastructure;

class EnvVariables {
    static String dbName() {
        return System.getenv("RDS_DB_NAME");
    }

    static String dbStage() {
        return System.getenv("DB_STAGE");
    }

    static String dbSecret() {
        return System.getenv("RDS_SECRET");
    }
}
