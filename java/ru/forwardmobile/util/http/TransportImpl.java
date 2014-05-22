package ru.forwardmobile.util.http;

import android.util.Log;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * @author Pavel Melent'ev, Vasiliy Vanin
 */
public class TransportImpl implements ITransport {
        
        public TransportImpl() {
        }

        public IResponse send(IRequest request) throws Exception {
        
                try {
                        
                        Socket socket = open(request.getHost(), request.getPort(), request.getTimeout());
                      
                        OutputStream out = socket.getOutputStream();
                        
                        try {
                                
                                write(out, request);
                                
                                InputStream in = socket.getInputStream();
                                
                                try {
                                        byte[] data = read(in);
                                        return ResponseFactory.getResponse(data);
                                        
                                } finally {
                                        in.close();
                                }
                                
                        } finally {
                                out.close();
                        }
                        
                } catch(Exception e) {
                        throw new Exception("Http error: " + e.toString());
                }
        
        }

        private Socket open(String host, Integer port, Integer timeout) throws Exception {
                try {
                        Socket socket = new Socket(host, port);
                        socket.setSoTimeout(timeout * 1000);
                        return socket;
                } catch(IOException e) {
                        throw new Exception("Opening socket: " + e.toString());
                }
        }

        private void write(OutputStream out, IRequest request) throws Exception {
                try {
                        StringBuilder line = new StringBuilder();
                        out.write(request.toString().getBytes());
                        out.flush();
                        
                } catch(IOException e) {
                        throw new Exception("Writing data: " + e.toString()); 
                }
        }
        
        private byte[] read(InputStream in) throws Exception {
                try {
                        ByteArrayOutputStream data = new ByteArrayOutputStream();
                        byte[] buffer = new byte[1024];
                        int result = 0;
                        while ( ( result = in.read(buffer, 0, buffer.length) ) > 0 ) {
                                data.write(buffer, 0, result);
                        }
                        return data.toByteArray();
                } catch(IOException e) {
                        throw new Exception("Reading data: " + e.toString());
                }
        }
        
}
