package dev.viper.utils.worldedit;

import com.fastasyncworldedit.core.FaweAPI;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import org.bukkit.Location;

public class AsyncWorldEditor extends SyncWorldEditor {
   public void paste(Clipboard clipboard, Location corner1, boolean ignoreAir) {
      FaweAPI.getTaskManager().async(() -> super.paste(clipboard, corner1, ignoreAir));
   }

   public void save(Clipboard clipboard, String path) {
      FaweAPI.getTaskManager().async(() -> super.save(clipboard, path));
   }
}
