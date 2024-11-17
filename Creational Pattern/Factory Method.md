

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

<div align="center"><img src="https://github.com/user-attachments/assets/24c7d21c-bee2-4674-acce-7825f68e26d2"></div>

<br>


<br>

사과는 부사사과와 홍옥사과 2종류. 

<br>

사과를 서빙해주는 메서드. 현재 사과는 부사사과. 사과를 씻고-껍질깎고-조각낸 다음 return. 손님에게 서빙.



Apple 클래스는 추상 클래스로두고, 사과의 세부종류인 부사사과와 홍옥사과를 하위 클래스로 둔다. 

<br>

식당은 사과를 디저트로 제공해주기에 사과를 필요로한다고 가정하자.

식당(레스토랑) 클래스에서는 준비해둔 사과를 -> 사과를 씻고-껍질깎고-조각낸 다음 손님에게 서빙해주는 일련의 과정을 따른다.

<br>

사과를 필요로하는 곳이 식당 뿐만은 아닐 것이다. 예를들어 일반 집에서는 아침에 사과를 먹는다.  

다만, 식당과는 달리 집에서는 사과 껍질을깎고&조각내는 과정없이 그냥 씻은채로 아침에 먹는다.  

  

이러한 시나리오 구조에 대한 코드는 아래와 같다.  

<br>

```java
public abstract class Apple {
    public abstract void wash();
    public abstract void peel();
    public abstract void slice();
}
public class Busa extends Apple{
    public void wash() {
        System.out.println("Busa: wash");
    }
    public void peel() {
        System.out.println("Busa: peel");
    }
    public void slice() {
        System.out.println("Busa: slice");
    }
}
public class Hongok extends Apple{
    public void wash() {
        System.out.println("Hongok: wash");
    }
    public void peel() {
        System.out.println("Hongok: peel");
    }
    public void slice() {
        System.out.println("Hongok: slice");
    }
}


public class Restaurant {
    public Apple servingApple(){ //사과 서빙 메서드.
        Apple apple = new Busa(); //부사 사과를 준비해두고
        apple.wash(); //사과를 씻고
        apple.peel(); //사과껍질을 깎고
        apple.slice(); //사과를 자르고
        return apple; //사과를 손님에게 제공한다.
    }
}
public class Home {
    public Apple getApppleAppleForBreakFast(String kind){
        Apple apple = new Hongok();
        apple.wash();
        return apple; //집에서 아침에는, 사과를 씻기만하고 바로 먹는다.
    }
}
```

<br>

**기존 코드 설계는 위와 같다.**    
### 허나, 이러한 구조는 문제점이있다.   

<br><br><hr><br>

### <p align="center">`<초기설계의 문제점>`</p> 

<br>

+ 현재 설계는 Home 이나 Restaurant 모두 **new 연산자를 사용하여 Apple 객체를 생성하고있다.**
  > new라는 연산자는 `new 구체적인클래스이름();` 구조이다.  
  > 즉, 우리가 생성하고자하는 구체적인 클래스이름을 new 연산자 뒤에 지정을해줘야 객체생성이 된다.  

<br>

이것이 문제가된다.   

<br>

+ **예를들어 식당에서 부사사과가 아니라 홍옥사과를 서빙하고싶은 경우 어떻게해야할까?**
  
=> **new `Busa`();** 를 **new `Hongok`();** 처럼 구체적인 클래스이름을 변경해줘야만할 것이다.  
> 마찬가지로 집에서 아침에 홍옥사과가 아닌 부사사과를 먹고싶다면... Hongok->Busa

<br><br>

**즉, 우리가 생성하고자하는 인스턴스를 변경하려고하면 반드시 new 뒤에오는 클래스이름을 바꿔줘야만한다.** 

<br>

왜?  -> **우리가 필요로하는 인스턴스를 new 연산자로 직접만들어서 사용하고있기 때문이다.**  

<br>

그리고, 만약 **경우에 따라 부사 or 홍옥사과를 선택을 하고싶다면?**   

아래와 같이 서빙 메서드의 매개변수로 받은 문자열에 따라 사과를 선택하도록하면 될 것이다.  

