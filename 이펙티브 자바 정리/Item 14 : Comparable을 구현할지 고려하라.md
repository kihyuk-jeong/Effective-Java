#### ■ Comparable 인터페이스

Comparable 인터페이스는 객체간 비교를 용이하게 해주는 인터페이스이다. 이 인터페이스를 구현한 객체들은 서로간의 비교 연산이 가능하다. Comparable을 구현한 객체들의 배열은 다음처럼 손쉽게 정렬할 수 있다. 

```java
Arrays.sort(a);
```

 

자바 플랫폼 라이브러리의 모든 값 클래스와 열거타입이 Comparable을 구현했다. 알파벳, 숫자, 연대 같이 순서가 명확한 값 클래스를 작성한다면 반드시 Comparable 인터페이스를 구현하자.

 

#### ■ compareTo

Comparable 인터페이스의 유일한 메서드인 compareTo의 규약을 살펴 보겠다.

 

1. 매개 변수 인스턴스와 자신의 인스턴스를 비교하도록 해야한다. 또한 자신의 인스턴스가 매개변수보다 작으면 음수, 같으면 0, 크면 양수를 반환한다.
2. 모든 x,y에 대해 sgn(x.compareTo(y)) == -sgn(y.compareTo(x))여야 한다.
3. x.compareTo(y) > 0 && y.compareTo(z) > 0 이면 x.compareTo(z) > 0이다. (추이성)
4. x.compareTo(y) == 0 이면 모든 z에 대해 sgn(x.compareTo(z)) == sgn(y.compareTo(z)) 이다.

비교를 활용하는 클래스의 예로는 정렬된 컬렉션인 TreeSet과 TreeMap이 있고, 검색과 정렬 알고리즘을 활용하는 유틸리티 클래스인 Collections와 Arrays가 있다.

 

위의 compareTo의 마지막 규약은 필수는 아니지만 꼭 지키길 권한다. Collections,Set, 혹은 Map의 구현체에서 예상과 다르게 동작할 수 있기 때문이다. 이 인터페이스들은 equals 메서드의 규약을 따른다고 되어 있지만, 정렬된 컬렉션들은 동치성을 비교할 때 equals 대신 CompareTo를 사용하기 때문이다. 따라서 주의해야한다.

 

 

compareTo와 equals가 일관되지 않는 BigDecimal 클래스를 예로 생각해보자. 빈 HashSet 인스턴스를 생성한 다음 new BigDecimal("1.0")과 new BigDecimal("1.00")을 차례로 추가한다. 이 두 BigDecimal은 equals 메서드로 비교하면 서로 다르기 때문에 HashSet은 원소를 2개 갖게 된다. 하지만 HashSet 대신 TreeSet을 사용하면 원소를 하나만 갖게 된다. compareTo 메서드로 비교하면 두 BigDecimal 인스턴스가 똑같기 때문이다.

####  

#### ■ Comparable 인터페이스 구현 시 주의 사항

기존 클래스를 확장한 구체 클래스에서 새로운 값 컴포넌트(필드)를 추가했다면 compareTo 규약을 지킬 방법이 없다. 

이 주의사항은 equals에도 적용된다. 우회법은 역시 equals에서의 우회법과 같다. Comparable을 구현한 클래스를 확장해 값 컴포넌트를 추가하고 싶다면, 확장하는 대신 독립된 클래스를 만들고, 이 클래스에 원래 클래스의 인스턴스를 가리키는 필드를 두자. 그런 다음 내부 인스턴스를 반환하는 '뷰' 메서드를 제공하자. 이렇게 하면 바깥 클래스에 우리가 원하는 compareTo 메서드를 구현해 넣을 수 있다. 클라이언트는 필요에 따라 바깥 클래스의 인스턴스를 필드 안에 담긴 원래 클래스의 인스턴스로 다룰 수도 있을 것이다.

 

**기본 타입 필드를 비교할 때는 ">", "<" 이런 연산자를 사용하지 않고 자바7부터 추가된 정적 메서드인 Double.compare, Float.compare 사용을 권한다. 이 방식은 거추장스럽고 오류를 유발하니, 이제는 추천하지 않는다.**

