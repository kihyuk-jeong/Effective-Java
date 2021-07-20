package 자바코딩의기술.step3_주석.구현_결정_설명하기;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 구현 결정 설명하기란, 개발자가 왜 이 메소드를 선택했는지 설명해라는 의미이다.
 * 문제 해결 전 클래스의 isInStock 메소드를에 Collections.binarySearch 라는 메소드를 사용하고 있다.
 * 바로 위 주석으로 '빠른 구현' 이라고 해당 메소드의 사용 이유를 아주 간략하게 설명하고 있다.
 *
 */

class Inventory {

    private List<Supply> list = new ArrayList<>();

    void add(Supply supply) {
        list.add(supply);
        Collections.sort(list);
    }

    boolean isInStock(String name) {
        // fast implementation
        return Collections.binarySearch(list, new Supply(name)) != -1;
    }
}

// 개선 후, 훨씬 더 유용한 주석이 되었다.
class Inventory_improving {

    private List<Supply> list = new ArrayList<>();

    void add(Supply supply) {
        list.add(supply);
        Collections.sort(list);
    }

    boolean isInStock(String name) {
        /**
         * 재고가 남아있는지 재고명으로 확인해야 한다면,
         * 재고가 천 개 이상일 때 심각한 성능 이슈에 직면한다.
         * 1초 안에 항목을 추출하기 위해
         * 비록 재고를 정렬된 채로 유지해야 하지만
         * 이진 알고리즘을 쓰기로 결정했다.
         */
        return Collections.binarySearch(list, new Supply(name)) != -1;
    }
}

