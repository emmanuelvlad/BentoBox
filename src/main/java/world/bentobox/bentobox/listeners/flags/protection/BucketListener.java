package world.bentobox.bentobox.listeners.flags.protection;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.MushroomCow;
import org.bukkit.entity.Player;
import org.bukkit.entity.TropicalFish;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;

import world.bentobox.bentobox.api.flags.FlagListener;
import world.bentobox.bentobox.lists.Flags;

/**
 * Handles interaction with beds
 * Note - bed protection from breaking or placing is done elsewhere.
 * @author tastybento
 *
 */
public class BucketListener extends FlagListener {

    /**
     * Prevents emptying of buckets
     * @param e - event
     */
    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onBucketEmpty(final PlayerBucketEmptyEvent e) {
        // This is where the water or lava actually will be dumped
        Block dumpBlock = e.getBlockClicked().getRelative(e.getBlockFace());
        checkIsland(e, e.getPlayer(), dumpBlock.getLocation(), Flags.BUCKET);
    }

    /**
     * Prevents collecting of lava, water, milk. If bucket use is denied in general, it is blocked.
     * @param e - event
     */
    @EventHandler(priority = EventPriority.LOW)
    public void onBucketFill(final PlayerBucketFillEvent e) {
        Player p = e.getPlayer();
        Location l = e.getBlockClicked().getLocation();
        // Check filling of various liquids
        switch (e.getItemStack().getType()) {
        case LAVA_BUCKET -> checkIsland(e, p, l, Flags.COLLECT_LAVA);
        case WATER_BUCKET -> checkIsland(e, p, l, Flags.COLLECT_WATER);
        case MILK_BUCKET -> checkIsland(e, p, l, Flags.MILKING);
        default -> checkIsland(e, p, l, Flags.BUCKET);
        }
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onTropicalFishScooping(final PlayerInteractEntityEvent e) {
        if (e.getRightClicked() instanceof TropicalFish && e.getPlayer().getInventory().getItemInMainHand().getType().equals(Material.WATER_BUCKET)) {
            checkIsland(e, e.getPlayer(), e.getRightClicked().getLocation(), Flags.FISH_SCOOPING);
        }
    }


    /**
     * Prevents collecting mushroom strew from MushroomCow if player does not have access to Milking flag.
     * @param e - event
     */
    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onBowlFill(final PlayerInteractEntityEvent e) {
        if (e.getRightClicked() instanceof MushroomCow && e.getPlayer().getInventory().getItemInMainHand().getType().equals(Material.BOWL)) {
            checkIsland(e, e.getPlayer(), e.getRightClicked().getLocation(), Flags.MILKING);
        }
    }
}