#### 什么是Mock?

在面向对象程序设计中，模拟对象（英语：mock object，也译作模仿对象）是以可控的方式模拟真实对象行为的**假的对象**。比如:对象B依赖于对象A，但是A代码还没写是一个空类空方法不能用，我们来mock一个假的A来完成测试。

#### 为什么要使用Mock?

> 在单元测试中，模拟对象可以模拟复杂的、真实的对象的行为， 如果真实的对象无法放入单元测试中，使用模拟对象就很有帮助。

在下面的情形，可能需要使用模拟对象来代替真实对象：

- 真实对象的行为是不确定的（例如，当前的时间或当前的温度）；
- 真实对象很难搭建起来；
- 真实对象的行为很难触发（例如，网络错误）；
- 真实对象速度很慢（例如，一个完整的数据库，在测试之前可能需要初始化）；
- 真实的对象是用户界面，或包括用户界面在内；
- 真实的对象使用了回调机制；
- 真实对象可能还不存在（例如，其他程序员还为完成工作）；
- 真实对象可能包含不能用作测试的信息（高度保密信息等）和方法。

#### Mockito

Mockito是GitHub上使用最广泛的Mock框架,并与JUnit结合使用.Mockito框架可以创建和配置mock对象.使用Mockito简化了具有外部依赖的类的测试开发!

```Java
@Data
public class User { 
    private Long id; 
    private String name; 
    private Integer age;      
}
 
@RestController 
@RequestMapping(value="/users")     // 通过这里配置使下面的映射都在/users下 
public class UserController { 
 
    // 创建线程安全的Map 
    static Map<Long, User> users = Collections.synchronizedMap(new HashMap<Long, User>()); 
 
    @RequestMapping(value="/", method=RequestMethod.GET) 
    public List<User> getUserList() { 
        // 处理"/users/"的GET请求，用来获取用户列表 
        // 还可以通过@RequestParam从页面中传递参数来进行查询条件或者翻页信息的传递 
        List<User> r = new ArrayList<User>(users.values()); 
        return r; 
    } 
 
    @RequestMapping(value="/", method=RequestMethod.POST) 
    public String postUser(@ModelAttribute User user) { 
        // 处理"/users/"的POST请求，用来创建User 
        // 除了@ModelAttribute绑定参数之外，还可以通过@RequestParam从页面中传递参数 
        users.put(user.getId(), user); 
        return "success"; 
    } 
 
    @RequestMapping(value="/{id}", method=RequestMethod.GET) 
    public User getUser(@PathVariable Long id) { 
        // 处理"/users/{id}"的GET请求，用来获取url中id值的User信息 
        // url中的id可通过@PathVariable绑定到函数的参数中 
        return users.get(id); 
    } 
 
    @RequestMapping(value="/{id}", method=RequestMethod.PUT) 
    public String putUser(@PathVariable Long id, @ModelAttribute User user) { 
        // 处理"/users/{id}"的PUT请求，用来更新User信息 
        User u = users.get(id); 
        u.setName(user.getName()); 
        u.setAge(user.getAge()); 
        users.put(id, u); 
        return "success"; 
    } 
 
    @RequestMapping(value="/{id}", method=RequestMethod.DELETE) 
    public String deleteUser(@PathVariable Long id) { 
        // 处理"/users/{id}"的DELETE请求，用来删除User 
        users.remove(id); 
        return "success"; 
    } 
 
}
 
@RunWith(SpringJUnit4ClassRunner.class) 
@SpringApplicationConfiguration(classes = MockServletContext.class) 
@WebAppConfiguration 
public class ApplicationTests { 
 
	private MockMvc mvc; 
 
	@Before 
	public void setUp() throws Exception { 
		mvc = MockMvcBuilders.standaloneSetup(new UserController()).build(); 
	} 
 
	@Test 
	public void testUserController() throws Exception { 
        // 测试UserController 
		RequestBuilder request = null; 
 
		// 1、get查一下user列表，应该为空 
		request = get("/users/"); 
		mvc.perform(request) 
				.andExpect(status().isOk()) 
				.andExpect(content().string(equalTo("[]"))); 
 
		// 2、post提交一个user 
		request = post("/users/") 
				.param("id", "1") 
				.param("name", "测试大师") 
				.param("age", "20"); 
		mvc.perform(request) 
		        .andExpect(content().string(equalTo("success"))); 
 
		// 3、get获取user列表，应该有刚才插入的数据 
		request = get("/users/"); 
		mvc.perform(request) 
				.andExpect(status().isOk()) 
				.andExpect(content().string(equalTo("[{\"id\":1,\"name\":\"测试大师\",\"age\":20}]"))); 
 
		// 4、put修改id为1的user 
		request = put("/users/1") 
				.param("name", "测试终极大师") 
				.param("age", "30"); 
		mvc.perform(request) 
				.andExpect(content().string(equalTo("success"))); 
 
		// 5、get一个id为1的user 
		request = get("/users/1"); 
		mvc.perform(request) 
				.andExpect(content().string(equalTo("{\"id\":1,\"name\":\"测试终极大师\",\"age\":30}"))); 
 
		// 6、del删除id为1的user 
		request = delete("/users/1"); 
		mvc.perform(request) 
				.andExpect(content().string(equalTo("success"))); 
 
		// 7、get查一下user列表，应该为空 
		request = get("/users/"); 
		mvc.perform(request) 
				.andExpect(status().isOk()) 
				.andExpect(content().string(equalTo("[]"))); 
 
	} 
 
}
```


