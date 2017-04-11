package demo.annotations;

/**
 * Created by Helly on 2017/04/10.
 */
@PersonInfo(name = "Bob", age = 30, sex = true)
public class Bob implements Person {
    private String name;
    private int age;
    private boolean sex;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public boolean isSex() {
        return sex;
    }

    public void setSex(boolean sex) {
        this.sex = sex;
    }

    public Bob() {
    }

    public Bob(String name, int age, boolean sex) {
        this();
        this.name = name;
        this.age = age;
        this.sex = sex;
    }

    public void hello() {
        System.out.println("world");
    }
}
