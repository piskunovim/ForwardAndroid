package ru.forwardmobile.util.http;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class Test {

        public static void main(String[] args) throws Exception {
        
                if ( args.length != 3 ) {
                        System.out.println("Usage: test host port contentfile");
                        System.exit(1);
                }
                String host = args[0];
                Integer port = new Integer(args[1]);
                String contentFileName = args[2];
                File content = new File(contentFileName);
                if ( !content.exists() ) {
                        throw new Exception("File \"" + args[2] + "\" does not exist!");
                }
                StringBuilder data = null;
                BufferedReader fileIn = new BufferedReader(new FileReader(content));
                try {
                        data = new StringBuilder();
                        String line = null;
                        while ( ( line = fileIn.readLine() ) != null ) {
                                if ( data.length() != 0 ) {
                                        data.append('\n');
                                }
                                data.append(line);
                        }
                } finally {
                        fileIn.close();
                }
                System.out.println("Content bytes read: " + data.length());
                Socket socket = new Socket(host, port);
                BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                try {
                        
                        out.write("POST / HTTP/1.0\n");
                        out.write("Content-length: " + data.length() + "\n");
                        out.write("\n");
                        out.write(data.toString());
                        out.flush();
                        
                        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                        
                        try {
                                
                                String line = null;
                                
                                while ( ( line = in.readLine() ) != null ) {
                                        System.out.println(line);
                                }
                                
                        } finally {
                                in.close();
                        }
                        
                } finally {
                        out.close();
                }
        
        }

}
