# <div align="center"><Strategy Pattern 실습></div>

## Case) 펭수의 이동모드   

  
현재 펭수는 걸을 수도 있고, 점프할 수도 있다고 가정해보자.

그리고 사용자가 modeSelected를 통해 선택한 모드로 펭수의 이동방법을 설정하도록 프로그래밍해보면 아래와 같을 것이다.

## 기존 코드

```java
public class Paengsu {
    public void modeSelected(Mode mode) {
        switch (mode) {
            case WALK:
                walk();
                break;
            case JUMP:
                jump();
                break;
        }
    }
    public void walk() {
        System.out.println("Walking");
    }

    public void jump() {
        System.out.println("Jumping");
    }
}
```

<br>
위의 코드는 펭수가 걷고 점프할 수 있다는 가정하에는 문제없는 코드이다.  

하지만, 만약 펭수가 이 외에도 날 수도 있고 기어갈 수도 있다면 어떨까?  

modeSelected 메서드 내부의 switch 문에 case 를 추가해줘야하고, Paengsu클래스에 fly() 와 같은 메서드도 추가해주는 등  
**알고리즘의 추가 및 변경시마다 기존 코드를 뜯어고쳐야하는 OCP 위반 문제가 발생한다.**

이럴때 Strategy Pattern을 적용하여 코드를 유연하게 할 수 있다.

아래의 Strategy Pattern 적용 순서에 따라 위의 코드를 변경해보자.

## <Strategy Pattern 적용 순서>

<br>

#### 1. 변경되는 것이 무엇인지를 식별한다. (`이동 알고리즘`)  

<br>

#### 2. 그 변경되는 구체적인 실제 알고리즘들을 클래스로 모델링한다. (`Walking, Jumping...`)  

<br>

#### 3. 2단계에서 모델링한 구체적인 클래스(전략)들을 포괄하는 포괄개념을 `인터페이스 및 추상클래스로` 모델링한다.
#### ( `Strategy` = Moving interface)  

<br>

#### 4. 2단계에서 모델링한 클래스들을, 포괄개념인 인터페이스를 구현하는 구체전략 클래스(Concrete Strategy)로 연결한다. 
#### ( `Concrete Strategy` = Walking, Jumping...)

<br><br>

<div align="center">
  <img src="https://github.com/user-attachments/assets/1e40d5cc-fff2-4aaa-a862-ec01d025d335">
  </div>




<br>

## Strategy Pattern을 적용시킨 코드

```java
public class Paengsu {
    private Moving moving;

    public void setMoving(Moving moving) {
        this.moving = moving;
    }

    public void move() {  //포괄개념인 이동 인터페이스에게 위임.
        moving.move();
    }
}
```
```java
public interface Moving {
        public void move();
}
```
```java
public class Walking implements Moving{
        @Override
    public void move() {
        System.out.println("Walking");
    }

```
```java
public class Flying implements Moving{
    @Override
    public void move() {
        System.out.println("Flying");
    }
}
```

```java
public class Main {
    public static void main(String[] args) {
        Paengsu p3=new Paengsu();
        p3.setMoving(new Flying()); //날라 다니는 이동방법을 추가하고싶다면 => 기존코드 변경없이, 포괄개념을 구현하는 Flying구체전략클래스 생성.
        p3.move();
}
}
```

