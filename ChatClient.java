import java.io.*;
import java.net.Socket;

/**
 * Created by denzel on 12/18/17.
 */
public class ChatClient implements Runnable{
    private static int ID = 1;
    static String message;
    public ChatClient(String message){
        this.message = message;
    }

    @Override
    public void run() {
        Socket socket;
        try{
            socket = new Socket(NETWORK_CONSTANTS.SERVER_ADDRESS,NETWORK_CONSTANTS.SERVER_PORT);
            PrintWriter pw = new PrintWriter(socket.getOutputStream()); //Send text
            pw.write("Receiving from Client: " + Thread.currentThread().getId() + " | Request ID: " + ID++ +"\n");
            pw.flush();
            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream())); //Receive text
            while(br!=null && br.read()!=-1){
                System.out.println("Client: " + ID++ + " "+ br.readLine());
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new Thread(new ChatClient("test")).start();
    }
}
