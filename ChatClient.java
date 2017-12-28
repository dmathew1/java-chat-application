import java.io.*;
import java.net.Socket;
import java.util.Scanner;

/**
 * Created by denzel on 12/18/17.
 */
public class ChatClient implements Runnable{

    Socket socket;
    PrintWriter pw;
    BufferedReader br;
    Scanner in;


    public ChatClient() throws IOException{
        socket = new Socket(NETWORK_CONSTANTS.SERVER_ADDRESS,NETWORK_CONSTANTS.SERVER_PORT);
        pw = new PrintWriter(socket.getOutputStream(),true); //Send text
        br = new BufferedReader(new InputStreamReader(socket.getInputStream())); //Receive text
        in = new Scanner(System.in);
        new Thread(new ReceiverThread()).start();
    }


    @Override
    public void run() {
        while (!socket.isClosed()) {
            if (in.hasNext()) {
                String line = in.nextLine();
                pw.write(line + "\n");
                pw.flush();
                if(line.equals("exit")){
                    try{
                        closeConnection();
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private void closeConnection() throws IOException{
        br.close();
        pw.close();
        socket.close();
        System.out.println("Client terminating.");
        Thread.currentThread().interrupt();
    }

    private class ReceiverThread implements Runnable{
        @Override
        public void run() {
            try {
                while(!socket.isClosed()) {
                    String line = br.readLine();
                    while (line != null) {
                        System.out.println(line);
                        line = br.readLine();
                    }
                }
                closeConnection();
            }catch (IOException e){

            }
        }
    }

    public static void main(String[] args) throws IOException {
        new Thread(new ChatClient()).start();
    }
}


