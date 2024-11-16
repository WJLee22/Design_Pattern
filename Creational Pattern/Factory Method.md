

#   Factory Method Pattern

<br>

## Factory Method(팩토리 메서드) 패턴이란?

<br>

+ **팩토리 메서드 패턴(Factory Method Pattern)은 여러 클래스에서 공통으로 사용하는 메서드를 템플릿화 하여 상위 클래스에서 정의하고, 하위 클래스마다 세부 동작 사항을 다르게 구현하는 행위패턴**

> 디자인 패턴에서의 **템플릿**은 변하지 않는 것을 의미한다.

<br>

+ **변하지 않는 기능(템플릿)은 상위 클래스에 만들어 고정해두고 자주 변경되며 확장할 기능은 하위 클래스에서 만들어, 세부 실행 내용은 다양화 될 수 있도록하는 패턴**  
  
<br>



<br>

## 사과를 디저트로 제공하는 식당클래스 예제를 통해 알아보는 Decorator Pattern      

<br>

## `<초기설계>`

<div align="center"><img src="https://github.com/user-attachments/assets/b2175945-7fd0-4949-94ab-9cb785cede92"></div>

<br>

<div align="center"><img src="https://github.com/user-attachments/assets/7cdf0f42-172e-45fd-9815-63a036f565bb"></div>

> 출처: Java객체지향 디자인패턴(한빛미디어)

<br>

- **`HyundaiMotor 클래스`: 엘리베이터를 구동시키는데 필요한 HyundaiMotor사의 모터를 나타낸 클래스** (direction값에 따라 모터를 움직여 엘리베이터를 구동시키는 기능인 move()&moveHyundaiMotor() 메서드)
  > move()는 외부에서 호출되는 메서드,  move()를 통해서 -> **실제 모터를 구동시키는건 moveHyundaiMotor() 메서드**
  
  > 단, 모터를 구동시키기전에 getMotorStatus()메서드로 현재 모터의 상태를 확인해보고 -> 현재 모터의 상태가 MOVING이라면 구동시킬 필요가 없으니 로직에서 break;
  >   
  > **현재 모터가 STOPPED 상태라면**, 즉 구동중이 아니라면 **엘베 문이 열려있는지 파악후** -> **구동시킴**(현재 모터 상태를 setMotorStatus(Moving)로 변경) 

<br>

- **`Door 클래스`: 엘리베이터 문 클래스** (엘리베이터 문이 열린채로 구동시키면 위험하니, 모터 클래스는, 현재 엘리베이터 문의 상태 파악을 위해 이 클래스와 연관관계)
  > 문을 열고 닫을 수 있도록하는 open(), close() 메서드와 현재 문의 OPENED 상태인지 CLOSED 상태인지를 알려주는 getDoorStatus()메서드 제공.
  >   
  > 즉, 모터 클래스는, Door클래스의 getDoorStatus()메서드로 현재 문의 상태를 확인하고-> 현재 문이 열려있으면 문을 close하고 구동 - 닫혀있으면 그냥 구동

  <br>
  
- **`Direction enumeration`: 모터를 어느 방향으로 움직일 것인지 그 방향을 나타내는 enumeration타입의 Direction** (UP, DOWN)
- **`MotorStatus enumeration`: 모터가 구동중인지 멈춰있는지 상태를 나타내는 enumeration타입의 MotorStatus** (MOVING, STOPPED)
- **`DoorStatus enumeration`: 엘리베이터 문이 열러있는지 닫혀있는지 상태를 나타내는 enumeration타입의 DoorStatus** (OPENED, CLOSED)

  <br>
  
  ✔
  > **public `enum` Direction{UP, DOWN}** => UP, DOWN은 Direction 클래스 타입의 객체를 가리키는, 메서드 영역에 할당된 레퍼런스 변수이다.
  > 
  > 즉, enum 상수(UP,DOWN)는 힙에 저장된 실제 객체(UP,DOWN)를 가리키는 final static 타입의 레퍼런스 변수인 것. 실제 저 enum 상수들은 힙 영역에 Direction타입 인스턴스로 존재한다.
  
  
