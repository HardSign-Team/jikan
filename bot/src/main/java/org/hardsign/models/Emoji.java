package org.hardsign.models;

public enum Emoji {
    FaceWithTongue("\uD83D\uDE1B"),
    Pensive("đ"),
    Ok("â"),
    WhiteQuestion("â"),
    RedQuestion("â"),
    Play("âļī¸"),
    Stop("âš"),
    Back("âŠī¸"),
    RedCross("â"),
    YellowCircle("\uD83D\uDFE1"),
    GreenCircle("\uD83D\uDFE2"),
    TrashCan("\uD83D\uDDD1"),
    ArrowsCircle("\uD83D\uDD04"),
    Clipboard("\uD83D\uDCCB"),
    Memo("\uD83D\uDCDD"),
    Clock1("\uD83D\uDD50"),
    Calendar("\uD83D\uDCC6"),
    UpwardChart("\uD83D\uDCC8")
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
