# wang-smile-common
> `code-generator`: 代码生成工具
> 
> `smile-common`: 常用工具包
>
> `corder-sample`: 代码生成工具案例

------

### 代码生成工具的使用方法：
##### 1, 将`wang-smile-common`下载到本地，通过`Maven`进行`install`操作(将项目打包并将jar包安装到本地仓库)
##### 2, 项目的`pom.xml`文件中引入相关`jar`包, 添加以下依赖：
```xml
<!--工具包-->
<dependency>
    <artifactId>smile-common</artifactId>
    <groupId>wang.smile.common</groupId>
    <version>0.0.1-SNAPSHOT</version>
</dependency>
<!--代码生成包-->
<dependency>
    <artifactId>code-generator</artifactId>
    <groupId>wang.smile.common</groupId>
    <version>0.0.1-SNAPSHOT</version>
    <scope>test</scope>
</dependency>
```

##### 3, 代码摸板以及相关数据库配置都放在`test`目录中进行，具体代码及配置参考`corder-sample`项目中的使用方法

![案例](https://github.com/smilewangsy/wang-smile-common/blob/master/20180805201915.png)