import java.util.ArrayList;
import java.util.concurrent.Callable;

public class BlockC implements Callable<String> {

    ArrayList<ClientHash> list;
    AddValidateClients obj;
    String ip;
    int op;
    int len;

    public BlockC(ArrayList<ClientHash> list, String ip, AddValidateClients obj, int op, int l) {
        this.list = list;
        this.ip = ip;
        this.obj = obj;
        this.op = op;
        this.len = l;
    }

    @Override
    public String call() throws Exception {
        if (op == 1) {
            return addNewClient();
        }
        return "Not a valid option";
    }

    private String addNewClient() {
        obj.addToList(list, ip, len);
        return "Done by tracker/peer num - " + Thread.currentThread().getId();
    }

}
