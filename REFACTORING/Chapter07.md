# 객체 간의 기능 이동

## 메서드 이동(Move Method)
- 메서드가 자신이 속한 클래스보다 다른 클래스의 기능이 더 많이 이용할 땐 그 메서드가 제일 많이 이용하는 클래스 안에서 비슷한 내용의 새 메서드를 작성하자
- 기존 메서드는 간단한 대리 메서들 전환하든지 아예 삭제하자

### 동기
- 클래스에 기능이 너무 많거나 클래스가 다른 클래스와 과하게 연동되어 의존성이 지나칠 때는 메서드를 옮기는 것이 좋음
  - 메서드를 옮기면 클래스가 간견해지고 여러 기능을 더 명확히 구현할 수 있음

### 방법
- 원본 클래스에 정의되어 있는 원본 메서드에 사용된 모든 기능을 검사하고 그 기능들도 전부 옮겨야할지 판단
  - 옮길 메서드에만 사용되는 기능도 그 메서드와 함께 옮겨야 함
  - 그 기능이 다른 메서드에도 사용된다면 그 메서드도 함께 옮기는 것을 고려
- 원본 클래스의 하위클래스와 상위클래스에서 그 메서드에 대한 다른 선언이 있는지 검사
  - 다른 선언이 있다면 대상 클래스에도 재정의를 넣을 수 있을 때만 옮길 수 있을지도 모름
- 그 메서드를 다른 대상 클래스 안에 선언
  - 대상 클래스 안에 있을 때 더욱 어울리는 다른 이름으로 그 메서드를 정의해도 됨
- 원본 메서드의 코드를 대상 메서드에 복사한 후, 대상 클래스 안에서 잘 돌아가게끔 대상 메서드를 약간 수정
  - 대상 메서드가 원본 메서드를 사용한다면 대상 메서드 안에서 원본 객체를 참조할 방법을 정해야 함
  - 대상 클래스에 원본 객체를 참조하는 기능이 없다면 원본 객체 참조를 대상 메서드에 매개 변수로 전달
  - 대상 메서드에 예외처리 코드가 들어 있다면 예외를 논리적으로 어느 클래스가 처리할지 정하자
- 대상 클래스를 컴파일
- 원본 객체에서 대상 객체를 참조할 방법 정하기
  - 대상 클래스를 참조하는 속성이나 메서드가 이미 존재할 수도 있지만, 없다면 대상 클래스를 참조하는 메서드를 쉽게 작성할 수 있는지 살펴봄
  - 쉽지 않다면 원본 클래스 안에 대상 클래스를 저장할 수 있는 새 속성을 선언
  - 수정한 코드를 그대로 둬도 되지만, 리팩토링을 완료할 때까지 임시로 놔뒀다가 나중에 삭제해도 됨
- 원본 메서드를 위임 메서드로 전환
- 컴파일과 테스트 실시
- 원본 메서드를 삭제하든지, 아님 위임 메서드로 사용하게 내버려두기
  - 참조가 많을 땐 원본 메서드를 위임 메서드로 내버려두는 방법이 더 편함
- 원본 메서드를 삭제할 때는 기존의 참조를 전부 대상 메서드 참조로 수정
  - 각 참조를 하나씩 수정하면서 그때마다 테스트를 실시해도 되지만, 찾아바꾸기 기능을 한 번 실행해서 모든 참조를 한꺼번에 수정하는 것이 더 간편

