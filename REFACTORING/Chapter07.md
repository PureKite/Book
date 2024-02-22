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

## 클래스 내용 직접 삽입(Inline Class)

## 대리 객체 은폐(Hide Delegate)

## 과잉 중개 메서드 제거(Remove Middle Man)

## 외래 클래스에 메서드 추가(Introduce Foreign Method)

## 국소적 상속확장 클래스 사용(Introduce Local Extension)