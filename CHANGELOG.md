# Changelog

## Unreleased

- Accept the second registered FMSC APPID `00000001` in the D1 C0 check.
- New panel type "A0X · 左右半屏互换（原厂纵向扫描）" (`A0X`) for A0 boards on the
  factory vertical scan, where the A0 layout showed the halves swapped. On the
  tested Xiaomi 17 Pro the page currently appears as white text on black.
- When D8 asks for the default PIN, verify it a second time after the D8
  recheck, as the reference PN532 flow does.
- After enabling QTag and ReaderMode, ask the NFC service to resume polling
  (`INfcAdapter.resumePolling`). HyperOS can otherwise keep polling paused and
  skip the Q-Poll config, so the panel is not found (port of CLF184's fix in
  qpoll-lite 1.4.36).
- Dry-hold mode: optional charge before refresh (off / 5 / 10 / 20 s, default
  10 s). After the upload the field stays on without APDUs so the panel starts
  the waveform with a full store; for refreshes that stop after one or two
  flashes (cf. CLF184's pre-send charge in qpoll-lite).
- Dry-hold mode turns the field off right after each refresh round instead of
  at the next page turn, and is on by default on Android 16+ (as qpoll-lite
  does for SDK 36+).
- Keep the phone screen on while an e-ink session is active; the system turns
  the NFC field off with the screen, which cut refreshes short.
- Optional dry-hold refresh: after D4, keep the field for 30 s without DE
  polling and delay the system presence check past the hold (for NXP SN220
  phones that lose the tag during refresh). D4 is resent on `6986`.
- In dry-hold mode, a command that fails because the link dropped waits for
  the panel to be re-discovered, re-runs the D1/D8 checks and retries
  (up to 2 times) instead of ending the session. Low-voltage status words
  (6502/68CA/698A) wait 10 s in the field and retry (up to 3 times).
- Dry-hold mode follows qpoll-lite's Android 16+ / SN220U path: 2000 ms D4
  power-up, no preloading, and each page turn fully drops the field
  (ReaderMode and QTag off for 1 s) before re-raising it QTag-first and
  sending the full frame.
- Reading background defaults to following the system dark mode; native
  widgets and dialogs use a matching dark theme.
- Buttons keep an 8 dp visual gap between neighbours.
- The system navigation buttons stay visible, including in black-screen
  mode, and the bar is restored when leaving it.
- Tapping the panel button while still connecting no longer blacks out the
  phone screen.

## 0.3.9

- Final usable baseline for the Huawei 552x768 four-color NFC e-ink case.
- Local TXT bookshelf, chapter detection, pagination, search and bookmarks.
- Configurable font family, weight, size, line spacing and margins.
- Page turns by touch, horizontal swipe, volume keys and dark reading mode.
- Persistent Shizuku panel session.
- Next-page frame preloading and D4-only refresh on a preload hit.
- LZO1X compression and cached compression blocks.
- E-ink footer with chapter/page, current time and battery.
- Search result keyword highlighting.
- 32 MiB per-file import limit with streaming import progress.

## Earlier builds

0.1.0 through 0.3.8 were local iterative test builds. Their notes remain in the
Git history and GitHub Releases when available.