```java
public class Restaurant {
    public Apple servingApple(String kind) { //사과 서빙 메서드.
        Apple apple = null;
        if (kind.equals("busa")) apple = new Busa();
        if(kind.equals("hongok")) apple = new Hongok();
        apple.wash(); //사과를 씻고
        apple.peel(); //사과껍질을 깎고
        apple.slice(); //사과를 자르고
        return apple; //사과를 손님에게 제공한다.
    }
}
public class Home {
    public Apple getApppleAppleForBreakFast(String kind){
        Apple apple = null;
        if (kind.equals("busa")) apple = new Busa();
        if(kind.equals("hongok")) apple = new Hongok();
        apple.wash();
        return apple; //집에서 아침에는, 사과를 씻기만하고 바로 먹는다.
    }
}
```
<br>  



#### 그러나 이 역시도 문제가 있다.  

<br>

만약 **사과의 종류가 추가되는 경우, 사과가 필요한 모든 곳에서 이 새로운 종류의 사과에 대한 처리를 해줘야하기에 `기존 코드를 변경`해줘야한다.**

<br>

예를들어, 식당에서 새로운 사과종인 홍로사과를 새로운 디저트로 선보이고자 한다면, if 문을 추가해줘야한다.  



```java
		    if (kind.equals("busa")) apple = new Busa();
        if(kind.equals("hongok")) apple = new Hongok();
        if(kind.equals("hongro")) apple = new Hongro();
```



새로운 종류의 사과의 추가로인해서 식당 말고도 집이라던지 등등,  
**new 연산자로 사과 인스턴스를 생성하는 부분들은 모두 영향을 받게될 것이다.  
즉 새로운 종류의 사과에 대한 처리를 위해 코드 변경이 뒤 따라온다.**  



 #### 그렇다면 이 문제들을 어떻게 해결해야 할까? 

<br><hr><br>

### <문제점 개선>


 지금껏 여러 디자인 패턴들을 알아보면서 일관되게 봐왔던 것이 있다.   

바로 -> **`변경이 되기 쉬운 부분, 변경이 자주 일어나는 부분은 따로 클래스로 모델링하여(캡슐화) 분리시켰다. `**  



**지금 이 사과 예제에서 주로 `변경되는 부분은 무엇인가?`**

<br>

```java
	Apple apple = null;
	if (kind.equals("busa")) apple = new Busa();
    if(kind.equals("hongok")) apple = new Hongok();
    if(kind.equals("hongro")) apple = new Hongro();
```



**사과 객체를 생성하는 부분, 바로 이 부분이다.** 이 부분은, 사과종류가 추가될때마다 그에따라 if문을 매번 추가해줘야한다. 

<br>

이 부분을 **AppleFactory라는 클래스로 모델링하여 분리** 시켜보자. 



```java
public class AppleFactory {
    public static Apple getApple(String kind){
        Apple apple = null;
        if (kind.equals("busa")) apple = new Busa();
        if(kind.equals("hongok")) apple = new Hongok();
        if(kind.equals("hongro")) apple = new Hongro();
        return apple;
    }
}

```
> 여기서 getApple() 메서드를 인스턴스 메서드가 아닌 **static** 메서드로 두어서, 굳이 AppleFactory객체 생성없이 사용하자.   

<br>

이렇게 **공통 부분들을 AppleFactory클래스의 getApple() 메서드에 정의**해두고, 사과 객체가 필요한 곳에서는 이 getApple() 메서드를 사용하면 된다.  



```java
public class Restaurant {
    public Apple servingApple(String kind) { //사과 서빙 메서드.
        Apple apple = AppleFactory.getApple(kind);

        apple.wash(); //사과를 씻고
        apple.peel(); //사과껍질을 깎고
        apple.slice(); //사과를 자르고
        return apple; //사과를 손님에게 제공한다.
    }
}
public class Home {
    public Apple getApppleAppleForBreakFast(String kind){
        Apple apple = AppleFactory.getApple(kind);
        apple.wash();
        return apple; //집에서 아침에는, 사과를 씻기만하고 바로 먹는다.
    }
}
```

<br>

이렇듯, 객체를 생성하는 로직을 따로 분리해서 클래스로 캡슐화하였고,    

**이를 통해 Home 이나 Restaurant 에서는 new 연산자를 통해 직접 사과 객체를 생성하지 않게 되었다.**  

<br><hr>

#### 객체를 생성하는 역할은 AppleFactory클래스의 getApple() 메서드에서 대신 하고있다.  



### 이때, getApple() 메서드처럼, 객체를 생성하는 역할을하는 메서드를 `Factory Method`라고 한다.

> factory = 공장. 즉, 마치 공장처럼 인스턴스를 생성해주는 그러한 메서드다.

<hr><br>

