<details>
  <summary>Item 1 : 생성자 대신 정적 팩토리 메서드를 고려하라</summary>
  <div markdown="1">
    
### Item 1 : 생성자 대신 정적 팩토리 메서드를 고려하라

- 클래스는 클라이언트에게 public 생성자 대신 static 팩토리 메서드를 제공할 수 있습니다.
- 여기서 말하는 팩토리 메서드는 디자인 패턴에서 나오는 팩토리 메서드 패턴과 다른 의미입니다.

---

#### 장점 1 : 이름을 가질 수 있다.

```java
public class Book {

    private String name;

    private Book(String name) {
        this.name = name;
    }

    public static Book createBookWithName(String name) {
        return new Book(name);
    }
}
```

- 생성자에 넘기는 매게변수와 생성자 자체만으로는 반환될 객체의 특성을 제대로 설명하지 못한다.
- 따라서 위와 같이 생성자는 private로 선언하고 static을 통해 객체를 생성할 수 있도록 하면 createBookWithName 이라는 이름을 가지게 되어 명시적인 선언이 가능하다.

- 한 클래스에 시그니처가 같은 생성자가 여러 개 필요할 것 같으면, 생서자를 정적 팩터리 메서드로 바꾸고 각각의 차이를 잘 드러내는 이름을 지어주자.



#### 장점2 :  호출될 때마다 인스턴스를 새로 생성하지는 않아도 된다.

```java
public class Singleton {

    private static Singleton singleton = new Singleton();

    private Singleton() {
    }

    public static Singleton getSingletonInstance() {
        return singleton;
    }
}
```

- 보통 간단한 싱글톤 객체를 만들 때 이 방법을 사용한다.
- 해당 방법을 사용하면 생성 비용이 큰 객체가 자주 요청되는 상황에서 성능을 상당히 끌어올려 준다.



#### 장점3 : 반환 타입의 하위 타입 객체를 반환할 수 있는 능력이 있다.

이 능력은 반환할 객체의 클래스를 자유롭게 선택할 수 있게 하는 '엄청난 유연성'을 선물한다.

```java
public interface Item {

    void print();

    static Item createItem(String name) {
        return new Book2(name);
    }
}

class Book2 implements Item {

    String name;

    Book2(String name) {
        this.name = name;
    }

    @Override
    public void print() {
        System.out.println(name);
    }
}
```

- Item Interface와 이를 구현한 Book2가 존재할 때 Book2의 접근자를 pakage-private로 지정하면 다른 패키지에서는 Book2를 생설할 수 없다.
- 외부에서는 다음과 같이 Item을 이용해 Book2를 생성해야 하고, 이로 인해 Book2는 캡슐화되어 API를 작게 유지할 수 있게 된다.

```java
class Main {
    public static void main(String[] args){
        Item item = Item.createItem("LOW");
        item.print();
    }
}
```



#### 장점4 : 입력 매개변수에 따라 매번 다른 클래스의 객체를 반환할 수 있다.

객체의 하위 타입이면 매개 변수에 따라 원하는 클래스 객체를 반환할 수 있다.

```java
public interface Number {

    static Number createNumber(int num) {
        if(num > -1000 && num <1000) {
            return new SmallNumber();
        }
        return new BigNumber();
    }

}

class SmallNumber implements Number {

}

class BigNumber implements Number {

}
```

- Number란 인터페이스가 있을 때 매개변수의 값에 따라 객체를 반환하여 효율적으로 메모리를 사용할 수 있다.

#### 장점5: 정적 팩토리 메서드는 작성하는 시점에는 반환할 객체의 클래스가 존재하지 않아도 된다.

장점 3,4와 관련된 내용으로 메서드 안에서 객체를 반환할 때, 당장 클래스가 존재하지 않아도 특정 텍스트 파일에서 인터페이스 구현체의 위치를 알려주는 곳의 정보를 가지고 해당 객체를 읽어 생성할 수 있다.

