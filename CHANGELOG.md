# Changelog

## Unreleased

- Accept the second registered FMSC APPID `00000001` in the D1 C0 check.
- Optional dry-hold refresh: after D4, keep the field for 30 s without DE
  polling and delay the system presence check past the hold (for NXP SN220
  phones that lose the tag during refresh). D4 is resent on `6986`.
- In dry-hold mode, a command that fails because the link dropped waits for
  the panel to be re-discovered, re-runs the D1/D8 checks and retries
  (up to 2 times) instead of ending the session. Low-voltage status words
  (6502/68CA/698A) wait 10 s in the field and retry (up to 3 times).
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
