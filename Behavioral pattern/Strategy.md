#   Strategy Pattern

<br>

## Strategy 패턴이란?

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

<br>

### 기존 설계의 문제점  
#### <p align="center"><문제점1></p>  
 
> 만약 기존 설계에서, 로봇의 공격 및 이동 방법을 수정하려면 어떻게 해야하는가?  
> (ex. 태권v의 이동방법을 walk가 아닌 fly로 만들고싶다면)
>
>이 경우, **태권v 클래스의 move()메서드 내부의 println의 "can walk" 구문을 "can fly" 로 코드를 변경**해줘야할 것이다.
>  
>**허나, 기능 추가 및 수정을위해 기존 코드를 변경한다? => 이는 OCP 원칙을 위반하는 것이다.**  

<br>

#### <p align="center"><문제점2></p>  
 
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
>현재 이 선가드는 아톰의 이동방법을 따르고 태권v의 공격방법을 따른다고 했다.
>
>so, **아톰의 move()코드와 중복이되고 태권v의 attack()코드와도 중복되어진다.**
>
>나중에 아톰의 이동방법 알고리즘이나 태권v의 공격 알고리즘이 변경되어진다면, 선가드가 따르던 해당 알고리즘도 똑같이 변경해줘야한다.
>
>코드의 중복이 생기면 생길수록, 해당 중복되는 코드에 변경사항이 생기게되면 변경을 일관되게 동일하게 빠짐없이 변경시켜야한다. 이는 쉬운작업이 아니다.
>
>**변경되는부분인 공격-이동 알고리즘이 각 클래스 내부의 메서드형태이기때문에 기존의 코드가 영향을 받을 수 밖에없는 OCP 원칙 위배가 발생한다.** 새로운 종류의 로봇 생성시에는 문제가 없지만 공격-이동 알고리즘 변경시에는 위배된다는 것이다.
>
>그렇다면, 이 공격-이동 알고리즘 변경으로인한 OCP 원칙 위배를 어떻게 해결해야할까?
>
>변화의 단위를 클래스로 모델링해야한다. => **so, 이동-공격 알고리즘을 클래스로 모델링해야한다.**
