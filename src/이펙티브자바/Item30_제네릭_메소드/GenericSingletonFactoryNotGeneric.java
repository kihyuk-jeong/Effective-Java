package 이펙티브자바.Item30_제네릭_메소드;

import java.util.function.Function;

public class GenericSingletonFactoryNotGeneric {

    public static Function<String, String> stringIdentityFunction() {
        return (t) -> t;
    }

    public static Function<Number, Number> integerIdentityFunction() {
        return (t) -> t;
    }

    public static void main(String[] args) {
        String [] strings = {"a", "b", "c"};
        Function<String, String> identity = stringIdentityFunction();
        for (String s : strings) {
            System.out.println(identity.apply(s));
        }

        Number [] numbers = {1, 2, 3};
        Function<Number, Number> identity2 = integerIdentityFunction();
        for (Number n : numbers) {
            System.out.println(identity2.apply(n));
        }
    }
}
