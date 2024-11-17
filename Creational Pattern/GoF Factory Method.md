#   `GoF` Factory Method Pattern

<br>

##  `GoF` Factory Method( `GoF`팩토리 메서드) 패턴이란?
> ✔ 4명이 학자들이 제안한 Factory Method패턴
<br>

#### 이전에 알아본 Factory Method 패턴은 매우 간단한 설계방식이었다.  
#### 이번에 살펴볼 `GoF Factory Method 패턴`은 Factory Method 패턴과 `다르다`.  
#### 한번 차이점을 알아보자.  

<br>

+ **`GoF`팩토리 메서드 패턴(GoF Factory Method Pattern)은 객체 생성 로직을 공장(Factory) 클래스로 캡슐화 처리하여, 객체생성을 직접하는게아닌 공장클래스에서 대신 생성하게 하는 행위패턴**

> 즉, 각 클라이언트에서 직접 new 연산자를 통해 제품 객체를 생성하는 것이 아닌, 제품 객체 생성을 담당하는 공장 클래스에서 객체 생성을 도맡아서 한다.  

  
<br>

[이전 팩토리 메서드 패턴 참고](https://github.com/WJLee22/Design_Pattern/blob/main/Creational%20Pattern/Factory%20Method.md)

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
