#### Jasypt加密配置文件

----
- <https://github.com/ulisesbocchio/jasypt-spring-boot>
- 在配置文件上传至GitHub等仓库时，如果配置文件中含有隐秘信息，则需要加密处理


- 首先引入Jasypt的maven坐标

  ```java
  <dependency>
      <groupId>com.github.ulisesbocchio</groupId>
      <artifactId>jasypt-spring-boot-starter</artifactId>
      <version>2.1.2</version>
  </dependency>
  ```

- 配置秘钥

    - properties 或 yml中配置
      ```java
      # 配置文件加密秘钥，不保险
      jasypt:
        encryptor:
          password: demo
      ```
    
    -  命令行存储方式
    
      ```java
      java -jar xxx.jar --jasypt.encryptor.password=xxx &;
      ```
    
    -   环境变量存储方式
    
       设置环境变量：
    
       ```java
       # 打开/etc/profile文件
       vim /etc/profile
       # 文件末尾插入
       export JASYPT_PASSWORD = xxxx
       # 执行该配置
       source /etc/profile  
       # 启动命令：
       java -jar xxx.jar --jasypt.encryptor.password=${JASYPT_PASSWORD} &;
       ```
    
- 生成密文
```java
@RunWith(SpringRunner.class)
@SpringBootTest
public class BlogApplicationTests {

    @Autowired
    StringEncryptor stringEncryptor;

    @Test
    public void test() {
        // 加密
        System.out.println("password:" + stringEncryptor.encrypt("yourpassword"));
        //解密
        System.out.println(stringEncryptor.decrypt("6eaMh/RX5oXUVca9ignvtg=="));
    }
}
```

- 在配置文件中将隐私信息例如 数据库相关信息 使用 ***ENC(密文)*** 替换
```java
spring.datasource.password=ENC(6eaMh/RX5oXUVca9ignvtg==)
```