<br><br>

```java
public enum Direction {
    UP, DOWN
}

public enum DoorStatus {
    OPENED, CLOSED
}

public enum MotorStatus {
    MOVING, STOPPED
}
```
```java
import java.security.PrivateKey;

public class HyundaiMotor {
    private Door door; //Door 클래스와 연관관계.
    private MotorStatus motorStatus;


    public HyundaiMotor(Door door) {
        this.door = door;
    }

    private MotorStatus getMotorStatus() {
        return motorStatus;
    }

    private void setMotorStatus(MotorStatus motorStatus) {
        this.motorStatus = motorStatus;
    }

    //모터를 실제로 움직이기 전에 움직일 수 있는 상태인지를 먼저 확인하고 실제 모터를 구동시키고자하는 move() 로직.
    public void move(Direction direction) {
        MotorStatus morMotorStatus = getMotorStatus();
        //일단 현재 모터가 움직이는 중이면 움직일 필요가 없으니 return으로 move로직 종료.
        if (morMotorStatus == MotorStatus.MOVING) return;  //MOVING 인스턴스는 1개만 존재함. 주소값(레퍼런스)으로 참조 가능.

        DoorStatus doorStatus = door.getDoorStatus();
        //2차로, 현재 문이 열려있으면 모터 구동이 불가능하니, 문을 닫고나서 모터를 움직일 수 있도록.
        if (doorStatus == DoorStatus.OPENED) door.close();

        //이제, 1.모터는 멈춰있는 상태 + 2.문은 닫혀있는 안전한 상태 임을 모두 확인했으니 모터를 주어진 Direction으로 구동가능.
        moveHyundaiMotor(); //모터를 구동시키자.
        setMotorStatus(MotorStatus.MOVING);//모터를 구동시켰으니. 모터의 상태를 작동중으로 변경.
    }

        //실제로 현대모터를 구동시키는 메서드(로직).
        private void moveHyundaiMotor(){
            System.out.println("Hyundai Motor Initiated");
        }
    }
```
```java
public class Door {
    private DoorStatus doorStatus;

    public Door() {
        doorStatus = DoorStatus.CLOSED; //doorStatus 필드에는 DoorStatus 클래스에 정의되어 있는 static final 타입의 OPENED 레퍼런스 변수의 값이 할당됨.
        //OPENED 레퍼런스 변수의 값은 => 힙 영역에 할당되어있는 DoorStatus 타입의 객체인 OPENED의 주소(레퍼런스).
    }

    public void open() {
        doorStatus = DoorStatus.OPENED;
    }

    public void close() {
        doorStatus = DoorStatus.CLOSED;
    }

    public DoorStatus getDoorStatus() {
        return doorStatus;
    }
}
```
```java
public class Main {
    public static void main(String[] args){
        Door door = new Door();
        HyundaiMotor hyundaiMotor = new HyundaiMotor(door);
        hyundaiMotor.move(Direction.UP);
    }
}
```

**기존 코드 설계는 위와 같다.**    
**하지만 위와 같은 설계는 문제점이 있다** 

<br><br><hr><br>

### <p align="center">`<초기설계의 문제점>`</p> 

<br>

+ **현재 설계는 HyundaiMotor 클래스가 현대모터를 구동시키고있다.**
+ **그런데, 만약 현대가 아닌 다른 회사의 모터를 제어해야하는 경우에는 어떻게 해야 하는가?**
  > **예를 들어 LG모터를 구동시키고 싶다면?**
  
`=> 이 경우, HyundaiMotor 클래스와 기능이 동일한 LGMotor 클래스를 만들어내면 될 것이다.`  

**하지만 생성자 이름이나 일부 메서드의 이름만 다르고 나머지 부분들은 아예 동일하기때문에,    
아래와 같이 중복되는 맴버들을 슈퍼클래스로 모델링하여 일반화 관계(상속관계)로 설계하면 수월할 것이다.** 