```java
public abstract class StaticFactoryMethodType {
    
    public abstract void getName();
    
    public static StaticFactoryMethodType getNewInstance() {
        StaticFactoryMethodType temp = null;
        try {
            Class<?> childClass = Class.forName("algorithm.dataStructure.StaticFactoryMethodTypeChild");
            temp = (StaticFactoryMethodType) childClass.newInstance();
            
        }catch (ClassNotFoundException e) {
           System.out.println("클래스가 없습니다.");
        } catch (InstantiationException  e) {
            System.out.println("메모리에 올릴수 없습니다.");
        } catch (IllegalAccessException  e) {
            System.out.println("클래스파일 접근 오류입니다.");
        }
        
        return temp;
    }
}
```

```java
public class StaticFactoryMethodTypeChild extends StaticFactoryMethodType {

    @Override
    public void getName() {
        System.out.println("정상 로드 되었습니다");
    }

}
```

```java
public static void main(String args[]){
    StaticFactoryMethodType staticFactoryMethodType = StaticFactoryMethodType.getNewInstance();
    
    staticFactoryMethodType.getName();
}
```

---

#### 단점1 : 상속을 하려면 public이나 protected 생성자가 필요하니 정적 팩터리 메서드만 만들면 하위 클래스를 만들 수 없다.

단, 이 책에서 소개하고 있는 **Item17. 불변 타입**과 **Item18. 상속 보다는 컴포지션을 사용하라**의 제약을 지켜야 한다면 오히려 장점으로 받아들일 수도 있다.



#### 단점2 : 정적 팩터리 메서드는 프로그래머가 찾기 어렵다.

생성자 처럼 API 설명에 명확히 드러나지 않으니 사용자는 정적 팩터리 메서드 방식 클래스를 인스턴스화할 방법을 알아야한다. 즉 API 문서를 잘 써 놓고 메서드 이름도 널리 알려진 규약을 따라 짓는 식으로 문제를 완화해줘야 한다. 다음은 정적 팩터리 메서드에 흔히 사용하는 명명 방식들이다.



- **from** : 매개변수를 하나 받아서 해당 타입의 인스턴스를 반환하는 형변환 메서드

  ```java
  Date d = Date.from(instant);
  ```

- **of :** 여러 매개변수를 받아 적합한 타입의 인스턴스를 반환하는 집계 메서드

  ```java
  Set<Rank> faceCards = EnumSet.of(JACK, QUEEN, KING);
  ```

- **valueOf:**  from과 of의 더 자세한 버전

  ```java
  BigInteger prime = BigInteger.valueOf(Integer.MAX_VALUE);
  ```

- **instance 혹은 getInstance** : (매개변수를 받는다면) 매개변수로 명시한 인스턴스를 반환하지만, 같은 인스턴스 임을 보장하지는 않는다. (즉, 이전에 반환했던 것과 같을수도, 새로 생성된것을 반환할수도 있음)

  ```java
  StackWalker luke = StackWalker.getInstance(options);
  ```

- **create 혹은 newInstance :** instance 혹은 getInstance와 같지만 매번 새로운 인스턴스를 생성해 반환함을 보장한다.

  ```java
  Object newArray = Array.newInstance(classObject, arrayLen);
  ```

- **getType** : getInstance와 같지만, 생성할 클래스가 아닌 다른 클래스에 팩터리 메서드를 정의할 때 쓴다. `Type`은 팩토리 메서드가 반환할 객체의 타입이다.

- **newType :** newInstance와 같지만, 생성할 클래스가 아닌 다른 클래스에 팩토리 메서드를 정의할 때 쓴다. `Type`은 팩토리 메서드가 반환할 객체의 타입이다.



</div>
</details>


<details>
  <summary>Item 2 : 생성자에 매개변수가 많다면 빌더를 고려하라</summary>
  <div markdown="1">
    
   ## Item2. 생성자에 매개변수가 많다면 빌더를 고려하라

```java
public class NutritionFacts {
    private final int servingSize;
    private final int servings;
    private final int calories;
    private final int fat;
    private final int sodium;
    private final int carbohydrate;

    public NutritionFacts(int servingSize, int servings, int calories, int fat, int sodium,
        int carbohydrate) {
        this.servingSize = servingSize;
        this.servings = servings;
        this.calories = calories;
        this.fat = fat;
        this.sodium = sodium;
        this.carbohydrate = carbohydrate;
    }
}
```

