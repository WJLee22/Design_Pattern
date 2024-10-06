
#   Command Pattern

<br>

## Command(커맨드) 패턴이란?

+ 실행될 기능을 캡슐화(클래스화)함으로써 각 기능을 객체로 만들어 관리하기에, 주어진 여러 기능을 실행할 수 있는 재사용성이 높은 클래스를 설계하는 패턴  

+ 이벤트가 발생했을 때(ex 버튼 클릭), 해당 이벤트에 대해 실행될 기능을 다양하게 해주면서도 기능의 변경이 필요한 경우엔 쉽게 변경이 가능하도록해주는 패턴   

+ 변경 및 추가 삭제가 빈번한 경우, 이벤트를 발생시키는 클래스(ex 버튼)를 변경하지 않으면서도 기능변경이 가능하도록해주는 패턴   

<br>

>커맨드 패턴은 실행될 기능을 캡슐화함으로써 기능의 실행을 요구하는 호출자 클래스(Invoker)와 실제 기능을 실행하는 수신자 클래스(Receiver) 사이의 의존성을 제거한다 (호출자와 수신자간의 연관관계 제거). 

>따라서 실행될 기능의 변경에도 호출자 클래스를 수정없이 그대로 사용할 수 있도록 해준다.
>
>(즉 호출자 클래스를 수정없이 그대로 사용할 수 있기때문에, 실행될 기능을 캡슐화함으로써 주어진 여러 기능을 실행할 수 있는 클래스 설계가 가능하다 by 커맨드 패턴)
>
>

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

## 1차 설계 개선

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
기존의 코드를 계속 변경시켜줘야한다는 **OCP 위반이 발생하기때문이다.**
> (Ex 버튼에 TV 켜기 기능 추가시 열거형 Mode에 새로운 TV 모드값을 추가해줘야하고, Tv 클래스와 연관관계를 맺기위해 Button 클래스에 private tv맴버변수 추가 및 생성자 코드를 변경해줘야하며 pressed() 메서드의 switch문에 TV case도 새로 추가해줘야할 것이다.)

<br>

기능 하나 추가만으로도 기존코드에 위와 같은 변경이 일어나게된다.

<br>

**즉, 개선된 현재의 설계 역시 OCP를 만족하지못한다.**

<br> 

### <div align="center">이 OCP 위반문제를 Strategy 패턴을 응용하여 한번 해결해보자.</div>

<br><br><br>

## 2차 설계 개선(by Strategy 패턴 응용)

<br>

+ **기능을 갖는 주체(`Context`) : `Button 클래스`**

<br>

+ **실제 변하는 것(`Concrete Strategy`) : `AlarmStartCommmand, LampTurnOnCommand... 클래스`**  
**(만능 버튼의 기능은, 실제로 경우에 따라서 알람시작 or 램프켜기 or...로 변경된다)**
> 즉, 실제 변하는 것은 **버튼이 수행하는 기능**이다. 알람을 시작시키거나 램프를 키거나하는 이 버튼의 기능이자 실행동작(execute)을, `Command 구체 전략 클래스`로 모델링해준다

<br>

+ **변경되는 동작 및 기능(구체전략)들을 포괄하는-추상화시킨 개념(`Strategy`) : `Command 인터페이스`**
> 버튼이 수행하는 기능들을 일반화모델링한 개념

<br><br>

<div align="center">
<img src="https://github.com/user-attachments/assets/73841c7c-1631-4cd6-9f1f-a29602c524bd">
</div>

<br>

**Strategy 패턴과 설계구조 모양새가 비슷해보이나, 전략이 아닌, 무언가를 실행하는 명령기능들을 하위에 두었다는게 차이점이다.**

<br>

코드로 작성하면 아래와 같다. 

<br>

