# 함수

## 작게 만들어라
- 함수를 만드는 첫 번재 규칙은 '작게'
### 블록과 들여쓰기
- if 문/ else 문/ while 문 등에 들어가는 블록은 한 줄이어야 함
  - 거기서 함수를 호출
- 블록 안에서 호출하는 함수 이름을 적절히 짓는다면 코드를 이해하기도 쉬워짐
- 함수에서 들여쓰기 수준은 1단이나 2단을 넘어서면 안 됨
    - 함수를 읽고 이해하기 쉬워짐

## 한 가지만 해라
- 함수는 한 가지를 해야 한다. 그 한 가지를 잘 해야 한다. 그 한 가지만 해야 한다.
- 함수가 지정된 함수 이름 아래에서 추상화 수준이 하나인 단계만 수행한다면 그 함수는 한 가지 작업만 함
- 단순히 다른 표현이 아니라 의미 있는 이름으로 다르함수를 추출할 수 있다면 그 함수는 여러 가지 작업을 하는 셈
### 함수 내 섹션
- 한 하뭇에서 여러 가지 작업을 한다는 증거
- 한 가지 작업만 하는 함수는 자연스럽게 섹션으로 나누기 어려움

## 함수 당 추상화 수준은 하나로
- '한 가지' 작업만 하려면 함수 내 모든 문장이 동일한 추상화 수준에 있어야 함
- 한 함수 내에 추상화 수준을 섞으면 코드를 읽는 사람이 헷갈림
### 위에서 아래로 코드 읽기: 내려가기 규칙
- 코드는 위에서 아래로 이야기처럼 읽혀야 좋음
- 내려가기 규칙? 위에서 아래로 프로그램을 읽으면 함수 추상화 수준이 한 번에 한 단계씩 낮아짐
- 추상화 수준이 하나인 함수를 구현하기란 쉽지 않음
  - 하지만 매우 중요한 규칙
  - 핵심은 짧으면서 '한 가지'만 하는 함수

## Switch 문
- switch 문은 작게 만들기 어려움
- '한 가지' 작업만 하는 switch문도 만들기 어려움
  - 본질적으로 switch문은 N 가지를 처리
- 다형성을 이용
- 예제 : 직원 유형에 따라 다른 값을 계산해서 반환하는 함수
  ```java
  public Money calculatePay(Employee e) throws InvalidEmployeeType{
      switch (e.type){
        case COMMISSIONED:
            return calculateCommissionedPay(e);
        case HOURLY:
            return calculateHourlyPay(e);
        case SALARIED:
            return calculateSalariedPay(e);
        default:
            throw new InvalidEmployeeType(e.type);
      }
  }
  ```
  - 문제
    - 함수가 길다
    - '한 가지' 작업만 수행하지 않음
    - SRP(Single Responsibility Principle)를 위반
    - OCP(Open Closed Principle)를 위반
- 예제 : 문제 해결 코드
  ```java
  public abstract class Employee{
    public abstract boolean isPayday();
    public abstract Money calculatePay();
    public abstract void deliverPay(Money pay);
  }
  
  public interface EmployeeFactory{
    public Employee makeEmployee(EmployeeRecord r)
        throws InvalidEmployeeType;
  }
  
  public class EmployeeFactoryImpl implements EmployeeFactory{
    public Employee makeEmployee(EmployeeRecord r) throws InvalidEmployeeType{
        switch (r.type){
            case COMMISSIONED:
                return new CommissionedEmployee(r);
            case HOURLY:
                return new HourlyEmployee(r);
            case SALARIED:
                return new SalariedEmployee(r);
            default:
                throw new ImvalidEmployeeType(r.type);
        } 
    }
  }
  ```
  - switch 문을 추상 팩토리에 꽁꽁 숨김
- 다형적 객체를 생성하는 코드 안에서 switch 문을 상속 관계로 숨긴 후에 다른 코드에게 절대로 노출하지 않음

