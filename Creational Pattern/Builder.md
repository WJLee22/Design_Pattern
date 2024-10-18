

#   Builder Pattern

<br>

## Builder(빌더) 패턴이란?

<br>

+ **객체를 생성할때, 생성자의 인자가 많은 경우에 사용**

+ **생성자의 인자들 중에서, 우리가 반드시 제공해줘야하는 `필수적 인자`들과 + 재공하지않아도되는 `선택적 인자`가 혼합되어 있는 경우에 사용되는 생성형 디자인패턴**

+ **Immutable 객체(변경할 수 없는 객체)를 생성하고 싶은 경우에 사용. 객체가 생성된 이후 수정이 불가능**        

<br>



<br>

## 책 클래스에 대한 객체생성 예제를 통해 알아보는 Builder Pattern      

<br>

```java
public class Book{
    private Long id; //필수적인 속성
   	private String isbn; //필수적인 속성
   	private String title; //선택적인 속성
   	private String author; //선택적인 속성
   	private int pages; //선택적인 속성
   	private String category; //선택적인 속성
}
```

**위와 같이 책 클래스가 있다. 이 책 클래스에 대한 객체를 생성하는 예를 살펴보자.**  

<br>
<br>

## <Telescoping Constructor패턴>
> **[ 점층적 생성자 패턴 ]**
> 
> Telescope= 망원경, 망원경은 렌즈를 점층적으로 쌓은 형태.  
> so, 이와 비슷하게 생성자를 점점 추가하면서 속성정보를 설정하여 객체를 생성시키는 Telescoping Constructor패턴
>
<br>

**이 _Telescoping_ _Constructor_ 패턴은 필수 인자를 받는 생성자를 정의한 후에 선택적 인자를 추가로 받는 생성자를 계속해서 정의하는 형식이다.**


**<div align="center">코드를 통해 Telescoping Constructor패턴을 이해해보자.</div>**

<br>

```java
public class Book{
    private Long id; //필수적인 속성
    private String isbn; //필수적인 속성
    private String title; //선택적인 속성
    private String author; //선택적인 속성
    private int pages; //선택적인 속성
    private String category; //선택적인 속성

    public Book(String isbn, Long id) {
        this.isbn = isbn;
        this.id = id;
    }
}
```

<br>

위와같이 필수적인 속성인 책의 isbn과 id 값을 인자로 받는 생성자를 정의한 후에

<br>

```java
    public Book( Long id, String isbn, String title) {
        this.id = id;
        this.isbn = isbn;
        this.title = title;
    }

    public Book(Long id, String isbn, String title, String author) {
        this.id = id;
        this.isbn = isbn;
        this.title = title;
        this.author = author;
    }

    public Book(Long id, String isbn, String title, String author, int pages) {
        this.id = id;
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.pages = pages;
    }

    public Book(Long id, String isbn, String title, String author, int pages, String category) {
        this.id = id;
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.pages = pages;
        this.category = category;
    }
```
<br>

필수적 정보인 id, isbn 만을 인자로받는 생성자와 더불어서, 계속해서 선택적 인자를 하나씩 추가하면서 생성자를 정의하였다.  
결국 모든 속성정보들을 인자로받는 생성자까지 만들어진 모습이다.  

 <br>

**이것을 `Telescoping Constructor패턴` 즉, `점층적 생성자 패턴`이라고 한다.**

<br><br>

## Telescoping Constructor패턴의 문제점  

<br>

+ 인자의 갯수에 따라 생성자의 갯수도 많이지게 된다.
  
+ 인자의 갯수가 많이짐에따라 각 인자의 의미를 알기가 어려워지고 가독성이 떨어지게된다.
  
+ 인자가 같은 타입인 경우에는 해당 인자들을 구분하기어려워 잘못기입하는 경우도 발생한다.
  > title 인자 위치에 author 인자를 작성한다던지..

+ 또한, 사용하지않는 불필요한 인자가 있을때는 일일히 null 및 0값으로 지정해줘야하는 번거로움이있다 .
  >  title 이나 author 속성은 필요없는 경우, 해당 인자들을 null로 작성해줘야함. 

<br>

**이러한 Telescoping Constructor패턴의 문제점을 다소 해결하기위한 접근 방법으로 `JavaBeans 패턴`이 있다.**  

<br><br>

## <JavaBeans 패턴> 

> ### Setter 메서드로 각 속성의 값을 설정하는 방법
>
> 각 속성에 대한 setter 메서드로 각 속성값을 설정한다.
>  
> ex)
>```java
>   public Book(Long id, String isbn) {this.id=id; this.isbn=isbn;}
>    public void setId(Long id){this.id=id;}
>    public void setIsbn(String isbn){this.isbn=isbn;}
>    public void setTitle(String title){this.title=title;}
>    public void setAuthor(String author){this.author=author;}
>    public void setPages(int pages){this.pages=pages;}
>    public void setCategory(String category){this.category=category;}
> ```
>

<br>

인자들이 여러개인 경우 각 인자들을 파악하기 어려웠던 Telescoping Constructor패턴과는 달리, 

JavaBeans 패턴은 명시적으로 **`set+속성이름`** 의 **`Setter 메서드`** 를 제공해주기때문에  

**어떤 속성을 어떤 값으로 설정하였다는 것이 보다 명시적이고 직관적이기때문에 가독성이 향상되었다.**  

<br>

#### 허나, Setter 메서드가 있다는건 언제든지 해당 속성의 값을 변경 및 설정이 가능하다는 말이다.  
#### 즉, 가독성은 향상되었으나 불변의 객체 `immutable Object를 만들 수 없게되었다는 단점`이있다.  

<br><br><hr><br>

# <div align="center"><Builder 패턴></div>

<br>

> **우리가 만들고자하는 객체를 생성시킬 수 있는 기능을가진 `inner클래스`인 `builder`를 제공해준다.**
> 
> ex)
> ```java
> public class Book{
>public static class BookBuilder{
> ...
>   }
> }
> ```




<br>
<br>

