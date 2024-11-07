

#   Template Method Pattern

<br>

## Template Method(템플릿 메서드) 패턴이란?

<br>

+ **템플릿 메서드 패턴(Template Method Pattern)은 여러 클래스에서 공통으로 사용하는 메서드를 템플릿화 하여 상위 클래스에서 정의하고, 하위 클래스마다 세부 동작 사항을 다르게 구현하는 행위패턴**

> 디자인 패턴에서의 **템플릿**은 변하지 않는 것을 의미한다.

<br>

+ **변하지 않는 기능(템플릿)은 상위 클래스에 만들어 고정해두고 자주 변경되며 확장할 기능은 하위 클래스에서 만들어, 세부 실행 내용은 다양화 될 수 있도록하는 패턴**  
  
<br>



<br>

## 엘리베이터 모터 구동시키기 예제를 통해 알아보는 Decorator Pattern      

<br>

## `<초기설계>`

<div align="center"><img src="https://github.com/user-attachments/assets/3e633839-9563-4fdd-bb8d-9787bb1a3b86"></div>

- **`HyundaiMotor 클래스`: 엘리베이터를 구동시키는데 필요한 HyundaiMotor사의 모터를 나타낸 클래스** (direction값에 따라 모터를 움직여 엘리베이터를 구동시키는 기능인 move()&moveHyundaiMotor() 메서드)
  > move()는 외부에서 호출되는 메서드,  move()를 통해서 -> **실제 모터를 구동시키는건 moveHyundaiMotor() 메서드**
  
  > 단, 모터를 구동시키기전에 getMotorStatus()메서드로 현재 모터의 상태를 확인해보고 -> 현재 모터의 상태가 moving이라면 break;
  >   
  > 현재 모터가 구동중이 아니라면 구동시킴 -> 현재 모터 상태를 setMotorStatus(Moving)로 변경. 

- **`Door 클래스`: 엘리베이터 문 클래스** (엘리베이터 문이 열린채로 구동시키면 위험하니, 모터 클래스는, 현재 엘리베이터 문의 상태 파악을 위해 이 클래스와 연관관계)
  > 문을 열고 닫을 수 있도록하는 open(), close() 메서드와 현재 문의 OPENED 상태인지 CLOSED 상태인지를 알려주는 getDoorStatus()메서드 제공.
  >   
  > 즉, 모터 클래스는, Door클래스의 getDoorStatus()메서드로 현재 문의 상태를 확인하고-> 현재 문이 열려있으면 문을 close하고 구동 - 닫혀있으면 그냥 구동
  
- **`Direction enumeration`: 모터를 어느 방향으로 움직일 것인지 그 방향을 나타내는 enumeration타입의 Direction** (UP, DOWN)
- **`MotorStatus enumeration`: 모터가 구동중인지 멈춰있는지 상태를 나타내는 enumeration타입의 MotorStatus** (MOVING, STOPPED)
- **`DoorStatus enumeration`: 엘리베이터 문이 열러있는지 닫혀있는지 상태를 나타내는 enumeration타입의 DoorStatus** (OPENED, CLOSED)
<br>

```java
public class RoadDisplay {

    public void draw() {
        System.out.println("Basic Road Display");
    }
}
```
```java
public class RoadDisplayWithLane extends RoadDisplay{
    public void draw(){
        super.draw(); //기본 도로 그리기.
        drawLane(); //추가적으로 차선을 그리기.
    }

    private void drawLane(){
        System.out.println("Lane Display");
    }
}

```
```java
public class Main {
    public static void main(String[] args) {
        RoadDisplay road = new RoadDisplay();
        road.draw(); //기본적인 도로가 표시됨

        RoadDisplayWithLane roadWithLane = new RoadDisplayWithLane();
        roadWithLane.draw(); //기본 도로 + 차선까지 표시됨
    }
}

```
**도로 표시를 위한 기존 코드 설계는 위와 같다.**    
**하지만 위와 같은 설계는 문제점이 있다** 

<br><br><hr><br>

### <p align="center">`<초기설계의 문제점>`</p> 

<br>

+ **만약 추가적인 도로 표시 기능을 구현하고 싶다면 어떻게 해야 하는가?  
  예를 들어 기본 도로 표시에 + 교통량을 표시하고 싶다면?**
  
