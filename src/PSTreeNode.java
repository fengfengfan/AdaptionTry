import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by fun on 15/10/31.
 */
public class PSTreeNode {    //sequenceID 不用＋ or — 1， 初始化时候就已经加过了，不是从0开始的。
    private String label;
    private Map<Integer , Integer> sequenceId; //<seqID,timeStap>
    private PSTreeNode parent;
    private ArrayList<PSTreeNode> children;
    private int layer;

    public PSTreeNode() {

    }

    public void coverSequenceId(Map<Integer , Integer> newSequenceId){
        this.sequenceId = newSequenceId;
    }
    public void rootNodeCreateChild(String label,int seqId,int timeStap,int layer){
        PSTreeNode child = new PSTreeNode(label,seqId,timeStap,layer);
        child.setParent(this);
        this.children.add(child);
    }

    public void setParent(PSTreeNode parent){
        this.parent = parent;
    }

    public void commonNodeCreateChild(String label,int seqId, int layer){
        if(layer > 2){
            PSTreeNode child = new PSTreeNode(label,seqId,-1,layer);
            child.setParent(this);
            this.children.add(child);
        }
        else {
            int timeStamp = this.getSequenceId().get(seqId);
            PSTreeNode child = new PSTreeNode(label,seqId,timeStamp,layer);
            child.setParent(this);
            this.children.add(child);
        }

    }
    public PSTreeNode(String lable,int seqId,int timeStap,int layer){
        this.label = lable;
        this.sequenceId = new HashMap<Integer , Integer>();
        if(layer > 2){
            this.setSequenceId(seqId , -1); //-1 means dont store timestamp
        }
        else {
            this.setSequenceId(seqId, timeStap);
        }
        this.parent = new PSTreeNode();
        this.children = new ArrayList<PSTreeNode>();
        this.layer = layer;

    }

    public void setSequenceId(int seqId,int timeStap){
        if(timeStap >= 0) {
            sequenceId.put(seqId,timeStap);
        }
        else {
            sequenceId.put(seqId,null);
        }

    }

    public Map<Integer,Integer> getSequenceId(){
        return sequenceId;
    }



    public PSTreeNode getChildWithLabel(String lable){
        if(this.children == null){
            return null;
        }
        for (int i = 0; i < children.size(); i++) {
            if(children.get(i).getLabel().equals(lable)){
                return children.get(i);
            }
        }
        return null;
    }

    public PSTreeNode(int layer){
        this.setLayer(layer);
        this.children = new ArrayList<PSTreeNode>();
    }

    public PSTreeNode(String label,int layer,int nodeType){
        this.layer = layer;
        this.label = label;
        this.children = new ArrayList<PSTreeNode>();
    }

    public ArrayList<PSTreeNode> getChildren(){
        return children;
    }

    public PSTreeNode getParent(){
        return parent;
    }

    public int getLayer() {
        return layer;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setLayer(int layer) {
        this.layer = layer;
    }

    public boolean isHasChildren(){
        if(this.children == null){
            return false;
        }
        else {
            return true;
        }
    }

    public String toString(){
        return "lable = " + this.getLabel() + " layer = " + this.getLayer();
    }

    public void addTwoNchildren(PSTreeNode node1, PSTreeNode node2){ //use to test
        this.children.add(node1);
        this.children.add(node2);
    }


}