```java

public class Button {
    private Command theCommand; //버튼이 수행하는 커멘트를 레퍼런스하는 필드

    //커멘드를 설정해주는 설정자
    public void setTheCommand(Command theCommand) {
        this.theCommand = theCommand;
    }

    public void pressed(){
    theCommand.execute();
    // 버튼 입장에서는, theCommand가 어떤 커멘드인지에 구애받지않고 theCommand.execute()로 기능을 실행가능함
    // (버튼이 실행하는 기능이 숨겨져있는 캡슐화, 커멘드마다 실행되는 기능이 다르다는 다형성)
    // 알람을 시작한다던지 램프를 켠다던지와 같은 버튼의 실행기능(command)을 외부에서 설정만해주면 어떤 기능이던지 해당 기능을 execute 가능.
    }
}
```
```java
public interface Command {
    public void execute();
}
```
```java
//알람을 시작하도록하는 버튼의 기능이자, 실행명령인 AlarmStart Command 클래스
public class AlarmStartCommand implements Command{

    private Alarm theAlarm;//AlarmStartCommand 클래스는 "알람을 시작해라"는 버튼의 실행명령이자 기능이므로
                           //Alarm 과 연관관계를 맺어야한다.

    public AlarmStartCommand(Alarm theAlarm) {
        this.theAlarm = theAlarm;
    }

   @Override
    public void execute() { //버튼이 수행하는 하나의 기능이자 명령.
        theAlarm.start(); // 기능은 알람 시작하기.
    }
}
```
```java
//램프를 켜도록하는 버튼의 기능이자, 실행명령인 LampTurnOn Command 클래스

public class LampTurnOnCommand implements Command{

    private Lamp theLamp;//LampTurnOnCommand 클래스는 "램프를 켜라"는 버튼의 실행명령이자 기능이므로
                         //Lamp 와 연관관계를 맺어야한다.

    public LampTurnOnCommand(Lamp theLamp) {
        this.theLamp = theLamp;
    }

    @Override
    public void execute() { //버튼이 수행하는 하나의 기능이자 명령.
        theLamp.turnOn(); //그 기능은 램프 켜기.
    }
}
```
```java
public class Main {
    public static void main(String[] args) {
        Alarm alarm = new Alarm();
        Lamp lamp= new Lamp();
        Button button = new Button();

        //알람을 start 시키기위해서는, 알람을 start 시키는 Command가 필요하다
        //alarm 인스턴스를 생성자에 넘겨주어, 해당 alarm 인스턴스에 대해서 start하도록 명령
        ////alarmOnCommand와 alarm이 연관관계 맺도록.
        Command alarmOnCommand = new AlarmStartCommand(alarm);

        //이 button이라는 만능버튼의 기능이자 실행명령을 alarmOnCommand, 즉 알람시작 기능으로 설정
        button.setTheCommand(alarmOnCommand);
        button.pressed(); //현재 이 버튼의 기능은 알람시작 기능이므로, 버튼을 누르면 알람이 시작됨

        Command lampOnCommand = new LampTurnOnCommand(lamp); //lampOnCommand와 lamp가 연관관계 맺도록.

        button.setTheCommand(lampOnCommand); //이번엔 만능버튼의 기능을 램프켜기로 설정.
        button.pressed(); //현재 이 버튼의 기능은 램프켜기 기능이므로, 버튼을 누르면 램프가 켜짐

    }
}
```

<br>

> **이로써, OCP 원칙을 위반하지않으면서 버튼의 기능을 변경할 수 있게되었다.**

> **또한 Stratrgy 패턴을 응용하였기에, 버튼에 새로운 기능을 추가할때도 기존코드의 변경없이 기능추가가 가능해졌다.**

<br>

**<div align="center">그렇다면, 만능 버튼에 TV를 켜는 기능을 한번 추가해보자.</div>**  

<br>

> **변화하는 것(TV켜기 기능)을 Command 인터페이스를 구현하는 하위 개념으로 모델링하고, TV 클래스와 연관관계를 맺도록 설계하면 된다.**

<br><br>

<div align="center">
<img src="https://github.com/user-attachments/assets/3c9f61bc-4d54-4cbd-ba36-d4d560d77a9b">
</div>

<br>

```java
public class Tv {

    public void on(){
        System.out.println("Tv on");
    }

}
```
```java
public class TvOnCommand implements Command{
    private Tv tv;

    public TvOnCommand(Tv tv) {
        this.tv = tv;
    }

    @Override
    public void execute() {
        tv.on();
    }
}
```

