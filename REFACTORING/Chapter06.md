# 메서드 정리

## 메서드 추출(Extract Method)
- 어떤 코드를 그룹으로 묶어도 되겠다고 판단될 땐 그 코드를 빼내어 목적을 잘 나타내는 직관적 이름의 메서드로 만들자

### 동기
- 메서드가 너무 길거나 코드에 주석을 달아야만 의도를 이해할 수 있을 때
- 직관적인 이름의 간결한 메서드가 좋은 이유
  - 메서드가 절절히 잘개 쪼개져 있으면 다르메서드에서 쉽게 사용할 수 있음
  - 상위 계층의 메서드에서 주석 같은 더 많은 정보를 읽어들일 수 있음
  - 재정의하기도 훨씬 수월

### 방법
- 목적에 부합하는 이름의 새 메서드 생성(메서드명은 원리가 아니라 기능을 나타내는 이름)
  - 더 이해하기 쉬운 이름으로 추출하지 않을 바에는 차라리 코드를 추출하지 말아야 함
- 기존 메서드에서 빼낸 코드를 새로 생성한 메서드로 복사
- 빼낸 코드에서 기존 메서드의 모든 지역변수 참조를 찾자. 그것들은 새로 생성한 메서드의 지역변수나 매개변수로 사용할 것
- 빼낸 코드 안에서만 사용되는 임시변수가 있는지 파악해서 있다면 그것들을 새로 생성한 메서드 안에 임시변수로 선언
- 추출 코드에 의해 변경되는 지역변수가 있는지 파악
  - 만약 하나의 지역변수만 변경된다면 추출 코드를 메서드 호출처럼 취급할 수 있는지 알아내고 그 결과를 관련된 변수에 대입할 수 있는지 알아내자
  - 위의 방법이 까다롭거나 둘 이상의 지역변수가 변경될 때는 메서드를 추출하기 위해 먼저 임시변수 분리등의 기법 적용
    - 임시변수를 제거하려면 임시변수를 메서드 호출로 전환 기법 적용
- 빼낸 코드에서 읽어들인 지역변수를 대상 메서드에 매개변수로 전달
- 모든 지역변수 처리를 완료했으면 컴파일을 실시
- 원본 메서드 안에 있는 빼낸 코드 부분을 새로 생성한 메서드 호출로 수정
  - 대상 메서드로 임시변수를 옮겼으면 그 임시변수가 원본 코드 외부에 선언되어 있었는지 검사해서 그렇다면 대상 코드에서는 그 선언 부분을 삭제
- 컴파일과 테스트 실시

### 예제 : 재역변수 사용 안함
- 메서드 추출 전
```java
void printOwing(){
    Enumeration e = orders.elements();
    double outstanding = 0.0;

    // 배너 출력
    System.out.println("*****************");
    System.out.println("**** 고객 외상 ****");
    System.out.println("*****************");

    // 외상액 계산
    while(e.hasMoreElements()){
        Order each = (Order) e.nextElement();
        outstanding += each.getAmount();
    }
    
    //세부 내역 출력
    System.out.println("고객명: " + name);
    System.out.println("외상액: " + outstanding);
}
```
- 메서드 추출 후
```java
void printOwing(){
    Enumeration e = orders.elements();
    double outstanding = 0.0;
    
    printBanner();
    
    //외상액 계산
    while(e.hasMoreElements()){
        Order each = (Order) e.nextElement();
        outstading += each.getAmount();
    }
    
    //세부 내역 출력
    System.out.println("고객명: " + name);
    System.out.println("외상액: " + outstanding);
}

void printBanner(){
    // 배너 출력 코드
    System.out.println("*****************");
    System.out.println("**** 고객 외상 ****");
    System.out.println("*****************");
} 
```

