## Item 9. try-finally 보다는 try-with-resources를 사용하라

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
