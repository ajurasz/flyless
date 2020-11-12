package io.github.ajurasz.flyless;

import java.lang.reflect.Field;
import java.util.Map;

class EnvironmentVariables {
    @SuppressWarnings("unchecked")
    static Map<String, String> getenv() {
        try {
            Map<String, String> env = System.getenv();
            Field field = env.getClass().getDeclaredField("m");
            field.setAccessible(true);
            return (Map<String, String>) field.get(env);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
