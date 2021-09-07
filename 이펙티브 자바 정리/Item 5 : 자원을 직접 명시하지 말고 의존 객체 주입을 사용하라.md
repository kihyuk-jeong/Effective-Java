## Item 5 : 자원을 직접 명시하지 말고 의존 객체 주입을 사용하라

많은 클래스가 하나 이상의 자원에 의존한다. 가령 맞춤법 검사기는 사전에 의존하는데, 이런 클래스를 **정적 유틸 클래스**로 구현한 모습은 드물지 않게 볼 수 있다.

```java
// 정적 유틸리티 - 잘못된 예
public class SpellChecker {
   private static final Lexicon dictionary = new Lexicon(); // 특정 자원을 명시
   
   private SpellChecker() {} // 인스턴스화 방지 - 아이템 4
   
   // ...생략...
}
```

```java
// 싱글턴 - 잘못된 예
public class SpellChecker {
   private final Lexicon dictionary = new Lexicon(); // 특정 자원을 명시

   private SpellChecker() {} // 싱글턴 보증 - 아이템 3
   public static SpellChecker INSTANCE = new SpellChecker();
   
   // ...생략...
}
```

위 두가지 예 모두 사전을 단 하나만 사용한다고 가정한다는 점에서 그리 훌륭해 보이지는 않는다. (확장에 닫혀있다 = OCP 원칙 위반)

실전에서는 사전이 언어별로 따로 있고 특수 어휘용 사전을 별도로 두기도 한다. 심지어 테스트용 사전도 필요할 수 있다. 사전 하나로 이 모든 쓰임에 대응할 수 있기를 바라는건 너무 순진한 생각이다. 

`final` 키워드를 삭제하고 `setter` 메서드 등을 통해 사전을 그때그때 교체하는 방법도 생각해 볼 수 있지만, 어색하고, 오류를 내기 쉬우며, 멀티 스레드에서는 사용할 수 없다.
**사용하는 자원에 따라 동작이 달라지는 클래스에는 정적 유틸리티 클래스나 싱글턴 방식이 적합하지 않다.** 이 경우에는 직접 의존 객체를 주입받는 방식을 사용하자.

```java
// 의존 객체 주입 방식
public class SpellChecker {
   private final Lexicon dictionary; // 특정 자원을 명시하지 않음
   
   private SpellChecker(Lexicon dictionary) {
      this.dictionary = Objects.requireNonNull(dictionary);
   } // 의존성 주입
   
   // ...생략...
}
```

이 클래스를 사용하는 클라이언트는 상황에 맞는 사전을 생성자를 통해 주입하기만 하면 된다. 또한 불변을 보장하며, 같은 자원을 사용하려는 다른 클라이언트들과 안심하고 객체를 공유할 수 있다.

의존성 주입 패턴의 변형으로, 생성자에 자원 *팩토리를 넘겨주는 방식이 있다. 자바 8에서 등장하는 `Supplier<T>` 인터페이스가 팩토리를 표현한 완벽한 예이다.

> *팩토리란 팩토리 메서드 패턴(Factory Method Pattern)[Gamma95]에서 언급하는 형태의 클래스이며, 호출할때마다 특정 타입의 인스턴스를 반복해서 만들어주는 객체를 말한다.

이 방식을 사용하면 클라이언트는 자신이 명시한 타입의 하위 타입이라면 무엇이든 생성할 수 있는 팩토리를 넘길 수 있다. 예컨대 다음 코드는 클라이언트가 제공한 팩토리가 생성하는 타일(`Tile`)을 가지고 모자이크(`Mosaic`)를 만드는 메서드이다.

```java
Mosaic create(Supplier<? extends Tile> tileFactory) { ... }
```

사실 이런 의존 객체 주입이 유연성과 테스트 용이성을 개선해 주긴 하지만, 직접적으로 사용하는 경우는 거의 드물다. 의존성이 수천개나 되는 큰 프로젝트에서는 코드를 어지럽게 만들기도 하기 때문이다. 큰 규모의 프로젝트에서는 주로 스프링(Spring)과 같은 의존 객체 주입 프레임워크가 이와 같은 어지러움을 해소해 준다.
