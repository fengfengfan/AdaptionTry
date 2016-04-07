/**
 * Created by fun on 15/12/8.
 */

import org.omg.SendingContext.RunTime;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

public class ParallelPisa {
    private int support;
    private int POI;
    //private ExecutorService pool;
    private ThreadPoolExecutor myPool;
    private Controller myController;

    public ParallelPisa(int support , int POI){
        this.support = support;
        this.POI = POI;
        //this.pool = Executors.newFixedThreadPool(3);
        this.myPool =  new ThreadPoolExecutor(4, 4,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>());

    }

    public void parallelPisaAlgorithm(ArrayList<ArrayList<String>> transaction , int timeLength){
        PSTreeNode root = new PSTreeNode(0); //root node
        int currentTime = 1;
        while (currentTime <= timeLength){
            if (currentTime == 4){
                //myController.adaptWithRootAndVelocity(root, transaction.get((currentTime - 1)), myPool);
            }
            parallelTraverse(currentTime, root, transaction.get((currentTime - 1)));
            currentTime++;
            System.out.println("------------------------" + myPool.getCorePoolSize());
        }
        myPool.shutdown();
    }


    public void parallelTraverse(int currentTime , PSTreeNode root , ArrayList<String> data){
        int subtreeCount = root.getChildren().size() - 1;
        ArrayList<Future<Integer>> resultList = new ArrayList<>();
        for (int i = subtreeCount; i >= 0; i--) {
            Future<Integer> subtreeResult = myPool.submit(new SubtreeTask(root.getChildren().get(i) ,
                    support , POI , currentTime , data));
            resultList.add(subtreeResult);
        }
        while (!isSubtreeDone(resultList)){

        }
        handleRootNode(currentTime , root , data);
    }


    public boolean isSubtreeDone(ArrayList<Future<Integer>> allResult){
        int length = allResult.size();
        for (int i = 0; i < length; i++) {
            if (!allResult.get(i).isDone()){
                return false;
            }
        }
        return true;
    }

    public void updateSequenceIdOfRootChild(PSTreeNode node,int seqId,int timeStap){
        Map<Integer,Integer> seqenceID = new HashMap<>();
        seqenceID = node.getSequenceId();
        if(seqenceID.get(seqId) != null){
            node.setSequenceId(seqId,timeStap);
        }
        else {
            node.setSequenceId(seqId,timeStap);
        }

    }

    public void handleRootNode(int currentTime , PSTreeNode node , ArrayList<String> data){
        for (int i = 0; i < data.size(); i++) {
            if(!data.get(i).equals(" ")){
                ArrayList<String> allItem = getAllCombinationOfElement(data.get(i));
                for (int j = 0; j < allItem.size(); j++) {
                    PSTreeNode child = node.getChildWithLabel(allItem.get(j));
                    if(child == null){
                        node.rootNodeCreateChild(allItem.get(j), (i + 1), currentTime, (node.getLayer() + 1));
                    }
                    else {
                        this.updateSequenceIdOfRootChild(child, (i + 1), currentTime);
                    }
                }
            }
        }
    }

    public ArrayList<String> getAllCombinationOfElement(String element){
        char[] data = element.toCharArray();
        ArrayList<String> result = new ArrayList<>();
        result = this.combination(data);
        return result;
    }

    public ArrayList<String> combination(char data[]){
        ArrayList<String> result = new ArrayList<String>();
        if(data==null||data.length==0){
            return null;
        }
        List<Character> list = new ArrayList();
        for(int i=1;i<=data.length;i++){
            this.combine(data, 0, i, list, result);
        }
        return result;
    }

    public void combine(char []cs,int begin,int number,List<Character> list,ArrayList<String> result){
        if(number==0){
            char[] temp = new char[list.size()];
            for (int i = 0; i < list.size(); i++) {
                temp[i] = list.get(i);
            }
            String r = new String(temp);
            result.add(r);
            return ;
        }
        if(begin==cs.length){
            return;
        }
        list.add(cs[begin]);
        combine(cs,begin+1,number-1,list,result);
        list.remove((Character)cs[begin]);
        combine(cs,begin+1,number,list,result);
        return ;
    }

}
