package org.betonquest.betonquest.compatibility.holograms.lines;


public class TopXLine {
    final String playerName;
    final long count;

    /**
     * Creates a new instance of TopXLine
     *
     * @param playerName Name of player
     * @param count      Value of point
     */
    public TopXLine(String playerName, long count) {
        this.playerName = playerName;
        this.count = count;
    }
}
