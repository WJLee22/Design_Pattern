# Composite Pattern

<br>

## Composite 패턴이란?

<br>

+ **Composite 패턴은 객체를 트리 구조로 구성하여 부분-전체 계층을 표현할 수 있게 해주는 구조 패턴**

+ **클라이언트가 단일 객체와 그룹(복합) 객체를 동일하게 다룰 수 있게 통일시켜주는 패턴**
> 그룹(복합) 객체는 여러 개의 객체를 포함가능한, 여러 개의 객체로 구성된 객체  
> ex) 파일구조에서 여러 파일들을 담아두는 **디렉터리**도 **하나의 파일**이듯, 복합 객체도 복합 객체에 포함되는 하나의 객체가 될 수 있다

 
<br>

<br>

## 택배 포장 예제를 통해 알아보는 Composite Pattern

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
    private List<ParcelItem> items = new ArrayList<>();

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


<br><br><hr><br>

## Composite 패턴의 일반적인 구조

<br>

<div align="center">
<img src="">
</div>
  
<br>
<br>
<br><br>
