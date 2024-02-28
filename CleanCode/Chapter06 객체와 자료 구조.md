# 객체와 자료 구조
- 변수를 private로 정의하는 이유가 있음
  - 변수에 의존하지 않았으면 싶어서
- get 함수와 set 함수를 당연하게 public으로 노출하는 걸까?

## 자료 추상화
- [6-1] 예제
  ```java
  public class Point{
      public double x;
      public double y;
  }
  ```
- [6-2] 예제
  ```java
  public interface Point{
      double getX();
      double getY();
      void setCartesian(double x, double y);
      double getR();
      double getTheta();
      void setPolar(double r, double theta);
  }
  ```
  - 인터페이스는 직교좌표계를 사용하는지, 극좌표계를 사용하는지 알  수 없음
    - 그럼에도 인터페이스는 자료 구조를 명백하게 표현
  - 6-2는 자료 구조 이상을 표현
    - 클래스 메소드가 접근 정책을 강제
    - 좌표를 읽을 때는 가 값을 개별적으로 읽어야 함
    - 좌푤르 설정할 때는 두 값을 한꺼번에 설정
  - class Point()는 확실히 직교좌표계를 사용
    - 개별적으로 좌표 값을 읽고 설정하도록 강제
    - 변수를 private로 선언하더라도 각 값마다 get 함수와 set 함수를 제공한다면 구현을 외부로 노출하는 셈
  - 변수 사이에 함수라는 계층을 넣는다고 구현이 감춰지지 않음 => 구현을 감추려면 추상화가 필요
    - 추상 인터페이스를 제공해 사용자가 구현을 모른 채 자료의 핵심을 조작할 수 있어야 진정한 의미의 클래스

- [6-3] 예제
  ```java
  public interface Vehicle{
      double getFuelTankCapacityInGallons();
      double getGallonsOfGasoline();
  }
  ```
- [6-4] 예제
  ```java
  public interface Vehicle{
      double getPercentFuelRemaining();
  }
  ```
  - 6-3과 6-4에서는 6-4가 더 나음
  - 자료를 세세하게 공개하기보다는 추상적인 개념으로 표현하는 편이 나음
  - 인터페이스나 조회/설정 함수만으로 추상화가 이뤄지지 않음

## 자료/객체 비대칭
- 객체와 자료 구조의 개념은 사실상 정반대
  - 객체는 추상화 뒤로 자료를 숨긴 채 자료를 다루는 함수만 공개
  - 지료 구조는 자료를 그대로 공개하며 별다른 함수는 제공하지 않음
- 6-5 정차적인 도형
  ```java
  public class Square{
      public Point topLeft;
      public double side;
  }
  
  public class Rectangle{
      public Point topLeft;
      public double height;
      public double width;
  }
  
  public class Circle{
      public Point center;
      public double radius;
  }
  
  public class Geometry{
      public final double PI = 3.141592653689793;
      public double area(Object shape) throws NoSuchShapeException{
          if(shape instanceof Square){
              Square s = (Square) shape;
              return s.side * s.side;
          } else if(shape instanceof Rectangle){
              Rectangle r = (Rectangle) shape;
              return r.height * r.width;
          } else if(shape instanceof Circle){
              Circle c = (Circle) shape;
              return PI * c.radius * c.radius;
          }
          throw new NoSuchShapeException();
      }
  }
  ```
  - 클래스가 절차적  
  - Geometry 클래스에 새로운 함수를 추가하고 싶어도 도형 클래스는 아무런 영향을 받지 않음
  - 새로운 도형을 추가하고 싶다면 Geometry 클래스에 속한 함수를 모두 고쳐야 함
- 6-6 다목적 도형
  ```java
  public class Square implements Shape{
      private Point topLeft;
      private double side;
      public double area(){
          return side * side;
      }
  }
  
  public class Rectangle implements Shape{
      private Point topLeft;
      private double height;
      private double width;
      
      public double area(){
          return height * height;
      }
  }
  
  public class Circle implements Shape{
      private Point center;
      private double radius;
      private final double PI = 3.1415926536689793;
      public double area(){
          return PI * radius * radius;
      }
  }
  ```
  - 객체 지향적인 도형 클래스
    - area()는 다형 메소드
    - Geometry 클래스는 필요 없음
    - 새 함수를 추가하고 싶다면 도형 클래스 전부를 고쳐야 함
- 절차적인 코드는 새로운 자료 구조를 추가하기 어려움
  - 모든 함수를 고쳐야 함
- 객체 지향 코드는 새로운 함수를 추가하기 어려움
  - 모든 클래스를 고쳐야 함
