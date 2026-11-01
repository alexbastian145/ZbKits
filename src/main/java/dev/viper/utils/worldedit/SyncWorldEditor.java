package dev.viper.utils.worldedit;

import com.fastasyncworldedit.core.FaweAPI;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.extent.clipboard.BlockArrayClipboard;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.BuiltInClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardWriter;
import com.sk89q.worldedit.function.operation.ForwardExtentCopy;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.session.ClipboardHolder;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;
import org.bukkit.Location;
import org.bukkit.World;

public class SyncWorldEditor {
   public Clipboard copy(Location corner1, Location corner2, boolean shouldCopyEntities) {
      BlockVector3 bottom = BlockVector3.at(corner1.getBlockX(), corner1.getBlockY(), corner1.getBlockZ());
      BlockVector3 top = BlockVector3.at(corner2.getBlockX(), corner2.getBlockY(), corner2.getBlockZ());
      CuboidRegion region = new CuboidRegion(bottom, top);
      Clipboard clipboard = new BlockArrayClipboard(region);
      clipboard.setOrigin(BlockVector3.at(corner1.getX(), corner1.getY(), corner1.getZ()));

      try {
         EditSession editSession = WorldEdit.getInstance().newEditSession(FaweAPI.getWorld(((World)Objects.requireNonNull(corner1.getWorld())).getName()));

         try {
            ForwardExtentCopy forwardExtentCopy = new ForwardExtentCopy(editSession, region, clipboard, region.getMinimumPoint());
            forwardExtentCopy.setCopyingEntities(shouldCopyEntities);
            Operations.complete(forwardExtentCopy);
         } catch (Throwable var12) {
            if (editSession != null) {
               try {
                  editSession.close();
               } catch (Throwable var11) {
                  var12.addSuppressed(var11);
               }
            }

            throw var12;
         }

         if (editSession != null) {
            editSession.close();
         }
      } catch (Exception e) {
         e.printStackTrace();
      }

      return clipboard;
   }

   public void paste(Clipboard clipboard, Location corner1, boolean ignoreAir) {
      if (clipboard != null) {
         try {
            EditSession editSession = WorldEdit.getInstance().newEditSession(FaweAPI.getWorld(((World)Objects.requireNonNull(corner1.getWorld())).getName()));

            try {
               Operation operation = (new ClipboardHolder(clipboard)).createPaste(editSession).to(BlockVector3.at(corner1.getX(), corner1.getY(), corner1.getZ())).ignoreAirBlocks(ignoreAir).build();
               Operations.complete(operation);
            } catch (Throwable var8) {
               if (editSession != null) {
                  try {
                     editSession.close();
                  } catch (Throwable var7) {
                     var8.addSuppressed(var7);
                  }
               }

               throw var8;
            }

            if (editSession != null) {
               editSession.close();
            }
         } catch (Exception e) {
            e.printStackTrace();
         }

      }
   }

   public void save(Clipboard clipboard, String path) {
      File file = new File(path);

      try {
         ClipboardWriter writer = BuiltInClipboardFormat.FAST.getWriter(new FileOutputStream(file));
         writer.write(clipboard);
         writer.close();
      } catch (IOException e) {
         e.printStackTrace();
      }

   }

   public Clipboard load(String path) {
      File file = new File(path);
      if (!file.exists()) {
         return null;
      } else {
         ClipboardFormat format = ClipboardFormats.findByFile(file);

         try {
            ClipboardReader reader = ((ClipboardFormat)Objects.requireNonNull(format)).getReader(new FileInputStream(file));

            Clipboard clipboard;
            try {
               clipboard = reader.read();
            } catch (Throwable var9) {
               if (reader != null) {
                  try {
                     reader.close();
                  } catch (Throwable var8) {
                     var9.addSuppressed(var8);
                  }
               }

               throw var9;
            }

            if (reader != null) {
               reader.close();
            }

            return clipboard;
         } catch (Exception e) {
            e.printStackTrace();
            return null;
         }
      }
   }
}
