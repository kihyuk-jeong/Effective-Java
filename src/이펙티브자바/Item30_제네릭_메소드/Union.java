package 이펙티브자바.Item30_제네릭_메소드;

import java.util.HashSet;
import java.util.Set;

public class Union {
    public static <E> Set<E> union(Set<E> s1, Set<E> s2) {
        Set<E> result = new HashSet<E>(s1);
        result.addAll(s2);
        return result;
    }

    public static void main(String[] args) {
        Set<String> guys = Set.of("톰", "제리");
        Set<String> stooges = Set.of("1", "2", "3");

        // Set<Integer> stooges = Set.of(1, 2, 3);
        // 서로 다른 타입인 경우 컴파일 타임에 오류를 잡을 수 있음
        // Set all = union(guys, stooges);

        Set<String> all = union(guys, stooges);

        // 타입 캐스팅 필요 없이 바로 사용 가능
        for (String o : all) {
            System.out.println(o);
        }
    }
}
