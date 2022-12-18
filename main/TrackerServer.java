import java.net.*;
import java.io.*;
import java.util.*;
import java.util.concurrent.*;

public class TrackerServer {

    public static void addToList(ArrayList<ClientHash> list, String ip) throws Exception {
        System.out.println("Request to add a client to list");
        List<BlockC> bList = new ArrayList<>();
        AddValidateClients obj = new AddValidateClients();
        ExecutorService exe = Executors.newFixedThreadPool(5);
        for (int i = 0; i < 5; i++) {
            BlockC bObj = new BlockC(list, ip, obj, 1, list.size());
            bList.add(bObj);
        }

        String result = exe.invokeAny(bList);

        System.out.println(result);

        exe.shutdown();
    }

    public static boolean validateClient(ArrayList<ClientHash> list, String torrReq) {
        System.out.println("Request for client validation");
        AddValidateClients obj = new AddValidateClients();
        String hash = "";
        for (int i = 0; i < torrReq.length(); i++) {
            char ch = torrReq.charAt(i);
            if (Character.compare(ch, ':') == 0) {
                String str = torrReq.substring(i + 1, i + 9);
                if (str.equals("authHash")) {
                    int j = i + 9;
                    String size = "";
                    while (Character.compare(':', torrReq.charAt(j)) != 0) {
                        size = size + torrReq.charAt(j);
                        j++;
                    }
                    int k = Integer.parseInt(size.trim());
                    System.out.println(k);
                    hash = torrReq.substring(j + 1, j + k + 1);
                    System.out.println("Hash: " + hash);
                    break;
                }
            }
        }

        return obj.validate(list, hash);
    }

    public static void main(String[] args) throws Exception {
        int mOp = 0;
        boolean flag = true;
        HashIP hash = new HashIP();
        Scanner sc = new Scanner(System.in);
        ArrayList<ClientHash> cList = new ArrayList<>();
        while (flag) {
            System.out.print("1 --> Register new client\n2 --> Serve client\n3 --> Close\nChoose: ");
            mOp = sc.nextInt();
            if (mOp == 1) {
                try {

                    ServerSocket server = new ServerSocket(1234);
                    System.out.println("Tracker waiting for a request");

                    Socket client = server.accept();
                    System.out.println("Connected to a client");
                    DataInputStream in = new DataInputStream(client.getInputStream());
                    DataOutputStream out = new DataOutputStream(client.getOutputStream());

                    String recv = "";
                    int op = 0;
                    String reply = "";
                    recv = in.readUTF();
                    if (recv.equals("New client req")) {
                        System.out.println("Client request received");
                        System.out.print("1 --> Add client to list\n2 --> Deny request\nOption: ");
                        op = sc.nextInt();
                        if (op == 1) {
                            String ip = client.getInetAddress().toString();
                            System.out.println("ip: " + ip);
                            String ipHash = String.valueOf(hash.getHash(ip));
                            addToList(cList, ipHash);
                            reply = "Request accepted and successfully added to the list";
                        } else if (op == 2) {
                            reply = "Request denied";
                        }
                    } else {
                        reply = "Not a valid request";
                    }

                    out.writeUTF(reply);

                    server.close();

                } catch (Exception e) {
                    System.out.println(e);
                }
            } else if (mOp == 2) {
                try {

                    System.out.println("Tracker ready to serve client");
                    ServerSocket server = new ServerSocket(1234);
                    Socket client = server.accept();
                    System.out.println("Connected to a client");
                    DataInputStream in = new DataInputStream(client.getInputStream());
                    DataOutputStream out = new DataOutputStream(client.getOutputStream());
                    String reply = "";
                    String recv = "";
                    recv = in.readUTF();
                    System.out.println("Msg from client: " + recv);
                    String temp = recv.trim();
                    if (validateClient(cList, temp)) {
                        reply = "Valid client";
                        System.out.println("Valid client");
                    } else {
                        reply = "Not a valid request";
                        System.out.println("Not a valid client");
                    }
                    out.writeUTF(reply);
                    server.close();
                } catch (Exception e) {
                    System.out.println(e);
                }
            } else if (mOp == 3) {
                System.out.println("Closing server");
                flag = false;
            } else {
                System.out.println("Invalid option");
            }
        }
        sc.close();
    }
}
