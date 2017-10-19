package parser;

import com.google.common.base.Joiner;

import java.util.Stack;

public class CustomStack<T> extends Stack<T> {

    public CustomStack(T initialValue) {
        push(initialValue);
    }

    @Override
    public synchronized String toString() {
        return Joiner.on("@").join(this);
    }
}