### 예제
- Account 클래스
  ```java
  class Account{
      private AccountType type;
      private int daysOverdrawn;
      
      double overdraftCharge(){
          if(type.isPremium()){
              double result = 10;
              if(daysOverdrawn > 7) result += (daysOverdrawn - 7) * 0.85;
              return result;
          }
          else return daysOverdrawn * 1.75;
      }
      
      double bankCharge(){
          double result = 4.5;
          if(daysOverdrawn > 0) result += overdraftCharge();
          return result;
      }
  }
  ```
  - 새 계좌 유형을 추가할 예정이며, 각 계정 유형마다 당좌대월 금액을 계산하는 공식이 다르다면 => overdraftCharge를 AccountType 클래스로 옮겨야 함
  - overdraftCharge 메서드가 이용하는 기능을 살펴보고 여러 메서드를 한꺼번에 옮겨야할지 고민
  - dayOverdrawn 필드는 Account 클래스에 그대로 둬야함
  - 메서드 이동
  ```java
  class AccountType{
    double overdraftCharge(int dayOverdrawn){
        if(isPremium()){
            double result = 10;
            if(daysOverdrawn > 7) result += (daysOverdrawn - 7) * 0.85;
            return result;
        }
        else return daysOverdrawn * 1.75;
    }
  }
  ```
  - 네 가지 작업 중 하나 실시
    - 그 기능을 대상 클래스로도 옮기기
    - 대상 클래스에서 원본 클래스로의 참조를 생성하거나 사용
    - 원본 객체를 대상 객체에 매개변수로 전달
    - 그 기능이 변수라면 그 변수를 매개변수로 전달
    - 변수를 매개변수로 전달
    ```java
    class Account{
        double overdraftCharge(){
            return type.overdraftCharge(daysOverdrawn);
        }
    }
    ```
  - 메서드를 원본 클래스 그대로 둬도 되고, 삭제해도 됨
    - 삭제하려면 호출 메서드를 전부 찾아서 AccountType 클래스에 들어 있는 메서드를 참조하게 수정
    ```java
    class Account{
        double bankCharge(){
            double result = 4.5;
            if(daysOverdrawn > 0) result += type.overdraftCharge(daysOverdrawn);
            return result;
        }
    }
    ```
    - 전부 수정했으면 Account 클래스에 들어 있는 메서드 정의를 삭제해도 됨
    - 매번 삭제한 후마다 테스트를 실시해도 되고, 한꺼번에 테스트해도 됨
  - 메서드가 private가 아니라면 이 메서드를 사용하는 다른 클래스를 찾아야 함
  - 원본 클래스의 여러 기능이 필요하거나 원본 클래스의 다른 메서드를 호출한다면 원본 객체를 전달하면 됨

## 필드 이동(Move Field)
- 어떤 필드가 자신이 속한 클래스보다 다른 클래스에서 더 많이 사용될 때는 대상 클래스 안에 새 필드를 선언하고 그 필드 참조 부분을 전부 새 필드 참조로 수정하자

### 동기
- 시스템이 발전할수록 새 클래스가 필요해지고 기능을 여기저기로 옮겨야 할 필요성이 생김
- 어떤 필드가 자신이 속한 클래스보다 다른 클래스에 있는 메서드를 더 많이 참조해서 정보를 이용한다면 그 필드를 옮기는 것을 생각
- 인터페이스에 따라 메서드를 옮기는 방법을 사용할 수도 있음
- 클래스 추출을 실시하는 중에도 필드 이동이 수반
  - 필드가 우선이고 메서드는 다음

### 방법
- 필드가 public이면 필드 캡슐화 기법 실시
  - 필드에 자주 접근하는 메서드를 옮기게 될 가능성이 높거나 그 필드에 많은 메서드가 접근할 때는 필드 자체 캡슐화 실시
- 컴파일과 테스트 실시
- 대상 클래스 안에 읽기/쓰기 메서드와 함께 필드 작성
- 대상 클래스 컴파일
- 원본 객체에서 대상 객체를 참조할 방법을 정하자
  - 기존 필드나 메서드에 대상 클래스를 참조하는 기능이 있을 수도 있음
  - 없다면 그런 기능의 메서드를 간편히 작성할 수 있는지 살펴보고 쉽게 만들수 없다면 원본 클래스에 대상 클래스를 저장할 수 있는 새 필드 생성
  - 그대로 둬도 되고 임시로 만들었다가 리팩토링 완료 후 삭제 가능
- 원본 클래스에서 필드 삭제
- 원본 필드를 참조하는 모든 부분을 대상 클래스에 있는 적절한 메서드를 참조하게 수정
  - 변수 접근 참조 부분은 대상 객체의 읽기 메서드 호출로 수정하고, 대입 참조 부분은 쓰기 메서드 호출로 수정
  - 필드가 private가 아니면 원본 클래스의 모든 하위클래스를 뒤져서 필드 참조 부분을 찾아내자
- 컴파일과 테스트 실시

## 예제 : 필드 캡슐화
- 전
  ```java
  class Account{
      private AccountType type;
      private double interestRate;
      
      double interestForAmountDays(double amount, int days){
          return interestRate * amount * days / 365;
      }
  }
  ```
- rate 필드를 AccountType 클래스로 옮기기
  ```java
  class AccountType{
      private double interestRate;
      
      void setInterestRate(double arg){
          interestRate = arg;
      }
      
      double getInterestRate(){
          return interestRate;
      }
  }
  ```