**문제점** : 생성자를 만들 때 사용자가 설정하길 원치 않는 매개변수까지 포함하기 쉬운데, 어쩔 수 없이 그런 매개변수에도 값을 지정해줘야 한다. 이 코드에서는 매개변수가 겨우 6개뿐이라 그리 나빠보이지 않을 수 있지만, 수가 더 늘어나면 금세 걷잡을 수 없게 된다.

요약하자면, **점층적 생성자 패턴도 쓸 수는 있지만, 매개변수 개수가 많아지면 클라이언트 코드를 작성하거나 읽기 어렵다. 즉 프로덕션 코드가 오염된다.**



### 해결책 : 자바빈

```java
NutritionFacts nutritionFacts = new NutritionFacts();
nutritionFacts.setServingSize(1);
```

기본 생성자로 객체를 생성한 뒤, setter를 통해 원하는 값을 주입하는 방법이다.

하지만 자바빈즈는 자신만의 심각한 단점을 지니고 있다.

- 불변 객체를 만들 수 없다.
- 하나의 객체를 만드는데 여러개의 메서드를 호출(setter)해야 한다.
- 객체가 완전히 생성되기 전까지는 일관성이 무너진 상태에 놓이게 된다.
- 이 외에 멀티 스레드 환경에서 setter 인한 데이터 정합성 문제 등..



#### 해결책 : 빌더

```java
public class Person {

    private final String name;
    private final Integer age;
    private final Integer birth;
    private final boolean isStudent;

    public static class Builder {

        // 필수 매개변수는 final로 지정(초기화를 강제화)
        private final String name;
        private final Integer age;

        // 선택 매개변수
        private Integer birth = 000000;
        private boolean isStudent = true;

        // 필수 매개변수만 받는 생성자 호출
        public Builder(String name, Integer age) {
            this.name = name;
            this.age = age;
        }

        // setter
        public Builder birth(int val) {
            birth = val;
            return this;
        }

        public Builder isStudent(boolean val) {
            isStudent = val;
            return this;
        }

        public Person build() {
            return new Person(this);
        }
    }

    private Person(Builder builder) {
        name = builder.name;
        age = builder.age;
        birth = builder.birth;
        isStudent = builder.isStudent;
    }

}
```

클라이언트에서 필수 매개변수만 가지고 있는 **생성자를 호출**해서 객체를 만든 후, 필요하면 (빌더에서 제공하는) **Setter메소드**로 선택 매개변수의 값을 추가한다.

빌더는 생성할 클래스 안에 **정적 멤버 클래스**로 만들어두는 게 보통이다.

#### 장점

1. 작성하기 쉽고 **가독성과 사용성**이 높아진다.
2. 인자에 **불변식 적용**이 가능하다.
3. 각 설정 메서드마다 가변인자 사용이 가능하다.
4. **유연하다.**

#### 단점

1. 빌더를 만드는 시간적인 비용이 발생한다.

  -> Lombok 사용으로 극복 가능하다고 생각함.



### 마무리

1. 생성자나 정적 팩터리가 처리해야 할 **매개변수가 많다면 빌더 패턴**을 선택하는 게 더 낫다. 생성자나 정적 팩터리 방식으로 시작했다가 나중에 매개변수가 많아지면 빌더 패턴으로 전환할 수도 있지만, 이전에 만들어준 생성자와 정적 팩터리가 아주 도드라져 보일 것이다. 그러니 애초에 빌더로 시작하는 편이 나을 때가 많다.

2. 빌더는 점층적 생성자보다 클라이언트 **코드를 읽고 쓰기가 훨씬 간결**하고, 자바빈즈보다 훨씬 **안전**하다.


  </div>
</details>

<details>
  <summary>아이템3 ~ 4 싱글턴 패턴과 private  </summary>
  <div markdown="1">
    
   ## Item 3 : private 생성자나 열거 타입으로 싱글턴임을 보증하라.

`싱글턴`이란 인스턴스를 오직 하나만 생성할 수 있는 클래스를 말한다. 싱글턴의 전형적인 예로, 함수와 같은 무상태(stateless) 객체나 설계상 유일해야 하는 시스템(단 하나만 생성되어야 하는 시스템) 컴포넌트를 들 수 있다. 

