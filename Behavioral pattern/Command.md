
#   Command Pattern

<br>

## Command(커맨드) 패턴이란?

<br>

## 만능 버튼 예제를 통해 알아보는 Command Pattern      

다양한 기능을 수행하는 버튼인 만능 버튼을 우리가 만들고자한다.  

이 만능 버튼은 눌리면 특정 기능을 수행하는데, 먼저 버튼을 누르면 램프를 켜는 기능을 구현하고자 한다.  


-   Lamp 클래스: 불을 켜는 기능을 제공하는 클래스 
- Button 클래스: 버튼이 눌렸음을 인식하는 클래스
  
이 램프 켜는 버튼을 설계하면 아래와 같다.  

<br>

## <기존 설계> 
<div align="center">
 <img src="https://github.com/user-attachments/assets/4ccd0c8d-7dab-4e82-ad78-f6f50ee89f16">
</div>

```java
public class Lamp {
    public void turnOn(){
        System.out.println("Lamp on");
    }
}
```
```java
public class Button {
    private Lamp theLamp;

    public Button(Lamp theLamp) {
        this.theLamp = theLamp;
    }

    public void pressed(){
        theLamp.turnOn();
    }
}
```
```java
public class Main {
    public static void main(String[] args) {
        Lamp lamp = new Lamp();
        Button lampButton = new Button(lamp);
        lampButton.pressed();
    }
}
```

<br>

## 기존 설계의 문제점  
### <p align="center">`<문제점1>`</p> 

<br>

<p align="center">우리의 목표는 만능 버튼을 만드는 것이다.</p> 
즉 버튼이 다양한 기능을 수행하도록 만들어야하는데, 현재로써는 버튼을 누르면 램프를 켜는 기능만 가능하다.  

버튼이 눌렸을때 램프를 켜는 대신 다른 기능을(ex 알람 시작) 수행하도록 변경하려면 코드가 어떻게 변경되어야할까?  

버튼이 눌렸을때 알람을 시작하는 기능을 하도록, 알람 클래스를 구현해보자.  

기존에는 버튼을 누르면 램프를 켰지만 아래와 같이 설계를 다시하면 램프대신 알람을 시작하도록 기능이 변경되었다.

<div align="center">
 <img src="https://github.com/user-attachments/assets/a9e44d22-03ef-48a8-a56b-2b28dbbf5cfb">
</div>

```java
public class Alarm {
    public void start(){System.out.println("Alarming ...");}
}
```
```java
public class Button {
    private Alarm theAlarm;
    public Button(Alarm theAlarm) {this.theAlarm = theAlarm;}
    public void pressed(){theAlarm.start();}
}
```
```java
public class Main {
    public static void main(String[] args) {
        Alarm alarm = new Alarm();
        Button lampButton = new Button(alarm);
        lampButton.pressed();
    }
}
```

<br>

이때, 버튼의 기능을 알람시작이라는 새로운 기능으로 변경시키려고하면 Alarm 클래스를 새로 생성하면 되지만 Button 클래스는 기존 코드가 변경이되었다.  

 **즉, 버튼의 기존의 기능을 변경하려면 기존의 코드를 변경해야하므로 => 이는 OCP 원칙을 위반하는 것이다.**     

<br>
<br>

### <p align="center">`<문제점2>`</p> 

<br>

>**또한, 이는 만능 버튼이라고 볼 수 없다.**
  
<br>

**만능 버튼이라함은 버튼하나만으로 다양한 기능을 가진 버튼인데, 지금 이 버튼의 설계는 특정 기능만을 수행하는 버튼이다.** 버튼에 새로운 기능이 요구됐을때 기존의 코드를 변경하면서 말이다.  
> (현재 설계=> 램프 기능 필요할땐 Lamp와, 알람 기능 필요할땐 Alarm과 연관관계 맺도록 기존코드 변경해야함)    

    

<br>

지금의 설계에서, 버튼을 통해서 수행되는 버튼의 기능을 변경시키려고하면 코드를 변경해야하는데  

진정한 만능 버튼이라면, **프로그램을 실행할때 버튼의 기능을 변경할 수 있어야한다.**  

그러나 현재의 코드설계는, 코드를 수정하고나서야 버튼에 새로운 기능을 부여할 수 있었다. 

