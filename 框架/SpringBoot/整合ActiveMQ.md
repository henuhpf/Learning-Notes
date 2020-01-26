#### SpringBoot 整合 ActiveMQ

---

- 开启连接池，启动项目会报错，提示JmsMessagingTemplate无法注入
  - 使用springboot2.0+及以下版本时候，maven配置依赖是：
    
    ```java
    <dependency>
    	<groupId>org.apache.activemq</groupId>
    	<artifactId>activemq-pool</artifactId>
    </dependency>
    ```
    
    
    
  - 使用springboot2.1+时候，maven配置依赖是：
    
    ```java
    <dependency>
        <groupId>org.messaginghub</groupId>
        <artifactId>pooled-jms</artifactId>
    </dependency>
    ```
    
  - 2.0+以下版本使用的是PooledConnectionFactory，它存在于org.apache.activemq.pool.PooledConnectionFactory
  
  - 2.1+版本使用的是JmsPoolConnectionFactory，它存在于org.messaginghub.pooled.jms.JmsPoolConnectionFactory
  
- ```java
  spring:
    activemq:
      user: admin
      password: admin
      broker-url: tcp://106.14.139.216:61616
      #true 表示使用内置的MQ，false则连接服务器
      in-memory: false
      pool:
        #true表示使用连接池；false时，每发送一条数据创建一个连接
        enabled: true
        max-connections: 50
      packages:
        #是否信任所有包，信任后所有包内的对象均可序列化传输
        trust-all: true
  ```
  
- 