생성자를 private으로 만들어 new를 통해 밖에서 호출 못하게, static method로 사용 가능.

ex) Spring Framework 안의 Bean들은 기본 scope이 ApplicationContext안의 scope에서 singleton.

---

#### 1. final 필드 방식의 싱글턴

```java
public class Singleton1 {

    public static final Singleton1 INSTANCE =new Singleton1();

    private Singleton1() {

    }
}
```

private 생성자는 `public static final` 필드인 Singleton1.INSTANCE를 초기화 할때 단 한번만 호출된다.  public 이나 protected 생성자가 없으므로 해당 클래스가 초기화될 때 만들어진 인스턴스가 전체 시스템에서 하나뿐임이 보장된다. 클라이언트는 손 쓸 방법이 없다. (리플렉션으로 접근하는 방법 제외)

##### 장점

1. 해당 클래스가 싱글턴임이 API에 명백히 드러난다. 즉, public static 필드가 final이니 절대로 다른 객체를 참조할 수 없다.
2. 간결하다.



#### 2. 정적 팩터리 방식의 싱글턴

```java
public class Elvis {
    private static final Elivs INSTANCE = new Elvis();
    private Elvis() { ... }
    public static Elvis getInstance () {
        return INSTANCE;
    }
    
    public void leaveTheBuilding() { ... }
}
```

`Elvis.getInstance()`는 항상 같은 객체의 참조를 반환하므로 제2의 Elvis 인스턴스란 결코 만들어지지 않는다. 

##### 장점

1. API를 바꾸지 않고도 싱글턴이 아니게 변경할 수 있다. (  return INSTANCE 부분을 new INSTANCE()로 변경)
2. 원한다면 정적 팩터리를 제너릭 싱글턴 팩토리로 만들 수 있다.

3. 정적 팩터리의 메서드 참조를 공급자(supplier)로 사용할 수 있다.

   **Supplier : `get`** 메서드만을 가지고 아무 Type이나 리턴할 수 있는 인터페이스

```java
Supplier<Elivs> supplier = Elivs::getInstance();
Elivs elivs = elivsSupplier.get();
```



#### 두 방식의 문제점

각 클래스를 직렬화한 후 역직렬화할 때 새로운 인스턴스를 만들어서 반환한다.

**역직렬화는 기본 생성자를 호출하지 않고 값을 복사해서 새로운 인스턴스를 반환**한다. 그때 통하는게 readResolve() 메서드이다.  이를 방지하기 위해 `readResolve` 메소드를 만들고 `transient` 키워드를 사용해 직렬화를 제외시킨다. 

싱글턴 클래스를 직렬화하려면 단순히 `Serializable`을 구현하고 선언하는 것만으로 부족하다.
모든 인스턴스 필드를 일시적(`transient`)라고 선언하고 `readResolve` 메서드를 제공해야 한다.

이렇게 하지 않으면 직렬화된 인스턴스를 역직렬화할 때마다 새로운 인스턴스가 만들어진다.

가짜 `Elvis`탄생을 예방하고 싶다면 `Elvis` 클래스에 다음의 `readResolve` 메서드를 추가하자

##### 역직렬화시 반드시 호출되는 `readResolve` 메소드를 싱글턴을 리턴하도록 수정

```java
private Object readResolve() {
    return INSTANCE;
}
```

진짜 Elvis를 반환하고, 가짜 Elvis는 가비지 컬렉터에 맡긴다.



### 3. 원소가 하나인 열거 타입을 선언하는 방식

```java
public enum Elvis {
	INSTANCE;
    
    public String getNema() {
        return "Elivs";
    }
    
    public void leaveTheBuilding() { ... }
}
```

Elvis 타입의 인스턴스는 INSTANCE 하나뿐. 더이상 만들 수 없다. 복잡한 직렬화 상황이나 리플렉션 공격에서도 제2의 인스턴스가 생기는 일을 완벽히 막아준다. 

단, 만들려는 싱글턴이 Enum 이외의 다른 상위 클래스를 상속해야 한다면 이 방법은 사용할 수 없다.





