package org.nfcresearch.qpolllab;

/** Huawei JD79665AA panel variants; selection is physical, never inferred from D1. */
public enum PanelType {
    /**
     * A0X: A0 boards that keep the factory vertical scan (A1 field 00120030FFFFFF).
     * On such a board the A0 layout showed the left/right halves swapped, so the
     * halves are exchanged before the A0 even/odd interleave (same as qpoll-lite's A0X).
     */
    A0, A1, A0X;
    public static PanelType parse(String value) {
        if("A0".equals(value))return A0;
        if("A1".equals(value))return A1;
        if("A0X".equals(value))return A0X;
        throw new IllegalArgumentException("请先选择屏幕类型 A0、A1 或 A0X");
    }
    /** Controller row that receives logical source row {@code row}. */
    int target(int row) {
        final int rows=PanelProtocol.WIDTH,half=rows/2;
        if(this==A1)return row;
        if(this==A0X)row=row<half?row+half:row-half;
        return row<half?2*row:2*rows-1-2*row;
    }
    public byte[] pack(byte[] codes) {
        return map(PanelProtocol.packCodes(codes,PanelProtocol.WIDTH,PanelProtocol.HEIGHT),false);
    }
    private byte[] map(byte[] raw,boolean inverse) {
        if(raw.length!=PanelProtocol.IMAGE_BYTES)throw new IllegalArgumentException("屏幕数据长度错误");
        if(this==A1)return raw.clone();
        byte[] out=new byte[raw.length];
        // Transposed controller rows: first half -> even rows, second half -> reversed odd rows.
        final int rows=PanelProtocol.WIDTH,stride=PanelProtocol.HEIGHT/4;
        for(int row=0;row<rows;row++) {
            int target=target(row);
            System.arraycopy(raw,(inverse?target:row)*stride,out,(inverse?row:target)*stride,stride);
        }
        return out;
    }
    public int[] preview(byte[] packed) {
        return ImageRaster.preview(map(packed,true),PanelProtocol.WIDTH,PanelProtocol.HEIGHT);
    }
    /** Stored scan 00 uses bottom-up pixels; scan 01 uses top-down pixels in each row. */
    public static byte[] forScan(byte[] packed,boolean topDown) {
        if(packed.length!=PanelProtocol.IMAGE_BYTES)throw new IllegalArgumentException("屏幕数据长度错误");
        if(!topDown)return packed.clone();
        int stride=PanelProtocol.HEIGHT/4;byte[] out=new byte[packed.length];
        for(int row=0;row<PanelProtocol.WIDTH;row++)for(int i=0;i<stride;i++) {
            int b=packed[row*stride+stride-1-i]&255;
            out[row*stride+i]=(byte)(((b&3)<<6)|((b&12)<<2)|((b&48)>>2)|((b&192)>>6));
        }
        return out;
    }
}
