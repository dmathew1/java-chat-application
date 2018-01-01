import com.sun.net.httpserver.HttpServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by denzel on 12/18/17.
 */
public class MultiThreadedChatServer {

    private static ServerSocket serverSocket;
    private static HashMap<Integer, PrintWriter> writers = new HashMap<>();
    private static int ID = 1;

    public static void main(String[] args){
        ExecutorService executors = Executors.newFixedThreadPool(5);

        try {
//            HttpServer httpServer = HttpServer.create();
            serverSocket = new ServerSocket(NETWORK_CONSTANTS.SERVER_PORT);
            System.out.println("Listening on: " + NETWORK_CONSTANTS.SERVER_ADDRESS + ":" + NETWORK_CONSTANTS.SERVER_PORT);
            while (true) {
                Socket socket = serverSocket.accept();
                writers.put(ID,new PrintWriter(socket.getOutputStream(),true));
                System.out.println("Starting new thread...");
                executors.execute(new WorkerThread(ID,socket));
                ID++;
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }


    private static class WorkerThread implements Runnable {
        Socket socket;
        BufferedReader br;
        PrintWriter pw;
        Integer id;

        public WorkerThread(Integer id, Socket socket) throws IOException{
            this.socket = socket;
            this.id = id;
            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            pw = writers.get(id);
        }

        private void closeConnection(){
           try {
               pw.write("exit\n");
               pw.flush();
               writers.remove(this.id);
               socket.close();
               Thread.currentThread().interrupt();
               System.out.println("Closing socket...");

           }catch (IOException e) {
               e.printStackTrace();
           }
        }

        @Override
        public void run() {
            try {
                String line ="";
                while (!socket.isClosed()) {
                    line = br.readLine();
                    if (line!=null && line.equals("exit")) {
                        closeConnection();
                        break;
                    }
                    if (line != null && !line.equals("exit")) {
                        System.out.println("Received From Client " + this.id + ": message: " + line);

                        for (int key : writers.keySet()) {
                            if (!id.equals(key)) {
                                if (!socket.isOutputShutdown()) {
                                    pw = writers.get(key);
                                    pw.write("Client " + this.id + " sent: " + line + "\n");
                                    pw.flush();
                                }
                            }
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Why");
            }
        }
    }
}