<div align="center"><img src="https://github.com/user-attachments/assets/2e790691-32ba-48fe-bcbe-91417e9efdd7"></div>

```java
public abstract class Motor {
    protected Door door;
    private MotorStatus motorStatus;

    public Motor(Door door){
        this.door= door;
    }

    protected MotorStatus getMotorStatus() {return motorStatus;}
    protected void setMotorStatus(MotorStatus motorStatus) {this.motorStatus = motorStatus;}

    public abstract void move(Direction direction);
}

```
```java
public class LGMotor extends Motor{

    public LGMotor(Door door) {
        super(door);
    }

    public void move(Direction direction) {
        MotorStatus morMotorStatus = getMotorStatus();
        if (morMotorStatus == MotorStatus.MOVING) return;

        DoorStatus doorStatus = door.getDoorStatus();
        if (doorStatus == DoorStatus.OPENED) door.close();
        
        moveLGMotor(); //모터 구동
        setMotorStatus(MotorStatus.MOVING);
    }
        //실제로 현대모터를 구동시키는 메서드(로직).
        private void moveLGMotor(){
            System.out.println("LG Motor Initiated");
        }
    }
```
```java
public class HyundaiMotor extends Motor{

    public HyundaiMotor(Door door) {
        super(door);
    }

    public void move(Direction direction) {
        MotorStatus morMotorStatus = getMotorStatus();
        if (morMotorStatus == MotorStatus.MOVING) return;

        DoorStatus doorStatus = door.getDoorStatus();
        if (doorStatus == DoorStatus.OPENED) door.close();

        moveHyundaiMotor(); //모터를 구동시키자.
        setMotorStatus(MotorStatus.MOVING);
    }

        //실제로 현대모터를 구동시키는 메서드(로직).
        private void moveHyundaiMotor(){
            System.out.println("Hyundai Motor Initiated");
        }
    }
```
<br>

위와 같이 추상클래스에, 각 회사의 모터 클래스마다 공통적인 부분들(set,getMotorStatus & Door+MotorStatus필드)은 모두 정의해놓았고  

각 회사의 모터마다 구현이 다른 메서드는(예를들어, 각 회사마다 모터의 움직임이 다르니 move()의 로직은 각기 다르다) 정의는 없고 선언만해놓은 형태인 **추상메서드**로 선언해두었다.  

<br>

이로써 다른회사의 모터도 문제없이 작동할 수 있게된 것 같으나, 문제점이 있다.  

<br>

**HyundaiMotor와 LGMotor 클래스의 move() 메서드**를 보면, **`메서드의 작업 흐름이 거의 동일하나,`**  
### 각 회사마다 모터이름이 다르기에, **실제 모터를 구동시키는 기능을하는 `move"회사"Motor()`메서드** 호출부분에서  저 "회사" 안에 들어갈 이름이 각 회사마다 다를 것이다.  

<br>

#### 즉, 전체적인 move() 알고리즘의 step은 동일하나, ```일부step(ex: move"회사"Motor() 메서드)```만 다른경우가 존재한다는 것이다.  

<br>

#### "일부분만 빼고 나머지는 동일하다" -> 저 일부분 때문에 move()자체를 재사용 할 수가 없게되었네...  
#### 전체적인 흐름은 그대로두고, 그 일부분만 어떻게 처리하면 더 좋은 코드가 될텐데... 

<br>

### 이렇듯 `메서드의 동일한 흐름속에서`, `일부 step 들이 다른 경우에`, 전체적인 흐름은 동일하게 유지하되, 이 각 클래스의 특정 메서드는 각각 다르게 작동하도록 처리해주는것이 바로 Template Method 패턴의 중점이자 역할이다.  

<br><hr><br>
   
## + HyundaiMotor와 LGMotor클래스의 move 메서드 문제점 개선 by `Template Method 패턴`

