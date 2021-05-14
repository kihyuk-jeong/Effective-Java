<details>
  <summary>아이템1 : 생성자 대신 정적 팩토리 메서드를 고려하라</summary>
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
  <summary>아이템2 : 생성자에 매개변수가 많다면 빌더를 고려하라</summary>
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


