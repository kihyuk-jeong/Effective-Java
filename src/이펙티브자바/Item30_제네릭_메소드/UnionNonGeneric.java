package 이펙티브자바.Item30_제네릭_메소드;

import java.util.HashSet;
import java.util.Set;

public class UnionNonGeneric {

    public static Set union(Set s1, Set s2) {
        Set result = new HashSet(s1);
        result.addAll(s2);
        return result;
    }

    public static void main(String[] args) {
        Set guys = Set.of("톰", "제리");
        Set stooges = Set.of(1, 2, 3);
        Set all = union(guys, stooges);

        // 런타임 에러 발생
        for (Object o : all) {
            System.out.println((String)o);
        }
    }
}
