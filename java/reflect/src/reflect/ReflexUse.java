package reflect;

import base.Animal;
import base.HumanBehavior;
import base.Person;


import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;
import java.util.Arrays;

/**
 * @author 黄智扬
 * @describe
 * @date 2022/9/8 下午4:24
 */
public class ReflexUse
{

    public static void main(String[] args) {
        classDeal();
        instantiation();
        fieldDeal();
        methodDeal();
        proxyDeal();
    }

    public static void classDeal()
    {
        //获取class对象方式1
        Class<?> clazz;
        clazz = Person.class;

        //获取class对象方式2
        try
        {
            clazz = Class.forName("base.Person");
        }
        catch (ClassNotFoundException e)
        {
            e.printStackTrace();
        }

        //获取class对象方式3
        Object person = new Person();
        clazz = person.getClass();

        //获取类相关信息
        System.out.println("包名+类名为： "+clazz.getName());
        System.out.println("类名为： "+clazz.getSimpleName());
        System.out.println("包名为： "+clazz.getPackage());
        System.out.println("父类为: " + clazz.getSuperclass());
        System.out.println("是否为内部类: " + clazz.isAnonymousClass());
        System.out.println("是否为Animal类的子类: " + Animal.class.isAssignableFrom(clazz));
        System.out.println("是否为抽象类: " + Modifier.isAbstract(clazz.getModifiers()));
        System.out.println("是否为接口: " + clazz.isInterface());
    }

    /**
     * 获取class的构造函数，并实例化
     */
    public static void instantiation()
    {
        Class<?> clazz = Person.class;
        Person person;
        System.out.println("------------调用无参构造方法进行实例化---------------");
        try
        {
            person = (Person) clazz.newInstance();//调用该类的无参构造方法
            System.out.println(person.toString());
        }
        catch (IllegalAccessException | InstantiationException e)
        {
            e.printStackTrace();
        }

        System.out.println("------------调用有参构造方法进行实例化---------------");
        try
        {
            Constructor<?> constructor = clazz.getConstructor(String.class, int.class);
            person = (Person) constructor.newInstance("张三", 14);
            System.out.println(person.toString());
        }
        catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e)
        {
            e.printStackTrace();
        }


        System.out.println("------------获取所有构造方法---------------");
        for (Constructor<?> constructor : clazz.getConstructors())
        {
            System.out.println("构造方法打印： " + constructor.toString());
            System.out.println("输入参数为：");
            for (Class<?> parameterType : constructor.getParameterTypes())
            {
                System.out.println(parameterType);
            }
        }
    }

    /**
     * 获取class中属性，并获取和设置其值
     */
    public static void fieldDeal()
    {
        Class<?> clazz = Person.class;
        try
        {
            System.out.println("----------------------反射获取和修改public属性----------------------");
            Person person = new Person();
            Field field = clazz.getField("age");
            System.out.println("通过反射得到age: " + field.get(person));
            person.age = 20;
            System.out.println("通过反射得到age: " + field.get(person));

            field.set(person, 30);
            System.out.println(person.age);
        }
        catch (NoSuchFieldException | IllegalAccessException e)
        {
            e.printStackTrace();
        }

        System.out.println("----------------------反射获取和修改private属性----------------------");
        try
        {
            Person person = new Person();
            Field field = clazz.getDeclaredField("name");
            field.setAccessible(true);
            System.out.println("通过反射得到age: " + field.get(person));

            field.set(person, "Any");
            System.out.println(person.getName());
        }
        catch (NoSuchFieldException | IllegalAccessException e)
        {
            e.printStackTrace();
        }

        System.out.println("----------------------反射获取和修改静态属性----------------------");
        try
        {
            Field field = clazz.getDeclaredField("maxAge");
            field.setAccessible(true);
            System.out.println("通过反射得到age: " + field.get(null));//因为是静态属性，所以不用传入实例化对象

            field.set(null, 199);
            System.out.println("通过反射得到age: " + field.get(null));//因为是静态属性，所以不用传入实例化对象
        }
        catch (NoSuchFieldException | IllegalAccessException e)
        {
            e.printStackTrace();
        }

        System.out.println("----------------------反射获取获取所有属性----------------------");
        Field[] declaredFields = clazz.getDeclaredFields();
        Person person = new Person();
        for (Field declaredField : declaredFields)
        {
            try
            {
                declaredField.setAccessible(true);
                System.out.println("属性名为： " + declaredField.getName() + ", 属性类名为： " + declaredField.getType().getName() + ", 值为:" + declaredField.get(person));
            }
            catch (IllegalAccessException e)
            {
                e.printStackTrace();
            }
        }
    }

    public static void methodDeal()
    {
        Class<?> clazz = Person.class;
        System.out.println("----------------------反射调用public方法----------------------");
        try
        {
            Method say = clazz.getMethod("say", String.class);
            Person person = new Person("Any", 32);
            say.invoke(person, "lalala");
        }
        catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e)
        {
            e.printStackTrace();
        }

        System.out.println("----------------------反射调用private方法----------------------");
        try
        {
            Method work = clazz.getDeclaredMethod("work");
            work.setAccessible(true);
            Person person = new Person("Any", 32);
            work.invoke(person);
        }
        catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e)
        {
            e.printStackTrace();
        }

        System.out.println("----------------------反射调用静态方法----------------------");
        try
        {
            Method say = clazz.getDeclaredMethod("getMaxAge");
            say.setAccessible(true);
            Object value = say.invoke(null);//因为是静态方法，所以不用传入实例化对象
            System.out.println("getMaxAge返回值为：" + value);
        }
        catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e)
        {
            e.printStackTrace();
        }

        System.out.println("----------------------反射调用所有方法----------------------");
        Person person = new Person();
        Method[] declaredMethods = clazz.getDeclaredMethods();
        for (Method declaredMethod : declaredMethods)
        {
            Class<?>[] parameterTypes = declaredMethod.getParameterTypes();
            try
            {
                declaredMethod.setAccessible(true);
                Object result;
                if (parameterTypes.length == 0)
                {
                    result = declaredMethod.invoke(person);
                }
                else
                {
                    result = declaredMethod.invoke(person, "hi!");
                }
                System.out.println("方法名为： " + declaredMethod.getName() + ", 输入参数类型为：" + Arrays.toString(parameterTypes) + ", 返回值为： " + result);
            }
            catch (IllegalAccessException | InvocationTargetException e)
            {
                e.printStackTrace();
            }
        }
    }


    public static void proxyDeal()
    {
        Person person = new Person();
        HumanBehavior humanBehavior = (HumanBehavior) Proxy.newProxyInstance(((Object)person).getClass().getClassLoader(), ((Object)person).getClass().getInterfaces(), new InvocationHandler()
        {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable
            {
                System.out.println("---start----");
                return method.invoke(person, args);
            }
        });
        humanBehavior.write("ssss");
        humanBehavior.play();
    }


}
