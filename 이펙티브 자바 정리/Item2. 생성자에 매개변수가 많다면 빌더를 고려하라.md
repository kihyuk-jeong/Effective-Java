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

