import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;

/**
 * Created by denzel on 12/18/17.
 */
public class MultiThreadedChatServer {
    private static Socket socket;
    private static ServerSocket serverSocket;

    public static void main(String[] args){
        HashSet<PrintWriter> writers = new HashSet<>();
        try {
            serverSocket = new ServerSocket(NETWORK_CONSTANTS.SERVER_PORT);
            while (true) {
                socket = serverSocket.accept();
                new Thread(new WorkerThread(socket,writers)).run();
            }
        }catch (IOException e){
            try{
                socket.close();
            }catch (IOException f){
                f.printStackTrace();
            }
        }
    }
}
