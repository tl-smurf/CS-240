package spell;


public class Trie implements ITrie{

    private int wordCount;
    private int nodeCount;
    private Node root;

    public Trie() {
        this.wordCount = 0;
        this.nodeCount = 1;
        this.root = new Node();
    }

    @Override
    public void add(String word) {
        Node currNode = root;
        Node newNode = null;
        //Makes a char array out of the input word
        char[] wordChars = word.toCharArray();
        //Takes into account an empty input
        if (word.length() < 1) {
            return;
        }

        //Adds the nodes as needed
        for (int i = 0; i < wordChars.length; i++) {
            //if the letter isn't already there, add it
            if(currNode.children[wordChars[i] - 'a'] == null) {
                newNode = new Node();
                currNode.children[wordChars[i] - 'a'] = newNode;
                nodeCount++;
            }
            //set the current node to the next letter
            currNode = currNode.children[wordChars[i] - 'a'];
            //if this is the last letter of word, increment word count
            if (i == wordChars.length - 1) {
                currNode.incrementValue();
                wordCount++; //TODO: see if i need to put this just at the beginning of function
            }
        }
    }

    @Override
    public INode find(String word) {
        Node currNode = root;
        Node winnerNode = null;

        char[] wordChars = word.toCharArray();

        for (int i = 0; i < wordChars.length; i++) {
            //word is not in Trie
            if (currNode.children[wordChars[i] - 'a'] == null) {
                return null;
            }
            //increment node
            currNode = currNode.children[wordChars[i] - 'a'];
        }
        //Checks to see if it is a word
        if (currNode.getValue() == 0) {
            return null;
        }
        else {
            winnerNode = currNode;
            return winnerNode;
        }
    }

    @Override
    public int getWordCount() {
        return wordCount;
    }

    @Override
    public int getNodeCount() {
        return nodeCount;
    }

    @Override
    public int hashCode() {
        //get first non-null index value
        int index = 0;
        for (int i = 0; i < 26; i++) {
            if (root.children[i] != null) {
                index = i;
            }
        }
        return index * nodeCount * wordCount;
    }

    private boolean checkDemNodes(Node ogNode, Node newNode) {
        //Test cases
        if (ogNode == null && newNode != null) {
            return false;
        }
        if (ogNode != null && newNode == null) {
            return false;
        }
        if (ogNode == null && newNode == null) {
            //lol, easy route
            return true;
        }
        if (ogNode.getValue() != newNode.getValue()) {
            return false;
        }
        //recursive bit
        boolean areTheyEquals = false;
        Node[] ogNodes = ogNode.children;
        Node[] newNodes = newNode.children;;
        for (int i = 0; i < 26; i++) {
            areTheyEquals = checkDemNodes(ogNodes[i], newNodes[i]);
        }
        return areTheyEquals;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj.getClass() != this.getClass()) {
            return false;
        }
        //If you get to here, then they are the same object type
        Trie trieChecker = (Trie)obj;
        if (trieChecker.getNodeCount() != this.getNodeCount()) {
            return false;
        }
        if (trieChecker.getWordCount() != this.getWordCount()) {
            return false;
        }
        //Now we need to go node by node recursively... so we need another function
        Node newRoot = trieChecker.root;
        return checkDemNodes(root, newRoot);
    }


    private void trieTraverse(Node currNode, StringBuilder wordBuilder, StringBuilder listBuilder) {
        //If curr node represents a word, add it to the list
        if (currNode.getValue() != 0) {
            listBuilder.append(wordBuilder.toString());
            listBuilder.append("\n");
        }
        char currLetter = 'a';
        for (Node nextLetter: currNode.children) {
            if (nextLetter != null) {
                //This means that the letter exists in the Trie
                wordBuilder.append(currLetter);
                trieTraverse(nextLetter, wordBuilder, listBuilder);
                currLetter++;
            }
        }
        if (wordBuilder.length() != 0) {
            wordBuilder.delete(wordBuilder.length() - 1, wordBuilder.length());
        }
    }



    @Override
    public String toString() {
        StringBuilder wordBuilder = new StringBuilder();
        StringBuilder listBuilder = new StringBuilder();
        trieTraverse(root, wordBuilder, listBuilder);
        return listBuilder.toString();
    }
}
