package org.nfcresearch.qpolllab;

public final class PreloadTest {
    private static void check(boolean value, String message) {
        if (!value) throw new AssertionError(message);
    }

    public static void main(String[] args) throws Exception {
        ProtocolTest.Fake panel = new ProtocolTest.Fake();
        PanelProtocol protocol = new PanelProtocol(panel, panel);
        protocol.prepare();
        check(panel.d1 == 2, "profile read and write-time recheck");
        check(panel.d3 == 0 && panel.d4 == 0, "prepare sends no image/refresh");

        byte[] page = new byte[PanelProtocol.IMAGE_BYTES];
        protocol.writeImage(page);
        check(panel.d3 > 0 && panel.d3 < 476, "one complete compressed frame preloaded");
        check(panel.d4 == 0, "preload must never refresh");
        int packets = panel.d3;

        protocol.refresh(2000, 500);
        check(panel.d3 == packets, "fast turn must not resend D3");
        check(panel.d4 == 1, "fast turn refreshes once");
        check(panel.emitted("REFRESH_DONE"), "busy to idle confirmed");

        ProtocolTest.Fake hold = new ProtocolTest.Fake();
        hold.d4Busy = 1;
        PanelProtocol held = new PanelProtocol(hold, hold);
        held.prepare();
        held.writeImage(page);
        long before = hold.time;
        check(held.refreshHold(2000, 20000), "dry hold reports completion");
        check(hold.d4 == 2 && hold.de == 0, "6986 resends D4 once and hold never sends DE");
        check(hold.time - before >= 21000, "field held for 20 s after the retry pause");
        check(hold.emitted("REFRESH_DONE"), "dry hold completion event");

        ProtocolTest.Fake weak = new ProtocolTest.Fake();
        weak.failD4 = true;
        PanelProtocol weakProtocol = new PanelProtocol(weak, weak);
        weakProtocol.prepare();
        try {
            weakProtocol.refreshHold(2000, 20000);
            throw new AssertionError("failed D4 accepted");
        } catch (IllegalArgumentException expected) {
            check(weak.de == 0 && !weak.emitted("REFRESH_DONE"), "failed D4 stops without hold");
        }
        System.out.println("PreloadTest: PASS (D3 preloaded, D4-only page turn, dry hold)");
    }
}
