# OCP (개방폐쇄원칙)

## 좋은 소프트웨어를 위한 설계 원칙 SOLID

좋은 소프트웨어란,  
**이해하기 쉽고**, **변경하기 용이하며(유지보수 용이)**, **새로운 기능으로의 확장이 용이한 소프트웨어**를 말한다.  
이러한 장점을 갖춘 좋은 소프트웨어를 만들기 위해서는 설계 원칙을 준수해야 한다.

로버트 마틴이 주장한 다섯 가지 설계 원칙을 통틀어 **SOLID**(SRP, OCP, LSP, ISP, DIP)라고 부르며, 이 중 가장 핵심이자 디자인 패턴 이해에 필수적인 원칙이 **OCP**이다.

<br>

## OCP(개방-폐쇄 원칙)
<br>

**Open** = 개방: 새로운 기능을 추가할 수 있도록 열려 있어야 한다.  
**Closed** = 폐쇄: 새로운 기능의 추가로 인해 기존의 코드가 변경되지 않도록, 영향을 받지 않도록 폐쇄 설계되어야 한다.

<br>

즉,  
> **OCP 원칙은 기존 코드를 변경하지 않으면서 새로운 기능을 추가할 수 있도록 설계하는 원칙**
을 말한다.

<br>

## 학생 성적출력 예제

예제를 통해 OCP 원칙에 대해서 알아보자. 

학생 정보를 나타내는 `Student` 클래스가 있다. 이 클래스에는 이름, 중간 성적, 기말 성적, 과제 성적 정보가 있으며, 이를 통해 학점을 계산하는 `calculateGrade` 기능과 성적 정보를 XML 형식으로 출력해주는 **`print`** 기능이 존재한다.
다이어그램으로 표현해보면 아래와같다. 

<div align="center">
  <img src="https://github.com/user-attachments/assets/959e302b-ab1b-4424-b6e8-5e6c403ae49e">
</div>

<br>

그럼 성적 정보 출력을 담당하는 print 메서드를 코드로 살펴보자.

<br>

### 성적 정보를 xml 형식으로만 출력해주는 print 메서드
<hr>
<br>

```java
     public void print(String mode) {

            try {
                DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
                Document doc = docBuilder.newDocument();
                doc.setXmlStandalone(true);

                Element student = doc.createElement("학생성적정보");
                doc.appendChild(student);

                Element name = doc.createElement("이름");
                student.appendChild(name);
                name.appendChild(doc.createTextNode(this.name));

                Element gradeInfo = doc.createElement("성적정보");
                student.appendChild(gradeInfo);

                Element middleScore = doc.createElement("중간점수");
                gradeInfo.appendChild(middleScore);
                middleScore.appendChild(doc.createTextNode(String.valueOf(this.midScore)));

                Element finalScore = doc.createElement("기말점수");
                gradeInfo.appendChild(finalScore);
                finalScore.appendChild(doc.createTextNode(String.valueOf(this.finalScore)));
                Element hwScore = doc.createElement("과제점수");
                gradeInfo.appendChild(hwScore);
                hwScore.appendChild(doc.createTextNode(String.valueOf(this.hwScore)));


                Element grade = doc.createElement("학점");
                gradeInfo.appendChild(grade);
                grade.appendChild(doc.createTextNode(String.valueOf(calculateGrade())));

                // XML로 쓰기
                TransformerFactory transformerFactory = TransformerFactory.newInstance();

                Transformer transformer = transformerFactory.newTransformer();
                transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4"); //정렬 스페이스4칸
                transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
                transformer.setOutputProperty(OutputKeys.INDENT, "yes"); //들여쓰기
                transformer.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC, "yes"); //doc.setXmlStandalone(true); 했을때 붙어서 출력되는부분 개행

                DOMSource source = new DOMSource(doc);
                StreamResult result = new StreamResult(System.out);

                transformer.transform(source, result);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
```
<br>
이와 같이 print 메서드는 xml 형식으로만 성적을 출력해주는 기능을 담당한다.

<br><br>

> 현재 학생의 성적 출력 형식은 xml이다. 그러나, 만약 학생의 성적을 출력하는 형식이 xml 이 아닌 
  json 형식으로도 출력하고 싶어진다면 어떻게할까?