즉 new 연산자를 사용하지않고 - Factory Method를 통해서 객체를 생성하기때문에,  
원하는 사과종류로 변경을 시켜도 기존처럼 코드 변경(if문 추가) 할 일이 없어졌다.  

<br>

물론 AppleFactory클래스의 getApple() 메서드 내에 if문을 추가시켜야하겠지만,  
기존에는 사과객체 생성하는 모든 부분에 if문을 추가시켜줬다면, 이건 getApple() 메서드 내에서 한번만 수정해주면 끝이다.   

> 재사용성, 참 편리하다.  

<br><br>

물론 위처럼 getApple() 메서드를 **static 메서드**로 둬도 괜찮지만, 또다른 방법은 **`싱글톤 패턴`** 을 이용하는 방법도 있다.  

<br><hr><br>

## <div align="center"> <싱글톤 패턴 적용> </div>

<br>

#### 현재코드에서는 사과를 필요로하는 곳은 Home, Restaurant... 이다. 

이때, Home, Restaurant... 들이 각자 개별적인 인스턴스를 가지고서 작업하는 것이 아니고,  
**싱글톤 패턴을 적용해서 AppleFactory클래스의 객체를 딱 하나만 생성해두고  
사과를 필요로 하는 모든곳에서 이 `싱글톤 유일 객체` 하나를 공유하여 작업할 수 있도록 해보자.**   

<br>

```java
public class AppleFactory {
    private static AppleFactory instance = null; // 싱글톤 패턴을 이용한 유일 객체.
    //private 접근제한 생성자 -> 외부에서 이 AppleFactory 클래스 객체 생성불가.
    //즉, 이 클래스에서 객체 하나 생성해두고 외부에서는 이거를 참조해서 사용하도록.
    // => 싱글톤. (private 생성자)
    private AppleFactory(){}

    public static AppleFactory getInstance() {
        if(instance == null)
            instance = new AppleFactory();

        return instance;
    }

    public Apple getApple(String kind){
        Apple apple = null;
        if (kind.equals("busa")) apple = new Busa();
        if(kind.equals("hongok")) apple = new Hongok();
        if(kind.equals("hongro")) apple = new Hongro();
        return apple;
    }
}

```

```java
public class Restaurant {
    public Apple servingApple(String kind) { //사과 서빙 메서드.
        // 싱글톤 패턴을 이용.
        // static 메서드인 getInstance()로 -> AppleFactory클래스에 있는 유일 객체에 접근.참조.
        AppleFactory factory = AppleFactory.getInstance();
        // FactoryMethod인 getApple()로, 선택한 kind종류의 사과객체를 생성.
        Apple apple = factory.getApple(kind);
        //즉, 객체의 생성을 직접하지 않아도, 팩토리 메서드인 getApple 메서드에서 객체생성을 대신해줌.

        apple.wash(); //사과를 씻고
        apple.peel(); //사과껍질을 깎고
        apple.slice(); //사과를 자르고
        return apple; //사과를 손님에게 제공한다.
    }
}

public class Home {
    public Apple getApppleAppleForBreakFast(String kind){
        //Restaurant 에서 참조하는 AppleFactory인스턴스와 동일한 인스턴스를 참조중.
        //싱글턴 패턴 적용해서, AppleFactory 객체를 각각 생성할 필요x. 하나만 만들어두고 공유사용.
        AppleFactory factory = AppleFactory.getInstance();
        Apple apple = factory.getApple(kind);
        apple.wash();
        return apple; //집에서 아침에는, 사과를 씻기만하고 바로 먹는다.
    }
}
```

```java
public class Main {
    public static void main(String[] args) {
        Restaurant r1 = new Restaurant();
        r1.servingApple("busa"); //식당 디저트 사과종류를 부사사과로 설정.
    }
}

```

<br><br><br><hr><br>

## <div align="center"><예제 정리></div>

<br>

### 결론: 

#### 변경되는 부분을 클래스로 캡슐화하고,

#### `객체를 생성하는 로직`을, `캡슐화한 클래스의 메서드로 정의`하자.  

<br>

 이때, 바로 그 - **객체를 생성하는 메서드를** **`FactoryMethod`** 라고 부른다.  

<br>

so, 객체를 생성하는 로직(FactoryMethod)이 다른 클래스에 있기때문에,  
우리가 원하는 인스턴스가 변한다 할지라도 - 직접적으로 영향을 받지 않게된다.  
> ex) 사과종류가 추가될 때마다, 이 처리를 위해 사과객체를 필요로하는 곳마다 if()문을 추가해줘야했음.  

<br><br>


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