그래서 현재 설계는 기존 의도와 맞지않으므로 문제가 있는 설계이다.

<br><br>

#### <p align="center">그렇다면 현재의 버튼이 만능버튼으로써 동작하도록하려면 어떻게 해야할까? </p>

       Ex) 예를들어, 버튼이 처음 눌렸을 때는 램프를 켜고, 버튼이 두 번쨰 눌렸을 때는 알람을 동작시키려면?

<br>

> <p align="center">  버튼에 새로운 기능이 요구됐을때</p>    

>  **<p align="center">`기존의 코드를 변경하지않으면서`**  </p>

>  **<p align="center">`프로그램이 실행할때 버튼의 기능을 변경할 수 있도록`** 구현해야 할 것이다. </p>  



<br><br>

## 설계 변경(문제점 해결)

<br>

프로그램이 실행되었을때, 버튼으로 램프를 켤 수도 있고 알람도 시작시킬 수 있도록 설계를 변경해보자. 

> (버튼을 한번 눌렸을때 램프를켜고 또 한번 눌렸을때는 알람을 켜고...)

<br>

이를 위해선 Button 클래스가 어느하나만이 아닌,   

Lamp와 Alarm 클래스 둘과 모두 연관관계를 맺어야할 것이다.  

<br>

설계는 아래와 같다.  

<br><br>

<div align="center">
 <img src="https://github.com/user-attachments/assets/d0943ebd-8a3d-4c78-b7da-ab19dc293901">
</div>

<br>

```java
public class Lamp {
    public void turnOn(){ System.out.println("Lamp on"); }
}
```
```java
public class Alarm {
    public void start(){ System.out.println("Alarming ..."); }
}
```
```java
// 현재 버튼의 모드를 나타내는 Mode 열거형
// 현재 모드가 ALRAM 모드라면 알람start, LAMP 모드라면 램프on
enum Mode {LAMP, ALRAM}

public class Button {
    private Alarm theAlarm;
    private Lamp theLamp;
    // 버튼의 현재 모드를 나타내는 필드
    private Mode theMode;

    public void setTheMode(Mode theMode) {
        this.theMode = theMode;
    }

    public Button(Lamp theLamp,Alarm theAlarm) {
        this.theLamp = theLamp;
        this.theAlarm = theAlarm;
    }

    /* 현재 이 버튼이 어떤 모드인지에 따라서,
    램프를 켤 수도 있고 or 알람을 시작할 수도 있다. */
    public void pressed(){
        switch (theMode){
            case LAMP -> theLamp.turnOn();
            case ALRAM -> theAlarm.start();
        }
    }
}

```
```java
public class Main {
    public static void main(String[] args) {
        Lamp lamp = new Lamp();
        Alarm alarm = new Alarm();

        //버튼은 Lamp, Alarm 둘과 연관관계을 맺음
        Button button = new Button(lamp, alarm);

        //버튼의 모드를 LAMP로 설정하여 => 버튼의 기능을 램프켜기로 설정
        button.setTheMode(Mode.LAMP);
        button.pressed();

        //버튼의 모드를 ALRAM으로 설정하여 => 버튼의 기능을 알람시작으로 설정
        button.setTheMode(Mode.ALRAM);
        button.pressed();
    }
}
```

<br>

이로써, 모드값 변경을통해 프로그램 실행시에 버튼의 기능을 변경할 수 있게되었다.  

<br>

**하지만, 이 역시도 문제가 발생하는 코드설계이다.**  

<br>

왜냐하면, 버튼에 새로운 기능추가시 Button 클래스의 pressed() 메서드에 새로운 case를 더 추가해주는 등      
기존의 코드를 계속 변경시켜줘야한다는 OCP 위반이 발생하기때문이다.
> (Ex 버튼에 TV 켜기 기능 추가시 열거형 Mode에 새로운 TV 모드값을 추가해줘야하고, Tv 클래스와 연관관계를 맺기위해 Button 클래스에 private tv맴버변수 추가 및 생성자 코드를 변경해줘야하며 pressed() 메서드의 switch문에 TV case도 새로 추가해줘야할 것이다.)

<br>

기능 하나 추가만으로도 기존코드에 위와 같은 변경이 일어나게된다.

<br>

**즉, 개선된 현재의 설계 역시 OCP를 만족하지못한다.**




