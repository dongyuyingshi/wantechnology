package base;

public class Person extends Animal implements HumanBehavior
{
    private static int maxAge = 200;

    //私有属性
    private String name = "Tom";
    //公有属性
    public int age = 18;
    //构造方法

    public Person()
    {
        System.out.println("无参构造方法");
    }

    public Person(String name, int age)
    {
        this.name = name;
        this.age = age;
        System.out.println("有参构造方法: age: " + age + " name: " + name);
    }

    //公有方法
    @Override
    public void say(String content)
    {
        System.out.println(name + " say: " + content);
    }

    //私有方法
    private void work()
    {
        System.out.println(name + " working");
    }

    @Override
    public String toString()
    {
        return "Person{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }

    public String getName()
    {
        return name;
    }

    public static int getMaxAge()
    {
        return maxAge;
    }

    @Override
    public void play()
    {
        System.out.println(name+" play");
    }

    @Override
    public void write(String content)
    {
        System.out.println(name+" write: "+content);
    }
}