- Account 클래스 안의 메서드들을 AccountType 클래스를 사용하게끔 참조 변경하고, Account 클래스에서 interestRate 필드 삭제
  ```java
  double interestForAmountDays(double amount, int days){
      return type.getInterestRate() * amount * days / 365;
  }
  ```
  
### 예제 : 필드 자체 캡슐화
- 많은 메서드가 interestRate 필드를 사용한다면 필드 자체 캡슐화 실시
- 적절한 읽기/쓰기 메서드의 참조만 수정하면 됨
  ```java
  class Account{
    private AccountType type;
    private double interestRate;
  
    double interestForAmountDays(double amount, int days){
        return getInterestRate() * amount * days / 365;
    }
  
    private void setInterestRate(double arg){
        interestRate = arg; // => type.setInterestRate(arg);
    }
  
    private double getInterestRate(){
        return interestRate; // => type.getInterestRate();
    }
  }
  ```

## 클래스 추출(Extract Class)
- 두 클래스가 처리해야 할 기능이 하나의 클래스에 들어 있을 땐 새 클래스를 만들고 기존 클래스의 관련 필드와 메서드를 새 클래스로 옮기자

### 동기
- 클래스는 확실하게 추상화되어야 하며, 두세 가지의 명확한 기능을 담당해야 함
  - 클래스는 시간이 갈수록 방대해지기 마련
- 데이터나 메서드를 하나 제거하면 어떻게 될지, 다른 피륻와 메서드를 추가하는 건 합리적이지 않은지 자문해보기

### 방법
- 클래스의 기능 분리 방법을 정하자
- 분리한 기능을 넣을 새 클래스를 작성하자
  - 원본 클래스의 기능이 이름과 어울리지 않게 바뀌었으면 원본 클래스의 이름을 변경하자
- 원본 클래스에서 새 클래스로의 링크를 만들자
  - 양방향 링크를 만들어야 할 수도 있는데, 필요할 때까진 역방향 링크를 만들지 말자
- 옮길 필드마다 필드 이동 적용
- 필드를 하나씩 옮기 때마다 컴파일과 테스트 실시
- 메서드 이동을 실시해서 원본 클래스의 메서드를 새 클래스로 옮기자. 하급 메서드부터 시작해서 상급 메서드에 적용
- 메서드 이동을 실시할 때마다 테스트 실시
- 각 클래스를 다시 검사해서 인터페이스를 줄이자
  - 양방향 링크가 있다면 그것을 단방향으로 바꿀 수 있는지 알아보자
- 여러 곳에서 클래스에 접근할 수 있게 할지 결정하자. 여러 곳에서 접근할 수 있게 할 경우, 새 클래스를 참조 객체나 변경불가 값 객체로서 공개할지 여부를 결정하자

### 예제
- Person 클래스
  ```java
  class Person {
    private String name;
    private String officeAreaCode;
    private String officeNumber;
  
    public String getName() {
      return this.name;
    }
  
    public String getTelephoneNumber() {
      return "(" + this.officeAreaCode + ")" + this.officeNumber;
    }
  
    public String getOfficeAreaCode() {
      return this.officeNumber;
    }
  
    public void setOfficeNumber(String officeNumber) {
      this.officeNumber = officeNumber;
    }
  
    public String getOfficeNumber() {
      return this.officeNumber;
    }
  
    public void setOfficeAreaCode(String officeAreaCode) {
      this.officeAreaCode = officeAreaCode;
    }
  }
  ```
  - 전화번호 기능을 하나의 클래스로 떼어낼 수 있음 => 필드 이동
    - 전화번호 클래스 정의
      ```java
      class TelephoneNumber{
      }
      class Person {
        private TelephoneNumber officeTelephone = new TelephoneNumber();
      }
      ```
    - 필드 이동 및 메서드 이동 실시
      ```java
      class TelephoneNumber {
        private String areaCode;
      
        public String getAreaCode() {
            return areaCode;
        }
      
        public void setAreaCode(String areaCode) {
            this.areaCode = areaCode;
        }
      }
      
      class Person{
        public String getTelephoneNumber() {
            return "(" + getOfficeAreaCode() + ")" + this.officeNumber;
        }
      
        public String getOfficeAreaCode(){
            return officeTelephone.getAreaCode();
        }
      
        public setOfficeAreaCode(String arg){
            officeTelephone.setAreaCode(arg);
        }
      }
      ```
    - 메서드 이동
      ```java
      class Person {
        private String name;
        private TelephoneNumber officeTelephone = new TelephoneNumber();
      
        public String getName() {
          return name;
        }
      
        public TelephoneNumber getOfficeTelephone() {
          return this.officeTelephone.getTelephoneNumber();
        }
      }
      
      class TelephoneNumber {
        private String number;
        private String areaCode;
      
        public String getTelephoneNumber() {
          return "(" + this.areaCode + ")" + number;
        }
      
        public String getAreaCode() {
          return this.areaCode;
        }
      
        public void setAreaCode(String areaCode) {
          this.areaCode = areaCode;
        }
      
        public String getNumber() {
          return this.number;
        }
      
        public void setNumber(String number) {
          this.number = number;
        }
      }
      ```
