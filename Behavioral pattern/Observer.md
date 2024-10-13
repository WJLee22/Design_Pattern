

#   Observer Pattern

<br>

## Observer(옵서버) 패턴이란?

+ 설계하는 패턴  

+ 가능하도록해주는 패턴   

+ 가능하도록해주는 패턴   

<br>

>

>있도록 해준다.
>
>()
>
>

<br>

## 성적 출력 예제를 통해 알아보는 Command Pattern      

<br>

**을 우리가 만들고자한다.**  


-    클래스: ~~기능을 제공하는 클래스 
-  클래스: ~~하는 클래스

<br>

**<div align="center">이 ~~를 설계하면 아래와 같다.</div>**

<br>

## <기존 설계> 



<br>

## 기존 설계의 문제점  
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


<hr>

#### 기존코드
```java
import java.util.Collections;
import java.util.List;

public class MinMaxView {
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
        dataSheetView.update();
    }

    public List<Integer> getScoreRecord() {
        return scores;
    }
}
```

<br>

#### 요구사항에 맞게 변경된 ScoredRecord클래스 코드

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

    public List<Integer> getScoreRecord() {
        return scores;
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

<br><br>

## 설계 개선(by Strategy 패턴 응용)

ScoreRecord 클래스를 변경하지않고 성적 출력방식을 변경 및 추가하도록해보자.

<br>

새로운 출력형식이 추가되어도 기존의 ScoreRecord 클래스는 변경되지않도록하는 설계.

<br>

### 설계 단계

1. 무엇이 변화가 되는가? => 성적정보를 출력-표현하는 방식이 변한다, 성적 정보를 목록형식으로 출력했다가.. 최대값 최소값으로 출력했다가.. 평균값으로도 출력했다가... 즉 구체적인 View 클래스들(DataSheetView, MinMaxView...)

2. 그 변화되는 성적정보를 출력방식들을 하위 개념으로 둔다. 그 하위 클래스들의 포괄개념은, 성적 정보를 관찰하고있다가 변경사항이 생기면 성적정보를 가져다가 현재 출력 방식에 맞게 출력하도록하는 관찰자, 즉 Observer 인터페이스.

3. 관찰자 Observer가 관찰하는 대상인, 성적정보 데이터를 관리하는 SCoreRecord 클래스.

4. Observer는 성적 정보들을 동시에 다른 형식으로 출력해준다.
so, Strategy패턴과는 다르게, 한 데이터를 여러 다양한 형식으로 출력할 수 있게끔 SCoreRecord 클래스와 Observer는 다중성 * 연관관계를 가진다.   

5. 그리고, 성적 정보들을 출력해주는 클래스들은 성적 정보가 변했는지를 SCoreRecord로부터 통지받아야하기때문에, 성적 정보의 변경을 통지해주는 update() 메서드. 성적 정보가 업데이트되었으면 해당 최신정보를 출력해야 해야하기때문이다.(자기들만의 형식으로)

6. 
 

 

## 마무리

<br><br>

