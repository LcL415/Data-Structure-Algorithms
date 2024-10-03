import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    public static void main(String[] args) throws IOException {
        DatagramSocket ds= new DatagramSocket();
        BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
        String line;
        while((line=bf.readLine())!=null){

        if("886".equals(line)){
            break;
        }
        byte[] bys=line.getBytes();
            DatagramPacket dp=new DatagramPacket(bys,bys.length, InetAddress.getByName("10.0.0.161"),12345);
            ds.send(dp);
            //ds.close();
        }
    }
}