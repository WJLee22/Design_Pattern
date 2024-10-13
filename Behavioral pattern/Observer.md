

#   Observer Pattern

<br>

## Observer(옵서버) 패턴이란?

<br>

+ **데이터의 변경이 발생했을때, 상대 클래스 및 객체에 의존하지 않으면서 데이터 변경을 통지하고자 할 때 사용하는 패턴**

+ **데이터의 변경이 발생했다하더라도 통지대상인 상대 클래스 및 객체에 의존하지않기때문에, 새롭게 통지대상 클래스가 추가된다하더라도 데이터를 관리하는 클래스는 영향받지않도록 해주는 패턴**   

<br>



<br>

## 성적 출력 예제를 통해 알아보는 Observer Pattern      

<br>

**성적정보를 여러가지 방식으로 출력해주는 프로그램을 만들고자한다.**  

>여러가지 출력방식을 구현하기에 앞서, 목록형태로만 출력되도록 일단 구현해보자.

<br>

- ScoredRecord 클래스: 성적정보들을 저장/관리하는 클래스 
- DataSheetView클래스: 성적을 목록형태로 출력하는 클래스

<br>

**<div align="center">이 성적정보 출력 프로그램을 설계하면 아래와 같다.</div>**

<br>

## <기존 설계> 

![image](https://github.com/user-attachments/assets/be3f9a48-fda4-42de-926e-802919389281)


```java
import java.util.ArrayList;
import java.util.List;

public class ScoredRecord {
    private DataSheetView dataSheetView;
    private List<Integer> scores = new ArrayList<Integer>();

    public void setDataSheetView(DataSheetView dataSheetView) {
        this.dataSheetView = dataSheetView;
    }

    public void addScore(int score) {
        scores.add(score);
        dataSheetView.update(); //출력 클래스에게 성적 정보 변경 통지.
    }

    public List<Integer> getScoreRecord() {
        return scores;
    }
}
```
```java
import java.util.List;

public class DataSheetView {
    private ScoredRecord scoredRecord;
    private int viewCount;

    public DataSheetView(ScoredRecord scoredRecord, int viewCount) {
        this.scoredRecord = scoredRecord;
        this.viewCount = viewCount;
    }

    public void update() {
        List<Integer> record=scoredRecord.getScoreRecord();
        displaySCores(record, viewCount);
    }

    private void displaySCores(List<Integer> record, int viewCount) {
        System.out.println("List of "+ viewCount + " entries");
        for(int i=0; i<viewCount && i< record.size(); i++) {
            System.out.println(record.get(i));
        }
    }
}

```
```java
public class Main {
    public static void main(String[] args) {
        ScoredRecord scoredRecord = new ScoredRecord();
        DataSheetView dataSheetView = new DataSheetView(scoredRecord, 3);
        scoredRecord.setDataSheetView(dataSheetView);

        for(int index=1; index <= 5; index++) {
            int score= index*10;
            System.out.println("Adding "+score);
            scoredRecord.addScore(score);
        }
    }
}

```
<br>

## <기존 설계에 추가 요구사항 적용> 
### <p align="center">`<요구사항1>`</p> 


<br>

<p align="center">현재 프로그램은, 성적정보를 특정 갯수만큼 목록형태로 출력해주고있다</p> 
그런데 만약 리스트형식으로 출력해주지않고, 다른 방식으로 성적을 출력하도록 변경하고싶다면,   
예를 들어 성적정보들 중 최소/최대값만을 출력해주는 방식으로 변경하려면 코드에 어떤 영향을 주게되는가?

<br>

### <p align="center">`<요구사항2>`</p> 

<br>

<p align="center">뿐만 아니라 성적을 동시에 여러 가지 형태로 출력하려면 어떤 변경작업을 해야하는가?</p> 
예를 들어 기존처럼 목록형태로도 출력시키고 + 최소/최대값 형태로도 출력시키고자한다면 어떻게 해야하는가?

### <p align="center">`<요구사항3>`</p> 

<br>

<p align="center">프로그램을 실행시에 성적 출력방식이 변경되도록한다면 어떤 변경작업을 해야하는가?</p> 
예를 들어 처음에는 기존처럼 목록형태로 출력하고, 나중에 실행중에 최소/최대값 형태로 출력하고자 한다면?


<hr><br>

![image](https://github.com/user-attachments/assets/b897b82a-777c-4918-92cc-379040bee67f)

<br>

### <요구사항에 맞게 변경된 ScoredRecord클래스 코드>

```java
import java.util.ArrayList;
import java.util.List;

public class ScoredRecord {
    private MinMaxView minMaxView;
    private List<DataSheetView> dataSheetViews = new ArrayList<DataSheetView>();
    private List<Integer> scores = new ArrayList<Integer>();

    public void setMinMaxView(MinMaxView minMaxView) {
        this.minMaxView=minMaxView;
    }

    public void addScore(int score) {
        scores.add(score);
        minMaxView.update();

        for(DataSheetView dataSheetView : dataSheetViews)
            dataSheetView.update();
    }
    
    public void addDataSheetView(DataSheetView dataSheetView) {
        dataSheetViews.add(dataSheetView);
    }
    
    public List<Integer> getScoreRecord() {
        return scores;
    }
}

```
```java
public class Main {
    public static void main(String[] args) {
        ScoredRecord scoredRecord = new ScoredRecord();
        MinMaxView minMaxView = new MinMaxView(scoredRecord);
        DataSheetView dataSheetView3 = new DataSheetView(scoredRecord, 3);
        DataSheetView dataSheetView5 = new DataSheetView(scoredRecord, 5);

        scoredRecord.setMinMaxView(minMaxView);
        scoredRecord.addDataSheetView(dataSheetView3);
        scoredRecord.addDataSheetView(dataSheetView5);

        for(int index=1; index <= 5; index++) {
            int score= index*10;
            System.out.println("Adding "+score);
            scoredRecord.addScore(score);
        }
    }
}

```
<br>

### <p align="center">`<기존설계의 문제점>`</p> 

위 요구사항들을 만족시키도록 변경할 경우, 성적 정보 관리 클래스인 ScoreRecord를 상당히 많은부분 변경해줘야하는 OCP 위반 발생.

어느순간에는 성적정보를 리스트형태로 보여주다가 어떤 때에는 최소/최대값만 보여주도록 하고싶은 경우라면 ScoreRecord 코드 변경을 해줘야할 것이고, 동시에 여러 형태로 성적정보를 보여주고 싶은 경우에도 코드 변경은 불가피하다.  

또한, 성적정보들의 평균값도 출력해보고싶어서 AverageView를 정의한 경우에도 setAverageView 필드추가해주고.. setAverageView 메서드 추가해주고.. 기존 ScoreRecord 클래스의 코드에 추가해줘야하는 문제가 발생한다는 것이다.

**새로운 형식으로 성적정보를 출력 or 기존의 형식을 다른 형식으로 바꾸어 출력 or 성적을 동시에 여러 가지 형태로 출력**  

**`=> ScoreRecord 클래스의 변경이 요구됨`**

<br>

### <div align="center">이 OCP 위반문제를 해결해보자.</div>

<br><br><br>

## 설계 개선(by Strategy 패턴 응용)

ScoreRecord 클래스를 변경하지않고 성적 출력방식을 변경 및 추가하도록해보자.

<br>

새로운 출력형식이 추가되어도 기존의 ScoreRecord 클래스는 변경되지않도록하는 설계.

<br>

### <설계 단계>

1. 무엇이 변화가 되는가? => 성적정보를 출력-표현하는 방식이 변한다, 성적 정보를 목록형식으로 출력했다가.. 최대값 최소값으로 출력했다가.. 평균값으로도 출력했다가... 즉 구체적인 View 클래스들(DataSheetView, MinMaxView...)

2. 그 변화되는 성적정보를 출력방식들을 하위 개념으로 둔다. 그 하위 클래스들의 포괄개념은, **성적 정보를 관찰하고있다가 변경사항이 생기면 성적정보를 가져다가 현재 출력 방식에 맞게 출력하도록하는 관찰자**, 즉 **`Observer`** 인터페이스.

3. 관찰자 Observer가 관찰하는 대상인, 성적정보 데이터를 관리하는 SCoreRecord 클래스.

4. Observer는 성적 정보들을 동시에 다른 형식으로 출력해준다.  
so, Strategy패턴과는 다르게, 한 데이터를 여러 다양한 형식으로 출력할 수 있게끔 SCoreRecord 클래스와 Observer는 다중성 * 연관관계를 가진다. 즉, 기존처럼 구체적인 View 객체들과 직접 연관관계를 맺는것이 아닌, 포괄개념인 Observer들과 연관관계를 맺도록 변경한다.  

5. 그리고, 성적 정보들을 출력해주는 클래스들은 성적 정보가 변했는지를 SCoreRecord로부터 통지받아야하기때문에, 성적 정보의 변경을 통지해주는 update() 메서드. 성적 정보가 업데이트되었으면 해당 최신정보를 출력해야 해야하기때문이다.(자기들만의 형식으로)  
단, 각 출력 형식 클래스들은 SCoreRecord로부터 직접 최신 성적 데이터를 받아오기떄문에 SCoreRecord와 직접 연관관계를 맺는다.  

6. 이제 여기에 성적 정보들을 정렬하여 출력해주는 SortView 클래스를 추가해보자. 이제는 새로운 출력 방식을 추가해도 OCP 원칙에 만족하는 것을 확인해볼 수 있을 것이다.
   
<br><br><hr>


## <div align="center"><개선된 설계></div>

<div align="center"><img src="https://github.com/user-attachments/assets/a959926b-5935-4a2b-b0e9-4f469c41b2cd"></div>

 <br>

```java
public interface Observer {
    public void update();
}
```
```java
import java.util.List;

public class DataSheetView implements Observer{
    private ScoredRecord scoredRecord;
    private int viewCount;

    public DataSheetView(ScoredRecord scoredRecord, int viewCount) {
        this.scoredRecord = scoredRecord;
        this.viewCount = viewCount;
    }

    public void update() {
        List<Integer> record=scoredRecord.getScoreRecord();
        displaySCores(record, viewCount);
    }

    private void displaySCores(List<Integer> record, int viewCount) {
        System.out.println("List of "+ viewCount + " entries");
        for(int i=0; i<viewCount && i< record.size(); i++) {
            System.out.println(record.get(i));
        }
    }
}
```
```java
import java.util.Collections;
import java.util.List;

public class MinMaxView implements Observer{
    private  ScoredRecord scoredRecord;

    public MinMaxView(ScoredRecord scoredRecord){
        this.scoredRecord=scoredRecord;
    }

    public void update(){
        List<Integer> record=scoredRecord.getScoreRecord();
        displayScores(record);
    }

    private void displayScores(List<Integer> record) {
        int min= Collections.min(record);
        int max= Collections.max(record);

        System.out.println("Min" + min + " Max "+max);
    }
}
```
```java
import java.util.Collections;
import java.util.List;

public class SortView implements Observer{
    private  ScoredRecord scoredRecord;

    public SortView(ScoredRecord scoredRecord){
        this.scoredRecord=scoredRecord;
    }

    public void update(){
        List<Integer> record=scoredRecord.getScoreRecord();
        displayScores(record);
    }

    private void displayScores(List<Integer> record) {
        System.out.println("Sorted View");
        Collections.sort(record);

        for(Integer score: record)
            System.out.println(score);
    }
}

```
```java
import java.util.ArrayList;
import java.util.List;

public class ScoredRecord {
    private List<Observer> observers = new ArrayList<Observer>(); //Observer 여러개와 * 연관관계.

    public void attach(Observer observer){
        observers.add(observer);
    }

    public void detach(Observer observer){
        observers.remove(observer);
    }

    private List <Integer> scores = new ArrayList<Integer>();

    public void addScore(int score) {
        scores.add(score);

        for(Observer observer: observers)
            observer.update();
    }

    public List<Integer> getScoreRecord() {
        return scores;
    }
}

```
```java
import java.util.Random;

public class Main {
    public static void main(String[] args) {
        ScoredRecord scoredRecord = new ScoredRecord();
        Observer minMaxView = new MinMaxView(scoredRecord);
        Observer dataSheetView3 = new DataSheetView(scoredRecord, 3);
        Observer dataSheetView5 = new DataSheetView(scoredRecord, 5);

        scoredRecord.attach(minMaxView);
        scoredRecord.attach(dataSheetView3);
        scoredRecord.attach(dataSheetView5);

        Observer sortedView = new SortView(scoredRecord);
        scoredRecord.attach(sortedView); //옵서버로 등록.

        Random random = new Random();

        for(int index=1; index <= 5; index++) {
            int score= random.nextInt(101);
            System.out.println("Adding "+score);
            scoredRecord.addScore(score);
        }
    }
}
```
<br>

#### 자 이제 동시에 여러 출력방식으로 출력해도 + 새롭게 SortView 출력방식이 추가되어도  
#### 성적정보를 관리하는 중요한 역할을하는 `기존 ScoredRecord코드는 변경되지 않는다.`  

<br>
<br>
<hr>
<br>

## <div align="center"><성적정보 프로그램 개선: 최종 설계></div>

<br>

**성적정보를 다양한 형식으로 출력시켜주는 프로그램을 좀더 개선-정제시켜보자.**

현재 ScoreRecord 클래스를 보면, ScoreRecord 클래스가 관리하는 성적정보들에 변경이 있나없나 관찰하고있는 Observer들이 있는데,
그 Observer들을 등록시켜주는 attach(), Observer들을 제거시켜주는 detach(), 새로운 성적정보가 추가되어 성적정보에 변경이 생겼음을 자신을 관찰하고있는 Observer들에게 알려주는 update() 호출 등등 이러한 `Observer들을 관리하는 기능들`이 현재 ScoreRecord에 존재하고있다.  

그런데 이러한 Observer들을 관리하는 기능들은 ScoreRecord클래스만 쓸 수 있는게 아니라, 그 다른 어떤 성적을 관리하는 클래스에서도 위와같은 기능들이 필요할 것이다.     

so, 특정 ScoreRecord 클래스에서만 해당 기능들을 제공하는 것 보단, 성적데이터를 관리하는 다른 클래스에서도 기능들을 쓸 수 있도록  설계를 개선해보자.   

Observer들을 관리하는 기능들을 보유하고있는 별도의 Subject 클래스를 모델링하고, 성적데이터를 관리하는 클래스들이 이 Subject클래스를 상속받아서 가져다쓰도록하면 될 것이다.  

이제 ScoreRecord클래스가 아니라, ScoreRecord클래스가 상속받는 포괄개념인 Subject 클래스가 Observer들을 관리한다.  

<br><br>


<div align="center"><img src="https://github.com/user-attachments/assets/3c6e098e-335b-4a96-939f-64c2a070761a"></div>

<br>

```java
import java.util.ArrayList;
import java.util.List;

public class Subject {
    private List<Observer> observers = new ArrayList<Observer>(); //Observer 여러개와 * 연관관계.

    public void attach(Observer observer){
        observers.add(observer);
    }

    public void detach(Observer observer){
        observers.remove(observer);
    }

    protected void notifyObservers(){ //Observer들에게 성적정보 변경사항을 알림.
        for(Observer observer: observers)
            observer.update();
    }

}
```
```java
import java.util.ArrayList;
import java.util.List;

public class ScoredRecord extends Subject{

    private List <Integer> scores = new ArrayList<Integer>();

    public void addScore(int score) {
        scores.add(score);
        notifyObservers();
    }

    public List<Integer> getScoreRecord() {
        return scores;
    }
}

```

<br>

#### EX)

<div align="center"><img src="https://github.com/user-attachments/assets/ef67ea5a-fc7f-4e6a-8a72-f841c0c3f9ed"></div>

<br>
<br>


## 마무리

<br>

>
+ **옵서버 패턴은 통보 대상 객체의 관리를 Subject 클래스와 Observer 인터페이스로 일반화한다.**
> 
+ **그러면 데이터 변경을 통보하는 클래스(ConcreteSubject)는 통보 대상 클래스/객체(ConcreteObserver)에 대한 의존성을 제거할 수 있다.**
>  
+ **결과적으로 옵서버 패턴은 통보 대상 클래스나 대상 객체의 변경에도 ConcreteSubject 클래스를 수정없이 그대로 사용 할 수 있도록한다.**
>


<br><br>

`ConcreteSubject 클래스`인 -> scoredRecord 클래스가 관리하는 성적정보에 변경이 생겼으면,  

상대 클래스인 DataSheetView or MinMaxView 와 같은 `ConcreteObserver 클래스`들에게 데이터의 변경을 통지해야하는데,  

이때 해당 **상대클래스에게 의존하지 않으면서 데이터의 변경을 통지하고자하는 경우 Observer(옵서버) 패턴을 사용**한다.

<br>



<br><br>