### 예제 : 지역변수 사용
- 지역변수는 해당 메서드 안에서만 효력이 있으므로 메서드 추출을 적용하면 지역변수와 관련된 작업을 추가로 처리해야 함
  - 간혹 지역변수 때문에 리팩토링이 아예 불가능할 때도 있음
- 지역변수가 읽히기만 하고 변경되지 않을 때 => 그냥 매개변수로 전달
- 메서드 추출 전
```java
void printOwing(){
    Enumeration e = orders.elements();
    double outstanding = 0.0;
    
    printBanner();
    
    //외상액 계산
    while(e.hasMoreElements()){
        Order each = (Order) e.nextElement();
        outstading += each.getAmount();
    }
    
    //세부 내역 출력
    System.out.println("고객명: " + name);
    System.out.println("외상액: " + outstanding);
}
```
- 메서드 추출 후
```java
void printOwing(){
    Enumeration e = orders.elements();
    double outstanding = 0.0;
    
    printBanner();
    
    //외상액 계산
    while(e.hasMoreElements()){
        Order each = (Order) e.nextElement();
        outstading += each.getAmount();
    }
    
    printDetails(outstanding);
}

void printDetails(double outstanding){
    System.out.println("고객명: " + name);
    System.out.println("외상액: " + outstanding);
}
``` 

### 예제 : 지역변수를 다시 대입
- 지역변수로의 값 대입 => 임시변수만 생각
- 매개변수로의 값 대입이 있을 경우 => 매개변수로 값 대입 제거
- 임시변수로의 값 대입은 두 가지의 경우가 있음
    1. 임시변수가 추출한 코드 안에서만 사용되는 경우 => 임시변수를 추출한 코드로 옮기면 됨
    2. 임시변수가 추출한 코드 밖에서 사용되는 경우 => 임시변수가 코드가 추출된 후 사용되지 않는다면 추출한 코드에서 임시변수의 변경된 값을 반환하게 수정
- 메서드 추출 전
```java
void printOwing(){
    Enumeration e = orders.elements();
    double outstanding = 0.0;
    
    printBanner();
    
    //외상액 계산
    while(e.hasMoreElements()){
        Order each = (Order) e.nextElement();
        outstading += each.getAmount();
    }
    
    printDetails(outstanding);
}
```
- 메서드 추출 후
```java
void printOwing(){
    printBanner();
    double outstanding = getOutstanding();
    printDetails(outstanding);
}

double getOutstanding(){
    Enumeration e = orders.elements();
    double outstanding = 0.0;
    while(e.hasMoreElements()){
        Order each = (Order) e.nextElement();
        outstanding += each.getAmount();
    }
    return outstanding;
}
```
- 반환 값의 이름 변경 
```java
double getOutstanding(){
    Enumeration e = orders.elements();
    double result = 0.0;
    while(e.hasMoreElements()){
        Order each = (Order) e.nextElement();
        result += each.getAmount();
    }
    return result;
}
```

## 메서드 내용 직접 삽입(Inline Method)
- 메서드 기능이 너무 단순해서 메서드명만 봐도 너무 뻔할 땐 그 메서드의 기능을 호출하는 메서드에 넣어버리고 그 메서드는 삭제하자

### 동기
- 메서드 기능이 지나치게 단순할 땐 메서드를 없애야 함
  - 인다이렉션을 사용하면 해결되지만 불필요한 인다이렉션은 오히려 장애물이 됨
- 잘못 쪼개진 메서드에도 적용 가능
  - 잘못 쪼개진 메서드의 내용을 전부 하나의 큰 메서드에 직접 삽입한 후, 작은 메서드로 추출

### 방법
- 메서드가 재정의되어 있지 않은지 확인
  - 메서드가 하위클래스에 재정의되어 있다면 메서드 내용 직접 삽입을 실시하지 말 것
  - 없어진 메서드를 재정의하는 일이 생겨선 안 됨
- 그 메서드를 호출하는 부부을 모두 찾자
- 각 호출 부분을 메서드 내용으로 교체
- 테스트를 실시
- 메서드 정의를 삭제

