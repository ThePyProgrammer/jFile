# jFile

jFile, made by Prannaya Gupta.

## A Java File Wrapper based on the Python ```TextIOWrapper```
### Python ```TextIOWrapper```
```python
>>> open("hello.txt", 'w+')
<_io.TextIOWrapper name='hello.txt' mode='w+' encoding='cp1252'>
>>> open("hello.txt", 'r')
<_io.TextIOWrapper name='hello.txt' mode='r' encoding='cp1252'>
>>> file = open("hello.txt", 'r')
>>> file.read()
''
>>> file.readlines()
[]
>>> file.readline()
''
>>> file.close()
>>> file = open("hello.txt", 'w+')
>>> file.write("What is jFile? jFile is a file IO wrapper.\nThis is made on Java and is built upon built-in class java.io.File.")
110
>>> file.close()
>>> file = open("hello.txt", 'r')
>>> file.read()
'What is jFile? jFile is a file IO wrapper.\nThis is made on Java and is built upon built-in class java.io.File.'
>>> file.readlines()
[]
>>> file.readline()
''
>>> file.close()
>>> file = open("hello.txt", 'r')
>>> file.readlines()
['What is jFile? jFile is a file IO wrapper.\n', 'This is made on Java and is built upon built-in class java.io.File.']
>>> file.close()
>>> file = open("hello.txt", 'r')
>>> file.readline()
'What is jFile? jFile is a file IO wrapper.\n'
>>> file.readline()
'This is made on Java and is built upon built-in class java.io.File.'
>>> file.close()
```
This is the ```TextIOWrapper``` in the Python Programming Language.

### The Java ```File``` Wrapper
I have made this very similar wrapper in the Java Programming Language.

```java
java.io.File init = new java.io.File("hello.txt");
init.createNewFile();
init.close();
File file = new File("hello.txt");
String read = file.read(), firstLine = file.readLine(), secondLine = file.in.nextLine();
String[] allLines = file.readLines();
file.close();
File file = new File("hello.txt", 'w');
file.out.print("What is jFile? jFile is a file IO wrapper.\nThis is made on Java and is built upon built-in class java.io.File.");
String read = file.read(), firstLine = file.readLine(), secondLine = file.in.nextLine();
String[] allLines = file.readLines();
file.close();
```

## A Powerful File IO Wrapper
Based entirely on the built-in Java Classes ```java.io.File```, ```java.io.PrintWriter``` and ```java.util.Scanner```, this is an all inclusive class that can read almost any filetype that Java itself can read, which is almost every type of file. This is a simple way of reading and writing in a more seamless way than these separate classes, as is shown in the following code:
```java
java.io.File init = new java.io.File("hello.txt")
init.createNewFile()
init.close()
java.io.File file = new java.io.File("hello.txt")
java.util.Scanner sc = new java.util.Scanner(file);
String read = "";
while(sc.hasNext()) read += sc.nextLine();
sc.close();
sc = new java.util.Scanner(file);
String firstLine = sc.nextLine();
String secondLine = sc.nextLine();
sc.close();
sc = new java.util.Scanner(file);
ArrayList<String> arr = new ArrayList<>();
while(sc.hasNext()) arr.add(sc.nextLine());
String[] allLines = new String[arr.size()];
arr.toArray(allLines);
sc.close();
java.io.FileOutputStream stream = new FileOutputStream("hello.txt");
java.io.PrintWriter pw = new java.io.PrintWriter(stream);
pw.print("What is jFile? jFile is a file IO wrapper.\nThis is made on Java and is built upon built-in class java.io.File.");
pw.close();
java.util.Scanner sc = new java.util.Scanner(file);
String read = "";
while(sc.hasNext()) read += sc.nextLine();
sc.close();
sc = new java.util.Scanner(file);
String firstLine = sc.nextLine();
String secondLine = sc.nextLine();
sc.close();
sc = new java.util.Scanner(file);
ArrayList<String> arr = new ArrayList<>();
while(sc.hasNext()) arr.add(sc.nextLine());
String[] allLines = new String[arr.size()];
arr.toArray(allLines);
sc.close();
```
## A IO Wrapper that uses many other APIs available
### ```java.net.URI```
This code also employs some simple tactics from ```java.net.URI``` that can allow relativizing of paths.

### ```Base``` Class
This is a custom interface I have created to add Math functions, Random functions and custom I/O functions. It's quite a simple program aimed at adding static and default methods to each class. TL;DR, add a line ```implements Base``` to access all these methods. To explain ```Base```, just read the following code:

```java
class Trigo implements Base {
  public static void main(String[] args) {
    print("Hello World", 69, "Cool");
    print(sin(pi/3));
    print(cos(toRadians(randInt(-180, 180)));
    print(acos(randDouble(-1, 1)));
    new Trigo().print();
  }
  
  public String toString() { return "Trigo is cool"; }
}
```

This code replaces the following:

```java
class Trigo implements Base {
  public static void main(String[] args) {
    System.out.print("Hello World");
    System.out.print(69);
    System.out.print("Cool");
    System.out.print(Math.sin(Math.PI/3));
    System.out.print(Math.cos(Math.toRadians(-180 + new Random().nextInt(360)));
    System.out.print(Math.acos(-1 + 2*Math.random()));
    System.out.print(new Trigo().toString());
  }
  
  public String toString() { return "Trigo is cool"; }
}
```

While this may not be a very significant improvement, this class provides seamless functions instead of having to rely on static methods from the Math and Random classes.

### Integration of JavaFX
This class heavily uses [JavaFX 14](https://openjfx.io/), with the introduction of file getter methods to open file dialogs. This is immensely shown with the filetypes freely available in this, as follows:
* PNG
* JPG
* JFIF
* GIF
* ICO
* PDF
* HTML
* XHTML
* PHTML
* XML
* PHP
* CSS
* JavaScript
* JSON
* Java
* Kotlin
* Scala
* Python
* Ruby
* C
* CPP
* Arduino
* Visual Basic
* Golang
* Rust
* Swift
* Text
* Markdown
* Readme
* BibTeX
* LaTeX
* SQL

These are all FileChooser.ExtensionFilters builtin as public and static values.

```java
Stage stage = new Stage();
File pngFile = File.getFile(stage, File.PNG);
File pdfFile = File.getPDF(stage);
File[] htmlFiles = File.getHTMLs();
```

There are also functionalities to access FileChooser dialogs with only certain extension filters.
You can download JavaFX from here: https://gluonhq.com/products/javafx/


## Using this Wrapper

I have include a class file decompiled by JDK 13.0.1 and the actual Java file as well. ```File.java``` is the main Java file which you put in your project, preferably in the **model** section if you are using the MVC architecture. If you're simply testing, you can simply place ```File.class``` in there. That will also help you quite a bit. Remember to implement ```Base.java``` and ```Base.class``` likewise, otherwise the ```File``` class will not work. 
