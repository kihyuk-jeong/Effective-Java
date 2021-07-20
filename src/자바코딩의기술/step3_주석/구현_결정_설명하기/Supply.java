package 자바코딩의기술.step3_주석.구현_결정_설명하기;

import java.util.Objects;

public class Supply implements Comparable<Supply> {

    private final String name;

    public Supply(String name) {
        this.name = Objects.requireNonNull(name);
    }

    @Override
    public int compareTo(Supply o) {
        return this.name.compareTo(o.name);
    }
}