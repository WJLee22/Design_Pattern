
#  Singleton Pattern

## Ex

> "A 클래스의 객체를 하나만 생성해야 하는데 좋은 방법 없을까?"
> <br>=> Singleton 패턴을 적용시켜보자.

<br/>

## 싱글톤 패턴이란?

> Singleton is a creational design pattern that lets you ensure that a class has **only one instance**

싱글톤은 `클래스당 오직 하나의 객체` 를 생성하고 이 생성된 객체를 어디서든(타 클래스 인스턴스들) 참조할 수 있도록 하는 생성 패턴이다.

즉, 단 하나의 유일한 객체를 생성해내기위한 패턴이다.

<br/>

## 싱글톤 패턴 왜 사용하나?



### 메모리 절약 & 속도 이점 & 데이터 공유 용이 

  싱글톤 패턴은 클래스당 단 하나의 객체만을 생성해내는 코드 패턴이다.   
  그렇기에 리소스를 많이 차지하는 객체일경우, 싱글톤 패턴을 적용시키서 이 하나의 객체를 통해서 여러곳에서 참조한다면 메모리 절약에 매우 효과적이고, 이미 생성된 객체를 활용하는 것이니 속도면에서도 이점이 생긴다.
 
<br>

## 예재를 통한 Singleton 이해 
  <br>
  사용자의 은행 계좌를 나타내는 Account 클래스와, 해당 계좌에서의 입금/출금 발생 내역을 기록하는 로깅역할을 하는 Logger 클래스를 통해     싱글톤 패턴을 이해해보자. 
  
<br>

### <div align="center"><_기본 사례_></div>
 
  ```java
  public class Account {
    private String owner;
    private int balance;
    private Logger myLogger;

    public void setLogger(Logger myLogger) {
        this.myLogger = myLogger;
    }

    public Account(String owner, int balance) {
        this.owner = owner;
        this.balance = balance;
        this.myLogger = new Logger();
    }

    public String getOwner() {
        return owner;
    }

    public int getBalance() {
        return balance;
    }

    public void deposit(int money) {
        myLogger.log("owner" + " : " + this.getOwner() + " deposit " + money);
        balance += money;
    }

    public void withdraw(int money) {
        if (balance >= money) {
            myLogger.log("owner" + " : " + this.getOwner() + " withdraw " + money);
            balance -= money;
        }
    }
}

```
  ```java
  public class Logger {
    private final String LOGFILE = "log.txt";
    private PrintWriter writer;

    public Logger() {
        try {
            FileWriter fw = new FileWriter(LOGFILE);
            writer = new PrintWriter(fw, true);
        } catch (IOException e) {
        }
    }

    public void log(String message) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
        Date date = new Date(System.currentTimeMillis());
        writer.println(formatter.format(date) + " : " + message);
    }
}
```
  ```java
  public class Main {
    public static void main(String[] args) {
        Account acct1 = new Account("insang1", 1000000);
        acct1.deposit(20000);
        Account acct2 = new Account("insang2", 2000000);
        acct2.withdraw(5000);
        acct2.withdraw(2000);
    }
}


```



   <br>

위 코드를보면, Account 인스턴스 하나당 새로운 Logger 인스턴스 하나를 생성해서 1대1로 대응되는 관계를 보인다. 
그래서 Logger 인스턴스가 생성될때마다 log.txt파일을 새로 오픈하고 기존의 log.txt 파일에 쓰인 내용들을
덮어써버리는 문제가 발생한다. 
   
이 문제를 해결하기위해선 하나의 Logger 인스턴스를 만들고 이를 모든 account 인스턴스가 공유하도록하면 
 log.txt 파일에 다음 내용이 덮어쓰여지지않고 다음줄에 출력될 것이다. 

<br>

  ### <div align="center"><_1차 코드 개선_></div>
          
  ```java
 public class Main {
    public static void main(String[] args) {
        Logger logger = new Logger(); //하나의 Logger 인스턴스 생성
        Account acct1 = new Account("insang1", 1000000);
        acct1.setLogger(logger);
        acct1.deposit(20000);
        Account acct2 = new Account("insang2", 2000000);
        acct2.setLogger(logger);
        acct2.withdraw(5000);
        acct2.withdraw(2000);
    }
}

```
  ```java
// Account 클래스의 생성자 메서드 실행시마다 Logger 인스턴스를 생성하던 구문을 제거  
     public Account(String owner, int balance) {
        this.owner = owner;
        this.balance = balance;
//        this.myLogger = new Logger();
    }

```

위 코드처럼, 메인 메서드에서 하나의 로거 객체를 생성한다음 이 객체를 각 어카운트 객체의 setter 메서드를 통해
각 어카운트 객체들이 공유하도록 세팅하였다. 

자, 현재 코드상으로는 하나의 로거 객체를 생성하고 -> 이 객체를 여러 어카운트 객체들이 공유하는 우리가 원하는 모습이다.
  하지만 이 코드도 아직 싱글톤 패턴에 근접만했을뿐 완전한 싱글톤 패턴이 적용된 형태는 아니다. 

  그럼 무엇이 부족한 것일까? -> 바로, 하나의 인스턴스가 아닌 다수의 Logger 인스턴스 생성이 가능하다는 점이다. 
  싱글턴 패턴의 핵심은 하나의 객체를 생성하고 이를 다른 인스턴스들이 공유하도록하는 개념이다. 위와 같은 코드로     
  프로그램을 구성할 경우 싱글턴의 취지와 부합하지 않게된다. 

    그렇다면 여러 Logger 인스턴스를 생성하지 못하게하도록하는 수단을 제공해줘야할 것이다. 
  

  그럼 Logger 인스턴스를 단 하나만 생성할 수 있도록 제어할 수 있는 방법은 무엇이 있을까?

  <br>
  <hr>
  
### 싱글턴 패턴의 3가지 규칙

- Logger 클래스 내에 단일 logger 객체의 레퍼런스를 저장하기 위한 `static` 정적 참조 변수를 필드로 둔다.
  
- Logger 인스턴스를 `new` 연산자로 생성하려고 할 때 객체 생성을 제어하기 위해, 외부 클래스에서 Logger 클래스의 생성자 메서드에 접근하지 못하도록 생성자의 접근 지정자를 `private`으로 설정한다.
   
- 단일 Logger 객체를 반환해주는 역할을 하는 `getter` 메서드를 정의한다.




 <hr>
<br>

위의 3가지 규칙이 지켜져야 비로소 싱글턴 패턴이다라고 할 수 있다. 

<br>

이러한 싱글턴 패턴은 static 정적 참조 변수의 초기화 시기에따라 **Eager initialization** (이른 초기화 방식) & **Lazy initialization** (늦은 초기화 방식)으로 나뉜다.

<br>

## Singleton 구현 <**Eager initialization** & **Lazy initialization**>