## Item 4 : 인스턴스화를 막으려거든 private 생성자를 사용하라

자바 언어로 개발을 하다보면, 유틸리티 클래스와 같은 도구용 클래스, 즉 정적 메서드와 멤버만을 가지고 있는 클래스를 만들어 사용할 일이 있다. 즉, 이러한 정적 클래스들은 인스턴스화를 위해 설계된 클래스가 아니다.
**그러나 생성자를 따로 정의하지 않으면 컴파일러가 자동으로 기본 생성자를 만들어준다.** 즉, 매개변수를 받지 않는 public 생성자가 만들어지며, 클라이언트 입장에서는 이 생성자가 자동 생성된 것인지 구분할 수 없다. 그렇기에 의도와는 다르게 인스턴스화 되는 상황이 발생한다고 한다.

```java
public class UtilityClass {
   // 기본 생성자가 만들어 지는 것을 막는다.
   private UtilityClass {
      throw new AssertionError();
   }
   
   // ...생략...
}
```

`AssertionError` 예외를 던질 필요까진 없을지 모르지만, 클래스 안에서라도 실수로 생성자를 호출하지 않도록 한다. 보통 주석을 달아서 호출할 수 없는 생성자라는 것을 보여준다.
인스턴스화를 막을 때 뿐만 아니라, 상속을 불가능하게 하는 효과도 있으므로 잘 활용하면 좋을 것 같다.
  </div>
</details>

<details>
  <summary>Item 5 : 자원을 직접 명시하지 말고 의존 객체 주입을 사용하라</summary>
  <div markdown="1">
   
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
  </div>
</details>



<details>
  <summary>Item 6 : 불필요한 객체 생성을 피하라</summary>
  <div markdown="1">
    
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

-> 이 말은 즉슨, 작은 성능을 위해 코드의 최적화를 포기하지 말자 라는 뜻으로 해석할 수 있다고 생각한다..(내 객관적인 의견)
  </div>
</details>


<details>
  <summary>Item 7 : 다 쓴 객체 참조를 해제하라 (메모리 누수 방지하기)</summary>
  <div markdown="1">
    
   ##  아이템 7. 다 쓴 객체 참조를 해제하라

### 메모리 직접 관리, 메모리 누수의 주범

자바에 GC(가비지 콜렉터)가 있기 때문에, GC가 다 쓴 객체를 알아서 회수해간다고 해서 메모리 관리에 더 이상 신경쓰지 않아도 된다는 것은 오해다.

아래 Stack을 사용하는 프로그램을 오래 실행하다 보면 점차 GC 활동과 메모리 사용량이 늘어나 결국 성능이 저하될 것이다.

과연 메모리 누수가 일어나는 위치는 어디일까?

```java
public class Stack {
    private Object[] elements;
    private int size = 0;
    private static final int DEFAULT_INITIAL_CAPACITY = 16;

    public Stack() {
        elements = new Object[DEFAULT_INITIAL_CAPACITY];
    }

    public void push(Object e) {
        ensureCapacity();
        elements[size++] = e;
    }

    public Object pop() {
        if (size == 0)
            throw new EmptyStackException();
        return elements[--size]; //메모리 누수가 일어나는 공간
    }

    /**
     * 원소를 위한 공간을 적어도 하나 이상 확보한다.
     * 배열 크기를 늘려야 할 때마다 대략 두 배씩 늘린다.
     */
    private void ensureCapacity() {
        if (elements.length == size)
            elements = Arrays.copyOf(elements, 2 * size + 1);
    }
}
```

메모리 누수가 일어나는 공간은 바로 **return elements[--size]** 부분이다. 그 이유는 --size를 실행함으로써 size라는 위치에 저장되어있는 배열은 더이상 쓰이지 않는 객체가 되었다. 이후 push가 실행되더라도 새로운 Object를 할당받기 때문에 해당 객체는 **가비지 컬렉터가 참조하지도 못하는** 메모리 누수의 주범이 된다.

해결책은 간단하다.

