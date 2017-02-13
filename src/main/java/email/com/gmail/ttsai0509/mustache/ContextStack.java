package email.com.gmail.ttsai0509.mustache;

import java.util.Arrays;

@SuppressWarnings("UnnecessaryBoxing")
class ContextStack {

    private static final int LOWER_CAPACITY = 8;
    private static final int RESET_CAPACITY = 16;
    private static final int UPPER_CAPACITY = 32;
    private static final char[] NULL_OUTPUT = "null".toCharArray();

    private int length;
    private boolean hitUpper;
    private ContextFrame[] stack;

    ContextStack(Object root) {
        this.hitUpper = false;
        this.length = 0;
        this.stack = new ContextFrame[RESET_CAPACITY];
        this.stack[length++] = new ContextFrame(root, -1, false);
    }

    ContextFrame top() {
        return stack[length - 1];
    }

    void pop() {
        length--;

        if (hitUpper && length == LOWER_CAPACITY)
            stack = ArrayResize.resize(stack, LOWER_CAPACITY);
    }

    void push(char[] variable, int location, boolean inverse) {
        if (length == stack.length)
            stack = ArrayResize.resize(stack, stack.length * 2);

        // Push new ContextFrame
        Object resolved = resolve(prepareForResolve(variable));
        stack[length++] = new ContextFrame(resolved, location, inverse);

        // Next time we hit LOWER_CAPACITY, recreate stack
        if (length == UPPER_CAPACITY) hitUpper = true;
    }

    char[] render(char[] variable) {
        Object resolved = resolve(prepareForResolve(variable));

        if (resolved == null)
            return NULL_OUTPUT;
        else if (resolved instanceof Object[])
            return Arrays.toString((Object[]) resolved).toCharArray();
        else
            return String.valueOf(resolved).toCharArray();
    }

    Object resolve(ResolutionState state) {
        DevUtils.validateCaller(
                DevUtils.getMethod(ContextFrame.class, "resolveMethod", Object.class, String.class, ResolutionState.class, ContextStack.class),
                DevUtils.getMethod(ContextStack.class, "render", char[].class),
                DevUtils.getMethod(ContextStack.class, "push", char[].class, int.class, boolean.class));

        // Attempt to resolve variables from each context frame
        Object resolved = null;
        for (int i = length - 1; i >= 0; i--) {
            resolved = stack[i].resolve(state, this);
            state.reset(); // This state should not be maintained out here.
            if (resolved != null) break;
        }

        return resolved;
    }

    private ResolutionState prepareForResolve(char[] variable) {
        // This effectively isolates string manipulation to ContextStack
        StringBuilder sb = new StringBuilder();
        ResolutionState state = new ResolutionState();
        for (char c : variable) {
            switch (c) {
                case '.':
                    if (sb.length() > 0) state.add(sb.toString());
                    sb = new StringBuilder();
                    break;

                case '(':
                case ')':
                case ',':
                    if (sb.length() > 0) state.add(sb.toString());
                    state.add(String.valueOf(c));
                    sb = new StringBuilder();
                    break;

                default:
                    sb.append(c);
                    break;
            }
        }
        if (sb.length() > 0) state.add(sb.toString());

        return state;

    }

}