package pl.betoncraft.betonquest.notify;

import org.bukkit.entity.Player;
import pl.betoncraft.betonquest.utils.Utils;

import java.util.Collection;
import java.util.Map;

/**
 * Use Title Popup for Notification
 * <p>
 * Data Valuues:
 * * fadeIn: seconds to fade in
 * * stay: seconds to stay
 * * fadeOut: seconds to fade out
 * * subTitle: the subtitle to show, else blank
 */
public class TitleNotifyIO extends NotifyIO {


    // Variables

    private final int fadeIn;
    private final int stay;
    private final int fadeOut;
    private final String subTitle;


    public TitleNotifyIO(final Map<String, String> data) {
        super(data);

        fadeIn = Integer.valueOf(data.getOrDefault("fadein", "10"));
        stay = Integer.valueOf(data.getOrDefault("stay", "70"));
        fadeOut = Integer.valueOf(data.getOrDefault("fadeout", "20"));
        subTitle = getData().getOrDefault("subtitle", "").replace("_", " ");
    }

    @Override
    public void sendNotify(final String message, final Collection<? extends Player> players) {
        for (final Player player : players) {
            player.sendTitle(Utils.format(message), Utils.format(subTitle), fadeIn, stay, fadeOut);
        }

        sendNotificationSound(players);
    }
}
