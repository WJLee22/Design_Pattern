

#   Decorator Pattern

<br>

## Decorator(데코레이터) 패턴이란?

<br>

+ **데코레이터 패턴(Decorator Pattern)은 대상 객체에 대한 기능 확장이나 변경이 필요할때, 객체에 동적으로 새로운 기능을 추가하고자할 때 -> 객체의 결합을 통해 해결해주는 구조 패턴**

+ **Decorating 즉 장식-꾸며주는 역할, 객체 지향 프로그래밍에서 원본 객체에 대해서 무언가를 장식하여 더 멋진 기능을 가지게 해주는 생성형 디자인패턴**  
  
+ **데코레이터 패턴을 이용하면 필요한 추가 기능의 조합을 런타임에서 동적으로 생성가능**

+ **기존 서브클래스로 구성할때 보다 훨씬 유연하게 기능을 확장 가능 & 기능을 구현하는 클래스들을 분리함으로써 수정이 용이**

+ **객체를 여러 데코레이터로 래핑(연결)하여 기존 객체+여러 기능-동작을 결합할 수 있음**


  <br>
  
  > ex) new InputStreamReader(inputStream, "UTF-8");  
  > 바이트 스트림(InputStream)을 문자 스트림(InputStreamReader)으로 연결하는 경우도 데코레이터 패턴의 예시 중의 하나
  > 
  > InputStreamReader는 InputStream을 감싸서 바이트 스트림을 문자 스트림으로 변환하는 데코레이터 역할. 즉, InputStream 객체에 문자 스트림 기능을 추가하는 것
  > 
  
  > 이처럼 데코레이터 패턴을 이용하면 기존 스트림의 기능을 유지하면서 새로운 기능을 유연하게 추가할 수 있음  

<br>



<br>

## 네비게이션SW에서의 도로 표시 예제를 통해 알아보는 Decorator Pattern      

<br>

## `<초기설계>`

<div align="center"><img src="https://github.com/user-attachments/assets/3e633839-9563-4fdd-bb8d-9787bb1a3b86"></div>

- **RoadDisplay 클래스:** 기본 도로 표시 기능을 제공하는 클래스 (기본 도로 표시 기능을 제공해주는 draw() 메서드)
  
- **RoadDisplayWithLane 클래스:** 여기에 추가로, 기본 도로 표시에 추가적으로 차선을 표시하는 클래스( draw() + 차선표시해주는 drawLane() 메서드)

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
이는 단순히 super.draw()로 RoadDisplay의 기본 도로 표시 기능을 재사용하기위한 상속일 뿐이다.  
**단순 기능 재사용을 위한 일반화관계는 상속의 잘못된 사용이다.**


### <div align="center">이 OCP 위반문제를 해결해보자.</div>

<br><br><br>

## 설계 개선(by Strategy 패턴 응용)

ScoreRecord 클래스를 변경하지않고 성적 출력방식을 변경 및 추가하도록해보자.

<br>

새로운 출력형식이 추가되어도 기존의 ScoreRecord 클래스는 변경되지않도록하는 설계.

<br>