## 서술적인 이름을 사용해라
- 함수가 작고 단순할수록 서술적인 이름을 고르기도 쉬워짐
  - 이름이 길어도 괜찮음
- 서술적인 이름을 사용하면 개발자 머릿속에서도 설계가 뚜렷해져서 코드를 개선하기 쉬워짐
- 이름을 붙일 때는 일관성이 있어야 함
  - 모듈 내에서 함수 이름은 같은 문구, 명사, 동사를 사용

## 함수 인수
- 함수에서 이상적인 인수 개수는 0개(무항)이다.
  - 4개 이상은 특별한 이유가 필요하지만, 특별한 이유가 있어도 사용하면 안 됨
- 테스트 관점에서 보면 인수가 늘어날수록 각 인수에 유효한 값으로 모든 조합을 구성해서 테스트하기가 상당히 부담스러워짐
- 출력 인수는 입력 인수보다 이해하기 어려움
- 최선은 입력 인수가 없는 경우며, 차선은 입력 인수가 1개뿐인 경우
### 많이 쓰는 단항 형식
- 인수에 질문을 던지는 경우
- 인수를 뭔가로 변호나해 결과를 반환하는 경우
- 함수 이름을 지을 때는 두 경우를 분명히 구분
- 언제나 일관적인 방식으로 두 형식을 사용
- 아주 유용한 단항 함수 형식 => 이벤트
  - 이벤트 함수는 입력 인수만 있고 출력 인수는 없음
  - 이벤트 함수는 이벤트라는 사실이 코드에서 명확히 드러나야 함
  - 이름과 문맥을 주의해서 선택
- 위의 경우들이 아니라면 단항 함수는 가급적 피하기
### 플래그 인수
- 플래그 인수는 함수가 한꺼번에 여러 가지를 처리한다고 대놓고 공표하는 셈
### 이항 함수
- 인수가 2개인 함수는 인수가 1개인 함수보다 이해하기 어려움
- 이항 함수가 적절한 경우
  - 예: Point p = new Point(0, 0) 직교 좌표계 점은 일반적으로 인수 2개를 취함
- 프로그램을 짜다보면 불가피한 경우도 생김
  - 그만큼 위험이 따른다는 사실을 이해하고 가능하면 단항 함수로 바꾸도록 노력
### 삼항 함수
- 신중해야 함
- 예: assertEquals(1.0, amount, .001) 
  - 가치가 충분, 부동소수점 비교가 상대적이라는 사실은 언제든 주지할 중요한 사항
### 인수 객체
- 객체를 생성해 인수를 줄이는 방법은 눈속임이 아님
### 인수 목록
- 인수 개수가 가변적인 함수도 필요
- 예: String.format 메소드 public String format(String format, Object... args)
- 가변 인수를 취하는 함수는 단항, 이항, 삼항 함수로 취급할 수 있음
### 동사와 키워드
- 단항 함수는 함수와 인수가 동사/명사 쌍을 이뤄야 함
  - 예 : writeField(name)
- 함수 이름에 키워드를 추가하는 형식
  - 함수 이름에 인수 이름을 넣음
  - 예: assertExpectedEqualsActual(expected, actual)

## 부수 효과를 일으키지 마라
- 부수 효과는 거짓말
  - 많은 경우 일시적인 결합이나 순서 종속성을 초래
- 예 : UserValidator.java
  ```java
  public class UserValidator{
    private Cryptographer cryptographer;
  
    public boolean checkPassword(String userName, String password){
        User user = UserGateway.findByName(userName);
        if(user != User.NULL){
            String codedPhrase = user.getPhraseEncodedByPassword();
            String phrase = cryptographer.decrypt(codedPhrase, password);
            if("Valid Password".equals(phrase)){
                Session.initialize();
                return true;
            }
        }
        return false;
    }
  }
  ```
  - 함수가 일으키는 부수 효과 : Session.initialize() 호출
  - checkPassword 이름만 봐서는 세션을 초기화한다는 사실이 드러나지 않음
  - 일시적인 결합을 초래
    - checkPassword 함수는 세선을 초기화해도 괜찮은 경우에만 호출이 가능
  - checkPasswordAndInitializeSession 이름이 나음
    - '한 가지'만 한다는 규칙을 위반