<br>

<div align="center"><img src="https://github.com/user-attachments/assets/61ff8e81-8168-4e01-b464-ee23abd2aa6f"></div>

> 출처: Java객체지향 디자인패턴(한빛미디어)  
> **`빨간색 배경의 코드는 각 모터마다 동일한 부분이다.`**

<br><br>

위의 그림에서 `왼쪽이 HyundaiMotor클래스의 move() 메서드`이고, `오른쪽이 LGMotor클래스의 move() 메서드`이다.  
보다싶이, **전체적인 흐름이, 거의 모든 부분이 동일하다.**  

<br><br>

실행흐름을 보면  

+ 우선, getMotorStatus()로 모터의 상태를 체크한다.
+ 그다음, 모터가 구동중(MOVING)이라면 구동시키지않고 move()를 관둔다.
+ `구동중이 아니라면` + getDoorStatus()로 문의 상태를 체크한다.
+ 문이 열려있는 상태라면 문을 닫는다.

<br><br>

**위 흐름까지는 모든회사의 모터에서의 동일한 흐름이다.**   

<br>

### 그러나, 실제로 모터를 구동시키는 moveMotor() 로직은 회사마다 다르다.  
> 회사마다 모터가 다 다르니깐... 구동시키는 로직은 그 각 회사마다의 서로다른 모터를 구동시키도록 작성되어있음

<br>

이 후, 마지막에 setMotorStatus()로 모터의 상태를 움직이고있는 상태로 변경해주는 로직은 모터마다 동일하다.  

<br><br>

즉, 모든 회사의 모터 클래스 내의 move() 메서드 안에서 **저 moveMotor() 로직 딱 하나만 다르기때문에**  
#### 이 move() 메서드 전체를 공유하지 못하고, 각 회사의 모터클래스에 맞게 서로 다르게 정의할 수 밖에없게되었다.  

<br><br>

### 이렇듯, 여러 메서드들이 서로 거의 모든 부분은 동일하나 일부 step들만 다른경우에 어떻게하면 동일한 부분들을 공유할 수 있겠는가?  

<br><br>

이 해결책은 간단하다.  

<br>


> #### 이 "거의 모든 부분은 동일하나 일부 step들만 다른 메서드"에서, `동일한 부분들은, 상위 클래스에다가 메서드로 정의(이름&내용 동일)`하여 공유되도록 변경하고  
> ####  + 그 `서로다른 일부 step은 상위 클래스에서 추상메서드로 정의하여 자식들이 적합하게 오버라이딩`하는 방식으로 변경해보자.  

<br><br>

## Template Method 패턴을 적용시킨 코드

<br>

