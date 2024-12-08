#   `GoF` Factory Method Pattern

<br>

##  `GoF` Factory Method( `GoF`팩토리 메서드) 패턴이란?
> ✔ 4명이 학자들이 제안한 Factory Method패턴
<br>

#### 이전에 알아본 Factory Method 패턴은 매우 간단한 설계방식이었다.  
#### 이번에 살펴볼 `GoF Factory Method 패턴`은 Factory Method 패턴과 `다르다`.  
#### 한번 차이점을 알아보자.  

<br>

+ **`GoF`팩토리 메서드 패턴(GoF Factory Method Pattern)은 기본적으로 `Template Method 패턴을 차용`하는 패턴이다.**
  
+ **`공통로직은 상위에서 정의`하고, `객체 생성 로직은 하위에서 재정의`하도록하는 행위패턴**
  
+ **`객체 생성의 책임을 서브클래스로 위임하는 디자인 패턴`**

> 즉, 각 클라이언트에서 직접 new 연산자를 통해 제품 객체를 생성하는 것이 아닌, 제품 객체 생성을 담당하는 공장 클래스에서 객체 생성을 도맡아서 한다.  

  
<br>

[이전 팩토리 메서드 패턴 참고](https://github.com/WJLee22/Design_Pattern/blob/main/Creational%20Pattern/Factory%20Method.md)

<br>

<br>

## 사과를 디저트로 제공하는 식당클래스 예제를 통해 알아보는 `GoF` Factory Method Pattern (템플릿 메서드 패턴을 차용)         

<br>

## `<초기설계>`

<div align="center"><img src="https://github.com/user-attachments/assets/24c7d21c-bee2-4674-acce-7825f68e26d2"></div>

<br>


<br>

> ✔Factory Method 패턴에서의 사과 디저트 예제를 기반으로한다.    

<br>

사과를 고객에게 디저트로 제공하는 식당이 인기가 엄청나게 불어나서 **식당을 서울지점과 뉴욕지점으로 확대하려고한다.**  

지점을 확대하려는 계획중에, 사과를 서빙하기 위해 수행하는 일련의 단계를 어느 지점이든 공통되도록 통일시키고자 한다.    

#### 1. `일단 사과를 얻어오고 (getApple)`
#### 2. 사과를 씻고 (wash)
#### 3. 사과의 껍질을 벗기고 (peel) 
#### 4. 사과를 조각내고 (slice)


<br>

위와같이, 사과를 서빙하기까지의 일련의 과정중에서 wash peel slice 3개의 과정은 지점마다 동일하다.  

#### 반면에 사과를 얻어오는 getApple 동작은 지점마다 달라질 수 있다.  
> 서울 지역에서 얻을 수 있는 사과와 뉴욕에서 얻을 수 있는 사과는 원산지 문제로 각기다른 사과를 디저트로 사용할 수 있으니.   

<br>

#### 즉 4단계 중에서, 1번 단계인 `사과객체를 생성-얻어오는 단계만 지점마다 달라지고` `나머지는 동일`하다. 

### 일련의 단계들중에서 한 단계만 수행이 다르다? 뭔가 생각나지 않는가? -> 바로 `Template Mehod Pattern` 이다.    

#### 상위 클래스에 일반적인 흐름을 정의해놓고, 하위클래스에서는 특정단계를 재정의해서   
#### 전체적으로는 상위클래스에서 정의해놓은 흐름을따르고, 특정 단계를 수행하게될때는 하위클래스에서 재정의한 흐름을 따르도록... =>`Template Mehod Pattern 차용`  

<br>

#### 즉, 사과를 서빙하는 전체적인 흐름은 상위에 정의 & 사과를 얻어오는 과정만 하위클래스인 서울시점-뉴욕지점에서 재정의.  
#### 이로써 지점별로 사과종류만 달리할 수 있다.  

<br><br>

<div align="center"><img src="https://github.com/user-attachments/assets/38736952-2cf3-400a-bd95-52337d327f21"></div>

> 출처: Java객체지향 디자인패턴(한빛미디어)

<br>

위처럼 식당클래스를 상위클래스로 정의하는데, 식당에서 사과를 서빙하는 일련의 단계-과정들이 seringApple()메서드에서 쭉 정의되어있다.  
> 사과를 얻어오고 - 씻고 - 깎고 - 조각내고 - 서빙.

<br>

여기서 seringApple()메서드가 Template Method이다.  
> 전체적인 작업 흐름을 정의하는 메서드니깐. 

<br>

이때, 앞서말했던것처럼 사과를 가져오는 과정이 지점마다 다르다.
> 서울지점은 서울에서 구하기쉬운 사과들(부사-홍옥-홍로)중에서 선택할거고, 뉴욕도 역시(코루,핑크레이디,크리스피)중에서..   

<br>

#### so, 이 getApple()메서드를 추상메서드로 정의해두고 -> 하위클래스(서울-뉴욕지점)에서 재정의하도록하자.  
> 여기서 사과를 얻어오는 과정인 getApple 추상메서드를 factory method라고한다.

<br>

### 이와같이, 전체적인 흐름은 Template Method 패턴을 이용 + 객체 생성부분은 하위클래스에서 재정의하도록하는 패턴을 `GoF Factory Method 패턴` 이라고한다.  

<br><br>

이러한 시나리오 구조에 대한 코드는 아래와 같다.  

<br>

```java
//일반적인 사과를 서빙하는기능의 템플릿메서드를 갖고있는 클래스.
public abstract class Restaurant {
    public Apple servingApple(String kind){
        Apple apple = getApple(kind);
        apple.wash();
        apple.peel();
        apple.slice();
        return apple;
    } //이 메서드가 바로 Template Method.

    // 전체적인 흐름들 중, 객체 생성부분 -> 추상메서드로 구현.
    // 이 메서드가 바로 Factory Method. 객체 생성 담당.
    // 하위클래스에서, 자기가 원하는 사과를 얻을 수 있도록 재정의해야함.
    // 레스토랑 클래스의 하위-> 서울&뉴욕지점.
    public abstract Apple getApple(String kind);
}
```
```java
public class SeoulRestaurant extends Restaurant {
    // 사과를 서빙하기위한 전체적인 과정은, 상위클래스인 Restaurant에서 정의한
// servingApple: Template 메서드를 따르지만,
// 서빙되는 대상인 사과의 종류는 바로 이 서울지점 하위클래스에서 재정의.
    @Override
    public Apple getApple(String kind) {
        Apple apple = null;
        //서울지점에서는, 서울에서 구하기쉬운
        // 부사-홍옥-홍로사과 중에서 선택하여 디저트로 서빙해준다.
        if (kind.equals("busa")) apple = new Busa();
        if (kind.equals("hongok")) apple = new Hongok();
        if (kind.equals("hongro")) apple = new Hongro();
        return apple;
    }
}
```
```java
public class NYRestaurant extends Restaurant{
    @Override
    public Apple getApple(String kind) {
        Apple apple = null;
        //뉴욕지점에서는, 뉴욕지역에 구하기쉬운
        // 코루-크리스피-핑크레이디사과 중에서 선택하여 디저트로 서빙해준다.
        if (kind.equals("koru")) apple = new Koru();
        if (kind.equals("crispy")) apple = new EveryCrispy();
        if (kind.equals("pl")) apple = new PinkLady();
        return apple;
    }
}
```
```java
public abstract class Apple {
    public abstract void wash();
    public abstract void peel();
    public abstract void slice();
}

public class Busa extends Apple{
    @Override
    public void wash() {System.out.println("Busa: wash"); }

    @Override
    public void peel() {ystem.out.println("Busa: peel"); }

    @Override
    public void slice() {System.out.println("Busa: slice");}
}

public class Koru extends Apple {
    @Override
    public void wash() {System.out.println("Koru: wash"); }

    @Override
    public void peel() {System.out.println("Koru: peel"); }

    @Override
    public void slice() {System.out.println("Koru: slice"); }
}
.
.
.
```
```java
public class Main {
    public static void main(String[] args) {
        Restaurant seoul1 = new SeoulRestaurant(); //서울1지점 오픈.
        seoul1.servingApple("busa"); // 서울1지점은 부사사과로 사과디저트 서빙.

        NYRestaurant ny1 = new NYRestaurant(); //뉴욕1지점 오픈.
        ny1.servingApple("crispy"); // 뉴욕1지점은 크리스피사과로 사과디저트 서빙.
    }
}
```

<br>

만약 추가로 프랑스 파리 지점에도 식당을 오픈하고 싶다? -->>    
**파리지점을 레스토랑 클래스 하위에 두고** + **서빙 방식은 템플릿메서드를 사용하고** +    
**사과 객체 생성의 경우엔 파리지점클래스에서 파리지점만의 사과객체생성 로직을 재정의**하면 된다.  
> ex) 파리에서 구하기 쉬운 사과종 if문(그린애플, 퍼플애플, 샤인애플...)

