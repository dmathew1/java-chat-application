import java.io.*;
import java.net.Socket;

/**
 * Created by denzel on 12/18/17.
 */
public class ChatClient implements Runnable{
    private static int ID = 1;
    static int message;
    public ChatClient(int message){
        this.message = message;
    }

    @Override
    public void run() {
        Socket socket;
        try{
            socket = new Socket(NETWORK_CONSTANTS.SERVER_ADDRESS,NETWORK_CONSTANTS.SERVER_PORT);
            PrintWriter pw = new PrintWriter(socket.getOutputStream(),true); //Send text
            pw.write("Receiving from Client: " + Thread.currentThread().getId() + " | Request ID: " + ID++ +"\n");
            pw.flush();
            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream())); //Receive text
            System.out.println(br.readLine());

        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
