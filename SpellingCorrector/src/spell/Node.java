package spell;

public class Node implements INode{

    private int counter;
    public Node[] children;

    public Node() {
        children = new Node[26];
        counter = 0;
    }

    @Override
    public int getValue() {
        return counter;
    }

    @Override
    public void incrementValue() {
        counter++;
    }

    @Override
    public INode[] getChildren() {
        return children;
    }

}
