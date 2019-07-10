package com.alibaba.fastjson.validator;

public class JSONBytes implements JSONConstant {
    private byte[] jsonBytes;
    private int position;
    private int maxPosition;

    JSONBytes(byte[] Bytes) {
        jsonBytes = Bytes;
        position = 0;
        maxPosition = Bytes.length;
    }

    public int len() {
        return jsonBytes.length;
    }

    public byte [] getJsonBytes() {
        return jsonBytes;
    }

    public int getMaxPosition() {
        return maxPosition;
    }

    public int getPosition() {
        return position;
    }

    public String getPartOfjson() {
        return new String(jsonBytes, 0, 40);
    }

    public String getPartOfjson(int start, int len) {
        return new String(jsonBytes,start, len);
    }

    public void validateLen(int len) {
        if(maxPosition < position + len) {
            throw new UnexceptedEOFException(getPartOfjson());
        }
    }

    public void moveX(int x) {
        if(x != 0) {
            validateLen(x);
            int nLen = maxPosition - position - x;
            byte[] nJsonBytes = new byte[nLen];
            for(int i = 0; i < nLen; i++) {
                nJsonBytes[i] = jsonBytes[i + x];
            }
            jsonBytes = nJsonBytes;
            position += x;
        }
    }

    public void moveOne() {
        moveX(1);
    }

    public char byteX(int x) {
        validateLen(x);
        return (char)jsonBytes[x];
    }

    public char firstByte() {
        return byteX(0);
    }

    /**
     * After reading a byte, we need to Trim the spaces before next valid byte
     * Tab, Space, Enter will be ignore in non string scope of Json
     * We only need to trim the space of left side in bytes.
     */
    public void TrimLeftSpace(){
        int nmove = 0;
        for(byte b : jsonBytes) {
            if(!Character.isWhitespace(b)) {
                moveX(nmove);
                return;
            }
            nmove++;
        }
        moveX(len());
    }

    /**
     * Expect function verify that if the first byte of jsonBytes is the target byte
     */
    public void Expect(char b) {
        if(firstByte() != b) {
            throw new InvalidJSONException("expect character:" + (char)b + "\n" + getPartOfjson());
        }
        moveOne();
        TrimLeftSpace();
    }

}