> => print 메서드에 json 형식으로 출력하는 코드 구문을 추가하면 될 것이다. 하지만, 이 경우 print 메서드의 코드에 json 형식으로의 출력을 구현하게되면서 기존코드인 student 클래스의 변경이 발생하게되어 OCP 원칙에 위반되어진다.
     
xml 뿐만이 아닌 json 형식으로도 출력되도록 추가변경한 아래 코드를 한번보자. 

<br>

### xml + json 형식으로도 출력되도록 추가변경한 print 메서드
<hr>
<br>

````java
    public void print(String mode) {
        if (mode.equals("xml")) {
            try {
                DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
                Document doc = docBuilder.newDocument();
                doc.setXmlStandalone(true);
                ... 
                
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else if (mode.equals("json")) {
            JSONObject entry = new JSONObject();

            Map item = new LinkedHashMap();
            item.put("name", this.name);
            item.put("midScore", this.midScore);
            item.put("finalScore", this.finalScore);
            item.put("hwScore", this.hwScore);
            item.put("grade", calculateGrade());

            JSONObject json = new JSONObject(item);

            System.out.println(json.toJSONString());
        }
    }
````


이처럼, 새로운 형식으로 성적을 출력하고자 할 때에는 print 메서드가 계속 변경되기때문에, **기존의 코드가 변경 => OCP 원칙에 위반되어진다.**
그렇다면 OCP를 만족하는 설계가 되기위해서는 어떻게해야할까?

<br>
<hr>
 
## OCP 만족을 위한 4단계

OCP를 만족하는 설계가 되기위해서는 크게 다음 4가지 단계를 거쳐야한다.

<br>

**1. 변하는 것이 무엇인지를 식별한다.**  

**2. 해당 변하는 부분을 클래스로 분리하여 모델링한다.**  

**3. 변하는 부분들을 포용하는 개념을 추상 클래스나 인터페이스로 **추상화** 모델링한다.**  

**4. 2번 단계에서 분리해낸 클래스를 - 3번 단계에서 추상화된 개념의 자식클래스로 모델링한다.**   

<br>
 <hr>
<br>

위의 4가지 단계를 적용하여 student 클래스를 OCP 원칙에 만족하도록 리모델링하기위한 단계는 아래와 같다.

<br>

> **1. 변하는 것 => student 클래스에서는 print 메서드의 출력형식**  

> **2. 변하는 부분인 출력형식을, xml 형식으로 성적을 출력해주는 PrintInXml 클래스 + json 형식으로 성적을 출력해주는 PrintInJson 클래스로 분리**  

>**3. PrintInXml 클래스와 PrintInJson 클래스를 포용하는 개념("출력한다")을 Printer 인터페이스로 추상화한다.**  

>**4. PrintInXml 클래스와 PrintInJson 클래스가 Printer 인터페이스를 구현하도록 모델링한다.** 
<br>

Printer 인터페이스에는 **"출력한다"** 는 기능인 print가 있고, 각 하위 클래스에서는 print메서드를 각 클래스의 출력 형식에 맞게 오버라이딩하면 될 것이다. 
즉, 출력 형식에 따라 서브 클래스로 분리한 것이다.

<br><br>

위 4가지 단계를거쳐 리모델링한 OCP를 만족하는 최종 설계는 다음과 같다. 

<br>

## OCP 원칙을 만족하는 최종설계

<div align="center">
  <img src="https://github.com/user-attachments/assets/421608d5-0597-4575-9ba1-931dfcddb4d4">
</div>

<br>

이와 같은 설계를 통해, Student 클래스는 Printer 와 연관관계를 맺고 Printer의 print 메서드를 통해서 경우에 따라 각기다른 형식으로 성적 정보 출력이 가능하다.
만약 xml - json 이외에도 또 다른 형식으로 성적정보를 출력하고싶다면, 새로운 출력 형식을 표현하는 클래스를 Print 인터페이스를 구현하는 파생 클래스로 추가하면된다.   

<div align="center">
  <img src="https://github.com/user-attachments/assets/2ef1a7e2-5ecb-4505-a0a0-074b646a801a">
</div>

<br>

기존 Studnet 클래스의 print 메서드를 변경시켜서 형식 추가하는 것이 아닌, 새로운 파생 클래스를 추가하는 방식인 것이다.  

이로써 **OCP 원칙을 만족하여 => 기존의 Student 클래스의 코드 변경없이, 다른 형태로 성적 정보를 출력하는 기능을 추가할 수 있게된다.** 

<br>

## 예제2 - 학점 출력

다음과 같이 Student 클래스에는 중간고사 점수, 기말고사 점수, 과제 점수를 넘겨받고 학점을 계산해주는 메서드 로직이 존재한다.   


```java

public class Student{

    private int midScore;
    private int finalScore;
    private int hwScore;

    public Student(int midScore, int finalScore, int hwScore) {
        this.midScore = midScore;
        this.finalScore = finalScore;
        this.hwScore = hwScore;
    }

    public Grade calculateGrade(){
        double total= 0.35*midScore+0.35*finalScore+0.3*hwScore;

        if (total >= 85) return Grade.A;
        if (total >= 75) return Grade.B;
        if (total >= 65) return Grade.C;
        if (total >= 50) return Grade.D;
        return Grade.F;
    }
}

```

그러나 이 코드 역시, 추후 교수님의 재량 및 학교 정책의 변경으로 인해 학점 계산방식이 변경될 경우 Student 클래스 내부의 calculateGrade 학점 계산 로직을 직접 수정해야하기에 OCP 를 위반하게된다. 

위에서 알아본 OCP 만족을 위한 4단계를 이 경우에 적용시켜 OCP를 만족하도록 코드를 수정해보자.


<br>

> **1. 변하는 것 => 학점 계산 로직**  

> **2. 변하는 부분인 학점 계산 로직을 클래스로 추출하여 모델링(GradeCalc1, GradeCalc2...)**  

>**3. 변화들을 포괄하는 개념을 일반화, 실체화하여 추상클래스로 모델링 (GradeCalculator 추상클래스)**  

>**4. 2단계에서 모델링한 클래스들을 실체화한 추상클래스의 하위 클래스로 설정**

<br>

먼저, 변하는 부분은 학점 계산 로직이다. 각 로직들을 별도의 클래스로 모델링하고, 이 클래스들을 포괄하는 GradeCalculator 라는 추상클래스도 모델링하여 각 클래스들이 이 추상클래스를 상속받아 서로 다른 로직들을 구현하도록하면된다. 

<br>

## OCP 만족하도록 개선한 코드

```java

public class Student{

    private int midScore;
    private int finalScore;
    private int hwScore;
    private GradeCalculator gradeCalculator; //연관관계를 통한 학점계산 위임.

    public void setGradeCalculator(GradeCalculator gradeCalculator) {
        this.gradeCalculator = gradeCalculator;
    }

    public Student(int midScore, int finalScore, int hwScore) {
        this.midScore = midScore;
        this.finalScore = finalScore;
        this.hwScore = hwScore;
    }

    public Grade calculateGrade(){
        return gradeCalculator.calculateGrade(midScore,finalScore,hwScore);
    }

}



/////////////////// 학점계산로직을 추상화한 추상클래스

public abstract class GradeCalculator {

    public abstract Grade calculateGrade(int midScore, int finalScore, int hwScore);
}



///////////////////추상클래스를 상속받은 학점계산로직 클래스1

public class GradeCalc1 extends GradeCalculator{
    //변화하는 것 = 학점 계산 로직. 이걸 클래스로 만들어냄.

        public Grade calculateGrade(int midScore, int finalScore, int hwScore){
            double total= 0.35*midScore+0.35*finalScore+0.3*hwScore;

            if (total >= 85) return Grade.A;
            if (total >= 75) return Grade.B;
            if (total >= 65) return Grade.C;
            if (total >= 50) return Grade.D;
            return Grade.F;
        }
    }



/////////////////// 추상클래스를 상속받은 학점계산로직 클래스2

public class GradeCalc2 extends GradeCalculator{
    public Grade calculateGrade(int midScore, int finalScore, int hwScore){
        //다른 학점 계산 로직. => 중간 기말 학점 비율 조정
        double total= 0.30*midScore+0.40*finalScore+0.3*hwScore;

        if (total >= 85) return Grade.A;
        if (total >= 75) return Grade.B;
        if (total >= 65) return Grade.C;
        if (total >= 50) return Grade.D;
        return Grade.F;
    }
}



///////////////////

public class Main {
    public static void main(String[] args) {

        Student s= new Student(80,90,70);
        s.setGradeCalculator(new GradeCalc2()); //Student 클래스를 손댈필요없이 학점계산로직 클래스를 별도로 모델링->객체화하여 사용. OCP 만족. 
        System.out.println(s.calculateGrade());
    }
}

```


