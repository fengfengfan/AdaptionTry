import java.util.ArrayList;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created by fun on 16/3/31.
 */
public class Controller {
    private float velocityLow;
    private float velocityMid;
    private float velocityHigh;

    public int countVelocity(ArrayList<String> data){
        return 0;
    }

    public void adaptWithRootAndVelocity(PSTreeNode root, ArrayList<String> data, ThreadPoolExecutor pool){
        pool.setCorePoolSize(Runtime.getRuntime().availableProcessors() + 2);
    }

}
