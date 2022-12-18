public class Reader {
    public Reader(String[] args) {
        try {
            TorrentProcessor tp = new TorrentProcessor();

            if (args.length < 1) {
                System.err.println(
                        "Incorrect use, please provide the path of the torrent file...\r\n" +
                                "\r\nCorrect use of Reader:\r\n" +
                                "Reader torrentPath");

                System.exit(1);
            }
            TorrentFile t = tp.getTorrentFile(tp.parseTorrent(args[0]));
            if (args.length > 1)
                Constants.SAVEPATH = args[1];
            if (t != null) {
                t.printData(true);
            } else {
                System.err.println(
                        "Provided file is not a valid torrent file");
                System.err.flush();
                System.exit(1);
            }
        } catch (Exception e) {

            System.out.println("Error while processing torrent file. Please restart the client");
            // e.printStackTrace();
            System.exit(1);
        }

    }

    public static void main(String[] args) {
        new Reader(args);
    }
}