```java
public Object pop() {
    if (size == 0)
        throw new EmptyStackException();
    Object result = elements[--size];
    elements[size] = null; // 다 쓴 참조 해제
    return result;
}
```

 해당 원소의 참조가 더이상 필요 없어지는 시점(Stack에서 꺼낼 때, 사용이 완료됨)에 `null`로 설정하여 다음 GC가 발생할 때 레퍼런스가 정리되게 한다. 만약 `null` 처리한 참조를 실수로 사용하려 할 때 프로그램이 `NullPointerException`을 던지며 종료할 수 있다. (그 자리에 있는 객체를 비우지 않고 실수로 잘못된 객체를 돌려주는 것보다는 차라리 괜찮다. `null` 처리 하지 않았다면 잘못된 일을 수행할 것이다.)

**→ 프로그래머는 비활성 영역이 되는 순간 `null` 처리해서 해당 객체를 더는 쓰지 않을 것임을 가비지 컬렉터에게 알려야 한다.**



### 객체 참조를 `null` 처리하는 일은 예외적이어야 한다.

그렇다고 필요 없는 객체를 볼 때마다 `null` 처리하면, 오히려 프로그램을 필요 이상으로 지저분하게 만든다.
**객체 참조를 `null` 처리하는 일은 예외적인 상황에서나 하는 것이지 평범한 일이 아니다.**

필요없는 객체 레퍼런스를 정리하는 최선책은 그 레퍼런스를 가리키는 변수를 특정한 범위(scope)안에서만 사용하는 것이다. 변수의 범위를 가능한 최소가 되게 정의했다면(item 57) 이 일은 자연스럽게 이뤄진다.

```java
Object pop() {

	Object age = 24;

	...

	age = null; // X
}
```

`Object age`는 scope이 `pop()`안에서만 형성되어 있으므로 scope 밖으로 나가면 무의미한 레퍼런스 변수가 되기 때문에 GC에 의해 정리가 된다. (굳이 `null` 처리 하지 않아도 됨)

### 언제 레퍼런스를 `null` 처리 해야 하는가?

**메모리를 직접 관리하는 클래스는 메모리 누수를 조심해야 한다.**

**메모리를 직접 관리할 때**, `Stack` 구현체처럼 `elements`라는 배열을 관리하는 경우에 GC는 어떤 객체가 필요 없는 객체인지 알 수 없으므로, 해당 레퍼런스를 `null`로 만들어 GC한테 필요없는 객체들이라고 알려줘야 한다.

### 캐시, 메모리 누수의 주범 (Map 사용시 주의사항)

캐시를 사용할 때도 메모리 누수 문제를 조심해야 한다. 객체의 레퍼런스를 캐시에 넣어 놓고, 캐시를 비우는 것을 잊기 쉽다. 여러 가지 해결책이 있지만, **캐시의 키**에 대한 레퍼런스가 캐시 밖에서 필요 없어지면 해당 엔트리를 캐시에서 자동으로 비워주는 `WeakHashMap`을 쓸 수 있다.

**캐시 구현의 안 좋은 예 - 객체를 다 쓴 뒤로도 key를 정리하지 않음.**

```java
public class CacheSample {
	public static void main(String[] args) {
		Object key = new Object();
		Object value = new Object();

		Map<Object, List> cache = new HashMap<>();
		cache.put(key, value);
		...
	}
}
```

key의 사용이 없어지더라도 `cache`가 key의 레퍼런스를 가지고 있으므로, GC의 대상이 될 수 없다.

**해결 - `WeakHashMap`**

캐시 외부에서 key를참조하는 동안만 엔트리가 살아있는 캐시가 필요하다면 `WeakHashMap`을 이용한다.
다 쓴 엔트리는 그 즉시 자동으로 제거된다. 단, `WeakHashMap`은 이런 상황에서만 유용하다.

```java
public class CacheSample {
	public static void main(String[] args) {
		Object key = new Object();
		Object value = new Object();

		Map<Object, List> cache = new WeakHashMap<>();
		cache.put(key, value);
		...
	}
}
```

캐시 값이 무의미해진다면 자동으로 처리해주는 `WeakHashMap`은 key 값을 모두 Weak 레퍼런스로 감싸 hard reference가 없어지면 GC의 대상이 된다.

