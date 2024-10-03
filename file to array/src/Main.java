import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader bf = new BufferedReader(new FileReader("(file to array)\\file.txt"));

        ArrayList<student> array = new ArrayList<student>();
        String string;
        while ((string = bf.readLine()) != null) {
            String[] split = string.split(",");
            student s = new student();
            s.setNumber(split[0]);
            s.setName(split[1]);
            s.setAge(Integer.parseInt(split[2]));
            s.setAddress(split[3]);
            array.add(s);
        }
        bf.close();
        for(student s:array){
            System.out.println(s.getNumber()+","+s.getName()+","+s.getAge()+","+s.getAddress());
        }

    }
}