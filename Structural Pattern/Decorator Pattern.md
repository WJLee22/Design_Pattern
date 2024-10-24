

#   Decorator Pattern

<br>

## Decorator(데코레이터) 패턴이란?

<br>

+ **객체를 생성할때, 생성자의 인자가 많은 경우에 사용**

+ **생성자의 인자들 중에서, 우리가 반드시 제공해줘야하는 `필수적 인자`들과 + 재공하지않아도되는 `선택적 인자`가 혼합되어 있는 경우에 유용하게 사용되는 생성형 디자인패턴**

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

Builder 패턴을 적용해서 기존 Book 클래스를 개선시켜보자. 

<br>

1. 먼저  Book 클래스에 대한 인스턴스는 Book 클래스의 inner Class인 BookBuilder를 통해서 생성하도록한다.
2. 메서드 체이닝을 통해, 기존 Telescoping Constructor 패턴의 가독성 문제를 해결한다.
3. 기존 JavaBeans 패턴의 immutable Object 생성불가문제를 해결하기위해 Setter 메서드는 구현하지 않도록한다.

<br><br>

### Builder 패턴을 적용시킨 코드

```java
public class Book {
    private Long id; //필수적인 속성
    private String isbn; //필수적인 속성
    private String title;
    private String author;
    private int pages;
    private String category;

    public static class BookBuilder {
        private Long id; //필수적인 속성
        private String isbn; //필수적인 속성
        private String title;
        private String author;
        private int pages;
        private String category;

        public Long getId() {
            return id;
        }

        public String getIsbn() {
            return isbn;
        }

        public String getTitle() {
            return title;
        }

        public String getAuthor() {
            return author;
        }

        public int getPages() {
            return pages;
        }

        public String getCategory() {
            return category;
        }

        //필수적인 속성들은 생성자로 설정해줌 
        public BookBuilder(Long id, String isbn) {
            this.id = id;
            this.isbn = isbn;
        }

        //보통 setter 메서드들은 반환형이 void 지만, 이 Builder는 반환형이 this로 - 자신을 반환함.
        //선택적인 속성들은 별도의 메서드로 설정해줌.메서드의 이름은 속성의 이름과 동일하게 for 가독성. 
        public BookBuilder title(String title) {this.title = title; return this;}// 메서드 체이닝을 위해 현재 객체 반환

        public BookBuilder author(String author) {this.author = author; return this;}

        public BookBuilder pages(int pages) {this.pages = pages; return this;}

        public BookBuilder category(String category) {this.category = category; return this;}

        //build 메서드는, BookBuilder 자신이 갖고있는 속성정보로 book인스턴스의 속성정보를 설정하고, 그 book인스턴스를 반환한다.
        //이 build 메서드를 가지고, BookBuilder인스턴스를 통해서 Book인스턴스를 생성&전달받을 수 있다.
        public Book build() {
            Book book = new Book();
            book.id = this.id;
            book.isbn = this.isbn;
            book.title = this.title;
            book.author = this.author;
            book.pages = this.pages;
            book.category = this.category;
            return book;
        }
    }
}
```
```java
public class Main {
    public static void main(String[] args) {

        //BookBuilder 인스턴스의 속성을 설정함과동시에, 해당 속성값을 그대로 갖는 Book 인스턴스를 생성&반환받음.
        //각 메서드의 반환값이 this이므로, 반환받은 객체에 연속적으로 접근하여 메서드를 호출하는 -> 메서드 체이닝이 가능하다.
        Book book1 = new Book.BookBuilder(1L, "isbn1234").author("WonJun Lee").build();
        Book book2 = new Book.BookBuilder(2L, "isbn2345").pages(360).author("WonJun Lee").build();
        System.out.println(book2.getAuthor());
    }
}
```

<br>

이처럼 직접 Book 인스턴스를 생성하는 것이 아닌, `Book 클래스 내부의 BookBuilder 이너클래스의 build 메서드로 Book 인스턴스를 생성하는 모습`이다. 

