package email.com.gmail.ttsai0509.mustache;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@SuppressWarnings({"SimplifiableIfStatement", "unchecked"})
class ContextFrame {

    private static final String SELF = "$";
    private static final String PARAM_START = "(";
    private static final String PARAM_END = ")";
    private static final String PARAM_NEXT = ",";

    private int index;
    private final int location;
    private final Object context;
    private final boolean visible;

    ContextFrame(Object context, int location, boolean inverse) {
        this.index = 0;
        this.location = location;
        this.context = context;
        this.visible = inverse == ((context == null)
                || (context instanceof Boolean && !((Boolean) context))
                || (context instanceof List && ((List) context).isEmpty())
                || (context instanceof Object[] && ((Object[]) context).length == 0));
    }

    void shift() {
        index++;
    }

    boolean canShift() {
        if (!this.visible)
            return false;
        if (context instanceof List)
            return index < ((List) context).size() - 1;
        if (context instanceof Object[])
            return index < ((Object[]) context).length - 1;
        return false;
    }

    boolean isRoot() {
        return location == -1;
    }

    boolean isVisible() {
        return visible;
    }

    int getLocation() {
        return location;
    }

    Object resolve(ResolutionState state, ContextStack stack) {
        Object root = context;
        if (context instanceof List)
            root = currentListObject(context);
        else if (context instanceof Object[])
            root = currentArrayObject(context);

        Object current = root;
        while (state.hasNext()) {

            String currVar = state.next();
            if (current == null)
                break;
            else if (SELF.equals(currVar))
                continue;
            else if (current instanceof Map)
                current = resolveMap(current, currVar);
            else
                current = resolveField(current, currVar);

            // Function application
            // TODO: Remove in favor of method lookup
            if (current instanceof Function)
                current = resolveFunction(current, root);
        }

        return current;
    }

    private Object currentListObject(Object context) {
        List list = (List) context;
        return (index < list.size()) ? list.get(index) : null;
    }

    private Object currentArrayObject(Object context) {
        Object[] array = (Object[]) context;
        return (index < array.length) ? array[index] : null;
    }

    private Object resolveMap(Object object, String variable) {
        return ((Map) object).get(variable);
    }

    private Object resolveField(Object object, String variable) {
        try {
            Field field = object.getClass().getDeclaredField(variable);
            field.setAccessible(true);
            return field.get(object);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            return null;
        }
    }

    private Object resolveFunction(Object object, Object param) {
        return ((Function) object).apply(param);
    }

    private Object resolveMethod(Object object, String variable, ResolutionState state, ContextStack stack) {
        List<Object> params = new ArrayList<>();
        while (!PARAM_END.equals(state.next())) {
           // Consume PARAM_START, PARAM_NEXT, PARAM_END
            params.add(stack.resolve(state));
        }

        List<Class<?>> paramTypes = params.stream().map(Object::getClass).collect(Collectors.toList());
        Class<?>[] paramTypesArray = (Class<?>[]) paramTypes.toArray(new Object[paramTypes.size()]);
        Object[] paramsArray = params.toArray(new Object[params.size()]);

        try {
            Method method = object.getClass().getDeclaredMethod(variable, paramTypesArray);
            method.setAccessible(true);
            return method.invoke(object, paramsArray);

        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            return null;
        }
    }

}