- 그런 다음, 새 클래스를 클라이언트에 어느 정도 공개할지 결정
  - 인터페이스에 위임 메서드를 작성해서 새 클래스를 완전히 감추든지, 아니면 새 클래스를 공개할 수도 있음
    - 새 클래스를 공개하는 방식을 사용할 땐 왜곡의 위험을 고려해야 함
- 클래스 추출은 두 결과 클래스에 따로 락을 걸 수 있어서 병렬 실행 프로그램의 생동감을 향상시키는 용도로 흔히 사용되는 기법
  - 두 객체 락을 걸 필요가 없다면 이 기법을 실시하지 않아도 됨
  - 위험성 : 두 객체에 동시에 락을 걸어야 할 경우는 트랜잭션과 다른 종류의 공유 락 영역으로 넘어가게 됨

## 클래스 내용 직접 삽입(Inline Class)
- 클래스에 기능이 너무 적을 땐 그 클래스의 모든 기능을 다른 클래스로 합쳐 넣고 원래의 클래스는 삭제하자

### 동기
- 직접 삽입은 클래스 추출과 반대
- 클래스가 더 이상 제 역할을 수행하지 못하며 존재할 이유가 없을 때 실시
- 클래스의 기능 대부분을 다른 곳으로 옮기는 리팩토링을 실시해서 남은 기능이 거의 없어졌을 때

### 방법
- 원본 클래스의 public 프로토콜 메서드를 합칠 클래스에 선언하고, 이 메서드를 전부 원본 클래스에 위임
  - 원본 클래스의 메서드 대신 별도의 인터페이스가 알맞다고 판단되면 클래스 내용 직접 삽입을 실시하기 전에 인터페이스 추출 기법 실시
- 원본 클래스의 모든 참조를 합칠 클래스 참조로 수정
  - 원본 클래스를 private로 선언하고 패키지 밖의 참조를 삭제
  - 컴파일러가 껍데기만 남은 원본 클래스 참조를 찾아낼 수 있게 원본 클래스명을 변경
- 컴파일과 테스트 실시
- 메서드 이동과 필드 이동을 실시해서 원본 클래스의 모든 기능을 합칠 클래스로 차례로 옮기자
- 원본 클래스를 삭제

## 대리 객체 은폐(Hide Delegate)
- 클라이언트가 객체의 대리 클래스를 호출할 땐 대리 클래스를 감추는 메서드를 서버에 작성하자

### 동기
- 객체를 캡슐화하면 무언가를 변경할 때 그 변화를 전달해야 할 객체가 줄어드므로 변경하기 쉬워짐
- 객체를 다루는 게 일이라면 필드를 반드시 은폐해야 함
- 대리 객체가 변경될 때 클라이언트도 변경해야 할 가능성이 있음
  - 의존성을 없애려면, 대리 객체를 감추는 간단한 위임 메서드를 서버에 두면 됨
- 서버의 일부 클라이언트나 모든 클라이언트에 대리 객체 은폐를 실시하는 것이 좋을 때도 있음
  - 모든 클라이언트를 대상으로 대리 객체를 감출 경우에는 서버의 인터페이스에서 대리 객체에 대한 모든 부분을 삭제해도 됨

### 방법
- 대리 객체에 들어 있는 각 메서드를 대상으로 서버에 간단한 위임 메서드를 작성
- 클라이언트를 수정해서 서버를 호출하게 만들자
  - 클라이언트 클래스가 서버 클래스와 같은 패키지에 들어 있지 않다면 대리 메서드의 접근을 같은 패키지에 든 클래스만 접근할 수 있게 수정하는 것을 고려
