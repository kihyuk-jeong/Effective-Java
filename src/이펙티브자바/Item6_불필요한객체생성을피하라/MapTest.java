package 이펙티브자바.Item6_불필요한객체생성을피하라;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class MapTest {

    public static void main(String[] args) {
        Map<String, Integer> map = new HashMap<>();
        map.put("bugger", 1);
        map.put("pizza", 2);

        Set<String> set1 = map.keySet();
        Set<String> set2 = map.keySet();

        System.out.println("set1 삭제 전 : " +  set2.size());
        System.out.println("set1 삭제 전 : " + map.size());

        set1.remove("bugger");

        System.out.println("set1 삭제 후 : " + set2.size());
        System.out.println("set1 삭제 후 : " + map.size());
    }
}
