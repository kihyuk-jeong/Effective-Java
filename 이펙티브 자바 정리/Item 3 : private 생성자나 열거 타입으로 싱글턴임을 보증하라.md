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
