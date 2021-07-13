package 자바코딩의기술.step2.API활용하기;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import 자바코딩의기술.step2.순회하며컬렉션수정하지않기.Supply;

class Inventory {

    private List<Supply> supplies = new ArrayList<>();

    int getQuantity(Supply supply) {
        if (supply == null) {
            throw new NullPointerException("supply must not be null");
        }

        int quantity = 0;
        for (Supply supplyInStock : supplies) {
            if (supply.equals(supplyInStock)) {
                quantity++;
            }
        }

        return quantity;

    }
}

/**
 * java API를 활용하자는 취지
 * frequency : frequency(여기에, 이게 몇개나 들어있을까)  결과를 리턴한다. (int형)
 * requireNonNull : requireNonNull(이 객체가 null 이면 ? , 예외와 함께 예외 메시지 출력) 예외는 NPE
 * java API를 활용하면 위 코드를 단 3줄로 처리가 가능하다.
 */

class Inventory_improving {

    private List<Supply> supplies = new ArrayList<>();

    int getQuantity(Supply supply) {
        Objects.requireNonNull(supply, "supply must not be null");

        return Collections.frequency(supplies, supply);
    }
}