### 예제
- 전
```java
int getRating(){
    return (moreThanFiveLateDeliveries()) ? 2 : 1;
}
boolean moreThanFiveLateDeliveries(){
    return numberOfLateDeliveries > 5;
}
```
- 후
```java
int getRating(){
    return (numberOfLateDeliveries > 5) ? 2 : 1;
}
```

## 임시변수 내용 직접 삽입(Inline Temp)
- 간단한 수식을 대입받는 임시변수로 인해 다른 리팩토링 기법 적용이 힘들 땐 그 임시변수를 참조하는 부분을 전부 수식으로 치환

### 동기
- 임시변수를 메서드 호출로 전환 기법을 실시하는 도중 병용하는 경우가 많음
  - 임시변수 내용 직접 삽입은 메서드 호출로 전환을 실시해야 하는 동기라고 할 수 있음
- 순수하게 사용되는 경우는 오직 메서드 호출의 결괏값이 임시변수에 대입될 때

### 방법
- 대입문의 우변에 문제가 없는지 확인
- 문제가 없다면 임시변수를 final로 선언하고 컴파일
  - 임시변수에는 값을 한 번만 대입할 수 있음
- 임시변수를 참조하는 모든 부분을 찾아서 대입문 우변의 수식으로 바꾸자
- 임시변수 선언과 대입문을 삭제하자
- 컴파일과 테스트 실시

### 예제
- 전
```java
double basePrice = anOrder.basePrice();
return (basePrice > 1000);
```
- 후
```java
return (anOrder.basePrice() > 1000);
```

## 임시변수를 메서드 호출로 전환(Replace Temp with Query)
- 수식의 결과를 저장하는 임시변수가 있을 땐 그 수식을 빼내어 메서드로 만든 후, 임시변수 참조 부분을 전부 수식으로 교체
- 새로 만든 메서드는 다른 메서드에서도 호출 가능

### 동기
- 메서드 추출을 적용하기 전에 반드시 적용해야 함
  - 지역변수가 많을수록 메서드 추출이 힘들어지므로 최대한 많은 변수를 메서드 호출로 고쳐야 함

### 방법
- 값이 한 번만 대입되는 임시변수를 찾자
  - 값이 여러 번 대입되는 임시변수가 있으면 임시변수 분리 기법 실시를 고려
- 그 임시변수를 final로 선언
- 컴파일 실시
  - 임시변수엔 값을 한 번만 대입할 수 있음
- 대입문 우변을 빼내어 메서드로 만들자
  - 처음엔 메서드를 private로 선언하고 나중에 여러 곳에서 사용하게 된다면 접근 제한을 완화
- 컴파일과 테스트 실시
- 임시변수를 대상으로 임시변수 내용 직접 삽입 기법 실시

### 예제
- 전
```java
double getPrice(){
    int basePrice = quantity * itemPrice;
    double discountFactor;
    if(basePrice > 1000) discountFactor = 0.95;
    else discountFactor = 0.98;
    return basePrice * discountFactor;
}
```
- 임시변수를 final로 선언
```java
double getPrice(){
    final int basePrice = quantity * itemPrice;
    final double discountFactor;
    if(basePrice > 1000) discountFactor = 0.95;
    else discountFactor = 0.98;
    return basePrice * discountFactor;
}
```
- 대입문 우변을 빼내어 메서드로 만들기
```java
double getPrice(){
    final int basePrice = basePrice();
    final double discountFactor;
    if(basePrice > 1000) discountFactor = 0.95;
    else discountFactor = 0.98;
    return basePrice * discountFactor;
}
private int basePrice(){
    return quntity * itemPrice;
}
```
- 컴파일과 테스트 실시 후 첫번째 임시변수 교체
```java
double getPrice(){
    final int basePrice = basePrice();
    final double discountFactor;
    if(basePrice() > 1000) discountFactor = 0.95;
    else discountFactor = 0.98;
    return basePrice * discountFactor;
}
```
- 컴파일 테스트 실시 후 두 번째 임시변수 변경
```java
double getPrice(){
    final double discountFactor;
    if(basePrice() > 1000) discountFactor = 0.95;
    else discountFactor = 0.98;
    return basePrice() * discountFactor;
}
```
- discountFactor 메서드로 빼내기
```java
double getPrice(){
    final double discountFactor = discountFactor();
    return basePrice() * discountFactor();
}

private double discountFactor(){
    if(basePrice() > 1000) return 0.95;
    else return 0.98;
}
```
- 최종
```java
double getPrice(){
    return basePrice() * discountFactor();
}
```

