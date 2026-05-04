package server;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.function.Supplier;

final class RemoteCallLogger {
    private static final int MAX_VALUE_LENGTH = 500;

    private RemoteCallLogger() {
    }

    static <T> T log(String service, String method, Object[] params, Supplier<T> call) {
        String prefix = "[RMI] " + service + "." + method;
        System.out.println(prefix + " called by client params=" + format(params));
        try {
            T result = call.get();
            System.out.println(prefix + " result=" + format(result));
            return result;
        } catch (RuntimeException e) {
            System.out.println(prefix + " error=" + e.getClass().getSimpleName() + ": " + e.getMessage());
            throw e;
        }
    }

    private static String format(Object value) {
        if (value == null) {
            return "null";
        }
        String text;
        Class<?> type = value.getClass();
        if (type.isArray()) {
            if (type.getComponentType().isPrimitive()) {
                text = primitiveArrayToString(value);
            } else {
                text = Arrays.deepToString((Object[]) value);
            }
        } else {
            text = String.valueOf(value);
        }
        return text.length() > MAX_VALUE_LENGTH ? text.substring(0, MAX_VALUE_LENGTH) + "...(truncated)" : text;
    }

    private static String primitiveArrayToString(Object array) {
        int length = Array.getLength(array);
        Object[] boxed = new Object[length];
        for (int i = 0; i < length; i++) {
            boxed[i] = Array.get(array, i);
        }
        return Arrays.deepToString(boxed);
    }
}
