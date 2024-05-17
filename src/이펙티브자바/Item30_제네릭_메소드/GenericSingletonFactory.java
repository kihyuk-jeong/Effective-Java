package 이펙티브자바.Item30_제네릭_메소드;

import java.util.Objects;
import java.util.function.UnaryOperator;

public class GenericSingletonFactory {

    private static UnaryOperator<Objects> IDENTITY_FN = (t) -> t;

    @SuppressWarnings("unchecked")
    public static <T> UnaryOperator<T> identityFunction() {
        return (UnaryOperator<T>) IDENTITY_FN;
    }

    public static void main(String[] args) {
        String [] strings = {"a", "b", "c"};
        UnaryOperator<String> identity = identityFunction();
        for (String s : strings) {
            System.out.println(identity.apply(s));
        }

        Number [] numbers = {1, 2, 3};
        UnaryOperator<Number> identity2 = identityFunction();
        for (Number n : numbers) {
            System.out.println(identity2.apply(n));
        }
    }
}