```java
public final class CaseInsensitiveString implements Comparable<CaseInsensitiveString> {

    private String s;

    public int compareTo(CaseInsensitiveString cis) {
        return String.CASE_INSENSITIVE_ORDER.compare(s, cis.s); //대소문자 구분 없이 비교
    }

}
```

 

#### ■ 필드가 여러개인 클래스에서 Comparable 인터페이스 구현

클래스에 핵심 필드가 여러개라면 어느 것을 먼저 비교할지가 중요하다. 가장 핵심적인 필드부터 비교를 하면 된다. 다음 코드는 item 10의 PhoneNumber 클래스의 compareTo 메서드에서 중요한 코드부터 비교한 예시이다.

```java
public int compareTo(PhoneNumber pn) {
    int result = Short.compare(areaCode, pn.areaCode);
    if (result == 0)  {
        result = Short.compare(prefix, pn.prefix);
        if (result == 0)
            result = Short.compare(lineNum, pn.lineNum);
    }
    return result;
}
```

 

자바 8부터는 Comparator 인터페이스가 비교자 생성 메서드와 함께 연쇄 방식으로 비교자를 생성할 수 있게 되었다. 이 비교자들을 Comparable 인터페이스가 원하는 compareTo 메서드를 구현하는데 활용할 수 있다. 다만, 성능이 조금 느려질 수 있다.

```java
private static final Comparator<PhoneNumber> COMPARATOR =
        Comparator.comparingInt((PhoneNumber pn) -> pn.areaCode)
                .thenComparingInt(pn -> pn.prefix)
                .thenComparingInt(pn -> pn.lineNum);

public int compareTo(PhoneNumber pn) {
    return COMPARATOR.compare(this, pn);
}
```

####  

#### **■** **Comparator의 보조 생성 메서드**

Comparator는 수많은 보조 생성 메서드들로 중무장하고 있다. long과 double용으로는 thenComparingLong과 thenComparingDouble이 있다. short처럼 더 작은 정수 타입에는 위 PhoneNumber처럼 int용 메서드를 사용하면 된다.

마찬가지로 float은 double용 메서드를 사용하면 된다. Comparator 이렇게 자바의 모든 숫자용 기본 타입에 대한 비교자 생성 메서드를 지원한다.

 

#### **■ 안티 패턴 - 값의 차를 기준으로 비교**

**값의 차를 기준으로 첫 번째 값이 두 번째 값보다 작으면 음수를,** 두 값이 같으면 0을, 첫 번째 값이 크면 양수를 반환하는 compareTo나 compare 메서드를 만들면 안된다. 이 방식은 정수 오버플로를 일으키거나IEEE 754 부동소수점 계산 방식에 따른 오류를 낼 수 있다. 또한 이번 아이템에서 설명한 방법대로 구현한 코드보다 월등히 빠르지도 않을 것이다.

```java
static Comparator<Object> hashCodeOrder = new Comparator<Object>() {
    @Override
    public int compare(Object o1, Object o2) {
        return o1.hashCode() - o2.hashCode();
    }
}
```

 

아래의 두 가지 방법 중 하나를 선택하여 구현해야 한다.

```java
static Comparator<Object> hashCodeOrder = new Comparator<Object>() {
    @Override
    public int compare(Object o1, Object o2) {
        return Integer.compare(o1.hashCode(), o2.hashCode());
    }
}
static Comparator<Object> hashCodeOrder = Comparator.comparingInt(o -> o.hashCode());
```

 

#### **■ 핵심 정리**

- 순서를 고려해야 하는 값 클래스를 작성한다면 꼭 Comparable 인터페이스를 구현하여 검색, 비교 기능을 제공하는 컬렉션과 어우러지도록 한다.
- compareTo 메서드에서 필드의 값을 비교할 때 < 와 > 연산자는 쓰지 않는다.
- 박싱된 기본 타입 클래스가 제공하는 정적 compare 메서드나 Comparator 인터페이스가 제공하는 비교자 생성 메서드를 사용한다.