## 직관적 임시변수 사용(Introduce Explaining Variable)
- 사용된 수식이 복잡할 땐 수식의 결과나 수식의 일부분을 용도에 부합하는 직관적 이름의 임시변수에 대입하자

### 동기
- 조건문에서 각 조건 절을 가져와서 직관적 이름의 임시변수를 사용해 그 조건의 의미를 설명하려 할 때 많이 사용
- 직관적 임시변수 사용 기법을 적용하는 것을 자제하고 그 대신 메서드 추출을 사용하려 노력해야함
  - 지역변수로 인해 메서드 추출을 적ㅇ요하기가 힘들 때에 실시

### 방법
- 임시변수를 final로 선언하고, 복잡한 수식에서 한 부분의 결과를 그 임시변수에 대입
- 그 수식에서 한 부분의 결과를 그 임시변수의 값으로 교체
  - 수식의 결과 부분이 반복될 경우엔 한 번에 하나씩 교체
- 컴파일과 테스트 실시
- 수식의 다른 부분을 대상으로 위의 과정을 반복 실시

### 예제 : 직관적 임시변수 사용
- 전
```java
double price(){
    // 결제액(price) = 총 구매액(base price) - 
    // 대량 구매 할인(quantity discount) + 배송비(shipping)
    return quantity * itemPrice - 
        Math.max(0, quantity - 500) * itemPrice * 0.05 +
        Math.min(quantity * itemPrice * 0.1, 100.0);
}
```
- 총 구매액을 임시변수로 변경
```java
double price(){
    // 결제액(price) = 총 구매액(base price) - 
    // 대량 구매 할인(quantity discount) + 배송비(shipping)
    final double basePrice = quantity * itemPrice;
    return basePrice - 
        Math.max(0, quantity - 500) * itemPrice * 0.05 +
        Math.min(quantity * itemPrice * 0.1, 100.0);
}
```
```java
double price(){
    // 결제액(price) = 총 구매액(base price) - 
    // 대량 구매 할인(quantity discount) + 배송비(shipping)
    final double basePrice = quantity * itemPrice;
    return basePrice - 
        Math.max(0, quantity - 500) * itemPrice * 0.05 +
        Math.min(basePrice * 0.1, 100.0);
}
```
- 대량 구매 할인액을 임시변수로 변경
```java
double price(){
    // 결제액(price) = 총 구매액(base price) - 
    // 대량 구매 할인(quantity discount) + 배송비(shipping)
    final double basePrice = quantity * itemPrice;
    final double quantityDiscount = Math.max(0, quantity - 500) * itemPrice * 0.05;
    return basePrice - quantityDiscount + Math.min(basePrice * 0.1, 100.0);
}
```
- 배송료를 임시변수로 변경
```java
double price(){
    // 결제액(price) = 총 구매액(base price) -
    // 대량 구매 할인(quantity discount) + 배송비(shipping)
    final double basePrice = quantity * itemPrice;
    final double quantityDiscount = Math.max(0, quantity - 500) * itemPrice * 0.05;
    final double shipping = Math.min(basePrice * 0.1, 100.0);
    return basePrice - quantityDiscount + shipping;
}
```