#### MockMvc对象有以下几个基本的方法:

- perform : 执行一个RequestBuilder请求，会自动执行SpringMVC的流程并映射到相应的控制器Controller执行处理。
- contentType：发送请求内容的序列化的格式，"application/json"表示JSON数据格式
- andExpect: 添加RequsetMatcher验证规则，验证控制器执行完成后结果是否正确，或者说是结果是否与我们期望（Expect）的一致。
- andDo: 添加ResultHandler结果处理器，比如调试时打印结果到控制台
- andReturn: 最后返回相应的MvcResult,然后进行自定义验证/进行下一步的异步处理



#### @SpringBootTest与@WebMvcTest区别

- @SpringBootTest注解告诉SpringBoot去寻找一个主配置类(例如带有@SpringBootApplication的配置类)，并使用它来启动Spring应用程序上下文。SpringBootTest加载完整的应用程序并注入所有可能的bean，因此速度会很慢。
- @WebMvcTest注解主要用于controller层测试，只覆盖应用程序的controller层，HTTP请求和响应是Mock出来的，因此不会创建真正的网络连接。WebMvcTest要快得多，因为我们只加载了应用程序的一小部分。

#### @MockBean

如果我们使用了WebMvcTest，只加载了Controller层的bean，那么Controller所依赖的Service没有被加载进来怎么办？ 我们可以用MockBean伪造模拟一个Service 。如下打桩的代码

```java
@RunWith(SpringRunner.class)
@WebMvcTest(ArticleRestController.class)
public class WebMockTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ArticleRestService service;

    @Test
    public void saveArticle() throws Exception {
        String article = "{\n" +
                "    \"id\": 1,\n";
    
    
        ObjectMapper objectMapper = new ObjectMapper();
        Article articleObj = objectMapper.readValue(article,Article.class);
    
        //打桩
        when(articleRestService.saveArticle(articleObj)).thenReturn("ok");
    
    
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.request(HttpMethod.POST, "/rest/article")
                .contentType("application/json").content(article))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print())
                .andReturn();
    
        log.info(result.getResponse().getContentAsString());
    
    }
}
```

也就是告诉测试用例程序，当你调用articleRestService.saveArticle(articleObj)方法的时候，不要去真的调用这个方法，直接返回一个结果（“ok”）就好了。articleRestService就是那个不能被测试或者不能测试的类，需要我们来模拟Mock一下。



```java
//模拟GET请求：
mockMvc.perform(MockMvcRequestBuilders.get("/user/{id}", userId));

//模拟Post请求：
mockMvc.perform(MockMvcRequestBuilders.post("uri", parameters));

//模拟文件上传：
mockMvc.perform(MockMvcRequestBuilders.multipart("uri").file("fileName", "file".getBytes("UTF-8")));


//模拟session和cookie：
mockMvc.perform(MockMvcRequestBuilders.get("uri").sessionAttr("name", "value"));
mockMvc.perform(MockMvcRequestBuilders.get("uri").cookie(new Cookie("name", "value")));

//设置HTTP Header：
mockMvc.perform(MockMvcRequestBuilders
                        .get("uri", parameters)
                        .contentType("application/x-www-form-urlencoded")
                        .accept("application/json")
                        .header("", ""));
```