### 출력 인수
- 예: appendFooter(s)
  - 무언가에 s를 바닥글로 첨부?
  - s에 바닥글을 첨부?
  - 인수 s는 입력일까? 출력일까?
    - 선언부 => public void appendFooter(StringBuffer report) => 출력
- 함수 선언부를 찾아보는 행위는 코드를 보다 주춤하는 행위와 동급
- 객체 지향 언어에서는 출력 인수를 사용할 필요가 거의 없음
  - 출력 인수로 사용하라고 설계한 변수가 바로 this
  - report.appendFooter()로 호출하는 편이 나음

## 명령과 조회를 분리하라
- 함수는 뭔가를 수행하거나 뭔가에 답하거나 둘 중 하나만 해야 함

## 오류 코드보다 예외를 사용하라
- 명령 함수에서 오류 코드를 반환하는 방식은 명령/조회 분리 규칙을 미묘하게 위반
  - if 문에서 명령을 표현식으로 사용하기 쉬운 탓
- 오류 코드 대신 예외를 사용하면 오류 처리 코드가 원래 코드에서 분리되어 코드가 깔끔해짐
### Try/Catch 블록 뽑아내기
- 코드 구존에 혼란을 일으키며, 정상적인 동작과 오류 처리 동작을 뒤섞음
- 예
  - 전
  ```java
  public void delete(Page page){
    try{
        deletePageAndAllReferences(page);
    }
    catch(Exception e){
        logError(e);
    }
  }
  
  private void deletePageAndAllReferences(Page page) throws Exception{
    deletePage(page);
    registry.deleteReference(page.name);
    configKeys.deleteKey(page.name.makeKey());
  }
  private void logError(Exception e){
    logger.log(e.getMessage());
  }
  ```
  - delete 함수는 오류만 처리
- 정상적인 동작과 오류 처리 동작을 분리하면 코드를 이해하고 수정하기 쉬워짐
### 오류 처리도 한 가지 작업이다
- 오류 처리도 '한 가지' 작업만 해야 함
### Error.java 의존성 자석
- Error enum(=> 의존성 자석)이 변한다면 Error enum을 사용하는 클래스 전부를 다시 컴파일하고 다시 배치해야 함
- 새 오류 코드를 추가하는 대신 기존 오류 코드를 재사용
- 요류 코드 대신 예외를 사용하면 새 예외를 Exception 클래스에서 파생
  - 재커파일/재배치 없이도 새 예외 클래스를 추가할 수 있음(OCP 예)

## 반복하지 마라
- 중복은 소프트웨어에서 모든 악의 근원
- 객체 지향 프로그래밍은 콛를 부모 클래스로 몰아서 중복을 없앰
  - 구조적 프로그래밍, AOP(Aspect Oriented Programming), COP(Component Oriented Programming) 어떤 면에서 중복을 제거하는 전략

## 구조적 프로그래밍
- 모든 함수와 함수 내 모든 블록에 입구와 출구가 하나만 존재
  - 함수는 return 문이 하나
  - 루프 안에서 break나 continue를 사용해선 안 됨
- 함수가 작다면 별 이익을 제공하지 못하고 함수가 아주 클 때만 상당한 이익을 제공

## 함수를 어떻게 짜죠?
- 처음에 서투른 코드를 빠짐없이 테스트하는 단위 테스트 케이스를 만들고 코드를 다듬고, 함수를 만들고, 이름을 바꾸고, 중복을 제거하는 등의 정리를 함

## 결론
- 요구사항 문서에 나오는 명사와 동사를 클래스와 함수 후보로 고려
  - 프로그래밍의 기술은 언제나 언어 설계의 기술

