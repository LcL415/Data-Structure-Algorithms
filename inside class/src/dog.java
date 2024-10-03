import java.util.Objects;

public class dog implements Jumping {
    int age;
    String sex;

    public dog() {
    }

    public dog(int age, String sex) {
        this.age = age;
        this.sex = sex;
    }

    public int getAge() {
        return age;
    }

    public String getSex() {
        return sex;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    @Override
    public void jump() {
        System.out.println("狗可以跳高了");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        dog dog = (dog) o;

        if (age != dog.age) return false;
        return Objects.equals(sex, dog.sex);
    }

    @Override
    public int hashCode() {
        int result = age;
        result = 31 * result + (sex != null ? sex.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "dog{" +
                "age=" + age +
                ", sex='" + sex + '\'' +
                '}';
    }
}
