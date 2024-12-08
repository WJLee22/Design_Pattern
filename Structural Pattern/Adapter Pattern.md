

#   Adapter Pattern

<br>

## Adapter(어댑터) 패턴이란?

<br>

+ **데코레이터 패턴(Decorator Pattern)은 서로 호환 되지 않은 것들을 중간에 어댑터로 연결하여 호환시키듯이,**  

+ **서로 호환성이 없는 클래스들 사이에 어댑터를 제공해주어 호환되게끔 연결해주는 구조 패턴**

+ **사용하고자하는 인터페이스와 제공자측에서 제공해주는 인터페이스가 서로 다른경우에, 호환을 위해 사용하는 패턴**    

> 여기서 호환성이 없는 경우는, 한 클래스(YourAdder)가 라이브러리 형태이거나 수정이 불가한 경우. 이러면 implements 사용도 불가하여 다른 클래스와의 호환성을 만들기가 어렵다.  


<br>

<br>

## 덧셈기 Adder 예제를 통해 알아보는 Adapter Pattern      

<br>

## `<초기설계>`

<div align="center"><img src="https://github.com/user-attachments/assets/3ac14f5e-2b00-47c3-97a8-4dfe02e01f56"></div>


<br>

UseAdder가 Adder인터페이스와 연관관계를 맺으면서 Adder를 구현한 MyAdder 클래스의 plus 연산을 이용하여 덧셈을 수행하는 기본적인 설계이다.  

<br>

**하지만 위와 같은 설계는 특정 경우에 문제가 발생한다.**  

<br>

만약 add3라는 연산이 있는 YourAdder 라는 새로운 덧셈 클래스를 정의하고 -> 이 YourAdder의 add3 연산을 UseAdder에서 사용하고자 한다고 가정하자.  
  
이 경우엔 기존설계와 마찬가지로 YourAdder를 Adder를 구현하도록 implements 를 붙여서 정의하면 될 것이다.   

<br>

**하지만, 만약 YourAdder가 변경 및 수정이 불가한 라이브러리와 같은 형태라면 어떨까?**   

<br>

이 경우엔 YourAdder에 implements 키워드를 붙여서 Adder를 구현하도록 할 수가 없다.    

우리는 YourAdder의 add3를 이용해서 덧셈을 해야만한다. 그런데 YourAdder에는 손을 댈 수 없는 상황이고.  

#### UseAdder는 Adder의 plus 연산을 이용해서 덧셈을 수행해왔는데 + useAdder의 add3를 사용해야만 하는 상황..  

<br>

### 즉, UseAdder에서는 plus라는 인터페이스를 요구하지만 정작 YourAdder가 제공하려는 인터페이스는 add3 인, 서로 호환되지않는 답이 없는 상황이다.    

<br>

<div align="center">어떻게 해야 할까? </div>    

<br>

### 이럴때 Adapter를 써보자.    

<br>

#### Adapter를 사용하면 Client인 UseAdder를 수정할 필요도, Adaptee인 YourAdder를 수정할 필요도 없어진다.  

UseAdder와 YourAdder 사이에 연결자 역할인 Adapter를 하나 두기만하면된다. 어댑터를 설치해주는 단계는 아래와같다.   

<br><hr><br>  

#### 1. Adapter 클래스를 하나 정의한다.
#### 2. Adder 인터페이스를 구현하도록 한다.
#### 3. 그와 동시에 YourAdder 클래스와 연관관계를 맺도록 한다.  
#### 4. 어댑터 클래스에서 Adder 인터페이스의 plus 추상연산을 오버라이딩할떄, add3연산을 포함하여 구현하도록 한다.  
#### 4. UseAdder에서 다형성을 통해 Adder 타입-> Adapter의 plus 연산 이용 -> 간접적인 YourAdder의 add3 이용 -> 호환 성공!!  

<br><hr><br>  

끝이다.  

이제 코드 및 다이어그램으로 알아보고 마무리한다.  

<br>

<div align="center"><img src="https://github.com/user-attachments/assets/7977a77e-18b9-4f40-aab5-ca8a1ef46003"></div>

<br> 


```java
public interface Adder {
    public int plus(int x, int y);
}

```
```java
public class MyAdder implements Adder {

    @Override
    public int plus(int x, int y) {
        return x + y;
    }
}

```
```java
public class YourAdderAdapter implements Adder{
    private YourAdder yourAdder;

    public YourAdderAdapter(YourAdder yourAdder){
        this.yourAdder=yourAdder;
    }

    @Override
    public int plus(int x, int y){
        return yourAdder.add3(x, y, 10);
    }
}
```
```java
public class YourAdder {
    // 이 yourAdder는 라이브러리라고 가정. so, 그냥 사용만 가능한 api임. 수정불가.
    // implements 도 불가능. 그렇다면 어떻게?
    // 이 YourAdder와 연관관계를 맺고 + Adder 인터페이스를 구현하는 YourAdderAdapter를 두자.
    // 이러면, UserAdder 가 다형성을 통해서 Adder -> YourAdderAdapter 와 연관관계를 맺게되고,
    // YourAdderAdapter를 통해서 YourAdder의 add3를 사용 가능.
    // 즉, YourAdderAdapter가 UserAdder 와 YourAdder 사이를 연결해주는 일종의 어뎁터인 것.
    public int add3(int x, int y, int z) {
        return x + y + z;
    }
}
```
```java
public class UseAdder {
    public int add(Adder adder, int x, int y){
        int r =0;
        r= adder.plus(x,y);
        return r;
    }
}
```
```java
public class Main {
    public static void main(String[] args) {

        Adder adder = new MyAdder();
        UseAdder use = new UseAdder();
        System.out.println(use.add(adder, 10 ,20));

        YourAdder yourAdder = new YourAdder();
        Adder adder1 = new YourAdderAdapter(yourAdder);
        System.out.println(use.add(adder1, 1,2));

    }
}
```

<br><br><hr><br>


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

