package email.com.gmail.ttsai0509.mustache;

import java.lang.reflect.Method;

enum DevUtils {

    ;

    static Method getMethod(Class<?> clazz, String method, Class<?>... params) {
        try {
            return clazz.getDeclaredMethod(method, params);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            return null;
        }
    }

    static void validateCaller(Method... valids) {
        StackTraceElement[] stack = Thread.currentThread().getStackTrace();

        StackTraceElement caller = stack[3];
        String callerClass = caller.getClassName();
        String callerMethod = caller.getMethodName();
        for (Method valid : valids) {
            if (valid == null) continue;
            String validClass = valid.getDeclaringClass().getCanonicalName();
            String validMethod = valid.getName();
            if (callerClass.equals(validClass) && callerMethod.equals(validMethod))
                return;
        }

        StackTraceElement callee = stack[2];
        String calleeClass = callee.getClassName();
        String calleeMethod = callee.getMethodName();
        throw new RuntimeException(String.format(
                "DevUtils: Method %s.%s can not be called by %s.%s",
                calleeClass, calleeMethod,
                callerClass, callerMethod)
        );
    }

}
