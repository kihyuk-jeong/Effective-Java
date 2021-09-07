## equals는 일반 규약을 지켜 재정의하라

------

 equals 메서드는 재정의하기 쉬워 보이지만 곳곳에 함정이 도사리고 있어서 자칫하면 끔찍한 결과를 초래한다. 

**문제를 회피하는 가장 쉬운 길은 아예 재정의하지 않는 것이다.** 그냥 두면 그 클래스의 인스턴스는 오직 자기 자신과만 같게 된다. **그러니 다음에서 열거한 상황 중 하나에 해당한다면 재정의하지 않는 것이 최선이다.** 

 

- 각 인스턴스가 본질적으로 고유하다. 값을 표현하는게 아니라 동작하는 개체를 표현하는 클래스가 여기 해당한다. Thread가 좋은 예로, Object의 equals 메서드는 이러한 클래스에 딱 맞게 구현되었다. 

- 인스턴스의 '논리적 동치성(logical equality)을 검사할 일이 없다. 예컨대 java.util.regex.Pattern은 equals를 재정의해서 두 Pattern의 인스턴스가 같은 정규표현식을 나타내는지를 검사하는, 즉 논리적 동치성을 검사하는 방법도 있다. 하지만 설계자는 이 방식을 원하지 않거나 애초에 필요하지 않다고 판단할 수도 있다. 설계자가 후자로 판단했다면 Object의 기본 equals만으로 해결된다.

- 상위 클래스에서 재정의한 equals가 하위 클래스에도 딱 들어맞는다. 예컨대 대부분의 Set 구현체는 AbstractSet이 구현한 equals를 상속받아 쓰고, List 구현체들은 AbstractList로부터, Map 구현체들은 AbstractMap으로부터 상속받아 그대로 쓴다.

- 클래스가 private이거나 package-private이고 equals 메서드를 호출할 일이 없다. 위험을 철저히 회피하는 스타일이라 equals가 실수로라도 호출되는 걸 막고 싶다면 다음처럼 구현해두자.

  ```
  @Override
  public boolean equals(Object o) {
     throw new AssertionError(); // 호출 금지!
  }
  ```

그렇다면, equals를 재정의해야 할 때는 언제일까? 

> 객체 식별성(object identity; 두 객체가 물리적으로 같은가)이 아니라 논리적 동치성을 확인해야 하는데, 상위 클래스의 equals가 논리적 동치성을 비교하도록 재정의되지 않았을 때다. 주로 값 클래스들이 여기 해당한다.

 값 클래스란, Integer와 String처럼 값을 표현하는 클래스를 말한다. **equals가 논리적 동치성을 확인하도록 재정의해두면, 그 인스턴스는 값을 비교하길 원하는 프로그래머의 기대에 부응함은 물론 Map의 키와 Set의 원소로 사용할 수 있게 된다.** 

 

 값 클래스라 해도, 값이 같은 인스턴스가 둘 이상 만들어지지 않음을 보장하는 인스턴스 통제 클래스라면 equals를 재정의하지 않아도 된다. Enum도 여기에 해당한다. 이런 클래스에서는 어차피 논리적으로 같은 인스턴스가 2개 이상 만들어지지 않으니 논리적 동치성과 객체 식별성이 사실상 똑같은 의미가 된다. 따라서 Object의 equals가 논리적 동치성까지 확인해준다고 볼 수 있다. 

 

equals 메서드를 재정의할 때는 반드시 일반 규약을 따라야 한다. 다음은 Object 명세에 적힌 규약이다. 

 

------

equals 메서드는 동치관계(equivalence relation)를 구현하며, 다음을 만족한다. 

- **반사성(relfexivity) : null이 아닌 모든 참조 값 x에 대해, x.equals(x)는 true**
- **대칭성(symmetry) : null이 아닌 모든 참조 값 x, y에 대해, x.equals(y)가 true면 y.equals(x)도 true다**
- **추이성(transitivity) : null이 아닌 모든 참조 값 x,y,z에 대해 x.equals(y), y.equals(z)가 true라면, x.equals(z)도 true다**
- **일관성(consistency) : null이 아닌 모든 참조 값 x,y에 대해 x.equals(y)를 반복해서 호출하면 항상 true를 반환하거나 항상 false를 반환한다**
- **null-아님 : null아닌 모든 참조 값 x에 대해, x.equals(null)은 false다**

