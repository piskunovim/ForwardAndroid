package ru.forwardmobile.tforwardpayment.spp;

import java.util.List;

/**
 * Created by Vasiliy Vanin on 08.06.14.
 */
public interface IResponseSet {

    public void addLine(byte[] line);
    public String getLine(int index);
    public String getBody();
    public byte[] getBytes();
    public int getSize();

    public ICommandResponse             getResponse(int index) throws Exception;
    public List<ICommandResponse> getResponses() throws Exception;

    public void setSignature(byte[] signature);
    public byte[] getSignature();
}