### 예제 : 메서드 추출을 적용할 것
- 최종
```java
double price(){
    return basePrice() - quantityDiscount() + shipping();
}

private double quantityDiscount(){
    return Math.max(0, quantity - 500) * itemPrice * 0.05;
}

private double shipping(){
    return Math.min(basePrice() * 0.1, 100.0);
}

private double basePrice(){
    return quantity * itemPrice;
}
```
- 메서드 추출 기법을 사용했을 때 이 메서드들을 객체의 다른 부분에서도 사용할 수 있기 때문
- 메서드 추출 기법 적용이 더 어렵거나 복잡할 때 직관적 임시변수 사용을 하면 코드가 돌아가는 원리를 이해하기 쉽게 만듦

## 임시변수 분리(Split Temporary Variable)
- 루프 변수나 값 누적용 임시변수가 아닌 임시변수에 여러 번 값이 대입될 땐 각 대입마다 다른 임시변수를 사용하자

## 동기
- 여러 용도로 사용되는 변수는 각 용도별로 다른 변수를 사용하게 분리해야 함
  - 임시변수 하나를 두 가지 용도로 사용하면 코드를 분석하는 사람에게 혼동을 줄 수 있기 때문

## 방법
- 선언문과 첫 번째 대입문에 있는 임시변수 일믕르 변경하자
  - 값 누적용 임시변수는 분리하면 안 됨
- 이름을 바꾼 새 임시변수를 final로 선언
- 그 임시변수의 모든 참조 부분ㅇ르 두 번째 대입문으로 수정
- 두 번째 대입문에 있는 임시변수를 선언
- 컴파일과 테스트를 실시
- 각 대입문마다 차례로 선언문에서 임시변수 이름을 변경하고, 그 다음 대입문까지 참조를 수정하며 위의 과정을 반복

## 예제
- 전
```java
double getDistanceTravelled(int time){
    double result;
    double acc = primaryForce / mass;
    int primaryTime = Math.min(time, delay);
    result = 0.5 * acc * primaryTime * primaryTime;
    int secondaryTime = time - delay;
    
    if(secondaryTime > 0){
        double primaryVel = acc * delay;
        acc = (primaryForce + secondaryForce) / mass;
        result += primaryVel * secondaryTime + 0.5 * acc * secondaryTime * secondaryTime;
    }
    return result;
}
```
- 최종
```java
double getDistanceTravelled(int time){
    double result;
    final double primaryAcc = primaryForce / mass;
    int primaryTime = Math.min(time, delay);
    result = 0.5 * primaryAcc * primaryTime * primaryTime;
    int secondaryTime = time - delay;
    
    if(secondaryTime > 0){
        double primaryVel = acc * delay;
        final doubel secondAcc = (primaryForce + secondaryForce) / mass;
        result += primaryVel * secondaryTime + 0.5 * secondAcc * secondaryTime * secondaryTime;
    }
    return result;
}
```

## 매개변수로의 값 대입 제거(Remove Assignments to Parameters)
- 매개변수로 값을 대입하는 코드가 있을 땐 매개변수 대신 임시변수를 사용하게 수정하자

### 동기
- 자바에선 매개변수에 값을 대입해서는 안 됨
    - 전달받은 매개변수에 다른 객체 참조를 대입하면 코드의 명료성도 떨어지고 '갑을 통한 전달'과 '참조를 통한 전달'을 혼동할 수 있음
    - 자바는 값을 통한 전달만 사용
      - 값을 통한 전달을 사용하면 어떠한 매개변수 값 변화도 호출한 루틴에 반영되지 않음