그렇다면, Object 명세에서 말하는 동치관계란 무엇일까? **쉽게 말해, 집합을 서로 같은 원소들로 이뤄진 부분집합으로 나누는 연산이다. 이 부분집합을 동치류(equivalence class; 동치 클래스)라 한다.** equals 메서드가 쓸모 있으려면 모든 원소가 같은 동치류에 속한 어떤 원소와도 서로 교환할 수 있어야 한다. 

 

------

#### 반사성

단순히 말하면 객체는 자기 자신과 같아야 한다는 뜻이다.

#### 대칭성

 두 객체는 서로에 대한 동치 여부에 똑같이 답해야 한다는 뜻이다. 이 책에서는, CaseInsensitiveString이라는 클래스와 String 클래스를 비교하고 있다. CaseInsensitiveString은 대소문자 구분이 없어서, String과 equals에서 true가 나올 수 있지만, String의 입장에서는 불가능하다. 그러므로, String 과 CaseInsensitiveString은 동치류가 아니다. 

#### 추이성

 첫 번째 객체와 두 번째 객체가 같고, 두 번째 객체와 세 번째 객체가 같다면, 첫 번째 객체와 세 번째 객체도 같아야 한다는 뜻이다. 

간단한 요건이지만 자칫하면 어기기 쉽다. 상위 클래스에는 없는 새로운 필드를 하위 클래스에 추가하는 상황을 생각해보자.

```
public class Point {
   private final int x;
   private final int y;
   
   public Point(int x, int y) {
      this.x = x;
      this.y = y;
   }
   
   @Override 
   public boolean equals(Object o) {
      if (!(o instanceof Point))
         return false;
      Point p = (Point)o;
      return p.x == x && p.y == y
      
   }
   
   ... // 나머지는 생략
}
```

이제 이 클래스를 확장해서 점에 색상을 더해본다.

 

```
public class ColorPoint extends Point {
   private final Color color;
   
   public ColorPoint(int x, int y, Color color) {
      super(x,y);
      this.color = color;
   }
   .. // 생략
}
```

equals 메서드는 어떻게 해야할까? 그대론 둔다면, Point의 구현이 상속되어 색상 정보는 무시한 채 비교를 수행한다. equals 규약을 어긴 것은 아니지만, 중요한 정보를 놓치게 되니 받아들일 수 없는 상황이다. 

 

그래서 다음과 같은 equals를 생각해보았다.

```
@Override
public boolean equals(Object o) {
   if (!(o instanceof ColorPoint))
      return false;
   return super.equals(o) && ((ColorPoint) o).color == color;
}
```

이 메서드는 일반 Point.equals(ColorPoint)의 결과와 ColorPoint.equals(Point)의 결과가 다를 수 있다. 

**즉, 대칭성이 위배 된다. Point는 ColorPoint의 색상을 무시하고, ColorPoint는 입력 매개변수의 클래스 종류가 다르다며, 매번 false만을 반환한다.** 

> ***여기서 드는 의문은, 실제로 대칭성을 고려해서 위와 같은 관계의 equals을 짜야할 필요가 있을까?\***

그렇다면, ColorPoint.equals가 Point와 비교할 때는 색상을 무시하도록 하면 어떨까?

```
@Override
public boolean equals(Object o) {
   if (!(o instanceof Point))
      return false;
   
   // o가 일반 Point면 색상을 무시하고 비교한다.
   if (!(o instanceof ColorPoint))
      return o.equals(this);
   
   // o가 ColorPoint면 색상까지 비교한다
   return super.equals(o) && ((ColorPoint) o).color == color;
   
}
```

이 방식은 대칭성은 지켜주지만, 추이성을 깨버린다.

