# Composite Pattern

<br>

## Composite 패턴이란?

<br>

+ **Composite 패턴은 객체를 트리 구조로 구성하여 부분-전체 계층을 표현할 수 있게 해주는 구조 패턴**

+ **Composite 패턴은 `트리 구조`를 갖는다**  

+ **부분(part)-전체(whole)의 관계를 가지는 객체들을 정의할 때 유용**

+ **컴퍼지트 패턴은 전체-부분의 관계를 가지는 객체들 간의 관계를 정의할 때 유용하다. 그리고 클라이언트는 전체와 부분을 구분하지 않고 동일한 인터페이스를 사용할 수 있다.**  

+ **클라이언트가 단일 객체와 그룹(복합) 객체를 동일하게 다룰 수 있게 통일시켜주는 패턴**
> 그룹(복합) 객체는 여러 개의 객체를 포함가능한, 여러 개의 객체로 구성된 객체  
> ex) 파일구조에서 여러 파일들을 담아두는 **디렉터리**도 **하나의 파일**이듯, 복합 객체도 복합 객체에 포함되는 하나의 객체가 될 수 있다

 
<br>

<br>

## 택배 포장 예제를 통해 알아보는 Composite Pattern

<br>

![image](https://github.com/user-attachments/assets/f9e01da8-c174-466a-aac1-3125b935cf29)

<br>

#### 위의 코드와 같이 각 item별로 list를 관리하게되면, 추후 박스에 담을 item 등장시마다 기존코드 변경 불가피.

#### so, item들을 포괄하는 개념이 필요. 

#### 가장 먼저 생각해볼 수 있는건 모든 객체들을 포괄하는 개념인 객체들의 모체 `Object`

<br>

#### 하지만, 알다싶이 Object로 객체들을 포괄하는 것은 매우 위험하다.    

<br>

![image](https://github.com/user-attachments/assets/48d2a7cc-0c35-4cae-89ac-a83e33e79c14)

<br>

#### 위의 코드와 같이, 해당 Object 객체를 조건문 + instanceof 연산자로 매번 판별해야하며, 새로운 객체 추가시마다 조건문을 그에따라 추가해줘야하기에 이 역시 OCP에 위반되기 때문이다.    

<br> 

그렇다면 그 방법이 떠오를 것이다.  

<br> 

#### ` "모든 item들을 포괄하는 포괄개념을 추상클래스로 모델링하고 + 이를 상속받도록하여 다형성을 이용" `  

#### 즉, box를 전체 객체 - 그외의 양말 및 트라우저 등 물품들을 부분 객체로 정의하자.(이떄 박스 또한 박스에 담기는 물품임!!)  


<br> 
<br> 

<div align="center"><img src="https://github.com/user-attachments/assets/2c2c65ca-45bb-4925-b706-86d161748fbe"></div>

<br>

```java
public class Main {
    public static void main(String[] args) {
        Box box1 = new Box(0);
        Socks s1 = new Socks(200); // 이 양말은 200g
        Socks s2 = new Socks(300); // 이 양말은 300g
        Trousers t1 = new Trousers(600);

        box1.addItems(s1);
        box1.addItems(s2);
        box1.addItems(t1);

        System.out.println(box1.price());

        // 큰 박스 안에는 작은 박스(box1)와 금(gold)
        Box box2 = new Box(0);
        Gold g1 = new Gold(800);
        box2.addItems(box1);
        box2.addItems(g1);
        System.out.println(box2.price());
    }
}
```
```java

public abstract class ParcelItem {
    protected int weight;

    public ParcelItem(int weight) {
        this.weight = weight;
    }

    public abstract int price();
}

```
```java
public class Socks extends ParcelItem {
    public Socks(int weight) {
        super(weight);
    }

    public int price() {
        return this.weight / 100 * 200;
    }
}
```
```java

public class Trousers extends ParcelItem {
    public Trousers(int weight) {
       super(weight);
    }

    public int price() {
        return this.weight / 100 * 200;
    }
}
```
```java
public class Gold extends ParcelItem {
    public Gold(int weight) {
        super(weight);
    }

    public int price() {
        return this.weight / 100 * 2000;
    }
}
```
```java
import java.util.ArrayList;
import java.util.List;

public class Box extends ParcelItem {
    private List<ParcelItem> items = new ArrayList<>(); //ParcelItem을 원소를 갖는 리스트.(즉, 모든 물건 다 담을 수 있음. 심지어 박스포함)

    public Box(int weight) {
        super(weight);
    }

    public int price() {
        int totalPrice = 0;
        for (ParcelItem item : items) {
            totalPrice += item.price();
        }
        return totalPrice;
    }

    public void addItems(ParcelItem item) {
        items.add(item);
    }
}
```

<br>

위 택배 박스의 상태를 보면, 아래와 같이 트리구조가 나타남을 확인해볼 수 있다.   

<br>


<div align="center"><img src="https://github.com/user-attachments/assets/290d80da-ffb7-4af2-aa1c-4d1c509ed1ca"></div>  

<br>

#### 이와같이, Composite Pattern은 `트리구조`와 매우 밀접해 있는 구조 디자인 패턴이다.  


<br><br><hr><br>

## Composite 패턴의 일반적인 구조

<br>

<div align="center">
<img src="https://github.com/user-attachments/assets/26ce75be-7503-4b7e-ac0a-0362f7d8c03b">
</div>

 > 출처: Java객체지향 디자인패턴(한빛미디어)  
 > 여기서, Composite -> Component간의 관계가 합성관계로 되어있지만, 집약관계도 상관없다.
 > 예제에서의 operation은 price 메서드
 > 예제에서의 addComponent는 addItems  
  
<br><br>

> + **`Component (추상클래스)`: leaf 와 Composite 객체들의 포괄개념. 단일 객체와 그룹 객체를 동일하게 다룰 수 있게 통일시켜주는 역할**    
> 예제에서의 ParcelItem 

<br>

> + **`Leaf 클래스`: 단일 객체 클래스**  
> 예제에서의 Socks, Trousers, gold, shirts.... 

<br>

> + **`Composite (추상클래스)`: 복수 개의 Component들을 담을 수 있는 전체(그룹) 객체 클래스**    
> 예제에서의 box 


<br>
<br>
<br><br>