즉, `WeakHashMap`을 사용할 때 key 레퍼런스가 쓸모 없어졌다면, (key - value) 엔트리를 GC의 대상이 되도록해 캐시에서 자동으로 비워준다.



### 결론

메모리 누수는 겉으로 잘 드러나지 않아 시스템에 수년간 잠복하는 사례도 있다. 이런 누수는 철저한 코드 리뷰나 힙 프로파일러 같은 디버깅 도구를 동원해야만 발견되기도 한다. 그래서 이런 종류의 문제는 예방법을 익혀두는 것이 매우 중요하다.
  </div>
</details>


<details>
  <summary>Item 9: try-finally 보다는 try-with-resources를 사용하라</summary>
  <div markdown="1">
    
   ## 아이템 9. try-finally 보다는 try-with-resources를 사용하라

자바 라이브러리에는 close 메소드를 호출해 직접 닫아줘야 하는 자원이 많다. 

ex) `InputStream`, `OutputStream`, `java.sql.Connection`

자원 닫기는 클라이언트가 놓치기 쉬워서 예측할 수 없는 성능 문제로 이어지기도 한다. 이런 자원 중 상당수가 안전망으로 `finalizer`를 활용하고는 있지만 finalizer는 그리 믿을만하지 못하다.(아이템8)

**try-finally - 더 이상 자원을 회수하는 최선의 방책이 아니다!**

```java
public static String firstLineOfFile(String path) throw IOException {
    BufferedReader br = new BufferedReader(new FileReader(path));
    try {
        return br.readLine();
    } finally {
        br.close();
    }
}
```

**자원이 둘 이상이면 try-finally 방식은 너무 지저분하다!**

```java
static void copy(String src, String dst) throws IOException {
	InputStream in = new FileInputStream(src);
	try {
		OutputStream out = new FileOutputStream(dst);
		try {
			byte[] buf = new byte[BUFFER_SIZE];
			int n;
			while ((n = in.read(buf)) >= 0)
			out.write(buf, 0, n);
		} finally {
			out.close();
		}
	} finally {
		in.close();
	}
}
```

앞의 두 코드 예제에는 결점이 있다.
예외는 *try* 블록과 *finally* 블록 모두에서 발생할 수 있다. (자원을 해제하는 `close` 메서드를 호출할 때)
*finally* 블록 내에서 다시 *try-finally* 를 사용하게 되면, 코드는 길어지고 가독성도 떨어진다.



### try-with-resources

이 구조를 사용하려면 해당 자원이 `AutoCloseable` 인터페이스를 구현해야한다.
(`AutoCloseable`: 단순히 `void`를 반환하는 `close` 메서드 하나만 정의한 인터페이스다.)
닫아야 하는 자원을 뜻하는 클래스를 작성한다면 `AutoCloseable`을 반드시 구현해야 한다.

```java
class Temp implements AUtoCloseable {
    ...
}
```

**try-with-resources - 자원을 회수하는 최선책!**

```java
public static String firstLineOfFile(String path) throw IOException {
    try (BufferedReader br = new BufferedReader(new FileReader(path))) {
        return br.readLine();
    } catch (Exception e) {
        return defaultVal;
    }
}
```

**복수의 자원을 처리하는 try-with-resources - 짧고 매혹적이다!**

```java
static void copy(String src, String dst) throws IOException {
	try (InputStream in = new FileInputStream(src);
		OutputStream out = new FileOutputStream(dst)) {
		byte[] buf = new byte[BUFFER_SIZE];
		int n;
		while ((n = in.read(buf)) >= 0)
		out.write(buf, 0, n);
	}
}
```



### 결론

꼭 회수해야 하는 자원을 다룰 떄는 try-finally 말고, try-with-resources를 사용하자. 예외는 없다.(만약 자바 7 미만을 사용한다면.. 어쩔수 없다.)  코드는 더 짧고 분명해지고, 만들어지는 예외 정보도 훨씬 유용하다. try-finally로 작성하면 실용적이지 못할 만큼 코드가 지저분해지는 경우라도, try-with-resources로는 정확하고 쉽게 자원을 회수할 수 있다.
  </div>
</details>

---