```
ColorPoint p1 = new ColorPoint(1, 2, Color.RED);
Point p2 = new Point(1,2);
ColorPoint p3 = new ColorPoint(1, 2, Color.BLUE);

p1.equals(p2) == true
p2.equals(p3) == true

p1.equals(p3) == false
```

또한, 이 방식은 무한 재귀에 빠질 위험도 있다. Point의 또 다른 하위 클래스 SmellPoint를 만들고, equals는 같은 방식으로 구현했다고 해보자. 그런 다음 myColorPoint.equals(mySmellPoint)를 호출하면 StackOverflowError를 일으킨다. 계속해서 서로의 equals만 호출하면서 스택에 쌓일 것이다.

 

그럼 해법은 무엇일까? 사실 이 현상은 모든 객체 지향 언어의 동치관계에서 나타나는 근본적인 문제다

**구체 클래스를 확장해 새로운 값을 추가하면서 equals 규약을 만족시킬 방법은 존재하지 않는다.** 객체 추상화의 이점을 포기하지 않는 한은 말이다.

 

이 말은 얼핏, equals 안의 instanceof 검사를 getClass 검사로 바꾸면 규약도 지키고 값도 추가하면서 구체 클래스를 상속할 수 있다는 뜻으로 들린다. 

```
@Ovverride
public boolean equals(Object o) {
   if (o == null || o.getClass() != getClass())
      return false;
   Point p = (Point) o;
   return p.x == x && p.y == y;
}
```

이번 equals는 getClass()를 활용하여, 같은 구현 클래스의 객체와 비교할 때만 true를 반환한다. 괜찮아 보이지만 실제로 활용할 수는 없다. Point의 하위 클래스는 정의상 여전히 Point이므로 어디서든 Point로써 활용될 수 있어야 한다. 

 

단위 원 안에 점이 있는지를 판별하는 메서드가 필요하다고 해보자.

```
public static boolean onUnitCircle(Point p) {
   return unitCircle.contains(p);
}
```

이런 경우에, Point를 상속한 CounterPoint 의 인스턴스를 넘기면, 무조건 false를 반환할 것이다. 컬렉션 구현체에서는 대부분 equals를 통하여 contains 작업을 처리하기때문이다. **이처럼 리스코프 치환 원칙을 위반한다.**

 

구체 클래스의 하위 클래스에서 값을 추가할 방법은 없지만 괜찮은 우회 방법이 있다고 한다. "상속 대신 컴포지션을 사용하라"는 아이템 18을 따르면 된다. Point를 상속하는 대신 Point를 ColorPoint의 private 필드로 두고, ColorPoint와 같은 위치의 일반 Point를 반환하는 뷰(view) 메서드를 public으로 추가하는 식이다.

```
public class ColorPoint {
   private final Point point;
   private final Color color;
   
   public ColorPoint(int x, int y, Color color) {
      point = new Point(x, y);
      this.color = Objects.requireNonNull(color);
   }
   
   /**
    * 이 ColorPoint의 Point 뷰를 반환한다.
    */
    public Point asPoint() {
       return point;
    }
    
    @Override
    public boolean equals(Object o) {
       if (!(o instanceof ColorPoint))
          return false;
       ColorPoint cp = (ColorPoint) o;
       return cp.point.equals(point) && cp.color.equals(color);
    }
    ... // 나머지 생략
}
```

자바 라이브러리에도 종종 구체 클래스를 확장해 값을 추가한 클래스가 있다. 

java.sql.Timestamp는 java.util.Date를 확장했다. 그 결과 대칭성을 위배하고, 엉뚱하게 동작할 수 있다. 설계 실수니 절대 따라해서는 안된다.

 

> 상위 클래스를 직접 인스턴스로 만드는게 불가능하다면 지금까지 이야기한 문제들은 일어나지 않는다. 추상 클래스의 하위 클래스에서라면 equals 규약을 지키면서도 값을 추가할 수 있다.

 

 

#### 일관성

 두 객체가 같다면 앞으로도 영원히 같아야 한다는 뜻이다. 불변 객체는 한번 다르면 끝까지 달라야 한다. 

