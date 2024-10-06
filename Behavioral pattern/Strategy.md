#   Strategy Pattern

<br>

## Strategy(전략) 패턴이란?

Strategy 패턴은 실행중에 알고리즘 전략을 선택하여 해당 객체의 동작을 실시간으로 변경할 수 있게 하는 행위 디자인 패턴이다.  

여기서 `'전략'`이란, 기능이나 동작이 될 수도 있는 특정한 목표를 수행하기 위한 행동 및 알고리즘을 말한다.  
>(ex: 로봇 객체의 이동 및 공격 알고리즘)  

객체가 수행할 수 있는 기능들을 각각 `전략클래스`로 미리 모델링(캡슐화)해두면, 이 모델링(캡슐화)되어진 전략클래스들을 이용하여 쉽게 전략을 교체할 수 있기때문에, 알고리즘 변경이 빈번하게 요구되는 경우나 쉽게 전략을 바꿔야 할 필요가 있는 경우에 적합한 행위 디자인 패턴이다.
>(ex: 로봇1 객체의 구체적인 이동 전략을 걷기 -> 뛰기로 바꿔야할 경우, 혹은 다른 기능들(공격,소리 등등)을 빈번하게 변경해야할 소지가 있는경우)

특히 게임 프로그래밍에서 게임캐릭터가 자신이 처한 상황에 따라 이동이나 공격하는 방식을 바꾸고 싶을때 스트레티지 패턴은 매우 유용하다. 

<br>

## 로봇 예제를 통해 알아보는 Strategy Pattern      

## <기존 설계> 
<div align="center">
 <img src="https://github.com/user-attachments/assets/35053918-3f6f-43f7-b9a3-41cd04ff9152">
</div>

```java 
public abstract class Robot {

    private String name;
    public abstract void move();
    public abstract void attack();

    public Robot(String name){this.name=name;}
    public String getName() {return name;}

}
```

```java 
public class Atom extends Robot{

    public Atom(String name) {
        super(name);
    }
    public void move() {System.out.println("Can fly");}
    public void attack() {System.out.println("Can punch");}
}
```

```java
public class TaekwonV extends Robot{

    public TaekwonV(String name) {
        super(name);
    }
    public void move() {System.out.println("can walk");}
    public void attack() {System.out.println("Missile");}
}
```

위와 같이 각 로봇종류를 일반화하여 하나의 추상클래스인 Robot을 만들어내고 이 Robot클래스를 Atom, TaekwonV가 상속받도록한 기본적인 일반화 구조이다.  

`(※이 로봇예제에서는, 공격 및 이동 방법이 전략(strategy)이다.)`
<br>

## <p align="center">기존 설계의 문제점</p>  
### <p align="center">`<문제점1>`</p>  
 
> 만약 기존 설계에서, 로봇의 공격 및 이동 방법을 수정하려면 어떻게 해야하는가?  
> (ex. 태권v의 이동방법을 walk가 아닌 fly로 만들고싶다면)
>
>이 경우, **태권v 클래스의 move()메서드 내부의 println의 "can walk" 구문을 "can fly" 로 코드를 변경**해줘야할 것이다.
>  
>**허나, 기능 추가 및 수정을위해 기존 코드를 변경한다? => 이는 OCP 원칙을 위반하는 것이다.**  

<br>

### <p align="center">`<문제점2>`</p>  
 
> **기존 설계에서, 아톰의 이동방법을따르고 태권v의 공격방법을따르는 새로운 종류의 선가드로봇을 만들려고하면 어떻게 해야하는가?**
>  
>이 경우, Robot 추상클래스를 상속하여 선가드 클래스를 생성하면 기존의 코드설계는 영향을 받지않기때문에 OCP 원칙을 지키면서 선가드를 추가할 수 있다. 
<div align="center">
<img src="https://github.com/user-attachments/assets/a0366f8d-e9aa-4635-9520-5a69b8307525">
</div>

```java 
public class Sungard extends Robot{

    public Sungard(String name) {
        super(name);
    }
    public void move() {System.out.println("Can fly");} //아톰의 이동방법을 사용
    public void attack() {System.out.println("Missile");} //태권v의 공격방법을 사용
}
```

