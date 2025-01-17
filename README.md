
[![Maven Central](https://img.shields.io/maven-central/v/org.noear/solon.svg)](https://mvnrepository.com/search?q=g:org.noear%20AND%20solon)

` QQ交流群：22200020 ` 

# Solon rpc for java

一个微型的Java RPC开发框架。

支持jdk8+；主框架0.1mb；组合不同的插件应对不同需求；方便定制；快速开发。

* 更快、更小、更自由
* 支持注解与手动两种控制模式，自由切换
* 自带IOC & AOP容器，支持PRC、REST API、MVC开发
* 采用Handler + Context 架构；强调插件式扩展
* 统一Http、WebSocket、Socket三种信号的开发体验
* 插件可扩展可切换：启动插件，扩展插件，序列化插件，数据插件，会话状态插件，视图插件(可共存) 等...
* 使用感觉与Springboot近似，迁移成本低


### Hello world：

```java
//Handler 模式：
public class App{
    public static void main(String[] args){
        SolonApp app = Solon.start(App.class,args);
        
        app.get("/",(c)->c.output("Hello world!"));
    }
}

//Controller 模式：
@Controller
public class App{
    public static void main(String[] args){
        Solon.start(App.class,args);
    }
  
    @Mapping("/")
    public Object home(Context c){
        return "Hello world!";  
    }
}
```


### 主框架及快速集成开发包：

###### 主框架

| 组件 | 说明 |
| --- | --- |
| org.noear:solon-parent | 框架版本管理 |
| org.noear:solon | 主框架 |
| org.noear:nami | 伴生框架（做为solon rpc 的客户端）；由 Fairy 更名而来 |

###### 快速集成开发包

| 组件 | 说明 |
| --- | --- |
| org.noear:solon-rpc | 可进行rpc开发的快速集成包 |
| org.noear:solon-web | 可进行web开发的快速集成包 |

### 附1：与其它框架的异同性

#### [《Solon 特性简集，相较于 Springboot 有什么区别？》](https://my.oschina.net/noear/blog/4863844)



### 附2：示例与文章
* 项目内的：[_test](./_test/) 和 [_demo](./_demo/)
* 更多示例：[solon_demo](https://gitee.com/noear/solon_demo)、[solon_rpc_demo](https://gitee.com/noear/solon_rpc_demo)、[solon_socketd_demo](https://gitee.com/noear/solon_socketd_demo)
* 更多文章：[https://www.cnblogs.com/noear/](https://www.cnblogs.com/noear/)

### 附3：快速入门示例
* Web 示例（mvc）
```xml
<parent>
    <groupId>org.noear</groupId>
    <artifactId>solon-parent</artifactId>
    <version>1.2.20</version>
</parent>

<dependencies>
    <dependency>
        <groupId>org.noear</groupId>
        <artifactId>solon-web</artifactId>
    </dependency>
</dependencies>

```
```
//资源路径说明（不用配置）
resources/application.properties（或 application.yml） 为应用配置文件
resources/static/ 为静态文件根目标
resources/WEB-INF/view/ 为视图文件根目标（支持多视图共存）

//调试模式：
启动参数添加：-debug=1
```
```java
public class App{
    public static void main(String[] args){
        Solon.start(App.class, args);
    }
}

/*
 * mvc控制器
 */
@Controller
public class DemoController{
    //for http
    @Mapping("/hallo/{u_u}")
    public ModelAndView hallo(String u_u){
        return new ModelAndView("hallo");
    }
    
    /*
    //for web socket （需添加：solon.boot.websocket 插件）
    @Mapping(value="/hallo/{u_u}", method = MethodType.WEBSOCKET)
    public ModelAndView hallo_ws(String u_u){
        return new ModelAndView("hallo");
    }
    */
}
```

* Rpc 示例

```java
// - interface : 定义协议
public interface DemoService{
    void setName(Integer user_id, String name);
}

// - server : 实现协议
@Mapping("/demo/*")
@Component(remoting = true)
public class DemoServiceImp implements DemoService{
    public void setName(int user_id, String name){
        
    }
}

// - client - 简单示例
//注入模式
//@NamiClient("http://127.0.0.1:8080/demo/") 
//DemoService client;

//构建模式
DemoService client = Nami.builder().upstream(n->"http://127.0.0.1:8080/demo/").create(DemoService.class); 
client.setName(1,'');


```

* 获取应用配置
```java
//非注入模式
Solon.cfg().get("app_key"); //=>String
Solon.cfg().getInt("app_id",0); //=>int
Solon.cfg().getProp("xxx.datasource"); //=>Properties

//注入模式
@Configuration //or @Controller, or @Component
public class Config{
    @Inject("${app_key}")
    String app_key;
}
```

* 事务与缓存控制（+验证）
```java
@Valid
@Controller
public class DemoController{
    @Db
    BaseMapper<UserModel> userService;
    
    @NotZero("user_id")
    @CacheRemove(tags = "user_${user_id}")
    @Tran
    @Mapping("/user/update")
    public void udpUser(int user_id, UserModel user){
        userService.updateById(user);
    }

    @NotZero("user_id")
    @Cache(tags = "user_${user_id}")
    public UserModel getUser(int user_id){
        return userService.selectById(user_id);
    }
}
```

* 文件上传与输出
```java
@Controller
public class DemoController{
    @Mapping("/file/upload")
    public void upload(UploadedFile file){
        IoUtil.save(file.content, "/data/file_" + file.name);
    }

    @Mapping("/file/down")
    public void down(Context ctx, String path){
        URL uri = Utils.getResource(path);

        ctx.contentType("json/text");
        ctx.output(uri.openStream());
    }
}
```

* Servlet 注解支持
```java
@WebFilter("/hello/*")
public class HelloFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        servletResponse.getWriter().write("Hello，我把你过滤了");
    }
}
```

* Quartz 定时任务
```java
@Quartz(cron7x = "0 0/1 * * * ? *")
public class HelloTask implements Runnable {
    public static void main(String[] args){
        Solon.start(QuartzRun2.class,args);
    }
    
    @Override
    public void run() {
        System.out.println("Hello world");
    }
}
```

* 体外扩展加载 jar 
```
demoApp.jar             #主程序
ext/                    #扩展目录
ext/ext.markdown.jar    #MD格式支持扩展包
```

* 单链接双向RPC（客户端链上服务端之后，形成双向RPC）
```java 
//server
@Mapping(value = "/demoh/rpc", method = MethodType.SOCKET)
@Component(remoting = true)
public class HelloRpcServiceImpl implements HelloRpcService {
    public String hello(String name) {
        //此处，可以根据 client session 创建一个连接 client 的 rpc service
        NameRpcService rpc = SocketD.create(Context.current(), NameRpcService.class);

        String name2 = rpc.name(name);

        return "name=" + name;
    }
}

//client
HelloRpcService rpc = SocketD.create("tcp://localhost:"+_port, HelloRpcService.class);

String rst = rpc.hello("noear");
```


### 附4：插件开发说明
* 新建一个 maven 项目
* 新建一个 java/{包名}/XPluginImp.java （implements XPlugin）
* 新建一个 resources/META-INF/solon/{包名.properties}
*    添加配置：solon.plugin={包名}.XPluginImp

### 附5：启动顺序参考
* 1.实例化 Solon.global() 并加载配置
* 2.加载扩展文件夹
* 3.扫描插件
* 4.运行builder函数
* 5.运行插件
* 6.扫描并加载java bean
* 7.加载渲染关系
* 8.完成

### 附6：Helloworld 的单机并发数 [《helloworld_wrk_test》](https://gitee.com/noear/helloworld_wrk_test)

> * 机器：2017 macbook pro 13, i7, 16g, MacOS 10.15, jdk11
> * 测试：wrk -t10 -c200 -d30s --latency "http://127.0.0.1:8080/"

|  solon 1.1.2 | 大小 | QPS | 
| -------- | -------- | -------- | 
| solon.boot.jlhttp(bio)     | 0.1m     | 4.7万左右     |
| solon.boot.jetty(nio, 支持servlet api)     | 1.8m     | 10.7万左右     | 
| solon.boot.undertow(nio, 支持servlet api)     | 4.2m     | 11.3万左右     | 
| solon.boot.smarthttp(aio)     | 0.3m     | 12.4万左右     | 


| spring boot 2.3.3  | 大小 |  QPS  | 
| -------- | -------- | -------- |
| spring-boot-starter-tomcat   | 16.1m |  3.2万左右  | 
| spring-boot-starter-jetty | 16m | 3.7万左右 |
| spring-boot-starter-undertow | 16.8m | 4.4万左右 |