클래스가 불변이든 가변이든 equals의 판단에 신뢰할 수 없는 자원이 끼어들게 해서는 안 된다. 이 제약을 어기면 일관성 조건을 만족시키기가 아주 어렵다. 

 **예컨대 java.net.URL의 equals는 주어진 URL과 매핑된 호스트의 IP주소를 이용해 비교한다. 호스트 이름을 IP 주소로 바꾸려면 네트워크를 통해야 하는데, 그 결과가 항상 같다고 보장할 수 없다. 이는 URL의 equals가 일반 규약을 어기게 하고, 실무에서도 종종 문제를 일으킨다.** 이는 커다란 실수였으니 절대 따라해서는 안 된다. 이런 문제를 피하려면, equals는 항시 메모리에 존재하는 객체만을 사용한 결정적(deterministic) 계산만 수행해야 한다. 

 

#### null-아님

 이름처럼 모든 객체가 null과 같지 않아야 한다는 뜻이다. o.equals(null)이 true를 반환하는 상황은 상상하기 어렵지만, 실수로 NullPointerException 을 던지는 코드는 흔할 것이다. 이 일반 규약은 이런 경우도 허용하지 않는다. 수 많은 클래스가 입력이 null 인지를 확인해 자신을 보호한다.

```
// 명시적 null 검사 - 필요 없다!
@Override
public boolean equals(Object o) {
   if (o == null)
      return false;
   ...
}
```

instanceof는 첫 번째 피연산자가 null이면 false를 반환한다. 따라서 null 검사는 타입 확인 단계를 통하여 검사하는 것이 좋다.

 

 

------

지금까지의 내용을 종합해서 양질의 equals 메서드 구현 방법을 단계별로 정리해보았다.

1. == 연산자를 사용해 입력이 자기 자신의 참조인지 확인한다. 자기 자신이면 true를 반환한다. 성능 최적화용이다.
2. instanceof 연산자로 입력이 올바른 타입인지 확인한다. 
3. 입력을 올바른 타입으로 형변환한다. 
4. 입력 객체와 자기 자신의 대응되는 '핵심' 필드들이 모두 일치하는지 하나씩 검사한다. 

float, double을 제외한 기본 타입 필드는 == 연산자로 비교하고, 참조 타입 필드는 각각의 equals 메서드로, float와 double 필드는 각각 정적 메서드인 Float.compare(float, float), Double.compare(double, double)로 비교한다. Float.equals, Double.equals 메서드를 대신 사용할 수도 있지만, 오토박싱을 일으킬수있으니 성능상 좋지 않다. 

 

앞서의 CaseInsensitiveString 예처럼 비교하기가 아주 복잡한 필드를 가진 클래스도 있다. 이럴 때는 그 필드의 표준형을 저장해둔 후 표준형끼리 비교하면 경제적이다. 특히 불변 클래스에 제격이다. 가변은 갱신이 필요하다.

 

어떤 필드를 먼저 비교하느냐가 equals의 성능을 좌우하기도 한다. 최상의 성능을 바란다면 다를 가능성이 더 크거나, 비교하는 비용이 싼 필드를 먼저 비교하자. 동기화용 락(lock) 필드 같이 객체의 논리적 상태와 관련 없는 필드는 비교하면 안 된다. 

 

**equals를 다 구현했다면 세 가지만 자문해보자. 대칭적인가? 추이성이 있는가? 일관적인가?** 

 

드디어 마지막 주의사항이다.

- **equals를 재정의할 땐 hashCode도 반드시 재정의하자(아이템 11).**
- **너무 복잡하게 해결하려 들지 말자. 필드들의 동치성만 검사해도 어렵지 않게 지킬 수 있다.** 
- **Object 외의 타입을 매개변수로 받는 equals 메서드는 선언하지 말자. 재정의가 아니다 다중정의다.** 

이를 작성하고 테스트 하는 것은 지루하다. 이 작업을 대신해줄 오픈소스가 있다. 구글이 만든 AutoValue 프레임워크다. 대다수의 IDE도 같은 기능을 지원하지만, AutoValue가 깔끔하고 읽기 좋다고 한다.
