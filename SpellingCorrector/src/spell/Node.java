package spell;

public class Node implements INode{

    private int counter = 0;
    public Node[] children;

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