<br><br><br><hr><br>


## GoF Factory Method 패턴의 일반적인 구조

<br>

<div align="center">
<img src="https://github.com/user-attachments/assets/89838447-827b-48ef-9c1b-de447565f2e2">
</div>

> 출처: Java객체지향 디자인패턴(한빛미디어)

<br><br>

> + **`Creator (추상클래스)`: 전체적인 로직-흐름을 나타내는 Template Method가 정의되어있는 추상클래스**  
>  **우리 예제에서는 `Restaurant 추상클래스`**  

<br>

> + **`ConcreteCreator 클래스`: 객체 생성 메서드인 Factory Method를 재정의하는 하위클래스**  
> **우리 예제에서는 `Seoul & NYRestaurant 클래스`**  
> 팩토리 메서드내에서 특정종류의 사과객체를 지역변수로 보유 -> **ConcreteProduct와 의존관계**  

<br>

> + **`Product (추상클래스)`: 팩토리 메서드로 생성되는 객체를 나타내는 추상클래스**
> **우리 예제에서는 `Apple 추상클래스`**   

<br><br><hr><br>

## 마무리

<br>

+ **`전체적인 로직을 나타내는 Template Method를 상위에 정의`해두고, `객체를 생성하는 부분인 Factory Method는 각 하위클래스에서 정의`하도록.**
   