이전에 Telescoping Constructor의 경우에는 전달되는 인자의 순서가 중요했었다.  

인자의 순서에 따라 인자를 전달받는 메서드의  매개변수가 원래 의도와는 다른 값을 전달 받을 수 있다는 위험이있기때문이다.  
하지만 이 Builder 패턴을 사용하면, `메서드 체이닝 기법`을 이용하기때문에, 어떤 속성을 어떤 순서로 전달해주던간에 상관없이 명시적으로 해당 속성에 대한 인자를 전달해 줄 수 있게된다.  
속성값을 설정하는 메서드의 이름자체가 속성의 이름을 반영하고있기때문에 가독성도 매우 뛰어나다.  

또한, Book 인스턴스에는 별도로 Setter 메서드를 구현해놓지 않았기때문에 `immutable Object` 조건도 만족하게된다.  

<br>

**즉, 빌더 패턴을 사용하면 객체를 생성할 때 필요한 속성만 설정할 수 있어, 가독성과 유지보수성을 높이는 데 도움이 된다.**  

**또한, 일단 객체를 만들어두면 이 객체의 상태를 변경시키고 싶지않은 경우에 매우 효과적인 패턴이다.**  

<br>

## Lombok 라이브러리 사용하여 Builder패턴 최적화

<br>

다만 이 Builder패턴은 유용하기는 하나, 

Builder클래스를 내부클래스로 별도로 만들어주어야하고 + 속성의 갯수만큼 속성설정 메서드들을 만들어주어야하며 + build 메서드도 만들어주어야하기때문에 **기존코드보다 코드의 양이 상당히 증가하게되는 경향이있다.**  

<br> 

그래서 **Builder패턴을 그대로 사용하면서 코드를 단순화할 수 있는 방법**으로 **`Lombok(롬복)`** 이라는 라이브러리를 사용한다.  

<br>

+ Lombok 라이브러리를 사용하면 Builder 어노테이션을 사용할 수 있기때문에, Builder 어노테이션을 통해 Builder 클래스 내부의 코드를 자동으로 생성할 수 있게된다.  

+ **`@Builder 어노테이션`** 을 사용하면, Lombok이 자동으로 내부에 빌더 inner클래스를 생성해준다.  

+ 또한, 빌더 클래스에서는 각 필드에 대한 설정 메서드가 생성되어있으며 이 메서드들은 this를 반환하므로 메서드 체이닝도 구현되어있다. 

+ **`@Getter 어노테이션`** 을 사용하면, getter 메서드 역시 자동으로 생성해준다.

+ **`@Setter 어노테이션`** 도 역할은 같으나, **`immutable Object 구현을 위해`** 따로 적용시키지않았다.

+ **`@NonNull 이노테이션`** 을 적용시키면 해당 필드는 필수적인 속성이되어 반드시 값을 할당해주어한다. 그렇지않으면 NullPointerException 예외를 발생시켜준다.

+ 즉, Lombok 라이브러리를 사용하면 Builder 패턴을 간편하게 적용시킬 수 있게된다.

     <br>

```java
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

@Getter // getter 메소드를 자동으로 생성
@Builder // @Builder 어노테이션을 사용하면 Lombok이 해당 클래스에 빌더 패턴을 자동으로 적용.
public class Book {
    @NonNull
    private Long id; // 필수적인 속성
    @NonNull
    private String isbn; // 필수적인 속성
    private String title;
    private String author;
    private int pages;
    private String category;
}
```
```java
public class Main {
    public static void main(String[] args) {
        Book book1 = new Book.BookBuilder().id(1L).isbn("isbn1234").author("WonJun Lee").build();
        Book book2 = new Book.BookBuilder().id(2L).isbn("isbn2345").pages(360).author("WonJunLee").build();
        System.out.println(book2.getAuthor());
    }
}

```




<br>
<br>
