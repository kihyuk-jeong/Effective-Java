package 자바코딩의기술.step2.순회하며_계산_집약적_연산하지_않기;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;
import 자바코딩의기술.step2.순회하며컬렉션수정하지않기.Supply;

class Inventory {

    private List<Supply> supplies = new ArrayList<>();

    List<Supply> find(String regex) {
        List<Supply> result = new LinkedList<>();
        for (Supply supply : supplies) {
            if (Pattern.matches(regex, supply.toString())) {
                result.add(supply);
            }
        }
        return result;
    }
}

class Inventory_improving {

    private List<Supply> supplies = new ArrayList<>();

    List<Supply> find(String regex) {
        List<Supply> result = new LinkedList<>();
        Pattern pattern = Pattern.compile(regex);
        for (Supply supply : supplies) {
            if (pattern.matcher(supply.toString()).matches()) {
                result.add(supply);
            }
        }
        return result;
    }
}