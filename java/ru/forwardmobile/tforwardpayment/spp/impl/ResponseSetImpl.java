package ru.forwardmobile.tforwardpayment.spp.impl;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import ru.forwardmobile.tforwardpayment.spp.ICommandResponse;
import ru.forwardmobile.tforwardpayment.spp.IResponseSet;
import ru.forwardmobile.util.http.Converter;

/**
 * Created by Vasiliy Vanin on 08.06.14.
 */
public class ResponseSetImpl implements IResponseSet {

    public static final char CRLF = '\n';

    // Строки ответа (в байтах)
    private List<byte[]> lines = new ArrayList<byte[]>();
    private byte[] signature = null;

    public void addLine(byte[] line) {
        lines.add(line);
    }

    public byte[] getBytes() {
        ByteArrayOutputStream body = new ByteArrayOutputStream();
        for ( byte[] line: lines ) {
            if ( body.size() == 0 ) {
                body.write(line, 0, line.length);
            } else {
                body.write(CRLF);
                body.write(line, 0, line.length);
            }
        }
        return body.toByteArray();
    }

    public String getBody() {
        return Converter.toUnicode(getBytes());
    }

    public String getLine(int index) {
        return Converter.toUnicode(lines.get(index));
    }

    public int getSize() {
        return lines.size();
    }

    public ICommandResponse getResponse(int index) throws Exception {
        return new CommandResponseImpl(Converter.toUnicode(lines.get(index)));
    }

    public List<ICommandResponse> getResponses() throws Exception {
        List<ICommandResponse> responses = new ArrayList<ICommandResponse>();
        for ( byte[] nextLine : lines  ) {
            String line = Converter.toUnicode(nextLine);
            responses.add(new CommandResponseImpl(line));
        }
        return responses;
    }

    public void setSignature(byte[] signature) {
        this.signature = signature;
    }

    public byte[] getSignature() {
        return this.signature;
    }
}