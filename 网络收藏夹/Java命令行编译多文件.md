参考http://stackoverflow.com/questions/6665420/package-does-not-exist-error

习惯了eclipse的自动编译，Java命令行编译、执行文件只会最基础的部分，就是对单文件的编译和执行，而且不包含任何外部JAR包。

####**单文件编译**
我们先讲简单文件编译，现在有个Hello.java文件,放在桌面
```
public class  Hello {
	public static void main( String[] args ){
		System.out.println("Hello World");
	}
}
```
命令行定位到桌面目录，用命令
```
javac Hello.java
```
进行编译，在用命令
```
java Hello
```
运行Java文件，能够正常运行Java文件，命令行中打出文字
Hello World

####**工程多文件编译**
第一种情况一般是在安装Java环境时用来测试是否环境搭建正确，在实际应用中几乎不太会用到，一般工程都会对代码进行分层，对常见的就是MVC架构，例如分三个包名`com.example.view`,`com.example.controller`,`com.example.model`