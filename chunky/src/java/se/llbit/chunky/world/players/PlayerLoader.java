package se.llbit.chunky.world.players;

import se.llbit.chunky.world.PlayerEntityData;
import se.llbit.log.Log;
import se.llbit.nbt.NamedTag;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Set;
import java.util.zip.GZIPInputStream;

public interface PlayerLoader {
  public Set<PlayerEntityData> load();

  private static void loadAdditionalPlayers(File worldDirectory, Set<PlayerEntityData> playerEntities) {
    loadPlayerData(new File(worldDirectory, "players"), playerEntities);
    loadPlayerData(new File(worldDirectory, "playerdata"), playerEntities);
  }

  private static void loadPlayerData(File playerdata, Set<PlayerEntityData> playerEntities) {
    if (playerdata.isDirectory()) {
      File[] players = playerdata.listFiles();
      if (players != null) {
        for (File player : players) {
          try (DataInputStream in = new DataInputStream(
            new GZIPInputStream(new FileInputStream(player)))) {
            playerEntities.add(new PlayerEntityData(NamedTag.read(in).unpack()));
          } catch (IOException e) {
            Log.infof("Could not read player data file '%s'", player.getAbsolutePath());
          }
        }
      }
    }
  }
}