+ **즉, 전체적인 흐름은 Template Method 패턴을 따르지만, 객체를 생성하는 부분은 하위클래스에서 정의하도록하여 객체 생성은 하위 클래스가 하도록 위임하는 생성패턴** 
   
+ **이것이 바로 => `GoF Factory Method 패턴`**  

<br><br><hr><br><br>

### `<Factory Method 패턴과 Template Method 패턴의 차이점>`

<br>

#### Factory Method 패턴은 Template Method 패턴의 변형으로 볼 수 있다.  
#### Factory Method 패턴은 `객체 생성에 중점`을 두고, Template Method 패턴은 `알고리즘의 구조를 정의하는 데 중점`을 둔다.  

<br><br>

 ### <div align="center"> [ Template Method 패턴과 Factory Method 패턴과의 관계 ]  </div>

 <br>

`템플릿 메서드 패턴`은 `행동 패턴`이고 `팩토리 메서드 패턴`은 `생성 패턴`이라 둘은 전혀 다른 패턴.  

다만, Template Method 패턴을 이용하여 인스턴스를 생성하는 공장을 구성한 것이 Factory Method 패턴이기 때문에, 두 패턴의 클래스 구조의 결은 동일하다.   

Template Method 패턴에서는 하위 클래스에서 구체적인 처리 알고리즘의 내용을 만들도록 추상 메소드를 상속 시켰었다.  

#### 이 로직을 알고리즘 내용이 아닌 `인스턴스 생성에 적용한 것이 Factory Method 패턴` 인 것이다.  

<br><br><hr><br><br>

<br><br>
