package 이펙티브자바.Item29_제네릭_클래스.bounded_type;

import java.util.Arrays;
import java.util.EmptyStackException;

// Number 의 구현체 또는 Number 를 상속한 클래스로 한정
public class Stack<E extends Number> {
    private E[] elements;
    private int size = 0;
    private static final int DEFAULT_INITIAL_CAPACITY = 16;

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
            elements = Arrays.copyOf(elements, 2 * size + 1);
        }
    }

    public static void main(String[] args) {
        // 컴파일 오류 (String 은 Number 의 구현체가 아님)
        // Stack<String> stack = new Stack<>();

        Stack<Integer> stack = new Stack<>();
    }
}
