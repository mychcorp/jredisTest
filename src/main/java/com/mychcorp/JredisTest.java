package com.mychcorp;


import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

/**
 * Created by mych on 4/9/17.
 */



public class JredisTest {
    private Jedis jedis;

    @Before
    public void setup() {
        //连接redis服务器，192.168.0.100:6379
        jedis = new Jedis("192.168.0.100", 6379);
        //权限认证
        jedis.auth("192.168.0.100");
    }

    /**
     * redis存储字符串
     */
    @Test
    public void testString() {
        //-----添加数据----------
        Transaction multi = jedis.multi();
        multi.set("name","mychcorp");//向key-->name中放入了value
        List<Object> exec = multi.exec();
        System.out.println("--->>"+exec);
        System.out.println(jedis.get("name"));//执行结果：mychcorp
        multi = jedis.multi();
        multi.append("name", " is cool"); //拼接
        exec = multi.exec();
        System.out.println("--->>"+exec);
        System.out.println(jedis.get("name"));
        jedis.expire("name",30);//expire time
//
//        jedis.del("name");  //删除某个键
//        System.out.println(jedis.get("name"));
//        //设置多个键值对
//        jedis.mset("name","mychcorp","age","26","qq","376490270");
//        jedis.incr("age"); //进行加1操作
//        System.out.println(jedis.get("name") + "-" + jedis.get("age") + "-" + jedis.get("qq"));

    }

    /**
     * redis操作Map
     */
    @Test
    public void testMap() {
        //-----添加数据----------

        Map<String, String> map = new HashMap<String, String>();
        map.put("name", "test1");
        map.put("age", "100");
        map.put("qq", "123456");
        Transaction multi = jedis.multi();
        multi.hmset("testMap", map);
        List<Object> exec = multi.exec();
        System.out.println("--->>"+exec);
//        jedis.hmset("user",map);
        //取出user中的name，执行结果:[minxr]-->注意结果是一个泛型的List
        //第一个参数是存入redis中map对象的key，后面跟的是放入map中的对象的key，后面的key可以跟多个，是可变参数
        List<String> rsmap = jedis.hmget("testMap", "name", "age", "qq");
        System.out.println(rsmap);

        //删除map中的某个键值
        jedis.hdel("testMap","age");
        System.out.println(jedis.hmget("testMap", "age")); //因为删除了，所以返回的是null
        System.out.println(jedis.hlen("testMap")); //返回key为user的键中存放的值的个数2
        System.out.println(jedis.exists("testMap"));//是否存在key为user的记录 返回true
        System.out.println(jedis.hkeys("testMap"));//返回map对象中的所有key
        System.out.println(jedis.hvals("testMap"));//返回map对象中的所有value

        Iterator<String> iter=jedis.hkeys("testMap").iterator();
        while (iter.hasNext()){
            String key = iter.next();
            System.out.println(key+":"+jedis.hmget("testMap",key));
        }
    }
//
//    /**
//     * jedis操作List
//     */
//    @Test
//    public void testList(){
//        //开始前，先移除所有的内容
//        jedis.del("java framework");
//        System.out.println(jedis.lrange("java framework",0,-1));
//        //先向key java framework中存放三条数据
//        jedis.lpush("java framework","spring");
//        jedis.lpush("java framework","struts");
//        jedis.lpush("java framework","hibernate");
//        //再取出所有数据jedis.lrange是按范围取出，
//        // 第一个是key，第二个是起始位置，第三个是结束位置，jedis.llen获取长度 -1表示取得所有
//        System.out.println(jedis.lrange("java framework",0,-1));
//
//        jedis.del("java framework");
//        jedis.rpush("java framework","spring");
//        jedis.rpush("java framework","struts");
//        jedis.rpush("java framework","hibernate");
//        System.out.println(jedis.lrange("java framework",0,-1));
//    }
//
//    /**
//     * jedis操作Set
//     */
//    @Test
//    public void testSet(){
//        //添加
//        jedis.sadd("user","test1");
//        jedis.sadd("user","test2");
//        jedis.sadd("user","test3");
//        jedis.sadd("user","test4");
//        jedis.sadd("user","who");
//        //移除noname
//        jedis.srem("user","who");
//        System.out.println(jedis.smembers("user"));//获取所有加入的value
//        System.out.println(jedis.sismember("user", "who"));//判断 who 是否是user集合的元素
//        System.out.println(jedis.srandmember("user"));
//        System.out.println(jedis.scard("user"));//返回集合的元素个数
//    }
//
//    @Test
//    public void test() throws InterruptedException {
//        //jedis 排序
//        //注意，此处的rpush和lpush是List的操作。是一个双向链表（但从表现来看的）
//        jedis.del("a");//先清除数据，再加入数据进行测试
//        jedis.rpush("a", "1");
//        jedis.lpush("a","6");
//        jedis.lpush("a","3");
//        jedis.lpush("a","9");
//        System.out.println(jedis.lrange("a",0,-1));// [9, 3, 6, 1]
//        System.out.println(jedis.sort("a")); //[1, 3, 6, 9]  //输入排序后结果
//        System.out.println(jedis.lrange("a",0,-1));
//    }

//    @Test
//    public void testRedisPool() {
//        RedisUtil.getJedis().set("newname", "中文测试");
//        System.out.println(RedisUtil.getJedis().get("newname"));
//    }
}