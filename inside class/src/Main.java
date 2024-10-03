// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    public static void main(String[] args) {
        // Press Alt+Enter with your caret at the highlighted text to see how
        // IntelliJ IDEA suggests fixing it.
        JumpOperation j = new JumpOperation();
        j.jump(new Jumping() {
            @Override
            public void jump() {
                long start=System.currentTimeMillis();
               // System.out.println("可以跳高了");
                for(int i=0;i<10000;i++)
                {
                    System.out.println(i);
                }
                long end=System.currentTimeMillis();
                System.out.println(start-end);

            }
        });
        dog dog = new dog();
        JumpOperation j2 = new JumpOperation();
        j2.jump(dog);

dog d1=new dog(3,"man");
dog d2=new dog(3,"man");
        System.out.println(d1.equals(d2));
        System.out.println(d1);
    }
}