```java
public abstract class Motor {
    protected Door door;
    private MotorStatus motorStatus;

    public Motor(Door door){
        this.door= door;
    }

    protected MotorStatus getMotorStatus() {return motorStatus;}
    protected void setMotorStatus(MotorStatus motorStatus) {this.motorStatus = motorStatus;}

    public void move(Direction direction) { //각 모터클래스마다 공유하는 공통 메서드=> Template Method.
        MotorStatus morMotorStatus = getMotorStatus();
        if (morMotorStatus == MotorStatus.MOVING) return;

        DoorStatus doorStatus = door.getDoorStatus();    // 쭉 공통된 부분 실행.
        if (doorStatus == DoorStatus.OPENED) door.close();

        moveMotor(direction); // 위에서 쭉 공통된 부분 실행하다가, 여기서 각 하위클래스에서 재정의한 구문 실행(primitive Method).
        setMotorStatus(MotorStatus.MOVING); // 다시 공통된 부분 실행.
    }
    //각 자식 모터클래스마다 서로다른 이 특정 step은, 자식마다 알아서 적합하게 오버라이딩하도록 추상메서드로 선언.
    protected abstract void moveMotor(Direction direction);
}

```
```java
public class HyundaiMotor extends Motor{

    public HyundaiMotor(Door door) {
        super(door);
    }

        //실제로 현대모터를 구동시키는 메서드 => 오버라이딩
        @Override
        protected void moveMotor(Direction direction){
            System.out.println("Hyundai Motor Initiated");
        }
    }
```
```java
public class LGMotor extends Motor{

    public LGMotor(Door door) {
        super(door);
    }

        //실제로 LG모터를 구동시키는 메서드 => 오버라이딩
        @Override
        protected void moveMotor(Direction direction){
            System.out.println("LG Motor Initiated");
        }
    }
```java
public class Main {
    public static void main(String[] args){
        Door door = new Door();
        
        Motor lgMotor = new LGMotor(door);
        lgMotor.move(Direction.UP);

        Motor hyundaiMotor = new HyundaiMotor(door);
        hyundaiMotor.move(Direction.UP);
    }
}
```

<br>

#### move() 메서드 내에서 모터 클래스마다 동일한 부분들은 상위 추상클래스인 Motor클래스에서 move() 이름그대로 일반 메서드로 정의시켰고, `(이로써 하위 클래스<각 회사의 모터 클래스들>이 공통된 전체적인 흐름인 move() 메서드를 그대로 재사용 할 수 있게됨)`  

#### 각 모터 클래스마다 정의했던 move"회사"Motor() 메서드를 => 모든 회사의 모터클래스에서 적합하게 오버라이딩하여 사용할 수 있도록 상위 추상클래스인 Motor클래스에서 "회사"이름을 뺀 `moveMotor()추상메서드`로 변경하였다. 

<br><br>

### 여기서, 상위 클래스에 정의된 move() 메서드를 template Method 라고한다.
> **`template Method:` 여러 클래스에서 공통으로 사용하는 메서드를 상위 클래스에 정의하여 템플릿화. 즉, 이 공통된 기능인 템플릿을 갖다가 써라.** 

<br><br>

#### + 새롭게 삼성사의 모터를 추가로 구현하고 싶다면

> **1. Motor추상클래스를 상속받고, 공통으로 사용되는 핵심 메서드인 move() 및 공통 맴버들을 재사용하도록한다.**
> 
> **2. 각 모터클래스마다 상이한 부분, 즉 실제모터구동 추상메서드를 삼성사의 모터의 구동로직에맞게 오버라이딩한다.**
 

```java
public class SamsungMotor extends Motor{

    public SamsungMotor(Door door){
        super(door);
    }

