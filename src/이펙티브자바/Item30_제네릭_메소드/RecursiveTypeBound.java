package 이펙티브자바.Item30_제네릭_메소드;

import java.util.Collection;
import java.util.List;

public class RecursiveTypeBound {

    public static <E extends Comparable<E>> E max(Collection<E> c) {

        if(c.isEmpty()) {
            throw new IllegalArgumentException("컬렉션이 비어 있습니다.");
        }

        E result = null;
        for (E e : c) {
            if (result == null || e.compareTo(result) > 0) {
                result = e;
            }
        }

        return result;
    }

    public static void main(String[] args) {
        List<String> argList = List.of("톰", "제리", "애니");
        System.out.println(max(argList));
    }
}
