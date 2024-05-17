package 이펙티브자바.Item31_와일드카드;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.EmptyStackException;
import java.util.Iterator;

public class Stack<E> {
    private E[] elements;
    private int size = 0;
    private static final int DEFAULT_INITIAL_CAPACITY = 16;

    @SuppressWarnings("unchecked")
    public Stack() {
        elements = (E[]) new Object[DEFAULT_INITIAL_CAPACITY];
    }

    public void push(E e) {
        ensureCapacity();
        elements[size++] = e;
    }

    public E pop() {
        if (size == 0) {
            throw new EmptyStackException();
        }
        E result = elements[--size];
        elements[size] = null;
        return result;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    private void ensureCapacity() {
        if (elements.length == size) {
            elements = java.util.Arrays.copyOf(elements, 2 * size + 1);
        }
    }

    public void pushAll(Iterator<? extends E> src) {
        while (src.hasNext()) {
            E e = src.next();
            push(e);
        }
    }

    public void popAll(Collection<? super E> dst) {
        while (!isEmpty())
            dst.add(pop());
    }

    public static void main(String[] args) {
        Stack<Number> numberStack = new Stack<>();
        Iterable<Integer> integers = Arrays.asList(3, 1, 4, 5, 6);
        numberStack.pushAll(integers.iterator());

        Iterable<Double> doubles = Arrays.asList(3.1, 1.2, 4.4, 5.1, 6.2);
        numberStack.pushAll(doubles.iterator());

        Collection<Object> collection = new ArrayList<>();
        numberStack.popAll(collection);

        for (Object o : collection) {
            System.out.println(o);
        }
    }
}
