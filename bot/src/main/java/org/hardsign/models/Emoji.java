package org.hardsign.models;

public enum Emoji {
    FaceWithTongue("\uD83D\uDE1B"),
    Pensive("😔"),
    Ok("✅"),
    WhiteQuestion("❔"),
    RedQuestion("❓"),
    Play("▶️"),
    Stop("⏹"),
    Back("↩️"),
    RedCross("❌"),
    YellowCircle("\uD83D\uDFE1"),
    GreenCircle("\uD83D\uDFE2"),
    TrashCan("\uD83D\uDDD1"),
    ArrowsCircle("\uD83D\uDD04"),
    Clipboard("\uD83D\uDCCB"),
    Memo("\uD83D\uDCDD"),
    Clock1("\uD83D\uDD50"),
    Calendar("\uD83D\uDCC6"),
    UpwardChart("\uD83D\uDCC8"),
    Gear("⚙️")
    ;

    private final String value;

    Emoji(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }
}
