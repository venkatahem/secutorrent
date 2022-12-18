import java.io.*;
import java.util.Scanner;

public class ReadTorrClient {

    public void displayFiles() throws Exception {
        try {
            String path = System.getProperty("user.dir");
            File f = new File(path);

            FilenameFilter filter = new FilenameFilter() {
                @Override
                public boolean accept(File f, String name) {
                    return name.endsWith(".torrent");
                }
            };

            File[] files = f.listFiles(filter);
            System.out.println("Torrent files available");

            for (int i = 0; i < files.length; i++) {
                System.out.println(files[i].getName());
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    public String sendTorrReq() throws Exception {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter filename: ");
        String fileName = sc.nextLine();

        File file = new File(fileName);

        BufferedReader br = new BufferedReader(new FileReader(file));

        String st;
        String content = "";
        while ((st = br.readLine()) != null) {
            System.out.println(st);
            content = content + st;
            Thread.sleep(1000);
        }
        sc.close();
        br.close();
        return content;
    }
}