- 즉, 객체 지향 코드에서 어려운 변경은 절차적 코드에서 쉬우며, 절차적 코드에서 어려운 변경은 객체 지향 코드에서 쉬움
  - 새로운 자료 타입이 필요한 경우 => 클래스와 객체 지향 기법이 가장 적합
  - 새로운 함수가 필요한 경우 => 절차적 코드와 자료 구조

## 디미터 법칙
- 잘 알려진 발견법으로, 모듈은 자신이 조작하는 객체의 속사정을 몰라야 한다는 법칙
- 객체는 자료를 숨기고 함수를 공개
- 객체는 조회 함수로 내부 구조를 공개하면 안 된다는 의미 => 내부 구조를 노출하는 셈
- 디미터 법칙 -> 클래스 C의 메소드 f는 다음 객체의 메소드만 호출해야 함
  - 클래스 C
  - f가 생성한 객체
  - f 인수로 넘어온 객체
  - C 인스턴스 변수에 저장된 객체

### 기차 충돌(train wreck)
```java
final String ouputDir = ctxt.getOptions().getScratchDir().getAbsolutePath();
```
- 여러 대의 객차가 한 줄로 이어진 기차처럼 보이기 때문 => 기차 충돌
  - 일반적으로 조잡하다 여겨지는 방식이므로 피하는 편이 좋음
```java
Options opts = ctxt.getOptions();
File scratchDir = opts.getScratechDir();
final String outputDir = scratchDir.getAbsolutePath();
```
- 객체라면 내부 구조를 숨겨야 하므로 확실히 디미터 법칙을 위반
- 자료 구조라면 당연히 내부 구조를 노출하므로 디미터 법칙이 적용되지 않음
- 조회 함수를 사용하는 바람에 혼란을 일으킴
```java
final String outputDir = ctxt.options.scratchDir.absolutePath;
```
- 디미터 법칙을 거론할 필요가 없어짐
- 자료 구조는 무조건 함수 없이 공개 변수만 포함하고 객체는 사적 변수와 공개 함수를 포함한다면 간단해짐
  - 하지만 단순한 자료 구조에도 조회 함수와 설정 함수를 정의하라 요구하는 프레임워크와 표준("bean")이 존재

### 잡종 구조
- 절반은 객체, 절반은 자료 구조
  - 중요한 기능을 수행하는 함수도 있고, 공개 변수나 공개 조회/설정 함수도 있음
  - 공개 조회/설정 함수는 사적 변수를 그대로 노출함
- 잡종 구조는 새로운 함수는 물론이고 새로운 자료 구조도 추가하기 어려움

### 구조체 감추기
- ctxt가 객체라면 뭔가를 하라고 말해야지 속을 드러내라고 말하면 안 됨
  - 임시 디렉토리의 절대 경로를 얻으려는 이유 -> 임시 파일을 생성하기 위해 -> ctxt 객체에 임시 파일을 생성하라고 시키는 것이 적당한 임무
  - 디미터 법칙을 위반하지 않음  

## 자료 전달 객체
- 자료 구조체의 전형적인 형태는 공개 변수만 있고 함수가 없는 클래스
- 자료 전달 객체(Data Transfer Object, DTO) => 굉장히 유용한 구조체
  - 데이터베이스와 통신하거나 소켓에서 받은 메시지의 구문을 분석할 때 유용
  - DTO는 데이터베이스에 저장된 가공되지 않은 정보를 응용 프로그램 코드에서 사용할 객체로 변환하는 일련의 단계에서 가장 처음으로 사용하는 구조체
- 일반적인 형태는 'Bean'구조
  - 빈은 private 변수를 조회/설정 함수로 조작
  - 사이비 캡슐화, 별다른 이익을 제공하지 않음

### 활성 레코드
- DTO의 특수한 형태
- 공개 변수가 있거나 사적 변수에 조회/설정 함수가 있는 자료구조지만, save나 find 같은 탐색 함수도 제공
  - 데이터베이스 테이블이나 다르소스에서 자료를 직접 변환한 결과
- 활성 레코드는 자료 구조로 취급
  - 비즈니스 규칙을 담으면서 내부 자료를 숨기는 객체는 따로 생성
    - 내부 자료는 활성 레코드의 인스턴스일 가능성이 높음

## 결론
- 객체는 동작을 공개하고 자료를 숨김
  - 기존 동작을 변경하지 않으면서 새 객체 타입을 추가하기 쉬움
  - 기존 객체에 새 동작을 추가하기는 어려움
- 자료 구조는 별다른 동작 없이 자료를 노출
  - 기존 자료 구조에 새 동작을 추가하기는 쉬움
  - 기존 함수에 새 자료 구조를 추가하기는 어려움
- 우수한 소프트웨어 개발자는 편견 없이 이 사실을 이해해 직면한 문제에 최적인 해결책을 취함