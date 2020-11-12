package io.github.ajurasz.infrastructure;

class EnvSecrets implements Secrets {
    @Override
    public RdsSecret byName(String secretName) {
        return new RdsSecret(
                getenvOrDefault("RDS_USERNAME", "user"),
                getenvOrDefault("RDS_PASSWORD", "password"),
                getenvOrDefault("RDS_HOST", "sam_flyless_mysql"),
                Integer.parseInt(getenvOrDefault("RDS_PORT", "3306"))
        );
    }

    private String getenvOrDefault(String name, String defaultValue) {
        var value = System.getenv(name);
        return value != null ? value : defaultValue;
    }
}
