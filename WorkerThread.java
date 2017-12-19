import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Date;
import java.util.HashSet;

/**
 * Created by denzel on 12/18/17.
 */
public class WorkerThread implements Runnable {
    private static int ID = 1;
    Socket socket;
    HashSet<PrintWriter> writers;

    public WorkerThread(Socket socket, HashSet<PrintWriter> writers){
        this.socket = socket;
        this.writers = writers;
    }

    @Override
    public void run() {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter pw = new PrintWriter(socket.getOutputStream());

            pw.write("Sending from server thread: test\n");
            pw.flush();
            while(br!=null && br.read()!=-1){
                System.out.println("Worker Thread: " + br.readLine());
            }

        }catch (IOException e){
            e.printStackTrace();
        }finally {
            try {
                socket.close();
                System.out.println("closing socket");
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }
}
