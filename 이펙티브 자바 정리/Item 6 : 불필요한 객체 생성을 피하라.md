## Item 6 : 불필요한 객체 생성을 피하라

같은 기능의 객체를 새로 생성하는 대신, 객체 하나를 재사용하는 편이 나을 때가 많다. 특히, 불변 객체는 언제든 재사용할 수 있다.

### 문자열 재사용

```java
String s = new String("java");
String s2 = new String("java");
System.out.println(s == s2)  // false
```

위 예제는 항상 새로운 객체들 만들어 heap 메모리 영역 내 String pool 이라는 공간을 활용할 수 없게된다.

```java
String s = "java";
String s2 = "java";
System.out.println(s1 == s2) // true
```

리터럴 형식으로 String 객체를 생성하면  String pool 이라는 공간 내에서  같은 내용의 String 객체 생성시 동일한 객체(기존의 객체)를 참조하도록 한다. 따라서 메모리를 절약할 수 있다.



### 무거운 객체

만드는데 메모리나 시간이 오래 걸리는 객체 즉 "비싼 객체"를 반복적으로 만들어야 한다면 캐싱해두고 재사용할 수 있는지 고려하는 것이 좋다.

##### 재사용 빈도가 높고 생성비용이 비싼 경우 - 캐싱하여 재사용 하자.

```java
static boolean isRomanNumeralSlow(String s) {
    return s.matches("^(?=.)M*(C[MD]|D?C{0,3})"
            + "(X[CL]|L?X{0,3})(I[XV]|V?I{0,3})$");
}
```

이 코드의 문제점은 `String.matches` 메서드를 사용하는데 있다. `String.matches`는 정규표현식으로 문자열 형태를 확인하는 가장 쉬운 방법이지만, 성능이 중요한 상황에서 반복해 사용하기 적합하지 않다.
이 메서드가 내부에서 만드는 정규 표현식용 `Pattern`인스턴스는 **한 번 쓰고 버려저서 곧바로 가비지 컬렉션 대상이 된다**.
`Pattern`은 생성비용이 높은 클래스 중 하나이다. 만약 늘 같은 `Pattern`이 필요함이 보장되고 재사용 빈도가 높다면 아래와 같이 상수(`static final`)로 초기에 캐싱해놓고 재사용할 수 있다.

```java
public class RomanNumerals {
    private static final Pattern ROMAN = Pattern.compile(
            "^(?=.)M*(C[MD]|D?C{0,3})"
                    + "(X[CL]|L?X{0,3})(I[XV]|V?I{0,3})$");

    static boolean isRomanNumeralFast(String s) {
        return ROMAN.matcher(s).matches();
    }
}
```

생성비용이 비싼 객체라면 "캐싱" 방식을 고려해야 한다. 자주 쓰는 값이라면 `static final`로 초기에 캐싱해놓고 재사용 하자.

불변 객체인 경우에 안정하게 재사용하는 것이 매우 명확하다. 하지만 몇몇 경우 분명하지 않다.
어댑터를 예로 들면, 어댑터는 인터페이스를 통해 뒤에 있는 객체로 연결해주는 view라 여러 개 만들 필요가 없다.



### 어댑터

##### 같은 인스턴스를 대변하는 여러 개의 인스턴스를 생성하지 말자

```java
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
```

Map 인터페이스의 `keySet` 메서드는 Map 객체 안의 키 전부를 담은 `Set` 인터페이스의 뷰를 반환한다.
하지만, 동일한 Map에서 호출하는 `keySet` 메서드는 같은 Map을 대변하기 때문에 반환한 객체 중 하나를 수정하면 다른 모든 객체가 따라서 바뀐다. 

따라서 `keySet`이 뷰 객체 여러 개를 만들 필요도 없고 이득도 없다.

### 오토박싱

##### 의도치않은 오토박싱이 숨어들지 않도록 주의하자

```java
private static long sum() {
	Long sum = 0L;
	for(long i=0; i<=Integer.MAX_VALUE; i++) {
		sum += i;
	}
	return sum;
}
```

오토박싱은 기본 타입과 박싱된 기본 타입을 섞어 쓸 때 자동으로 상호 변환해주는 기술이다.
의미상으로는 별다를 것 없지만 성능에서는 그렇지 않다.

sum 변수를 프리미티브 타입인 `long` 대신 래퍼클래스인 `Long`으로 선언해서, 2^31승개나 Long 인스턴스가 생성된다.  (`long` 타입인 `i`가 `Long` 타입인 `sum` 인스턴스에 더해질 때마다)

**박싱된 기본 타입보다는 기본 타입을 사용하고, 의도치 않은 오토박싱이 숨어들지 않도록 주의하자.**



### 오해 금지

"객체 생성은 비싸니 피해야 한다"로 오해하면 안 된다.

특히나 요즘의 JVM에서는 별다른 일을 하지 않는 작은 객체를 생성하고 회수하는 일이 크게 부담되지 않는다.
프로그램의 명확성, 간결성, 기능을 위해 객체를 추가로 생성하는 것이라면 일반적으로 좋은 일이다.

-> 이 말은 즉슨, 작은 성능을 위해 코드의 최적화를 포기하지 말자 라는 뜻으로 해석할 수 있다고 생각한다.