### 방법
- 매개변수 대신 사용할 임시변수를 선언하자
- 매개변수로 값을 대입하는 코드 뒤에 나오는 매개변수 참조를 전부 임시변수로 수정하자
- 매개변수로의 값 대입을 임시변수로의 값 대입으로 수정하자
- 컴파일과 테스트를 실시하자
  - 참조를 통한 호출일 때는 호출한 메서드에서 매개변수가 계속 사용되는지 살펴보자
  - 매개변수 참조를 통한 호출이 몇 개나 이 메서드의 뒷부분에 대입되고 사용되는지 확인하자
  - 하나의 값만 반환 값으로 전달하게 만들자
    - 둘 이상이면 데이터 뭉치를 개체로 전환하거나 별도의 메서드로 만들 수 있는지 살펴보자

### 예제
- 전
```java
int discount(int inputVal, int quantity, int yearToDate){
    if(inputVal > 50) inputVal -= 2;
    if(quantity > 100) inputVal -= 1;
    if(yearToDate > 10000) inputVal -= 4;
    return inputVal;
}
```
- 매개변수를 임시변수로 변환
```java
int discount(int inputVal, int quantity, int yearToDate){
    int result = inputVal;
    if(inputVal > 50) result -= 2;
    if(quantity > 100) result -= 1;
    if(yearToDate > 10000) result -= 4;
    return result;
}
```
- 이 관례를 강제 적용
```java
int discount(final int inputVal, final int quantity, final int yearToDate){
    int result = inputVal;
    if(inputVal > 50) result -= 2;
    if(quantity > 100) result -= 1;
    if(yearToDate > 10000) result -= 4;
    return result;
}
```

### 자바에서 값을 통한  전달
```java
public class Param{
    public static void main(String[] args) {
        Date d1 = new Date("1 Apr 98");
        nextDateUpdate(d1);
        System.out.println("nextDay 메서드 실행 후 d1 값 : " + d1);

        Date d2 = new Date("1 Apr 98");
        nextDateReplace(d2);
        System.out.println("nextDay 메서드 실행 후 d2 값: " + d2);
    }

    private static void nextDateUpdate(Date arg){
        arg.setDate(arg.getDate() + 1);
        System.out.println("nextDay 메서드 안의 arg 값: " + arg);
    }

    private static void nextDateReplace(Date arg){
        arg = new Date(arg.getYear(), arg.getMonth(), arg.getDate() + 1);
        System.out.println("nextDay 메서드 안의 arg 값: " + arg);
    }
}
```
- 결과값
```
nextDay 메서드 안의 arg 값: Thu Apr 02 00:00:00 KST 1998
nextDay 메서드 실행 후 d1 값 : Thu Apr 02 00:00:00 KST 1998
nextDay 메서드 안의 arg 값: Thu Apr 02 00:00:00 KST 1998
nextDay 메서드 실행 후 d2 값: Wed Apr 01 00:00:00 KST 1998
```

## 메서드를 메서드 객체로 전환(Replace Method with Method Object)
- 지역변수 때문에 메서드 추출을 적용할 수 없는 긴 메서드가 있을 땐 그 메서드 자체를 객체로 전호나해서 모든 지역변수를 객체의 필드로 만들자
- 그런 다음 그 메서드를 객체 안의 여러 메서드로 쪼개면 된다

### 동기
- 임시변수를 메서드 호출로 전환을 적용하고도 메서드를 분해할 수 없을 때 메서드 객체로 수정
- 메서드 객체로 전환 기법을 적용하면 모든 지역변수가 메서드 객체의 속성이 됨
  - 그 객체에 메서드 추출을 적용해서 원래의 메서드를 쪼개어 여러 개의 추가 메서드를 만들면 됨