```java
public class Main {
    public static void main(String[] args) {
        Alarm alarm = new Alarm();
        Lamp lamp= new Lamp();
        Button button = new Button();

        Command alarmOnCommand = new AlarmStartCommand(alarm);
        button.setTheCommand(alarmOnCommand);
        button.pressed(); 

        Command lampOnCommand = new LampTurnOnCommand(lamp);
        button.setTheCommand(lampOnCommand);
        button.pressed(); 
        
        // 버튼에 tv켜기 기능추가 
        Command tvOnCommand = new TvOnCommand(new Tv());
        button.setTheCommand(tvOnCommand); //만능버튼의 기능을 tv켜기로 설정.
        button.pressed(); //버튼을 누르면 tv가 켜짐
    }
}
```


<br>

**<div align="center">위와 같이, 기존 코드의 변경없이(OCP 위반없이) 만능 버튼에 새로운 기능을 추가시켰다.</div>**  

#### <div align="center">이것이 `Command 패턴`이다.</div>


<br><br>

## 커맨드 패턴의 일반적인 설계구조

<br>

<div align="center">
<img src="https://github.com/user-attachments/assets/a9999342-33e4-4eeb-9312-c2c0d13d89c5">
</div>

> 출처: Java객체지향 디자인패턴(한빛미디어) 

<br><br>

+ **`Invoker 클래스(호출자)`: 기능의 실행을 요청하는 `호출자 클래스`**  
  > 예제에서는 Button 클래스 

<br>

+ **`Command 인터페이스`: 실행될 기능에 대한 인터페이스로, 실행될 기능을 execute 메서드로 선언한다.**

<br>
  
+ **`ConcreteCommand 클래스`: 실제로 실행되는 기능을 나타내는 클래스.**  
  Command 인터페이스의 execute() 메서드를 구현하여 이를통해 자신이 수행할 기능을 정의한다.
  
    > 예제에서는 AlarmStartCommand, LampTurnOnCommand... 클래스

<br>
    
+ **`Reciever 클래스(수신자)`: ConcreteCommand의 기능 실행을 위해 사용되는 `수신자 클래스`.**  
                            실제 기능을 수행하는 주체.  
                            ConcreteCommand의 execute()를 통해 Reciever가 action을 취함.(ex 램프 불을켜고, 알람시작하고...)  
              
  
    > 예제에서는 Lamp, Alarm... 클래스 

<br><br><br>

### <div align="center">즉, 커맨드 패턴의 일반적인 구조는</div>  

<br>

> 현재 버튼의 기능이 램프켜기인 경우,
> 
> 누군가가 버튼을 눌러서 **버튼(Invoker)** 의 pressed가 실행이되면,
>   
> **AlarmStartCommand(ConcreteCommand)** 가 execute해서 **Alarm(Reciever)** 이 action하는 구조이다

<br><br>

**그래서 만약, 새로운 기능을 추가한다고하면 =>**          
```
Command 인터페이스의 하위개념으로 실행될 기능인 ConcreteCommand 클래스를 모델링해두고,

ConcreteCommand에서 execute()로 어떤 명령을 내리면,

연관관계이자 - 실제로 그 명령을 수행하는 주체인 Reciever 클래스에서 action을 수행하도록 설계하면 된다.
```

<br><br><hr><br>

## 마무리

<br><br>

> + **커맨드 패턴은 실행할 기능을 캡슐화하여, 각 기능을 객체로 만들어 관리. 이를 통해 기능의 재사용성과 유연성이 높아짐**

<br>

> + **버튼 클릭과 같은 이벤트가 발생했을 때, 해당 이벤트에 대응하는 여러 기능을 실행할 수 있도록한다.  
기능이 변경되거나 추가될 경우, 호출자(Invoker) 클래스를 수정하지 않고도 쉽게 기능변경 가능**

<br>

> + **커맨드 패턴은 호출자와 수신자(Receiver) 간의 의존성을 없앤다. 호출자는 어떤 기능이 실행될지를 알 필요가 없고, 오직 커맨드 객체만 알면된다. 이를 통해 코드의 결합도를 낮추고, 유지보수가 용이해짐**

<br>

> + **커맨드 패턴을 사용하면 기능을 추가하거나 삭제할 때 호출자 클래스를 수정할 필요 없이, 새로운 커맨드 객체를 생성하거나 기존의 커맨드 객체를 수정하여 쉽게 변경할 수 있음**

<br>

#### 결론적으로,  
#### `커맨드 패턴은` 기능의 캡슐화와 호출자/수신자 간의 의존성 제거를 통해 유연하고 재사용 가능한 코드 구조를 제공하는 디자인 패턴



