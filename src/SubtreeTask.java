import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

/**
 * Created by fun on 15/12/9.
 */
public class SubtreeTask implements Callable<Integer>{

    private PSTreeNode node;
    private int support;
    private int POI;
    private int currentTime;
    private ArrayList<String> data;

    public SubtreeTask(PSTreeNode node , int support , int POI , int currentTime , ArrayList<String> data){
        this.node = node;
        this.POI = POI;
        this.support = support;
        this.currentTime = currentTime;
        this.data = data;
    }

    @Override
    public Integer call() throws Exception{
        traverse(node);
        return 1;
    }

    public void traverse(PSTreeNode node){
        if(!node.isHasChildren()){
            handleCommonNode(node);
        }
        else {
            int count = node.getChildren().size() - 1;
            for (int i = count; i >= 0 ; i --) {
                traverse(node.getChildren().get(i));
            }
            handleCommonNode(node);
        }
    }

    public void handleCommonNode(PSTreeNode node){
        ArrayList<Integer> eachSeqId = new ArrayList<Integer>();
        for (Map.Entry<Integer,Integer> seq : node.getSequenceId().entrySet()){
            eachSeqId.add(seq.getKey());
        }
        for (int i = 0; i < eachSeqId.size(); i++) {
            int seqId = eachSeqId.get(i);
            if(!deleteExpiredSequence(node , seqId)){
                if(!data.get(seqId-1).equals(" ")){
                    ArrayList<String> allItem = getAllCombinationOfElement(data.get((seqId - 1)));
                    for (int j = 0; j < allItem.size(); j++) {
                        if(!isItemOnThePathFromRoot(node,allItem.get(j))){
                            PSTreeNode child = node.getChildWithLabel(allItem.get(j));
                            if(child == null){
                                node.commonNodeCreateChild(allItem.get(j),seqId,node.getLayer() + 1);
                            }
                            else {
                                updateSequenceIdOfCommonNode(child, seqId);
                            }
                        }
                    }
                }
            }
        }
        if(node.getSequenceId().size() == 0){
            node.getParent().getChildren().remove(node);
        }
        else if(node.getSequenceId().size() >= support && node.getLayer() != 1 ){
            System.out.println(outputFrequentlyPattern(node));
        }
    }

    public int lowLayerNodeGetTimeStamp(PSTreeNode node,int seqId){
        PSTreeNode temp  = node.getParent();
        while (temp.getLayer() != 2){
            temp = temp.getParent();
        }
        return temp.getSequenceId().get(seqId);
    }

    public boolean deleteExpiredSequence(PSTreeNode node, int seqID){
        if(node.getLayer() > 2){
            int timeStampOfNode = lowLayerNodeGetTimeStamp(node,seqID);
            if(timeStampOfNode <= currentTime - POI){
                node.getSequenceId().remove(seqID);
                return true;
            }
        }
        else {
            if(node.getSequenceId().get(seqID) <= currentTime - POI){
                node.getSequenceId().remove(seqID);
                return true;
            }
        }
        return false;
    }
    public boolean isItemOnThePathFromRoot(PSTreeNode node , String item){
        boolean bo = false;
        PSTreeNode temp = node;
        while (temp.getLayer() >= 1){
            if(item.equals(temp.getLabel())){
                bo = true;
                return bo;
            }
            temp = temp.getParent();
        }
        return bo;
    }

    public String outputFrequentlyPattern(PSTreeNode node) {
        int timeStar;
        if ((currentTime - POI) <= 0 ){
            timeStar = 1;
        }
        else {
            timeStar = currentTime - POI + 1;
        }
        String result =this.getFrequentlyPattern(node) + "is the frequently sequential pattern between t"
                +timeStar + "~~t" + currentTime + ",its support is " + node.getSequenceId().size();
        return result;
    }
    public void updateSequenceIdOfCommonNode(PSTreeNode nodeForUpdate,int seqId){
        if(nodeForUpdate.getLayer() == 2){
            int timeStamp = nodeForUpdate.getParent().getSequenceId().get(seqId);
            nodeForUpdate.setSequenceId(seqId,timeStamp);
        }
        else {
            nodeForUpdate.setSequenceId(seqId,-1);
        }
        if(nodeForUpdate.getSequenceId().size() >= support){
            System.out.println(outputFrequentlyPattern(nodeForUpdate));
        }
    }

    public ArrayList<String> getFrequentlyPattern(PSTreeNode node){
        ArrayList<String> result = new ArrayList<String>();
        PSTreeNode temp = node;
        while (temp.getLayer() > 0){
            result.add(0,temp.getLabel());
            temp = temp.getParent();
        }
        return result;
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