### 방법
- 전환할 메서드의 이름과 같은 이름으로 새 클래스를 생성하자
- 그 클래스에 원본 메서드가 들어 있던 객체를 나타내는 final 필드를 추가하고 원본 메서드 안의 각 임시변수와 매개변수에 해당하는 속성을 추가하자
- 새 클래스에 원본 객체와 각 매개변수를 받는 생성자 메서드를 작성하자
- 새 클래스에 compute라는 이름의 메서드를 작성하자
- 원본 메서드 내용을 compute 메서드 안에 복사해 넣자. 원본 객체에 있는 메서드를 호출할 땐 원본 객체를 나타내는 필드를 사용하자
- 컴파일을 실시하자
- 원본 메서드를 새 객체 생성과 compute 메서드 호출을 담당하는 메서드로 바꾸자

### 예제
- 전
```java
Class Account {
    int gamma(int inputVal, int quantity, int yearToDate){
        int importantValue1 = (inputVal * quantity) + delta();
        int importantValue2 = (inputVal * yearToDate) + 100;
        if((yearToDate - importantValue1) > 100)
            importantValue2 -= 20;
        int importantValue3 = importantValue2 * 7;
        
        // 기타 작업
        return importantValue3 - 2 * importantValue1;
    }
}
```
- 후
```java
Class Account {
    int gamma(int inputVal, int quantity, int yearToDate){
        return new Gamma(this, inputVal, quantity, yearToDate).compute();
    }
}

Class Gamma{
    private final Account account;
    private int inputVal;
    private int quantity;
    private int yearToDate;
    private int importantValue1;
    private int importantValue2;
    private int importantValue3;
    
    Gamma(Account source, int inputValArg, int quantityArg, int yearToDateArg){
        account = source;
        inputVal = inputValArg;
        quantity = quantityArg;
        yearToDate = yearToDateArg;
    }
    
    int compute(){
        int importantValue1 = (inputVal * quantity) + account.delta();
        int importantValue2 = (inputVal * yearToDate) + 100;
        if((yearToDate - importantValue1) > 100)
            importantValue2 -= 20;
        int importantValue3 = importantValue2 * 7;

        // 기타 작업
        return importantValue3 - 2 * importantValue1;
    }
    
}
```
- compute에서 메서드 추출 기법 실시
```java
int compute(){
    int importantValue1 = (inputVal * quantity) + account.delta();
    int importantValue2 = (inputVal * yearToDate) + 100;
    importantThing();
    int importantValue3 = importantValue2 * 7;

    // 기타 작업
    return importantValue3 - 2 * importantValue1;
}

void importantThing(){
    if((yearToDate - importantValue1) > 100)
        importantValue2 -= 20;
}
```

## 알고리즘 전환(Substitute Algorithm)
- 알고리즘을 더 분명한 것으로 교체해야 할 땐 해당 메서드의 내용을 새 알고리즘으로 바꾸자

### 동기
- 길고 어려운 알고리증믄 수정하기 어려우므로, 간단한 알고리즘으로 교체해야만 수정 작업이 편해짐

### 방법
- 교체할 간결한 알고리즘을 준비하자. 컴파일을 실시하자.
- 새 알고리즘을 실행하면서 여러 번의 테스트를 실시하자. 모든 테스트 결과가 같으면 성공한 것이다.
- 결과가 다르게 나온다면, 기존 알고리즘으로 테스트와 디버깅을 실시해 비교하자
  - 기존 알고리즘과 새 알고리즘을 대상으로 각 테스트 케이스를 실행하고 두 결과를 비교
  - 어느 테스트 케이스가 문제를 어떤 식으로 일으키는지 확인할 수 있음

### 예제
- 전
```java
String foundPerson(String[] people){
    for(int i = 0; i < people.length; i++){
        if(people[i].equals("Don")){
            return "Don";
        }
        if(people[i].equals("John")){
            return "John";
        }
        if(people[i].equals("Kent")){
            return "Kent";
        }
    }
    return "";
}
```
- 후

```java
String foundPerson(String[] people) {
  List candidates = List.of(new String[]{"Don", "John", "Kent"});
  for (int i = 0; i < people.length; i++)
    if (candidates.contains(people[i]))
      return people[i];
  return "";
}
```