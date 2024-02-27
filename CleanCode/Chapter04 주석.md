# 주석
- 코드만이 정확한 정보를 제공하는 유일한 출처
- 주석을 가능한 줄이도록 꾸준히 노력해야 함

## 주석은 나쁜 코드를 보완하지 못한다
- 코드에 주석을 추가하는 일반적인 이유는 코드 품질이 나쁘기 때문
- 표현력이 풍부하고 깔끔하며 주석이 거의 없는 코드가, 복잡하고 어수선하며 주석이 많이 달린 코드보다 훨씬 나음

## 코드로 의도를 표현하라
- 예제 : 직원에게 복지 혜택을 받을 자격 검사
    - if((employee.flags & HOURLY_FLAG) && (employee.age > 5))
    - if(employee.isEligibleForFullBenefits())
- 코드만으로 의도를 설명하기 어려운 경우가 존재하지만 코드로 대다수 의도를 표현할 수 있음

## 좋은 주석
- 어떤 주석은 필요하거나 유익

### 법적인 주석
- 회사가 정립한 구현 표준에 법적인 이유로 특정 주석을 넣으라고 명시
  - 예) 각 원시 파일 첫머리에 주석으로 들어가는 저작권 정보와 소유권 정보

### 정보를 제공하는 주석
- 기본적인 정보를 제공
  - 예 : 추상 메서드가 반환할 값 설명
- 위와 같은 주석이 유용하더라도, 가능하다면 함수 이름에 정보를 담는 편이 좋음

### 의도를 설명하는 주석
- 구현을 이해하게 도와주는 선을 넘어 결정에 깔린 의도까지 설명
- 예제

```java
import java.util.concurrent.atomic.AtomicBoolean;

public void testConcurrentAddWidgets() throws Exception {
  WidgetBuilder widgetBuilder = new WidgetBuilder(new Class[]{BoldWidget.class});
  String text = "'''bold'''";
  ParentWidget parent = new BoldWidget(new MockWidgetRoot(),
          "'''bold'''");
  AtomicBoolean failFlag = new AtomicBoolean();
  failFlag.set(false);
  
  // 스레드를 대량 생성하는 방법으로 어떻게든 경쟁 조건을 만들려고 시도한다.
  for(int i = 0; i < 25000; i++){
      WidgetBuilderThread widgetBuilderThread = new WidgetBuilderThread(widgetBuilder, text, parent, failFlag);
      Trhead thread = new Thread(widgetBuilderThread);
      thread.start();
  }
  assertEquals(false, failFlag.get());
}
```

### 의미를 명료하게 밝히는 주석
- 일반적으로는 인수나 반환 값 자체를 명확하게 만들면 더 좋겠지만, 인수나 반환 값이 표준 라이브러리나 변경하지 못하는 코드에 속한다면 의미를 명료하게 밝히는 주석이 유용
- 더 나은 방법이 없는지 고미하고 정확히 달도록 주의

### 결과를 경고하는 주석
- 다른 프로그매어게 겨로가를 경고할 목적의 주석 사용
- 적절한 예제

```java
import java.text.SimpleDateFormat;
import java.util.TimeZone;

public static SimpleDateFormat makeStandardHttpDateFormat() {
  // SimpleDateFormat은 스레드에 안전하지 못하다.
  // 따라서 각 인스턴스를 독립적으로 생성해야 한다.
  SimpleDateFormat df = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z");
  df.setTimeZone(TimeZone.getTimeZone("GMT"));
    return df;
}
```

### TODO 주석
- 앞으로 할 일을 TODO 주석으로 남겨두면 편함
  - 주기적으로 점검해 없애도 괜찮은 주석은 없애야 함

### 중요성을 강조하는 주석
- 예제
```java
String listItemContent = match.group(3).trim();
// 여기서 trim은 정말로 중요하다. trim 함수는 문자열에서 시작 공백을 제거한다.
// 문자열에서 시작 공백이 있으면 다른 문자열로 인식되기 때문이다.
new ListItemWidget(this, listItemContent, this.level + 1);
return buildList(text.substring(match.end()));
```

### 공개 API에서 Javadocs
- 설명이 잘 된 공개 API는 유용
- 공개 API를 구현한다면 반드시 훌륭한 Javadocs를 작성
  - Javadocs 역시 독자를 오도하거나, 잘못 놓이거나, 그릇된 정보를 전달할 가능성이 존재

## 나쁜 주석
- 대다수 주석이 이 범주에 속함

### 주절거리는 주석
- 이해가 안 되어 다른 모듈까지 뒤져야 하는 주석은 독자와 제대로 소통하지 못하는 주석

### 같은 이야기를 중복하는 주석
- 코드 내용을 중복하는 주석 => 코드보다 주석을 읽는 시간이 더 오래 걸림

### 오해할 여지가 있는 주석
- 프로그래머가 정확하지 못한 주석을 달아놓는 경우

### 의무적으로 다는 주석
- 모든 함수에 Javadocs를 달거나 모든 변수에 주석을 다는 규칙은 어리석음
- 이런 주석은 코드를 복잡하게 만들며, 거짓말을 퍼뜨리고, 혼동과 무질서를 초래

### 이력을 기록하는 주석
- 예전에 모든 모듈 첫머리에 변경 이력을 기록하고 관리하는 관례가 바람직했지만 현재는 혼라만 가중할 뿐이라 완전히 제거하는 편이 좋음

### 있으나 마나 한 주석
- 너무 당연한 사실을 언급하며 새로운 정보를 제공하지 못하는 주석

### 무서운 잡음
- 때로는 Javadocs도 잡음
  - 어떤 목적을 수행하지 않는 경우

### 함수나 변수로 표현할 수 있다면 주석을 달지 마라
- 주석이 필요하지 않도록 코드를 개선하는 편이 더 나음

### 위치를 표시하는 주석
- 너무 자주 사용하지 않는다면 배너는 눈에 띄며 주의를 환기함
- 그러므로 반드시 필요할 때만, 드물게 사용
- 배너를 남용하면 독자가 흔한 잡음으로 여겨 무시함

### 닫는 괄호에 다는 주석

### 공로를 돌리거나 저자를 표시하는 주석
- 위와 같은 정보는 소스 코드 제어 시스템에 저장하는 편이 나음

### 주석으로 처리한 코드
- 다른 사람들이 이유가 있어 남겨놓았다고 지우기를 주저하게 됨
- 현대에서는 주석으로 처리할 필요가 없으므로 코드를 삭제해라

### HTML 주석
- 주석에 HTML 태그를 삽입하는 책임은 프로그래머가 아니라 도구가 져야 함

### 전역 정보
- 주석을 달아야 한다면 근처에 있는 코드만 기술
- 코드 일부에 주석을 달면서 시스템 전반적인 정보를 기술하지 말 것

### 너무 많은 정보
- 흥미로운 역사나 관련 없는 정보를 장황하게 늘어놓지 말 것

### 모호한 관계
- 주석과 주석이 설명하는 코드는 둘 사이 관계가 명백해야 함

### 함수 헤더
- 짧은 함수는 긴 설명이 필요 없음
- 짧고 한 가지만 수행하며 이름을 잘 붙이 함수가 주석으로 헤더를 추가한 함수보다 훨씬 나음

### 비공개 코드에서 Javadocs
- 공개하지 않을 코드라면 Javadocs는 쓸모가 없음