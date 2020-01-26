#### war包部署到外置Tomcat容器

----
-  排除内置Tomcat依赖

```java
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
    <exclusions>
        <exclusion>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-tomcat</artifactId>
        </exclusion>
    </exclusions>
</dependency>
```

- 新增类继承 SpringBootServletInitializer 实现 configure(Note that a WebApplicationInitializer is only needed if you are building a war file and deploying it.
  If you prefer to run an embedded web server then you won't need this at all.)

```java
public class ServletInitializer extends SpringBootServletInitializer { 
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        //此处的Application.class为带有@SpringBootApplication注解的启动类
        return builder.sources(DemoApplication.class);
    } 
}
```

- 注意事项：使用外部Tomcat部署访问的时候，application.properties(或者application.yml)中的如下配置将失效，请**使用外置的tomcat的端口，tomcat的webapps下项目名进行访问**。

- build要有finalName标签,pom.xml中的构建build代码段，要有应用最终构建打包的名称。

```
 <finalName>boot-launch</finalName>
```

- war方式打包，打包结果将存储在项目的target目录下面。然后将war包部署到外置Tomcat上面：

```
mvn clean package -Dmaven.test.skip=true
```