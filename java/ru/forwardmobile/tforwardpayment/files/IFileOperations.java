package ru.forwardmobile.tforwardpayment.files;

import java.io.IOException;

/**
 * Created by PiskunovI on 21.01.2015.
 */
public interface IFileOperations {
    public void serverConnection();
    public void writeToFile(String string) throws IOException;
    public void sendFile(String fileName);
}
