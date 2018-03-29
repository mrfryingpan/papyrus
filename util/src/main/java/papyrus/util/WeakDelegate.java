package papyrus.util;

import android.support.annotation.NonNull;

import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class WeakDelegate {

    @SuppressWarnings("unchecked")
    public static <T> T of(@NonNull T t) {
        final WeakReference<T> tRef = new WeakReference<>(t);
        return (T) Proxy.newProxyInstance(t.getClass().getClassLoader(), t.getClass().getInterfaces(), new InvocationHandler() {
            @Override
            public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
                T t = tRef.get();
                if (t != null) {
                    return method.invoke(t, objects);
                } else {
                    Class<?> returnType = method.getReturnType();
                    if (!returnType.isPrimitive()) {
                        return null;
                    } else if (returnType == boolean.class) {
                        return false;
                    } else {
                        return 0;
                    }
                }
            }
        });
    }

    @SuppressWarnings("unchecked")
    public static <T> T dummy(Class<T> clazz) {
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, new InvocationHandler() {
            @Override
            public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
                return null;
            }
        });
    }
}
