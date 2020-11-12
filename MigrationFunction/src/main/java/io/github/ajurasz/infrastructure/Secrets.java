package io.github.ajurasz.infrastructure;

public interface Secrets {
    RdsSecret byName(String secretName);

    class RdsSecret {
        private String username;
        private String password;
        private String host;
        private int port;

        RdsSecret(String username, String password, String host, int port) {
            this.username = username;
            this.password = password;
            this.host = host;
            this.port = port;
        }

        String username() {
            return username;
        }

        String password() {
            return password;
        }

        String host() {
            return host;
        }

        int port() {
            return port;
        }
    }
}
