/*
 * Java Bittorrent API as its name indicates is a JAVA API that implements the Bittorrent Protocol
 * This project contains two packages:
 * 1. jBittorrentAPI is the "client" part, i.e. it implements all classes needed to publish
 *    files, share them and download them.
 *    This package also contains example classes on how a developer could create new applications.
 * 2. trackerBT is the "tracker" part, i.e. it implements a all classes needed to run
 *    a Bittorrent tracker that coordinates peers exchanges. *
 *
 * Copyright (C) 2007 Baptiste Dubuis, Artificial Intelligence Laboratory, EPFL
 *
 * This file is part of jbittorrentapi-v1.0.zip
 *
 * Java Bittorrent API is free software and a free user study set-up;
 * you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * Java Bittorrent API is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Java Bittorrent API; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 * @version 1.0
 * @author Baptiste Dubuis
 * To contact the author:
 * email: baptiste.dubuis@gmail.com
 *
 * More information about Java Bittorrent API:
 *    http://sourceforge.net/projects/bitext/
 */

// package jBittorrentAPI;

import java.io.FileOutputStream;
import java.net.InetAddress;
import java.util.ArrayList;
// import java.net.UnknownHostException;

/**
 * Simple example to show how it is possible to create a new .torrent file to
 * share files using bittorrent protocol
 */
class CreateTorrent {
    public static void main(String[] args) throws Exception {
        HashIP obj = new HashIP();
        /*
         * Predefined input
         * args = new String[12];
         * args[0] =
         * "C:\\Users\\dkvhe\\OneDrive\\Documents\\vs_code\\Java\\cn\\test\\hi.torrent";
         * args[1] = "https:\\jhvjvbjeh.verv";
         * args[2] = "64";
         * args[3] = "test_cn";
         * args[4] =
         * "C:\\Users\\dkvhe\\OneDrive\\Documents\\vs_code\\Java\\cn\\test\\HashIP.class";
         * args[5] =
         * "C:\\Users\\dkvhe\\OneDrive\\Documents\\vs_code\\Java\\cn\\test\\Client.class";
         * args[6] = "..";
         * args[7] = "vas";
         * args[8] = "..";
         * args[9] = "test_create";
         * args[10] = "..";
         * String ip = String.valueOf(InetAddress.getLocalHost());
         * System.out.println(ip);
         * String ip1 = "";
         * for (int i = 0; i < ip.length(); i++) {
         * char ch = ip.charAt(i);
         * if (Character.compare('/', ch) == 0) {
         * ip1 = ip.substring(i, ip.length());
         * break;
         * }
         * }
         * System.out.println(ip1);
         * args[11] = String.valueOf(obj.getHash(ip1));
         * args[11] = String.valueOf(obj.getHash("/192.168.212.197"));
         */
        if (args.length < 5) {
            System.err.println("Wrong parameter number\r\n\r\nUse:\r\n" +
                    "ExampleCreateTorrent <torrentPath> <announce url> <pieceLength> " +
                    "<filePath1> <filePath2> ... <..> <creator> <..> <comment>");
            System.exit(0);
        }
        TorrentProcessor tp = new TorrentProcessor();
        tp.setAnnounceURL(args[1]);
        try {
            tp.setPieceLength(Integer.parseInt(args[2]));
        } catch (Exception e) {
            System.err.println("Piece length must be an integer");
            System.exit(0);
        }
        int i = 3;
        ArrayList<String> files = new ArrayList<String>();
        if (!args[i + 1].equalsIgnoreCase("..")) {
            tp.setName(args[3]);
            i++;
        }
        while (i < args.length) {
            if (args[i].equalsIgnoreCase(".."))
                break;
            files.add(args[i]);
            i++;
        }
        try {
            tp.addFiles(files);
        } catch (Exception e) {
            System.err.println(
                    "Problem when adding files to torrent. Check your data");
            System.exit(0);
        }
        i++;
        String creator = "";
        while (i < args.length) {
            if (args[i].equalsIgnoreCase(".."))
                break;
            creator += args[i];
            i++;
        }
        tp.setCreator(creator);
        i++;
        String comment = "";
        while (i < args.length) {
            if (args[i].equalsIgnoreCase(".."))
                break;
            comment += args[i];
            i++;
        }
        tp.setComment(comment);
        i++;
        String authHash = "";
        String ip = "";
        while (i < args.length) {
            if (args[i].equalsIgnoreCase(".."))
                break;
            if (!ip.equals("null")) {
                ip = String.valueOf(InetAddress.getByName("127.0.0.1"));
                // ip = String.valueOf(InetAddress.getLocalHost());
                System.out.println(ip);
                String ip1 = "";
                for (int j = 0; j < ip.length(); j++) {
                    char ch = ip.charAt(j);
                    if (Character.compare('/', ch) == 0) {
                        ip1 = ip.substring(j, ip.length());
                        break;
                    }
                }
                authHash = String.valueOf(obj.getHash(ip1));
            } else {
                authHash = ip;
            }
            i++;
        }
        tp.setAuthHash(authHash);
        try {
            System.out.println("Hashing the files...");
            System.out.flush();
            tp.generatePieceHashes();
            System.out.println("Hash complete... Saving...");
            FileOutputStream fos = new FileOutputStream(args[0]);
            fos.write(tp.generateTorrent());
            System.out.println("Torrent created successfully!!!");
            fos.close();
        } catch (Exception e) {
            System.err.println("Error when writing to the torrent file...");
            System.exit(1);
        }
    }
}
