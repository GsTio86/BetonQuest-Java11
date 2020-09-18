package pl.betoncraft.betonquest.conversation;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import pl.betoncraft.betonquest.BetonQuest;
import pl.betoncraft.betonquest.utils.PlayerConverter;

import java.util.ArrayList;

public class SimpleInterceptor implements Interceptor, Listener {

    protected final Conversation conv;
    protected final Player player;
    private final ArrayList<String> messages = new ArrayList<>();

    public SimpleInterceptor(final Conversation conv, final String playerID) {
        this.conv = conv;
        this.player = PlayerConverter.getPlayer(playerID);
        Bukkit.getPluginManager().registerEvents(this, BetonQuest.getInstance());
    }

    /**
     * Send message, bypassing Interceptor
     */
    @Override
    public void sendMessage(final String message) {
        player.spigot().sendMessage(TextComponent.fromLegacyText(message));
    }

    @Override
    public void sendMessage(final BaseComponent... message) {
        player.spigot().sendMessage(message);
    }

    /**
     * This method prevents concurrent list modification
     */
    private synchronized void addMessage(final String message) {
        messages.add(message);
    }

    @Override
    public void end() {
        HandlerList.unregisterAll(this);

        // Send all messages to player
        for (final String message : messages) {
            player.sendMessage(message);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onChat(final AsyncPlayerChatEvent event) {
        // store all messages so they can be displayed to the player
        // once the conversation is finished
        if (event.getPlayer() != player && event.getRecipients().contains(player)) {
            event.getRecipients().remove(player);
            addMessage(String.format(event.getFormat(), event.getPlayer().getDisplayName(), event.getMessage()));
        }
    }
}
