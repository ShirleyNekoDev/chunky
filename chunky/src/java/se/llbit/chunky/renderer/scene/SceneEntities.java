package se.llbit.chunky.renderer.scene;

import se.llbit.chunky.PersistentSettings;
import se.llbit.chunky.chunk.BlockPalette;
import se.llbit.chunky.chunk.ChunkData;
import se.llbit.chunky.entity.ArmorStand;
import se.llbit.chunky.entity.Entity;
import se.llbit.chunky.entity.Lectern;
import se.llbit.chunky.entity.PaintingEntity;
import se.llbit.chunky.entity.PlayerEntity;
import se.llbit.chunky.entity.Poseable;
import se.llbit.chunky.world.World;
import se.llbit.json.JsonArray;
import se.llbit.json.JsonObject;
import se.llbit.json.JsonValue;
import se.llbit.log.Log;
import se.llbit.math.Grid;
import se.llbit.math.Octree;
import se.llbit.math.Ray;
import se.llbit.math.Vector3;
import se.llbit.math.Vector3i;
import se.llbit.math.bvh.BVH;
import se.llbit.nbt.CompoundTag;
import se.llbit.nbt.ListTag;
import se.llbit.nbt.Tag;
import se.llbit.util.TaskTracker;
import se.llbit.util.mojangapi.MojangApi;
import se.llbit.util.mojangapi.PlayerSkin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class SceneEntities {

  /**
   * Entities in the scene.
   */
  private ArrayList<Entity> entities = new ArrayList<>();

  /**
   * Poseable entities in the scene.
   */
  private ArrayList<Entity> actors = new ArrayList<>();

  /**
   * Poseable entities in the scene.
   */
  private Map<PlayerEntity, JsonObject> profiles = new HashMap<>();

  private BVH bvh = BVH.EMPTY;
  private BVH actorBvh = BVH.EMPTY;


  /**
   * The BVH implementation to use
   */
  private String bvhImplementation = PersistentSettings.getBvhMethod();

  protected boolean renderActors = true;


  private final Scene scene;

  SceneEntities(Scene scene) {
    this.scene = scene;
  }

  public Collection<Entity> getEntities() {
    return entities;
  }

  public Collection<Entity> getActors() {
    return actors;
  }

  public String getBvhImplementation() {
    return bvhImplementation;
  }

  public void setBvhImplementation(String bvhImplementation) {
    this.bvhImplementation = bvhImplementation;
  }

  public synchronized void copyState(SceneEntities other) {
    entities = other.entities;
    actors.clear();
    actors.addAll(other.actors); // Create a copy so that entity changes can be reset.
    actors.trimToSize();
    profiles = other.profiles;
    bvh = other.bvh;
    actorBvh = other.actorBvh;

    renderActors = other.renderActors;
    bvhImplementation = other.bvhImplementation;
  }

  public boolean closestIntersection(Ray ray) {
    if (bvh.closestIntersection(ray)) {
      return true;
    }
    if (renderActors) {
      if (actorBvh.closestIntersection(ray)) {
        return true;
      }
    }
    return false;
  }

  public synchronized void loadPlayers(TaskTracker taskTracker, World world) {
    try (TaskTracker.Task task = taskTracker.task("(2/6) Loading entities")) {
      entities.clear();
      if (actors.isEmpty() && PersistentSettings.getLoadPlayers()) {
        // We don't load actor entities if some already exists. Loading actor entities
        // risks resetting posed actors when reloading chunks for an existing scene.
        actors.clear();
        profiles = new HashMap<>();
        Collection<PlayerEntity> players = world.playerEntities();
        int done = 1;
        int target = players.size();
        for (PlayerEntity entity : players) {
          entity.randomPose();
          task.update(target, done);
          done += 1;
          JsonObject profile;
          try {
            profile = MojangApi.fetchProfile(entity.uuid);
            PlayerSkin skin = MojangApi.getSkinFromProfile(profile);
            if (skin != null) {
              String skinUrl = skin.getUrl();
              if (skinUrl != null) {
                entity.skin = MojangApi.downloadSkin(skinUrl).getAbsolutePath();
              }
              entity.model = skin.getModel();
            }
          } catch (IOException e) {
            Log.error(e);
            profile = new JsonObject();
          }
          profiles.put(entity, profile);
          actors.add(entity);
        }
      }

      entities.trimToSize();
      actors.trimToSize();
    }
  }

  public void addPlayer(PlayerEntity player) {
    if (!actors.contains(player)) {
      profiles.put(player, new JsonObject());
      actors.add(player);
      scene.rebuildActorBvh(); // TODO
    } else {
      Log.warn("Failed to add player: entity already exists (" + player + ")");
    }
    actors.trimToSize();
  }

  public JsonObject getPlayerProfile(PlayerEntity entity) {
    if (profiles.containsKey(entity)) {
      return profiles.get(entity);
    } else {
      return new JsonObject();
    }
  }

  public synchronized void loadEntities(TaskTracker taskTracker, ChunkData chunkData, int yClipMin, int yClipMax) {
    // Load entities from the chunk:
    for (CompoundTag tag : chunkData.getEntities()) {
      Tag posTag = tag.get("Pos");
      if (posTag.isList()) {
        ListTag pos = posTag.asList();
        double x = pos.get(0).doubleValue();
        double y = pos.get(1).doubleValue();
        double z = pos.get(2).doubleValue();

        if (y >= yClipMin && y < yClipMax) {
          String id = tag.get("id").stringValue("");
          if (id.equals("minecraft:painting") || id.equals("Painting")) {
            // Before 1.12 paintings had id=Painting.
            // After 1.12 paintings had id=minecraft:painting.
            float yaw = tag.get("Rotation").get(0).floatValue();
            entities.add(
              new PaintingEntity(new Vector3(x, y, z), tag.get("Motive").stringValue(), yaw));
          } else if (id.equals("minecraft:armor_stand")) {
            actors.add(new ArmorStand(new Vector3(x, y, z), tag));
          }
        }
      }
    }
  }

  public void removeEntity(Entity player) {
    if (player instanceof PlayerEntity) {
      profiles.remove(player);
    }
    actors.remove(player);
    scene.rebuildActorBvh(); // TODO
  }

  public synchronized void addBlockEntity(Entity entity) {
    Grid emitterGrid = scene.getEmitterGrid();
    Vector3i origin = scene.origin;
    if (entity instanceof Poseable && !(entity instanceof Lectern && !((Lectern) entity).hasBook())) {
      // don't add the actor again if it was already loaded from json
      if (actors.stream().noneMatch(actor -> {
        if (actor.getClass().equals(entity.getClass())) {
          Vector3 distance = new Vector3(actor.position);
          distance.sub(entity.position);
          return distance.lengthSquared() < Ray.EPSILON;
        }
        return false;
      })) {
        actors.add(entity);
      }
    } else {
      entities.add(entity);
      if (emitterGrid != null) {
        for (Grid.EmitterPosition emitterPos : entity.getEmitterPosition()) {
          emitterPos.x -= origin.x;
          emitterPos.y -= origin.y;
          emitterPos.z -= origin.z;
          emitterGrid.addEmitter(emitterPos);
        }
      }
    }
  }

  public synchronized void addBlockEntity2(Entity blockEntity) {
    Grid emitterGrid = scene.getEmitterGrid();
    Vector3i origin = scene.origin;
    if (blockEntity instanceof Poseable) {
      // don't add the actor again if it was already loaded from json
      if (actors.stream().noneMatch(actor -> {
        if (actor.getClass().equals(blockEntity.getClass())) {
          Vector3 distance = new Vector3(actor.position);
          distance.sub(blockEntity.position);
          return distance.lengthSquared() < Ray.EPSILON;
        }
        return false;
      })) {
        actors.add(blockEntity);
      }
    } else {
      entities.add(blockEntity);
      if (emitterGrid != null) {
        for (Grid.EmitterPosition emitterPos : blockEntity.getEmitterPosition()) {
          emitterPos.x -= origin.x;
          emitterPos.y -= origin.y;
          emitterPos.z -= origin.z;
          emitterGrid.addEmitter(emitterPos);
        }
      }
    }

    /*
    switch (block) {
      case Block.HEAD_ID:
        entities.add(new SkullEntity(position, entityTag, metadata));
        break;
      case Block.WALL_BANNER_ID: {
        entities.add(new WallBanner(position, metadata, entityTag));
        break;
      }
    }
    */
  }

  public synchronized void trimToSize() {
    entities.trimToSize();
    actors.trimToSize();
  }

  public synchronized void clear() {
    entities.clear();
    actors.clear();
  }

  public synchronized void loadDataFromOctree(Octree worldOctree) {
    BlockPalette palette = scene.getPalette();
    Vector3i origin = scene.origin;
    for (Entity entity : actors) {
      entity.loadDataFromOctree(worldOctree, palette, origin);
    }
    for (Entity entity : entities) {
      entity.loadDataFromOctree(worldOctree, palette, origin);
    }
  }

  public synchronized void buildBvh(TaskTracker.Task task) {
    Vector3i origin = scene.origin;
    Vector3 worldOffset = new Vector3(-origin.x, -origin.y, -origin.z);
    bvh = BVH.Factory.create(bvhImplementation, entities, worldOffset, task);
  }

  public synchronized void buildActorBvh(TaskTracker.Task task) {
    Vector3i origin = scene.origin;
    Vector3 worldOffset = new Vector3(-origin.x, -origin.y, -origin.z);
    actorBvh = BVH.Factory.create(bvhImplementation, actors, worldOffset, task);
  }

  Optional<PlayerEntity> findAnyPlayer() {
    return actors.stream()
      .filter(entity -> entity instanceof PlayerEntity)
      .findAny()
      .map(entity -> (PlayerEntity) entity);
  }

  void writeToJson(JsonObject json) {
    json.add("renderActors", renderActors);

    JsonArray entityArray = new JsonArray();
    for (Entity entity : entities) {
      entityArray.add(entity.toJson());
    }
    if (!entityArray.isEmpty()) {
      json.add("entities", entityArray);
    }
    JsonArray actorArray = new JsonArray();
    for (Entity entity : actors) {
      actorArray.add(entity.toJson());
    }
    if (!actorArray.isEmpty()) {
      json.add("actors", actorArray);
    }

    json.add("bvhImplementation", bvhImplementation);
  }

  void loadFromJson(JsonObject json) {
    renderActors = json.get("renderActors").boolValue(renderActors);

    if (json.get("entities").isArray() || json.get("actors").isArray()) {
      entities.clear();
      actors.clear();
      // Previously poseable entities were stored in the entities array
      // rather than the actors array. In future versions only the actors
      // array should contain poseable entities.
      for (JsonValue element : json.get("entities").array()) {
        Entity entity = Entity.fromJson(element.object());
        if (entity != null) {
          if (entity instanceof PlayerEntity) {
            actors.add(entity);
          } else {
            entities.add(entity);
          }
        }
      }
      for (JsonValue element : json.get("actors").array()) {
        Entity entity = Entity.fromJson(element.object());
        actors.add(entity);
      }
    }

    bvhImplementation = json.get("bvhImplementation").asString(PersistentSettings.getBvhMethod());
  }
}
