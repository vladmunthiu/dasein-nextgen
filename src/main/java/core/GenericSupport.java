package core;

import core.actions.Action;
import core.actions.Action1;
import core.actions.Func;
import core.actions.Func1;

import javax.naming.OperationNotSupportedException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;

/**
 * Created by vmunthiu on 2/15/2016.
 */
public class GenericSupport<T> {
    private Class<T> classType;
    private GenericInvocationHandler handler;

    public GenericSupport(Class<T> classType) {
        this.classType = classType;
        this.handler = new GenericInvocationHandler();
    }

    public void set(String key, Action value) {
        this.handler.set(key, value);
    }

    public void set(String key, Action1 value) {
        this.handler.set(key, value);
    }
    public void set(String key, Func value) {
        this.handler.set(key, value);
    }
    public void set(String key, Func1 value) {
        this.handler.set(key, value);
    }


    public T getInstance() {
        return (T) Proxy.newProxyInstance(classType.getClassLoader(),
                new Class[]{classType}, handler);
    }

    class GenericInvocationHandler implements InvocationHandler {
        public GenericInvocationHandler() {
            actions = new HashMap<String, Action>();
            actions1 = new HashMap<String, Action1>();
            functions = new HashMap<String, Func>();
            functions1 = new HashMap<String, Func1>();
        }

        public void set(String key, Action value) {
            actions.put(key, value);
        }

        public void set(String key, Action1 value) {
            actions1.put(key, value);
        }

        public void set(String key, Func value) {
            functions.put(key, value);
        }

        public void set(String key, Func1 value) {
            functions1.put(key, value);
        }

        private HashMap<String, Action> actions;
        private HashMap<String, Action1> actions1;
        private HashMap<String, Func> functions;
        private HashMap<String, Func1> functions1;

         @Override
        public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
             String methodName = method.getName();
                if(actions.containsKey(methodName)) {
                    actions.get(methodName).call();
                    return true;
                } else if(actions1.containsKey(methodName)) {
                    actions1.get(methodName).call(objects[0]);
                    return true;
                } else if (functions.containsKey(methodName)) {
                    return functions.get(methodName).call();
                } else if (functions1.containsKey(methodName)) {
                    return functions1.get(methodName).call(objects[0]);
                } else {
                    throw new OperationNotSupportedException();
                }
        }

    }
}
