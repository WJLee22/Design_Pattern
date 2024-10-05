
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

    

<br>

지금의 설계에서, 버튼을 통해서 수행되는 버튼의 기능을 변경시키려고하면 코드를 변경해야하는데  

진정한 만능 버튼이라면, **프로그램을 실행할때 버튼의 기능을 변경할 수 있어야한다.**  

그러나 현재의 코드설계는, 코드를 수정하고나서야 버튼에 새로운 기능을 부여할 수 있었다. 

그래서 현재 설계는 기존 의도와 맞지않으므로 문제가 있는 설계이다.

<br><br>

#### <p align="center">그렇다면 현재의 버튼이 만능버튼으로써 동작하도록하려면 어떻게 해야할까? </p>

<br>

> <p align="center">  버튼에 새로운 기능이 요구됐을때</p>    

>  **<p align="center">`기존의 코드를 변경하지않으면서`**  </p>

>  **<p align="center">`프로그램이 실행할때 버튼의 기능을 변경할 수 있도록`** 구현해야 할 것이다. </p>  



<br><br>

## 해결



