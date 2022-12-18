import java.util.ArrayList;

public class AddValidateClients {
    public synchronized void addToList(ArrayList<ClientHash> list, String ip, int l) {
        ClientHash cObj = new ClientHash();
        if (list.size() != l) {
            return;
        }
        if (list.isEmpty()) {
            cObj.setHash(ip);
            cObj.setPrevHash(null);
        } else {
            cObj.setHash(ip);
            ClientHash obj = list.get(list.size() - 1);
            String prevHash = obj.getHash();
            cObj.setPrevHash(prevHash);
        }
        System.out.println("New block created");
        list.add(cObj);
        System.out.println("Block has been added to the list");

        for (int i = 0; i < list.size(); i++) {
            ClientHash obj = list.get(i);
            System.out.println("Hash: " + obj.getHash() + " prevhash: " + obj.getPrevHash());
        }
    }

    public boolean validate(ArrayList<ClientHash> list, String hash) {
        boolean flag = false;
        for (int i = 0; i < list.size(); i++) {
            ClientHash obj = list.get(i);
            if (hash.equals(obj.getHash())) {
                flag = true;
                break;
            }
        }
        return flag;
    }
}