    @Override
    protected void moveMotor(Direction direction) { //삼성모터의 구동로직에 맞게 재정의.
        System.out.println("Samsung Motor Initiated");
    }
}
```

<br><br>

## 예제 정리

<br>

<div align="center">
<img src="https://github.com/user-attachments/assets/d86a8461-d2df-4f2b-bf83-38f32ea51d32">
</div>

> 출처: Java객체지향 디자인패턴(한빛미디어) 

<br><br>

+ 각 모터클래스마다 정의되어있던 move() 메서드에서 **공통적인 부분**을 **상위 클래스의 일반메서드**로 이동시켰다. => **`Motor클래스의 move()`**
+ move() 메서드에서 공통적인 부분을 제외한 나머지 **상이한 step부분들**은(여기선 move"회사"Motor메서드) **상위클래스의 추상메서드**로 이동시켰다. => **`Motor클래스의 moveMotor()`** 
> 자식클래스(각 회사마다의 모터클래스)에서 재정의하도록 추상메서드로 정의.  
> 접근제한자 **"#"** => protected : 자식클래스에서만 재정의 할 수 있도록 protected로 접근제한자 설정.

<br>

🛑🛑
> "부모 클래스의 객체를 자식 클래스의 객체로 대체해도 프로그램의 동작이 변경되지 않아야 한다"는 `리스코프 치환 원칙(LSP)`을 준수하기위해,
>   
> **오버라이딩된 메서드의 접근 지정자는 부모의 해당 추상메서드보다 더 좁은 범위를 가질 수 없다.**
> 
> 부모 메서드의 접근 지정자가 `protected`라면, 오버라이딩하는 자식 메서드의 접근 지정자는 `protected 또는 public`이어야한다.
>  
> 즉, 오버라이딩 할 때 부모 메서드의 접근 지정자보다 더 좁은 범위의 접근 지정자를 사용할 수 없다. 이는 **LSP원칙을 준수하고 프로그램의 안정성을 유지하기 위해서**이다.

<br><br><br>

<div align="center">
<img src="https://github.com/user-attachments/assets/7605e17a-eb01-40e7-8a64-64735a6e7857">
</div>

> 출처: Java객체지향 디자인패턴(한빛미디어) 

<br>

+ ### 이 예제에서 여러 클래스에서 *`공통으로 사용하는 메서드`* 이자 *`변하지 않는 고정기능`* 인 move()를 **`템플릿 메서드`** 라고 부른다.
  
+ ### 이 예제에서 여러 클래스에서 공통적인 부분들 외에 *`각 클래스마다 구현이 상이한 부분-달라지는 부분`* 인 moveMotor()를 `Primitive Method` 또는 `Hook Method` 라고 부른다.(`추상메서드임`)

<br><br>

## 마무리

<br>

>
+ **데코레이터 패턴은 기본 기능에 추가할 수 있는 기능의 종류가 많은 경우 유용한 Structural설계패턴 이다**  
>
+ **데코레이터 패턴은 기본 기능에 추가될 수 있는 많은 수의 부가기능에 대해서 `기존의 코드를 변경하지 않으면서도(OCP 만족)` `다양한 조합을 동적으로 구현`할 수 있는 패턴이다**
>

<br><hr><br>

## Template Method 패턴의 일반적인 설계구조

<br>

<div align="center">
<img src="https://github.com/user-attachments/assets/c099e58a-632d-481d-a259-7467daefa24b">
</div>

> 출처: Java객체지향 디자인패턴(한빛미디어)

<br><br>

> 
> AbstractClass 즉, 상위클래스는 추상클래스로 구현되어있음. 이 AbstractClass 내부에는,  
> 공통으로 사용하는 메서드 즉, **템플릿 메서드**가 정의되어있고,   
> 템플릿 메서드에서 상이한 부분인-자식클래스가 재정의해야하는 **추상메서드**인 **Primitive or Hook 메서드**도 정의되어있다.  
>
> 
> **하위 클래스에서는, 공통된 부분은 상속으로 받았으니, 이 달라지는 부분인 Primitive Operation만 오버라이딩하면 된다.**
>   

<br><hr><br>

<div align="center">
<img src="https://github.com/user-attachments/assets/13ede9be-7f87-4b51-ad1b-cbaa8911ed1f">
</div>

> 출처: Java객체지향 디자인패턴(한빛미디어)

<br>

1. **외부 client에서 ConcreteClass의 template Method를 호출**
   
2. **ConcreteClass는 templateMethod 흐름에따라 실행을 하다가**
   
3. **중간에, 하위클래스마다 상이한-구현이 다른부분만 각 하위클래스마다 오버라이딩(재정의)해둔 primitive Method를 호출실행.**
   
<br><hr><br>

<div align="center">
<img src="https://github.com/user-attachments/assets/2fce0480-412d-4225-a37d-f4b321c79df8">
</div>

<br><hr><br>

+ **전체적으로 동일하면서(`template Method`) 부분적으로 상이한 문장-step(`Primitive - Hook Method`)을 가지는 메서드의 코드 중복을 최소화 할때 유용한 행위 패턴**  

+ **`Template Method패턴`은, 전체적인 알고리즘은 동일하여 상위클래스에다가 구현해두고, 상이한 부분은 하위 클래스에서 구현할 수 있도록 해주는 디자인 패턴**  
  
+ **전체적인 알고리즘의 코드를 재사용하는데 유용하다**    

<br>
<br>
<br><br>
