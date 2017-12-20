
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by denzel on 12/18/17.
 */
public class Test {


    public static void executorService(){
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        for(int i = 0; i < 20000; i++){
            executorService.submit(new ChatClient(i));
        }
        executorService.shutdown();
    }

    public static void main(String[] args) throws Exception {

        executorService();
    }
}
