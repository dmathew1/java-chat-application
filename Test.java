
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by denzel on 12/18/17.
 */
public class Test {

    public static void noExecutorService(){
        for(int i=0; i < 10; i++){
            new Thread(new ChatClient("test")).run();
        }
    }

    public static void executorService(){
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        for(int i = 0; i < 5; i++){
            executorService.submit(new ChatClient("test"));
        }
        executorService.shutdown();
    }

    public static void main(String[] args) {

//        noExecutorService();
        executorService();
    }
}