`=> 이 경우, RoadDisplayWithLane 클래스와 마찬가지로 RoadDisplayWithTraffic 이라는 새로운 클래스를 만들어야 한다.`


![image](https://github.com/user-attachments/assets/a3ebfa1e-953b-4f28-82c7-64b761ad1427)

```java
public class RoadDisplayWithTraffic extends RoadDisplay{

    public void draw(){
        super.draw(); // 슈퍼클래스의 draw() => 기본 도로 표시.
        drawTraffic(); // 교통량 표시.
    }

    public void drawTraffic() {
        System.out.println("Traffic Display");
    }
}
```
```java
public class Main {
    public static void main(String[] args) {
        RoadDisplayWithTraffic roadWithTraffic = new RoadDisplayWithTraffic();
        roadWithTraffic.draw(); //기본 도로 + 교통량 표시됨
    }
}
```

<br>

위와 같이 추가적인 도로 표시 기능을 구현하고자할때 기본 도로 표시 클래스를 상속받는 추가적인 클래스를 만들어 일반화관계를 만들기에 기존 코드의 변경이없어서 크게 문제가 되지않는 것처럼 보일 수 있다.  

하지만 아래 요구사항을 생각해보면 문제점이 보일 것이다.  

<br>

+ **여러가지 추가 기능들을 `조합`하여 제공하고 싶다면 어떻게 해야 하는가?  
  예를 들어 기본 도로 표시에 차선 표시 기능과 교통량 표시 기능을 함께 제공하고 싶다면?**

=> 지금은 기본 도로 표시+차선 표시, 기본 도로 표시+교통량 표시... 처럼, 별도의 클래스를 만들고 그 안에 추가적인 표시 메서드를 구현하여 기본 도로 표시 메서드 호출 + 각각의 메서드를 호출하는 방식이었다.  

하지만 이와다르게 **`여러가지 추가 기능들을 하나로 조합하여,` 기본 도로만을 표시했을때=> 기본도로 + 차선 표시 + 교통량 표시 등등 여러가지 기능들을 조합하여 한번에 표시되도록 하고싶다면** 어떻게해야하는지를 묻는 것이다.    

<br>

만약 기존처럼 추가적인 클래스를 만들어나가는 방식이었다면   

![image](https://github.com/user-attachments/assets/93f5bd41-fc85-466a-8300-c02df4c6a775)

위처럼 차선,교통량을 표시해주는 메서드인 drawLaneTraffic()을 갖는 RoadDisplayWithLaneTraffic 클래스를 추가적으로 만들어야할 것이다.  

=> 이는, 현재 존재하는 차선 표시 클래스와 교통량 표시 클래스가 존재함에도 이 둘을 활용하지 않고 새로운 클래스를 만들었기에 코드의 낭비라고 볼 수 있다.  

아래의 표와 클래스 다이어그램을 보자.  
기본 도로 표시 기능에 + 추가기능인 차선 표시 - 교통량 표시 - 교차로 표시 기능들을 조합해야 하는 경우, 추가기능 조합의 가능한 경우의 수만큼 클래스를 생성해줘야한다. 만약 **여기에 추가적인 표시기능들이 추가된다면 더 많은 조합들이 생김으로써 클래스의 갯수가 기하급수적으로 증가될 것이다.**(조합의 갯수는 n(기능의 수)!+1+1개)

<br>

![image](https://github.com/user-attachments/assets/ba1ed0c7-9f2d-4677-9560-73527a17727c)

![image](https://github.com/user-attachments/assets/772ed322-511c-47b9-9e9c-65de275dc52a)

<br>

우리의 목표는, 이처럼 기능이 추가될때마다 구현해야하는 클래스의 갯수가 기하급수적으로 늘어나는 설계가 아니고  **`추가되는 기능의 수와 그에따라 추가되는 클래스의 수가 선형적으로 비례하는 설계`** 이다.  

지금 설계구조를보면, RoadDisplay라는 기본 도로 표시 기능의 클래스를 모든 표시기능의 클래스들이 상속받는 일반화관계 구조를 보이고 있다.  

이는 단순히 super.draw()로 RoadDisplay의 기본 도로 표시 기능을 서브클래스에서 재사용하기위한 상속일 뿐이다.  
**단순 기능 재사용을 위한 일반화관계는 상속의 잘못된 사용이다. 이는 `SOLID의 LSP원칙에 위반되기때문이다`**  

>  LSP(Liskov Substitution Principle): 부모 클래스의 인스턴스를 자식 클래스의 인스턴스로 대체해도 프로그램의 동작이 바뀌지 않아야 한다는 원칙  

상속 관계를 사용하는 것이 기능 재사용의 일반적인 방법처럼 보이지만, 상속은 IS-A 관계를 모델링할 때만 사용해야 한다.  
즉, 자식 클래스가 부모 클래스의 "일종"이라는 관계가 성립할 때만 상속을 사용하는 것이 적절하다.  

단순 기능 재사용을하려면 상속관계가 아닌 **연관관계**를 사용해야함을 명심하자.

<br>

### <div align="center">일반화관계(상속관계) 대신에 연관관계로 기본 도로 표시 기능을 재사용하도록 설계를 개선해보자.</div>

<br><br><br>

## 설계 개선

도로 표시 기능에 차선 표시 기능을하는 RaodDisplayWithLane 클래스와 도로 표시 기능의 RoadDisplay 클래스가 있다면, RaodDisplayWithLane클래스는 RoadDisplay클래스의 도로 표시 기능 draw() 메서드를 재사용하고 싶을 것이다.  

이때 **상속관계가 아닌 연관관계로** RoadDisplay클래스의 도로 표시 기능 draw() 메서드를 재사용해보자는 것이다.   


![image](https://github.com/user-attachments/assets/7438cfdd-ab66-4cb7-894b-ed6c4cd6eb03)

```java
public class RoadDisplayWithTraffic{
    private RoadDisplayWithLane roadDisplayWithLane;

    public RoadDisplayWithTraffic(RoadDisplayWithLane roadDisplayWithLane){
        this.roadDisplayWithLane=roadDisplayWithLane;
    }

    public void draw(){
        roadDisplayWithLane.draw(); // 슈퍼클래스의 draw() => 기본 도로 표시.
        drawTraffic(); // 교통량 표시.
    }

    public void drawTraffic() {
        System.out.println("Traffic Display");
    }
}
```

<br>

위처럼 연관관계를 이용하여 RoadDisplayWithTraffic 클래스가 RoadDisplayWithLane 클래스와 연관관계를 맺도록하면, 새로운 클래스를 만드는게 아니라 기존 클래스를 조합하여 기본 도로+차선+교통량 표시 기능을 구현할 수 있다.  

하지만 문제가 하나있다.  
만약 기본 도로+차선+교통량 표시 기능 전부가아니라 -> 기본 도로 표시에 + 교통량 표시만 하고 싶은 경우라면 **RoadDisplayWithTraffic 클래스가 roadDisplayWithLane 클래스가 아닌 RoadDisplay 클래스와 연관관계를 맺도록 코드를 변경**해줘야 할 것인데, **이러면 기존 코드가 변경되어야하기에 OCP 원칙에 어긋하게 된다.**  

> **if) 연관관계 변경시**
> 
> ```java
> public class RoadDisplayWithTraffic{
>   private RoadDisplay roadDisplay; // 연관관계 변경) RoadDisplayWithLane -> RoadDisplay
>
> public RoadDisplayWithTraffic(RoadDisplay roadDisplay){
>      this.roadDisplay=roadDisplay;
>   }
>
> public void draw(){
>     roadDisplay.draw(); 
>      drawTraffic(); // 교통량 표시.
>   }
>
>   public void drawTraffic() {
>        System.out.println("Traffic Display");
>   }
>}
>
>
> ```

<br>

위의 예시처럼, 기존의 클래스를 조합해서 재사용하기에 클래스를 만들지 않아도된다는 장점이 있지만, **기능을 변경하기위해 연관관계를 변경하면 OCP원칙을 위반하게된다.**  

그럼 어떻게 **연관관계를 통한 재사용성**과 **OCP 원칙 성립**을 유지하면서 네비게이션 SW를 설계해볼 수 있을까?  

<br>

**`이 기능들을 대표하는 하나의 포괄 개념을 만들어보자.`**  

<br>

![image](https://github.com/user-attachments/assets/7df047c1-4955-49be-9900-781518bf6c6b)



표시 기능에 대한 포괄 개념을 Display추상 클래스로 모델링하면, 표시 기능들이 추가되어도 Display의 하위 구체 클래스로 모델링해주면 된다.  

그런데, 지금 다이어그램을 보면 Display 추상클래스가 자기 자신과 연관관계를 맺는 것이 보인다. **이는 필드로 자기참조 인스턴스를 갖고있음을 말한다.**    

> 자기 참조: 클래스 내부에 자기 자신의 타입을 갖는 멤버 변수를 선언하는 것을 의미

<br>

Display는 모든 표시 기능들을 포괄하는 개념이기에 Display가 자기자신 Display와 연관관계를 맺는다는 것은,  
Display를 상속받은 구체 클래스들이 다른 모든 구체 클래스들과 연관관계를 맺을 수 있다는 것이다.  

<br>

> 다만, 기본 도로 표시 기능의 RoadDisplay클래스는 기본 기능을 제공하는 클래스이기에 추가 기능으로 취급하면 안된다.
> so, 추가기능들을 포괄하는 Display의 하위개념으로 둘 수 없어 따로 둔다.
> 
> RoadDispalyWithTraffic, RoadDispalyWithLane 등등의 기능 클래스들은 **다른 추가 기능들 + RoadDisplay의 기본 기능과 연관관계**를 맺으며 기능을 구현하는 반면, **RoadDisplay클래스는 기본 기능을 제공하는 클래스이기에 다른 기능 클래스들과 연관관계를 맺지않는다.**   

<br>

그럼, 이 따로 떨어진 RoadDisplay 클래스와 Display 추상클래스를 연관짓기위해 Display라는 포괄클래스를 하나 모델링 해주자. 기존 Display 추상클래스의 이름을 DisplayDecorator 로 이름을 변경하고, 새로운 포괄클래스의 이름을 Display로 하자.  

그럼 이 **Display 추상클래스는 기본 기능과(RoadDisplay) + 다른 추가 기능들(DisplayDecorator 타입)을 모두 포괄하는 개념이다.**  


<br>

이제 이 기본 기능+추가 기능 전체를 포괄하는 개념인 Display를 적용시킨 설계는 다음과같다.  

<br>

![image](https://github.com/user-attachments/assets/d55c1ad1-56fb-450f-9e47-139be9389596)

<br>

**DisplayDecorator 클래스가 Display 클래스와 연관관계를 맺음으로써,** 부가 기능들은 자기들 간의 연관관계를 맺으면서도 기본 기능과도 연관관계를 맺을 수 있게되었다.  

또한, RoadDisplay 기본 기능 클래스는 다른 누구와도 연관관계를 맺지 않도록되었다.  


```java
public abstract class Display {

    public abstract void draw();
}
```
```java
public class RoadDisplay extends Display {

    public void draw() {
        System.out.println("Basic Road Display");
    }
}
```
```java
public abstract class DisplayDecorator extends Display{
    private Display display;

    public DisplayDecorator(Display display) {
        this.display = display;
    }

    public void draw(){
        display.draw();
    }
}
```
```java
public class roadDisplayWithLane extends DisplayDecorator {

    public roadDisplayWithLane(Display display){
        super(display);
    }

    public void draw(){
        super.draw();
        drawLane(); //추가적으로 차선 표시.
    }

    private void drawLane(){
        System.out.println("Lane Display");
    }
}
```
```java
public class roadDisplayWithLaneTraffic extends DisplayDecorator {

    public roadDisplayWithLaneTraffic(Display display){
       super(display);
    }

    public void draw(){
        super.draw();
        drawTraffic(); // 교통량 표시.
    }

    public void drawTraffic() {
        System.out.println("Traffic Display");
    }
}
```
```java
public class roadDisplayWithLaneTrafficCrossing extends DisplayDecorator{

    public roadDisplayWithLaneTrafficCrossing(Display display) {
        super(display);
    }
    public void draw(){
        super.draw(); //기본 도로 표시 or 다른 추가적인 표시
        drawCrossing();
    }

    public void drawCrossing(){
        System.out.println("Crossing Display");
    }
}
```
```java
public class Main {
    public static void main(String[] args) {
        RoadDisplay road = new RoadDisplay();
        road.draw(); //기본적인 도로가 표시됨

        Display roadWithLane = new RoadDisplayWithLane(road);
        roadWithLane.draw(); //기본 도로 + 차선까지 표시됨

        Display roadWithLaneTraffic = new RoadDisplayWithTraffic(roadWithLane);
        roadWithLaneTraffic.draw(); //기본 도로 + 차선 + 교통량 표시됨

        Display roadWithLaneTrafficCrossing = new RoadDisplayWithCrossing(roadWithLaneTraffic);
        roadWithLaneTrafficCrossing.draw(); //기본 도로 + 차선 + 교통량 + 교차로 표시됨
    }
}

```

<br><br>

```java 
Display roadWithLaneTrafficCrossing = new RoadDisplayWithCrossing(roadWithLaneTraffic);
```  

위의 코드처럼, 인자값을 달리하여 여러가지 표시 기능들을 조합해볼 수 있다.  

위와 같은 설계로, 추가된 새로운 기능을 제공하기위해서 조합 경우의수의 증가로 클래스의 갯수가 기하급수적으로 늘어나는 단점이 해결되었다.  

**클래스의 갯수가 기능의 갯수와 선형적으로 증가하는 이상적인 형태가 된것이다.**

<br><br>

**✅여기서, 위 코드처럼 기본 기능에 부가기능들을 조합-데코레이팅하는 과정을 아래와같이 `체이닝을 이용하여 한줄로 간단하게 작성`해볼 수 있다.** 

```java
new RoadDisplayWithCrossing(new RoadDisplayWithTraffic(new RoadDisplayWithLane(road))).draw();
```

> 출력되는 순서는 내부에서 외부쪽으로, 즉 오른쪽에서 왼쪽순 (road -> Lane -> Traffic -> Crossing)

<br><br>

<div align="center">
<img src="https://github.com/user-attachments/assets/9f2086c8-5fbf-4e44-a9c3-4684a07a4886">
</div>

> 출처: Java객체지향 디자인패턴(한빛미디어)

<br><br>

## 마무리

<br>

>
+ **데코레이터 패턴은 기본 기능에 추가할 수 있는 기능의 종류가 많은 경우 유용한 Structural설계패턴 이다**  
>
+ **데코레이터 패턴은 기본 기능에 추가될 수 있는 많은 수의 부가기능에 대해서 `기존의 코드를 변경하지 않으면서도(OCP 만족)` `다양한 조합을 동적으로 구현`할 수 있는 패턴이다**
>

<br><hr><br>

## Decorator 패턴의 일반적인 설계구조

<br>

<div align="center">
<img src="https://github.com/user-attachments/assets/ad420e48-9f1d-49d8-b983-0d67c1fb1ded">
</div>

> 출처: Java객체지향 디자인패턴(한빛미디어)   
> 위 그림에서는 Decorator와 Component 간에 합성관계로 모델링되었지만, 예제에서처럼 연관관계로 모델링해도 된다.  

<br><br>

> + **`Component (추상클래스)`: 기본 기능을 뜻하는 ConcreteComponent와 추가 기능을 뜻하는 Decorator를 포함하는 추상클래스(기본기능 객체와 장식자 객체 모두를 묶는 역할)**

<br>

> + **`ConcreteComponent 클래스`: 기본 기능을 구현하는 클래스(데코레이팅 할, 꾸며줄 대상인 기본 객체)**

<br>

> + **`Decorator (추상클래스)`: 부가 기능들을 일반화시킨 추상클래스**

<br>

> + **`ConcreteDecorator 클래스`: 기본 기능을 꾸며줄, 기본 기능에 추가되는 부가기능 클래스**

<br>
<br>

+ **여기서, Decorator가 Component와 연관관계를 맺도록 설계된 이유는
부가기능인 ConcreteDecorator들이 자기들 뿐만아니라 기본기능 ConcreteComponent도 참조할 수 있도록 하기 위함이다**

<br>
<br>
<br><br>