- 각 메서드를 수정할 때마다 컴파일과 테스트를 실시
- 대리 객체를 읽고 써야 할 클라이언트가 하나도 남지 않게 되면, 서버에서 대리 객체가 사용하는 읽기/쓰기 메서드를 삭제
- 컴파일과 테스트 실시

### 예제
- Person 클래스와 Department 클래스
  ```java
  class Person {
    Department department;
  
    public Department getDepartment() {
      return department;
    }
  
    public void setDepartment(Department department) {
      this.department = department;
    }
  }
  
  class Department {
    private String chargeCode;
    private Person manager;
  
    public Department(Person manager) {
      this.manager = manager;
    }
  
    public Person getManager() {
      return manager;
    }
  }
  ```
- 클라이언트 클래스는 어떤 사람의 팀장이 누군지 알아내려면 먼저 부서를 알아내야 하므로 위임 메서드를 작성 후 모든 참조 부분 수정
  ```java
  manager = john.getDepartment().getManager();
  ```
- 의존성을 줄이려면 Department 클래스를 클라이언트가 알 수 없게 감춰야 함
  ```java
  public Person getManager(){
      return department.getManager();
  }
  
  manager = john.getManager();
  ```
  - Person 클래스에 들어 있는 getDepartment 읽기 메서드 삭제

## 과잉 중개 메서드 제거(Remove Middle Man)
- 클래스에 자잘한 위임이 너무 많을 땐 대리 겍체를 클라이언트가 직접 호출하게 하자

### 동기
- 대리 객체 은폐 기법의 단점은 클라이언트가 대리 개체의 새 기능을 사용해야 할 때마다 서버에 간단한 위임 메서드를 추가해야 한다는 점
  - 은폐의 적절한 정도를 알기란 어려움
- 시스템이 변경되면 은폐 정도의 기준도 변하기 때문에 필요할 때마다 보수

### 방법
- 대리 객체애 대한 접근 메서드를 작성
- 대리 메서드를 클라이언트가 사용할 때마다 서버에서 메서드를 제거하고 클라이언트에서의 호출을 대리 객체에서의 메서드 호출료 교체
- 메서드를 수정할 때마다 테스트를 실시

## 외래 클래스에 메서드 추가(Introduce Foreign Method)
- 사용 중인 서버 클래스에 메서드를 추가해야 하는데 그 클래스를 수정할 수 없을 땐 클라이언트 클래스 안에 서버 클래스의 인스턴스를 첫 번째 인자로 받는 메서드를 작성하자

### 동기
- 원본 클래스를 수정할 수 없다면 그 메서드를 클라이언트 클래스 안에 작성해야 함
- 새로 만들 메서드를 외래 메서드로 만들면 그 메서드가 원래는 원본 메서드인 서버 메서드에 있어야 할 메서드임을 분명히 나타낼 수 있음
  - 서버 클래스에 수많은 외래 메서드를 작성해야 하거나 하나의 외래 메서드를 여러 클래스가 사용해야 할 때는 이 기법 대신 국소적 상속확장 클래스 사용 기법 실시
- 가능하면 외래 메서드를 원래 있어야 할 위치로 옮기자

### 방법
- 필요한 기능의 메서드를 클라이언트 클래스 안에 작성
  - 그 메서드는 클라이언트 클래스의 어느 기능에도 접근해선 안 됨
  - 그 메서드에 값이 필요할 땐 매개변수로 전달해야 함
- 서버 클래스의 인스턴스를 첫 번째 매개변수로 만들기
- 그 메서드에 서버 클래스에 있을 외래 메서드 같은 주석을 달자
  - 이렇게 하면 나중에 그 외래 메서드를 옮길 일이 생겼을 때 문자열 검색 기능으로 외래 메서드를 쉽게 찾을 수 있음

### 예제
- 원본 코드
  ```java
  Date newStart = new Date(previousEnd.getYear(), previousend.getMonth(), previousEnd.getDate() + 1);
  ```
- 메서드 생성
  ```java
  Date newStart = nextDay(previousEnd);
  
  private static Date nextDay(Date arg){
      // Date 클래스에 있을 외래 메서드
      return new Date(arg.getYear(). arg.getMonth(), arg.getDate()+1);
  }
  ```