>이처럼 새로운 종류의 로봇을 생성할경우 OCP 원칙을 준수하기에, 기존설계는 로봇을 새롭게 생성하는 부분에있어서 전혀 문제가없는 설계이다. 하지만 여기엔 문제가 하나 존재한다. 
>     
>현재 `선가드 클래스는 아톰클래스의 이동방법을 따르고 태권v클래스의 공격방법을 따른다`고 했다.
>
>so, **선가드 클래스의 move()코드와 attack()코드는 아톰의 move()코드와 중복이되고 태권v의 attack()코드와도 중복되어진다.**
>
>나중에 아톰의 이동방법 알고리즘이나 태권v의 공격 알고리즘이 변경되어진다면, 선가드가 따르던 해당 알고리즘도 똑같이 변경해줘야한다.
>
>코드의 중복이 생기면 생길수록, 해당 중복되는 코드에 변경사항이 생기게되면 변경을 일관되게 동일하게 빠짐없이 변경시켜야한다. 이는 쉬운작업이 아니다.
>
>**변경되는부분인 공격-이동 알고리즘이 각 클래스 내부의 메서드형태이기때문에 기존의 코드가 영향을 받을 수 밖에없는 OCP 원칙 위배가 발생한다.** 새로운 종류의 로봇 생성시에는 문제가 없지만 공격-이동 알고리즘 변경시에는 위배된다는 것이다.
>
>그렇다면, **이 이동-공격 알고리즘 변경으로인한 OCP 원칙 위배를 어떻게 해결해야할까?**
>
>변화의 단위를 클래스로 모델링해야한다. => **so, 이동-공격 메서드(알고리즘)를 클래스로 모델링해야한다.**

<br>

### <div align="center">문제 해결을 위한 Strategy Pattern 적용단계는 아래와 같다. </div>
<br>

<br>
<hr>

# <div align="center">🛑<Strategy Pattern 적용 단계>🛑</div>

<br>

#### 1. 변경되는 것이 무엇인지를 식별한다.  

<br>

#### 2. 그 변경되는 구체적인 실제 알고리즘들을 클래스로 모델링한다.(메서드 -> 클래스화)

<br>

#### 3. 2단계에서 모델링한 구체적인 클래스(구체 전략)들을 포괄하는 포괄개념을 `인터페이스 및 추상클래스로` 모델링한다.

<br>

#### 4. 2단계에서 모델링한 클래스들을, 포괄개념인 인터페이스를 구현하는 구체전략 클래스(Concrete Strategy)로 연결한다. 

<br>
<hr>
<br>

## 기존 설계의 `문제점2` 해결법(메서드 클래스화)

기존에는 메서드로 모델링되었던 move,attack 메서드를 클래스로 모델링해보자.  

다음과 같이 이동하는 개개의 알고리즘 둘을 클래스로 모델링하였고, 공격하는 개개의 알고리즘 둘도 클래스로 모델링하였다.  


<div align="center">
<img src= "https://github.com/user-attachments/assets/37805834-56a1-4048-88d1-a25bbb46f7bf">
</div>

<br>

이동-공격 알고리즘(전략Strategy)들을 클래스로 모델링하였으니, 해당 클래스들을 사용할 수 있도록 기존 로봇 클래스들을 변경시키면 아래와 같다.     

<br>

### <p align="center">`<문제점들을 보완한 최종 설계>`</p>  
<div align="center">
<img src= "https://github.com/user-attachments/assets/07a976f0-6d08-4ca6-a022-b343f8a71cd9">
</div>

<br>

```java 
public class Robot {

    private String name;
    private MovingStrategy movingStrategy;
    private AttackStrategy attackStrategy;

    public void setMovingStrategy(MovingStrategy movingStrategy) {
        this.movingStrategy = movingStrategy;
    }

    public void setAttackStrategy(AttackStrategy attackStrategy) {
        this.attackStrategy = attackStrategy;
    }

    public void move(){
        this.movingStrategy.move();
    };
    public void attack(){
        this.attackStrategy.attack();
    };

    public Robot(String name){this.name=name;}
    public String getName() {return name;}

}
```
```java
public class TaekwonV extends Robot{
    public TaekwonV(String name) {
        super(name);
    }
}
```
```java
public class Atom extends Robot{
    public Atom(String name) {
        super(name);
    }
}
```
```java
public class Sungard extends Robot{
    public Sungard(String name) {
        super(name);
    }
}
```
```java
public interface MovingStrategy {
    public void move();
}
```
```java
public class FlyingStrategy implements MovingStrategy{

    @Override
    public void move() {
        System.out.println("can fly");
    }
}
```
```java
public class WalkingStrategy implements MovingStrategy{

    @Override
    public void move() {
        System.out.println("can walk");
    }
}
```
```java
public interface AttackStrategy {
    public void attack();
}
```
```java
public class MissileStrategy implements AttackStrategy{
    public void attack() {
        System.out.println("Missile");
    }
}
```
```java
public class PunchStrategy implements AttackStrategy{
    public void attack() {
        System.out.println("punch");
    }
}
```
```java 
public class Main {
    public static void main(String[] args) {
        Robot r1=new TaekwonV("insang1");
        Robot r2=new Atom("insang2");

        System.out.println(r1.getName());
        r1.setAttackStrategy(new MissileStrategy());
        r1.setMovingStrategy(new WalkingStrategy());
        r1.move();
        r1.attack();

        System.out.println(r2.getName());
        r2.setAttackStrategy(new PunchStrategy());
        r2.setMovingStrategy(new FlyingStrategy());
        r2.move();
        r2.attack();

        Robot r3=new Sungard("insang3");
        r3.setAttackStrategy(new MissileStrategy());
        r3.setMovingStrategy(new FlyingStrategy());
    }
}

```


