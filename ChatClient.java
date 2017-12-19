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
            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream())); //Receive text
            PrintWriter pw = new PrintWriter(socket.getOutputStream()); //Send text
            pw.append("Receiving from Thread: " + Thread.currentThread().getId() + " | Request ID: " + ID++);
            pw.flush();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
