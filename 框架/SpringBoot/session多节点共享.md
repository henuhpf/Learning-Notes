#### session多节点共享

----

- spring session 共享的实现原理
1. 用户第一次请求的时候，服务端生成SESSIONID，返回浏览器端。浏览器以cookies的形式保存起来
2. 同一IP(域名)，不同端口，在同一个浏览器cookies是共享的。用户访问不同IP(域名)的Cookies，在同一个浏览器一定是不一样的。对于这种情况需要在集群应用的前面加上负载均衡，如：nginx，haproxy。
3. SESSION正常是由Servlet容器来维护的，这样SESSION就无法共享。如果希望Session共享，就需要把session的存储放到一个统一的地方，如：redis。维护交给Spring session。
4. 除了Cookies可以维持Sessionid，Spring Session还提供了了另外一种方式，就是使用header传递SESSIONID。目的是为了方便类似于手机这种没有cookies的客户端进行session共享。

- 集成 Spring Session

1. 引入spring-session-redis的maven依赖

```java
<dependency>
     <groupId>org.springframework.session</groupId>
     <artifactId>spring-session-data-redis</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-redis</artifactId>
</dependency>
```

2. 配置启用Redis的httpSession

在启动类上方加上注解

```java
@EnableRedisHttpSession(maxInactiveIntervalInSeconds = 30 * 60 * 1000)
```

3. 配置redis链接信息(application.yml)

```java
spring:

    redis:
      database: 0
      host: 106.14.139.216
      password: 4rfv$RFV
      port: 6379
```

4. 测试

在两个项目里面都做上面的操作,并加入如下的测试代码:

```java
@RequestMapping(value="/uid",method = RequestMethod.GET)
public @ResponseBody String uid(HttpSession session) {
    UUID uid = (UUID) session.getAttribute("uid");
    if (uid == null) {
        uid = UUID.randomUUID();
    }
    session.setAttribute("uid", uid);
    return session.getId();
}
```