## 국소적 상속확장 클래스 사용(Introduce Local Extension)
- 사용 중인 서버 클래스에 여러 개의 메서드를 추가해야 하는데 클래스를 수정할 수 없을 땐 새 클래스를 작성하고 그 안에 필요한 여러 개의 메서드를 작성하자
- 이 상속확장 클래스를 원본 클래스의 하위클래스나 래퍼 클래스로 만들자

### 동기
- 원본 클래스를 수정하는 것이 불가능한데 필요한 메서드 수가 세 개 이상이면 필요한 메서드들을 적당한 곳에 모아둬야 함
  - 보편적인 객체지향 기법인 하위클래스화와 래퍼화를 실시
- 국소적 상속확장 클래스? 별도의 클래스지만 상속확장하는 클래스의 하위 타입
  - 원본 클래스의 모든 기능도 사용 가능하면서 추가 기능도 들어 있음
  - 원본 클래스를 사용할 것이 아니라 국소적 상속확장 클래스를 인스턴스화해서 사용
- 하위클래스와 래퍼 클래스 중에서 하위클래스가 작업량이 적음
  - 하위클래스로 만드는 방식의 문제점은 객체를 생성함과 동시에 하위클래스로 만들어야 한다는 점
- 래퍼 클래스를 사용하면 국소적 상속확장 클래스를 통해 이뤄진 수정이 원본 객체에도 반영되고, 그 반대로도 반영됨

### 방법
- 상속확장 클래스를 작성한 후 원본 클래스의 하위클래스나 래퍼 클래스로 만들자
- 상속확장 클래스를 변환 생성자 메서드를 작성하자
  - 생성자는 원본 클래스를 인자로 받음
  - 하위클래스의 경우 적절한 상위클래스 생성자를 호출하며, 래퍼 클래스의 경우 대리 필드에 그 인자를 할당
- 상속확장 클래스에 새 기능을 추가하자
- 필요한 위치마다 원본 클래스를 상속확장 클래스로 수정하자
- 이 클래스용으로 정의된 외래 메서드를 전부 상속확장 클래스로 옮기자

### 예제 : 하위클래스 사용
```java
// Date 클래스의 하위 클래스 작성
class MfDateSub extends Date

// 간단한 위임
public MfDateSub(String dateString){
    super(dateString);
}

// Date 클래스를 인자로 받는 변호나 생성자를 추가
public MfDateSub(Date arg){
    super(arg.getTime());
}

// 상속화장 클래스에 새 기능을 추가하고, 메서드 이동 기법 실시해서 외래 메서드를 전부 상속확장 클래스로 옮김
client class..
    // 원래는 Date 클래스에 있어야 할 외래 메서드
    private static Date nextDay(){
        return new Date(arg.getYear(), arg.getMonth(), arg.getDate()+1)
    }
    
// 앞의 코드는 다음과 같이 됨
class MfDate...
    Date nextDay(){
        return new Date(getYear(), getMonth(), getDate()+1);
    }
```

### 예제 : 래퍼 클래스 사용
```java
// 래퍼 클래스 선언
class MfDateWrap{
    private Date original;
}

// 래퍼 클래스로 만들 땐, 생성자 구성 방식을 달리해야 함, 원본 생성자 메서드는 간단한 위임을 통해 구현
public MfDateWrap(String dateString){
    original = new Date(dateString);
}

// 변환 생성자는 인스턴스 변수에 값을 할당하는 기능만 수행
public MfDateWrap(Date arg){
    original = arg;
}

// 원본 Date 클래스의 모든 메서드를 위임하는 작업
public int getYear(){
    return original.getYear();
}

// 메서드 이동 기법을 실시해서 날짜 전용 기능을 새 클래스에 넣자
클라이언트 클래스
    // 원래는 Date 클래스에 있어야 할 외래 메서드
    private static Date nextDay(Date arg){
        return new Date(arg.getYear(), arg.getMonth(), arg.getDate() + 1);
    }
    
// 앞의 코드가 다음과 같이 바뀜
class MfDate...
    Date nextDay(){
        return enw Date(getYear().getMonth(), getDate() + 1);
    }
```
- 대등성 검사를 재정의하는 것은 위험함 => 버그가 생길 위험이 높아짐
  - 대등성 검사라는 목적을 나타내고자 메서드명을 변경
    - public boolean equalsDate(MfDateWrap arg)