## 关于neo4j
#### spring boot控制台报cypher语法错误
spring集成的是spring-data-neo4j@5.0.8.RELEASE，这应该是因为neo4j版本太低，我换成3.4.3版本后解决了该问题
#### Store and its lock file has been locked by another process ...
如果我们的Neo4j服务器通过引用我们新创建的数据库启动和运行，那么我们不能执行我们的程序，因为服务器已经锁定了这个数据库。因为默认情况下Neo4j DB Server一次只接受一个锁。 在实时应用程序中，Ne04J DBA人员将更新数据库属性以允许一次允许一些数量的锁
* 重启服务器可以解决问题
* 手动杀死锁定db的进程
    * ```ps -aux | grep neo4j```获取进程号，注意root用户创建的才是neo4j的进程
    * ```sudo kill 进程号```
#### neo4j查询某个节点返回List时，其关联节点也被包含在返回结果中了

## mysql
#### 允许表输入中文
在创建表时在末尾加上```set default charset=utf8```，如
> create table(...) set default charset=utf8

## net.sf.json.JSONObject
#### 1.假设调用JSONObject.fromObject序列化一个对象，该对象中包含对另一个可序列化对象的引用，则这两个对象的所有属性都会被写入json字符串中。对不想序列化的字段加上transient修饰符可以解决这个问题。
但是新问题来了，Pig的fatherId和motherId字段是要和pig一起传给客户端的，原来的father和mother字段用transient修饰后已经不会再传给客户端了。
* 考虑在对象序列化的时候手动往JSONObject里put这两个字段，但是这要求自己实现一个JSON工具
#### 2 JSONException: null object
<pre><code>
JSONObject jsonObj = new JSONObject();
root.put("xxx", jsonObj);
</code></pre>
见: org.luncert.view.component.implement.ConfigManagerImpl#setProperty
这里会直接报错，因为jsonObj为“空”，这是这个包定义的“空”，即这个jsonObj内没有任何键值对。所以只有有键值对的jsonObj才能内嵌到另一个jsonObj中。

## 静态资源配置
* https://blog.csdn.net/difffate/article/details/78002995
* https://blog.csdn.net/qq_36481052/article/details/79075214
* 为什么spring boot默认静态资源配置不起作用：https://blog.csdn.net/leegoowang/article/details/78591086
* spring boot通用配置方法是重写addResourceHandlers方法，但是注意了，只有config类继承WebMvcConfigurerAdapter这个方法才有用，继承WebMvcConfigurationSupport是不行的，这个方法不会被调用。。。天坑，然而到现在的版本WebMvcConfigurerAdapter已经被抛弃了。结果我TM直接实现WebMvcConfigurer接口然后重写这个方法就行了。。。
<pre><code>@Override
public void addResourceHandlers(ResourceHandlerRegistry registry) {
    registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
    super.addResourceHandlers(registry);
}
</code></pre>

## maven内置属性
* https://www.cnblogs.com/king1302217/p/5829672.html

## spring boot乱码:
* https://blog.csdn.net/wangshuang1631/article/details/70753801
* https://www.jianshu.com/p/333ed5ee958d
* https://blog.csdn.net/qq_15071263/article/details/80248805
* https://blog.csdn.net/userrefister/article/details/73201344
* @RequestMapping(value = "pig", produces={"application/json;charset=UTF-8"})
* StringHttpMessageConverter: 我尝试了自己实现AbstractHttpMessageConverter，然而发现返回数据的时候我自己的类并没有被使用？？spring框架数据convert这块了解一下？
 

## spring boot基于注解获取应用配置文件内容: https://blog.csdn.net/u010005147/article/details/79720172

## default关键字了解一下？