<br>

이동-공격 메서드를 클래스로 추출했기때문에, TaeKwonV 와 Atom은 내부에 move()와 attack() 메서드를 포함하지않도록 변경했다.

이로써, **새로운 이동방법이라던지 공격방법과 같은 전략이 추가되어도 기존설계에는 영향을 주지않게되고, TaeKwonV 나 Atom이 이동 및 공격 전략을 변경하여도 기존 설계와 코드는 영향받지않기때문에 OCP 원칙을 만족하게된다.**  


<br>

> Ex) TaeKwonV의 공격방법을 Missile => Punch로 변경하려고한다면 

> 아래처럼, 기존 TaeKwonV 클래스의 코드변경없이 setter 메서드를 이용하여 TaeKwonV의 공격방법을 변경할 수 있다. 

```
r1.setAttackStrategy(new MissileStrategy()); //공격방법 => 미사일
```
```
r1.setAttackStrategy(new PunchStrategy()); //공격방법 => 펀치
```

#### <p align="center">so, `<문제점1> 해결`</p>

<br>
<hr>

<br><br>

또한, 새로운 로봇으로 선가드를 만들고 태권v의 미사일 공격기능과 아톰의 날기기능을 따르도록하고자 하는경우,  
기존 코드 설계에서는 코드의 중복으로인해 OCP 위반 문제가발생하였지만 - 개선된 설계에서는 아래처럼 setter 메서드를 이용하여 변경 및 추가하고자하는 이동 및 공격 알고리즘(전략) 객체를 매개변수로 넘겨주면 끝이다.  

**즉, setter 메서드를 이용하여 필요에 따라 동적으로 구체적인 전략을 바꿀 수 있게된다.  
(어느 순간에는 공격 전략이 미사일로, 어느 순간에는 펀치로..)**  

**이로써, 추후 태권v 의 공격 전략이 변경되어도 선가드의 코드 또한 같이 변경해줄 필요가 없어진다.**

<br> 

```
Robot r3=new Sungard("insang3");
r3.setAttackStrategy(new MissileStrategy());
```

#### <p align="center">so, `<문제점2> 해결`</p>

<br>
<hr>

<br><br>

또한, **새로운 이동전략(이동 알고리즘)을 추가하는 경우에도 기존 코드의 변경없이 가능하다.**  
#### <p align="center">JumpingStrategy를 추가해보자.</p>

<br><br>

<div align="center">
<img src= "https://github.com/user-attachments/assets/a66be6b8-4442-478f-af53-c2eecd04673e">
</div>

<br>

```java
public class JumpingStrategy implements MovingStrategy{

    @Override
    public void move() {
        System.out.println("Jumping");
    }
}
```
```java
public class Main {
    public static void main(String[] args) {
        Robot r1=new TaekwonV("insang1");

        System.out.println(r1.getName());
        r1.setMovingStrategy(new JumpingStrategy()); //Jumping 이라는 새로운 전략을 추가하고, r1객체의 이동전략을 Jumping으로 동적 변경.  
        r1.move();


    }
}
```
<br>

위처럼, MovingStrategy 인터페이스를 구현하여 Jumping이라는 새로운 전략(이동 알고리즘)을 추가할 수 있다.  
**OCP 위반없이.**

<br><br><hr><br>

## 마무리

<br>

> + **변화되는 것이 무엇인지를 파악하고(이 예제에서는 이동,공격 알고리즘)**
 
> + **그 변화되는 것을 클래스로 캡슐화시키고(전략을 클래스로 모델링)**
   
> + **전략을 변경해야할 필요가 있을경우 해당 전략으로 쉽게 변경하자.**   

<br><br>

> + **이 예제에서, 이동 알고리즘을 추상적으로 포괄하는 개념인 MovingStrategy 인터페이스를 `Strategy(전략)`인터페이스라고 한다.**

> + **이 예제에서, 전략 인터페이스를 구현하는 Walking - Flying 과 같은 구체적인 이동 알고리즘 클래스들을 `Concrete Strategy(구체 전략)`클래스라고 한다.**

> + **이 예제에서, 이동 전략을 사용하는 사용자 입장인 Robot클래스를 `Context` 클래스라고한다.**

<br><br>

> + **⚠ `Context`는 `구체적인 전략과 연관관계를 맺는 것이 아니라`, 구체적인 전략들을 포괄하는 추상적인 개념인 `Strategy 인터페이스와 연관관계를 맺는다`. 그래야 프로그램 실행 중에 알고리즘(전략) 선택변경이 가능해진다**

<br> 

###  이 Strategy 디자인 패턴은, 런타임 시점에(실행 중에) 알고리즘 전략을 선택하여 객체에게 다양한 동작을 할당해주는, 객체 동작을 실시간으로 동적으로 변경할 수 있도록 해주는 `행위 디자인 패턴`인 것이다.
  
<br><br><br><br>
