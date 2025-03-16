public class App {

    static BTree tree = new BTree();

    public static void main(String[] args) {
        tree.readFromFile("arquivos\\ordExt_teste.txt");
        tree.writeToFile("arquivos\\resposta.txt");
    }
    
}
