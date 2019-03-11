package se.llbit.chunky.chunk;

import se.llbit.chunky.block.*;
import se.llbit.chunky.resources.Texture;
import se.llbit.nbt.Tag;
import se.llbit.util.NotNull;

public class TagBlockSpec implements BlockSpec {
  private static final int MAGIC = 0xE6FFE636;
  public final Tag tag;

  public TagBlockSpec(@NotNull Tag tag) {
    this.tag = tag;
  }

  @Override public int hashCode() {
    return MAGIC ^ tag.hashCode();
  }

  @Override public boolean equals(Object obj) {
    return (obj instanceof TagBlockSpec)
        && ((TagBlockSpec) obj).tag.equals(tag);
  }

  /**
   * Converts NBT block data to Chunky block object.
   */
  @Override public Block toBlock() {
    // Reference: https://minecraft.gamepedia.com/Java_Edition_data_values#Blocks
    String name = tag.get("Name").stringValue("minecraft:air");
    if (name.startsWith("minecraft:")) {
      name = name.substring(10);
      // TODO: convert all old block IDs to the new block types.
      // TODO: clean up - register block loaders in hash map instead?
      switch (name) {
        case "air":
        case "cave_air":
        case "void_air":
          return Air.INSTANCE;
        case "infested_stone":
        case "stone":
          return new MinecraftBlock(name, Texture.stone);
        case "granite":
          return new MinecraftBlock(name, Texture.granite);
        case "polished_granite":
          return new MinecraftBlock(name, Texture.smoothGranite);
        case "diorite":
          return new MinecraftBlock(name, Texture.diorite);
        case "polished_diorite":
          return new MinecraftBlock(name, Texture.smoothDiorite);
        case "andesite":
          return new MinecraftBlock(name, Texture.andesite);
        case "polished_andesite":
          return new MinecraftBlock(name, Texture.smoothAndesite);
        case "grass_block":
          return snowCovered(tag, new GrassBlock());
        case "dirt":
          return new MinecraftBlock(name, Texture.dirt);
        case "coarse_dirt":
          return new MinecraftBlock(name, Texture.coarseDirt);
        case "podzol":
          return snowCovered(tag,
              new TexturedBlock(name, Texture.podzolSide, Texture.podzolTop, Texture.dirt));
        case "infested_cobblestone":
        case "cobblestone":
          return new MinecraftBlock(name, Texture.cobblestone);
        case "oak_planks":
          return new MinecraftBlock(name, Texture.oakPlanks);
        case "spruce_planks":
          return new MinecraftBlock(name, Texture.sprucePlanks);
        case "birch_planks":
          return new MinecraftBlock(name, Texture.birchPlanks);
        case "jungle_planks":
          return new MinecraftBlock(name, Texture.jungleTreePlanks);
        case "acacia_planks":
          return new MinecraftBlock(name, Texture.acaciaPlanks);
        case "dark_oak_planks":
          return new MinecraftBlock(name, Texture.darkOakPlanks);
        case "oak_sapling":
          return new SpriteBlock(name, Texture.oakSapling);
        case "spruce_sapling":
          return new SpriteBlock(name, Texture.spruceSapling);
        case "birch_sapling":
          return new SpriteBlock(name, Texture.birchSapling);
        case "jungle_sapling":
          return new SpriteBlock(name, Texture.jungleSapling);
        case "acacia_sapling":
          return new SpriteBlock(name, Texture.acaciaSapling);
        case "dark_oak_sapling":
          return new SpriteBlock(name, Texture.darkOakSapling);
        case "water": {
          int level = 0;
          try {
            level = Integer.parseInt(tag.get("Properties").get("level").stringValue("0"));
          } catch (NumberFormatException ignored) {
          }
          return new Water(level);
        }
        case "bubble_column":
          return new UnknownBlock(name);
        case "lava": {
          int level = 0;
          try {
            level = Integer.parseInt(tag.get("Properties").get("level").stringValue("0"));
          } catch (NumberFormatException ignored) {
          }
          return new Lava(level);
        }
        case "bedrock":
          return new MinecraftBlock(name, Texture.bedrock);
        case "sand":
          return new MinecraftBlock(name, Texture.sand);
        case "red_sand":
          return new MinecraftBlock(name, Texture.redSand);
        case "gravel":
          return new MinecraftBlock(name, Texture.gravel);
        case "gold_ore":
          return new MinecraftBlock(name, Texture.goldOre);
        case "iron_ore":
          return new MinecraftBlock(name, Texture.ironOre);
        case "coal_ore":
          return new MinecraftBlock(name, Texture.coalOre);
        case "oak_log":
          return log(tag,  Texture.oakWood, Texture.oakWoodTop);
        case "spruce_log":
          return log(tag,  Texture.spruceWood, Texture.spruceWoodTop);
        case "birch_log":
          return log(tag,  Texture.birchWood, Texture.birchWoodTop);
        case "jungle_log":
          return log(tag,  Texture.jungleWood, Texture.jungleTreeTop);
        case "acacia_log":
          return log(tag,  Texture.acaciaWood, Texture.acaciaWoodTop);
        case "dark_oak_log":
          return log(tag,  Texture.darkOakWood, Texture.darkOakWoodTop);
        case "stripped_oak_log":
          return log(tag,  Texture.strippedOakLog, Texture.strippedOakLogTop);
        case "stripped_spruce_log":
          return log(tag,  Texture.strippedSpruceLog, Texture.strippedSpruceLogTop);
        case "stripped_birch_log":
          return log(tag,  Texture.strippedBirchLog, Texture.strippedBirchLogTop);
        case "stripped_jungle_log":
          return log(tag,  Texture.strippedJungleLog, Texture.strippedJungleLogTop);
        case "stripped_acacia_log":
          return log(tag,  Texture.strippedAcaciaLog, Texture.strippedAcaciaLogTop);
        case "stripped_dark_oak_log":
          return log(tag,  Texture.strippedDarkOakLog, Texture.strippedDarkOakLogTop);
        case "stripped_oak_wood":
          return new MinecraftBlock(name, Texture.strippedOakLog);
        case "stripped_spruce_wood":
          return new MinecraftBlock(name, Texture.strippedSpruceLog);
        case "stripped_birch_wood":
          return new MinecraftBlock(name, Texture.strippedBirchLog);
        case "stripped_jungle_wood":
          return new MinecraftBlock(name, Texture.strippedJungleLog);
        case "stripped_acacia_wood":
          return new MinecraftBlock(name, Texture.strippedAcaciaLog);
        case "stripped_dark_oak_wood":
          return new MinecraftBlock(name, Texture.strippedDarkOakLog);
        case "oak_wood":
          return new MinecraftBlock(name, Texture.oakWood);
        case "spruce_wood":
          return new MinecraftBlock(name, Texture.spruceWood);
        case "birch_wood":
          return new MinecraftBlock(name, Texture.birchWood);
        case "jungle_wood":
          return new MinecraftBlock(name, Texture.jungleWood);
        case "acacia_wood":
          return new MinecraftBlock(name, Texture.acaciaWood);
        case "dark_oak_wood":
          return new MinecraftBlock(name, Texture.darkOakWood);
        case "oak_leaves":
          return new Leaves(name, Texture.oakLeaves);
        case "spruce_leaves":
          return new Leaves(name, Texture.spruceLeaves);
        case "birch_leaves":
          return new Leaves(name, Texture.birchLeaves);
        case "jungle_leaves":
          return new Leaves(name, Texture.jungleTreeLeaves);
        case "acacia_leaves":
          return new Leaves(name, Texture.acaciaLeaves);
        case "dark_oak_leaves":
          return new Leaves(name, Texture.darkOakLeaves);
        case "sponge":
          return new MinecraftBlock(name, Texture.sponge);
        case "wet_sponge":
          return new MinecraftBlock(name, Texture.wetSponge);
        case "glass":
          return new Glass(name, Texture.glass);
        case "lapis_ore":
          return new MinecraftBlock(name, Texture.lapisOre);
        case "lapis_block":
          return new MinecraftBlock(name, Texture.lapisBlock);
        case "dispenser":
          return dispenser(tag);
        case "sandstone":
          return new TexturedBlock(name, Texture.sandstoneSide,
              Texture.sandstoneTop, Texture.sandstoneBottom);
        case "chiseled_sandstone":
          return new TexturedBlock(name, Texture.sandstoneDecorated,
              Texture.sandstoneTop, Texture.sandstoneBottom);
        case "cut_sandstone":
          return new TexturedBlock(name, Texture.sandstoneCut,
              Texture.sandstoneTop, Texture.sandstoneBottom);
        case "note_block":
          return new MinecraftBlock(name, Texture.jukeboxSide);
        case "powered_rail":
          return powered_rail(tag);
        case "detector_rail":
          return detector_rail(tag);
        case "sticky_piston":
          return piston(tag, true);
        case "cobweb":
          return new SpriteBlock(name, Texture.cobweb);
        case "grass":
          return new Grass();
        case "fern":
          return new Fern();
        case "dead_bush":
          return new SpriteBlock(name, Texture.deadBush);
        case "seagrass":
          // TODO 1.13
          return new UnknownBlock(name);
        case "tall_seagrass":
          // TODO 1.13
          return new UnknownBlock(name);
        case "sea_pickle":
          // TODO 1.13
          return new UnknownBlock(name);
        case "piston":
          return piston(tag, false);
        case "piston_head":
          return pistonHead(tag);
        case "moving_piston":
          // Invisible.
          return Air.INSTANCE;
        case "white_wool":
          return new MinecraftBlock(name, Texture.whiteWool);
        case "orange_wool":
          return new MinecraftBlock(name, Texture.orangeWool);
        case "magenta_wool":
          return new MinecraftBlock(name, Texture.magentaWool);
        case "light_blue_wool":
          return new MinecraftBlock(name, Texture.lightBlueWool);
        case "yellow_wool":
          return new MinecraftBlock(name, Texture.yellowWool);
        case "lime_wool":
          return new MinecraftBlock(name, Texture.limeWool);
        case "pink_wool":
          return new MinecraftBlock(name, Texture.pinkWool);
        case "gray_wool":
          return new MinecraftBlock(name, Texture.grayWool);
        case "light_gray_wool":
          return new MinecraftBlock(name, Texture.lightGrayWool);
        case "cyan_wool":
          return new MinecraftBlock(name, Texture.cyanWool);
        case "purple_wool":
          return new MinecraftBlock(name, Texture.purpleWool);
        case "blue_wool":
          return new MinecraftBlock(name, Texture.blueWool);
        case "brown_wool":
          return new MinecraftBlock(name, Texture.brownWool);
        case "green_wool":
          return new MinecraftBlock(name, Texture.greenWool);
        case "red_wool":
          return new MinecraftBlock(name, Texture.redWool);
        case "black_wool":
          return new MinecraftBlock(name, Texture.blackWool);
        case "dandelion":
          return new SpriteBlock(name, Texture.dandelion);
        case "poppy":
          return new SpriteBlock(name, Texture.poppy);
        case "blue_orchid":
          return new SpriteBlock(name, Texture.blueOrchid);
        case "allium":
          return new SpriteBlock(name, Texture.allium);
        case "azure_bluet":
          return new SpriteBlock(name, Texture.azureBluet);
        case "red_tulip":
          return new SpriteBlock(name, Texture.redTulip);
        case "orange_tulip":
          return new SpriteBlock(name, Texture.orangeTulip);
        case "white_tulip":
          return new SpriteBlock(name, Texture.whiteTulip);
        case "pink_tulip":
          return new SpriteBlock(name, Texture.pinkTulip);
        case "oxeye_daisy":
          return new SpriteBlock(name, Texture.oxeyeDaisy);
        case "cornflower":
          return new UnknownBlock(name);
        case "lily_of_the_valley":
          return new UnknownBlock(name);
        case "wither_rose":
          return new UnknownBlock(name);
        case "brown_mushroom":
          return new SpriteBlock(name, Texture.brownMushroom);
        case "red_mushroom":
          return new SpriteBlock(name, Texture.redMushroom);
        case "gold_block":
          return new MinecraftBlock(name, Texture.goldBlock);
        case "iron_block":
          return new MinecraftBlock(name, Texture.ironBlock);
        case "oak_slab":
          return slab(tag, Texture.oakPlanks);
        case "spruce_slab":
          return slab(tag, Texture.sprucePlanks);
        case "birch_slab":
          return slab(tag, Texture.birchPlanks);
        case "jungle_slab":
          return slab(tag, Texture.jungleTreePlanks);
        case "acacia_slab":
          return slab(tag, Texture.acaciaPlanks);
        case "dark_oak_slab":
          return slab(tag, Texture.darkOakPlanks);
        case "stone_slab":
        case "smooth_stone_slab": // Rename of stone_slab in 1.14.
          return slab(tag, Texture.slabSide, Texture.slabTop);
        case "sandstone_slab":
          return slab(tag, Texture.sandstoneSide, Texture.sandstoneTop);
        case "petrified_oak_slab":
          return slab(tag, Texture.oakPlanks);
        case "cobblestone_slab":
          return slab(tag, Texture.cobblestone);
        case "brick_slab":
          return slab(tag, Texture.brick);
        case "stone_brick_slab":
          return slab(tag, Texture.stoneBrick);
        case "nether_brick_slab":
          return slab(tag, Texture.netherBrick);
        case "quartz_slab":
          return slab(tag, Texture.quartzSide, Texture.quartzTop);
        case "red_sandstone_slab":
          return slab(tag, Texture.redSandstoneSide, Texture.redSandstoneTop);
        case "purpur_slab":
          return slab(tag, Texture.purpurBlock);
        case "prismarine_slab":
          return slab(tag, Texture.prismarine);
        case "prismarine_brick_slab":
          return slab(tag, Texture.prismarineBricks);
        case "dark_prismarine_slab":
          return slab(tag, Texture.darkPrismarine);
        case "smooth_quartz":
          return new UnknownBlock(name);
        case "smooth_red_sandstone":
          return new MinecraftBlock(name, Texture.redSandstoneTop);
        case "smooth_sandstone":
          return new MinecraftBlock(name, Texture.sandstoneTop);
        case "smooth_stone":
          return new MinecraftBlock(name, Texture.slabTop);
        case "bricks":
          return new MinecraftBlock(name, Texture.brick);
        case "tnt":
          return new TexturedBlock(name, Texture.tntSide, Texture.tntTop, Texture.tntBottom);
        case "bookshelf":
          return new TexturedBlock(name, Texture.bookshelf, Texture.oakPlanks);
        case "mossy_cobblestone":
          return new MinecraftBlock(name, Texture.mossStone);
        case "obsidian":
          return new MinecraftBlock(name, Texture.obsidian);
        case "torch":
          return new Torch(name, Texture.torch);
        case "wall_torch":
          return wall_torch(tag, Texture.torch);
        case "end_rod":
          return end_rod(tag);
        case "chorus_plant":
          // TODO
          return new UnknownBlock(name);
        case "chorus_flower":
          // TODO
          return new UnknownBlock(name);
        case "purpur_block":
          return new MinecraftBlock(name, Texture.purpurBlock);
        case "purpur_pillar":
          return log(tag,  Texture.purpurPillarSide, Texture.purpurPillarTop);
        case "purpur_stairs":
          return stairs(tag, Texture.purpurBlock);
        case "oak_stairs":
          return stairs(tag, Texture.oakPlanks);
        case "spruce_stairs":
          return stairs(tag, Texture.sprucePlanks);
        case "birch_stairs":
          return stairs(tag, Texture.birchPlanks);
        case "jungle_stairs":
          return stairs(tag, Texture.jungleTreePlanks);
        case "acacia_stairs":
          return stairs(tag, Texture.acaciaPlanks);
        case "dark_oak_stairs":
          return stairs(tag, Texture.darkOakPlanks);
        case "chest":
          return chest(tag);
        case "diamond_ore":
          return new MinecraftBlock(name, Texture.diamondOre);
        case "diamond_block":
          return new MinecraftBlock(name, Texture.diamondBlock);
        case "crafting_table":
          return new TexturedBlock(name, Texture.workbenchFront, Texture.workbenchSide,
              Texture.workbenchSide, Texture.workbenchFront, Texture.workbenchTop, Texture.oakPlanks);
        case "farmland": {
          int moisture = 0;
          try {
            moisture = Integer.parseInt(tag.get("Properties").get("moisture").stringValue("0"));
          } catch (NumberFormatException ignored) {
          }
          return new Farmland(moisture);
        }
        case "furnace":
          return furnace(tag);
        case "ladder":
          // TODO
          return new UnknownBlock(name);
        case "rail":
          return rail(tag, Texture.rails);
        case "cobblestone_stairs":
          return stairs(tag, Texture.cobblestone);
        case "lever":
          // TODO
          return new UnknownBlock(name);
        case "stone_pressure_plate":
          // TODO
          return new UnknownBlock(name);
        case "oak_pressure_plate":
          // TODO
          return new UnknownBlock(name);
        case "spruce_pressure_plate":
          // TODO
          return new UnknownBlock(name);
        case "birch_pressure_plate":
          // TODO
          return new UnknownBlock(name);
        case "jungle_pressure_plate":
          // TODO
          return new UnknownBlock(name);
        case "acacia_pressure_plate":
          // TODO
          return new UnknownBlock(name);
        case "dark_oak_pressure_plate":
          // TODO
          return new UnknownBlock(name);
        case "redstone_ore":
          return new MinecraftBlock(name, Texture.redstoneOre);
        case "redstone_torch":
          // TODO
          return new UnknownBlock(name);
        case "redstone_wall_torch":
          // TODO
          return new UnknownBlock(name);
        case "stone_button":
          // TODO
          return new UnknownBlock(name);
        case "snow":
          return new MinecraftBlock(name, Texture.snowBlock);
        case "ice":
          return new MinecraftBlock(name, Texture.ice);
        case "snow_block":
          return new MinecraftBlock(name, Texture.snowBlock);
        case "cactus":
          // TODO
          return new UnknownBlock(name);
        case "clay":
          return new MinecraftBlock(name, Texture.clay);
        case "jukebox":
          // TODO
          return new UnknownBlock(name);
        case "oak_fence":
          // TODO
          return new UnknownBlock(name);
        case "spruce_fence":
          // TODO
          return new UnknownBlock(name);
        case "birch_fence":
          // TODO
          return new UnknownBlock(name);
        case "jungle_fence":
          // TODO
          return new UnknownBlock(name);
        case "acacia_fence":
          // TODO
          return new UnknownBlock(name);
        case "dark_oak_fence":
          // TODO
          return new UnknownBlock(name);
        case "pumpkin":
          // TODO
          return new UnknownBlock(name);
        case "carved_pumpkin":
          // TODO
          return new UnknownBlock(name);
        case "netherrack":
          return new MinecraftBlock(name, Texture.netherrack);
        case "soul_sand":
          // TODO
          return new UnknownBlock(name);
        case "glowstone":
          // TODO
          return new UnknownBlock(name);
        case "jack_o_lantern":
          // TODO
          return new UnknownBlock(name);
        case "oak_trapdoor":
          // TODO
          return new UnknownBlock(name);
        case "spruce_trapdoor":
          // TODO
          return new UnknownBlock(name);
        case "birch_trapdoor":
          // TODO
          return new UnknownBlock(name);
        case "jungle_trapdoor":
          // TODO
          return new UnknownBlock(name);
        case "acacia_trapdoor":
          // TODO
          return new UnknownBlock(name);
        case "dark_oak_trapdoor":
          // TODO
          return new UnknownBlock(name);
        case "infested_stone_bricks":
        case "stone_bricks":
          return new MinecraftBlock(name, Texture.stoneBrick);
        case "infested_mossy_stone_bricks":
        case "mossy_stone_bricks":
          return new MinecraftBlock(name, Texture.mossyStoneBrick);
        case "infested_cracked_stone_bricks":
        case "cracked_stone_bricks":
          return new MinecraftBlock(name, Texture.crackedStoneBrick);
        case "infested_chiseled_stone_bricks":
        case "chiseled_stone_bricks":
          return new MinecraftBlock(name, Texture.circleStoneBrick);
        case "nether_bricks":
          return new MinecraftBlock(name, Texture.netherBrick);
        case "brown_mushroom_block":
          return hugeMushroom(tag, Texture.hugeBrownMushroom);
        case "red_mushroom_block":
          return hugeMushroom(tag, Texture.hugeRedMushroom);
        case "mushroom_stem":
          return hugeMushroom(tag, Texture.mushroomStem);
        case "iron_bars":
          // TODO
          return new UnknownBlock(name);
        case "glass_pane":
          // TODO
          return new UnknownBlock(name);
        case "melon":
          return new TexturedBlock(name, Texture.melonSide, Texture.melonTop);
        case "vine":
          // TODO
          return new UnknownBlock(name);
        case "oak_fence_gate":
          // TODO
          return new UnknownBlock(name);
        case "spruce_fence_gate":
          // TODO
          return new UnknownBlock(name);
        case "birch_fence_gate":
          // TODO
          return new UnknownBlock(name);
        case "jungle_fence_gate":
          // TODO
          return new UnknownBlock(name);
        case "acacia_fence_gate":
          // TODO
          return new UnknownBlock(name);
        case "dark_oak_fence_gate":
          // TODO
          return new UnknownBlock(name);
        case "brick_stairs":
          return stairs(tag, Texture.brick);
        case "stone_brick_stairs":
          return stairs(tag, Texture.stoneBrick);
        case "mycelium":
          // TODO
          return new UnknownBlock(name);
        case "lily_pad":
          // TODO
          return new UnknownBlock(name);
        case "nether_brick_fence":
          // TODO
          return new UnknownBlock(name);
        case "nether_brick_stairs":
          return stairs(tag, Texture.netherBrick);
        case "enchanting_table":
          // TODO
          return new UnknownBlock(name);
        case "end_portal_frame":
          // TODO
          return new UnknownBlock(name);
        case "end_stone":
          return new UnknownBlock(name);
        case "end_stone_bricks":
          return new MinecraftBlock(name, Texture.endBricks);
        case "redstone_lamp":
          // TODO
          return new UnknownBlock(name);
        case "cocoa":
          // TODO
          return new UnknownBlock(name);
        case "sandstone_stairs":
          return stairs(tag, Texture.sandstoneSide, Texture.sandstoneTop, Texture.sandstoneBottom);
        case "emerald_ore":
          return new MinecraftBlock(name, Texture.emeraldOre);
        case "ender_chest":
          // TODO
          return new UnknownBlock(name);
        case "tripwire_hook":
          // TODO
          return new UnknownBlock(name);
        case "tripwire":
          // TODO
          return new UnknownBlock(name);
        case "emerald_block":
          return new MinecraftBlock(name, Texture.emeraldBlock);
        case "beacon":
          return new Beacon();
        case "cobblestone_wall":
          // TODO
          return new UnknownBlock(name);
        case "mossy_cobblestone_wall":
          // TODO
          return new UnknownBlock(name);
        case "oak_button":
          // TODO
          return new UnknownBlock(name);
        case "spruce_button":
          // TODO
          return new UnknownBlock(name);
        case "birch_button":
          // TODO
          return new UnknownBlock(name);
        case "jungle_button":
          // TODO
          return new UnknownBlock(name);
        case "acacia_button":
          // TODO
          return new UnknownBlock(name);
        case "dark_oak_button":
          // TODO
          return new UnknownBlock(name);
        case "anvil":
          // TODO
          return new UnknownBlock(name);
        case "chipped_anvil":
          // TODO
          return new UnknownBlock(name);
        case "damaged_anvil":
          // TODO
          return new UnknownBlock(name);
        case "trapped_chest":
          // TODO
          return new UnknownBlock(name);
        case "light_weighted_pressure_plate":
          // TODO
          return new UnknownBlock(name);
        case "heavy_weighted_pressure_plate":
          // TODO
          return new UnknownBlock(name);
        case "daylight_detector":
          // TODO
          return new UnknownBlock(name);
        case "redstone_block":
          return new MinecraftBlock(name, Texture.redstoneBlock);
        case "nether_quartz_ore":
          // TODO
          return new UnknownBlock(name);
        case "hopper":
          // TODO
          return new UnknownBlock(name);
        case "chiseled_quartz_block":
          // TODO
          return new UnknownBlock(name);
        case "quartz_block":
          // TODO
          return new UnknownBlock(name);
        case "quartz_pillar":
          // TODO
          return new UnknownBlock(name);
        case "quartz_stairs":
          return stairs(tag, Texture.quartzSide, Texture.quartzTop, Texture.quartzBottom);
        case "activator_rail":
          // TODO
          return new UnknownBlock(name);
        case "dropper":
          // TODO
          return new UnknownBlock(name);
        case "white_terracotta":
          return new MinecraftBlock(name, Texture.whiteClay);
        case "orange_terracotta":
          return new MinecraftBlock(name, Texture.orangeClay);
        case "magenta_terracotta":
          return new MinecraftBlock(name, Texture.magentaClay);
        case "light_blue_terracotta":
          return new MinecraftBlock(name, Texture.lightBlueClay);
        case "yellow_terracotta":
          return new MinecraftBlock(name, Texture.yellowClay);
        case "lime_terracotta":
          return new MinecraftBlock(name, Texture.limeClay);
        case "pink_terracotta":
          return new MinecraftBlock(name, Texture.pinkClay);
        case "gray_terracotta":
          return new MinecraftBlock(name, Texture.grayClay);
        case "light_gray_terracotta":
          return new MinecraftBlock(name, Texture.lightGrayClay);
        case "cyan_terracotta":
          return new MinecraftBlock(name, Texture.cyanClay);
        case "purple_terracotta":
          return new MinecraftBlock(name, Texture.purpleClay);
        case "blue_terracotta":
          return new MinecraftBlock(name, Texture.blueClay);
        case "brown_terracotta":
          return new MinecraftBlock(name, Texture.brownClay);
        case "green_terracotta":
          return new MinecraftBlock(name, Texture.greenClay);
        case "red_terracotta":
          return new MinecraftBlock(name, Texture.redClay);
        case "black_terracotta":
          return new MinecraftBlock(name, Texture.blackClay);
        case "iron_trapdoor":
          // TODO
          return new UnknownBlock(name);
        case "hay_block":
          return new TexturedBlock(name, Texture.hayBlockSide, Texture.hayBlockTop);
        case "white_carpet":
          return new Carpet(name, Texture.whiteWool);
        case "orange_carpet":
          return new Carpet(name, Texture.orangeWool);
        case "magenta_carpet":
          return new Carpet(name, Texture.magentaWool);
        case "light_blue_carpet":
          return new Carpet(name, Texture.lightBlueWool);
        case "yellow_carpet":
          return new Carpet(name, Texture.yellowWool);
        case "lime_carpet":
          return new Carpet(name, Texture.limeWool);
        case "pink_carpet":
          return new Carpet(name, Texture.pinkWool);
        case "gray_carpet":
          return new Carpet(name, Texture.grayWool);
        case "light_gray_carpet":
          return new Carpet(name, Texture.lightGrayWool);
        case "cyan_carpet":
          return new Carpet(name, Texture.cyanWool);
        case "purple_carpet":
          return new Carpet(name, Texture.purpleWool);
        case "blue_carpet":
          return new Carpet(name, Texture.blueWool);
        case "brown_carpet":
          return new Carpet(name, Texture.brownWool);
        case "green_carpet":
          return new Carpet(name, Texture.greenWool);
        case "red_carpet":
          return new Carpet(name, Texture.redWool);
        case "black_carpet":
          return new Carpet(name, Texture.blackWool);
        case "terracotta":
          return new MinecraftBlock(name, Texture.hardenedClay);
        case "coal_block":
          return new MinecraftBlock(name, Texture.coalBlock);
        case "packed_ice":
          return new MinecraftBlock(name, Texture.packedIce);
        case "slime_block":
          // TODO: improve slime block!
          return new MinecraftBlock(name, Texture.slime);
        case "grass_path":
          // TODO
          return new UnknownBlock(name);
        case "sunflower":
          // TODO
          return new UnknownBlock(name);
        case "lilac":
          return largeFlower(tag, Texture.lilacTop, Texture.lilacBottom);
        case "rose_bush":
          return largeFlower(tag, Texture.roseBushTop, Texture.roseBushBottom);
        case "peony":
          return largeFlower(tag, Texture.peonyTop, Texture.peonyBottom);
        case "tall_grass": {
          String half = tag.get("Properties").get("half").stringValue("lower");
          return new TallGrass(half);
        }
        case "large_fern": {
          String half = tag.get("Properties").get("half").stringValue("lower");
          return new LargeFern(half);
        }
        case "white_stained_glass":
          return new Glass(name, Texture.whiteGlass);
        case "orange_stained_glass":
          return new Glass(name, Texture.orangeGlass);
        case "magenta_stained_glass":
          return new Glass(name, Texture.magentaGlass);
        case "light_blue_stained_glass":
          return new Glass(name, Texture.lightBlueGlass);
        case "yellow_stained_glass":
          return new Glass(name, Texture.yellowGlass);
        case "lime_stained_glass":
          return new Glass(name, Texture.limeGlass);
        case "pink_stained_glass":
          return new Glass(name, Texture.pinkGlass);
        case "gray_stained_glass":
          return new Glass(name, Texture.grayGlass);
        case "light_gray_stained_glass":
          return new Glass(name, Texture.lightGrayGlass);
        case "cyan_stained_glass":
          return new Glass(name, Texture.cyanGlass);
        case "purple_stained_glass":
          return new Glass(name, Texture.purpleGlass);
        case "blue_stained_glass":
          return new Glass(name, Texture.blueGlass);
        case "brown_stained_glass":
          return new Glass(name, Texture.brownGlass);
        case "green_stained_glass":
          return new Glass(name, Texture.greenGlass);
        case "red_stained_glass":
          return new Glass(name, Texture.redGlass);
        case "black_stained_glass":
          return new Glass(name, Texture.blackGlass);
        case "white_stained_glass_pane":
          // TODO
          return new UnknownBlock(name);
        case "orange_stained_glass_pane":
          // TODO
          return new UnknownBlock(name);
        case "magenta_stained_glass_pane":
          // TODO
          return new UnknownBlock(name);
        case "light_blue_stained_glass_pane":
          // TODO
          return new UnknownBlock(name);
        case "yellow_stained_glass_pane":
          // TODO
          return new UnknownBlock(name);
        case "lime_stained_glass_pane":
          // TODO
          return new UnknownBlock(name);
        case "pink_stained_glass_pane":
          // TODO
          return new UnknownBlock(name);
        case "gray_stained_glass_pane":
          // TODO
          return new UnknownBlock(name);
        case "light_gray_stained_glass_pane":
          // TODO
          return new UnknownBlock(name);
        case "cyan_stained_glass_pane":
          // TODO
          return new UnknownBlock(name);
        case "purple_stained_glass_pane":
          // TODO
          return new UnknownBlock(name);
        case "blue_stained_glass_pane":
          // TODO
          return new UnknownBlock(name);
        case "brown_stained_glass_pane":
          // TODO
          return new UnknownBlock(name);
        case "green_stained_glass_pane":
          // TODO
          return new UnknownBlock(name);
        case "red_stained_glass_pane":
          // TODO
          return new UnknownBlock(name);
        case "black_stained_glass_pane":
          // TODO
          return new UnknownBlock(name);
        case "prismarine":
          return new MinecraftBlock(name, Texture.prismarine);
        case "prismarine_bricks":
          return new MinecraftBlock(name, Texture.prismarineBricks);
        case "dark_prismarine":
          return new MinecraftBlock(name, Texture.darkPrismarine);
        case "prismarine_stairs":
          return stairs(tag, Texture.prismarine);
        case "prismarine_brick_stairs":
          return stairs(tag, Texture.prismarineBricks);
        case "dark_prismarine_stairs":
          return stairs(tag, Texture.darkPrismarine);
        case "sea_lantern":
          // TODO
          return new UnknownBlock(name);
        case "red_sandstone":
          return new TexturedBlock(name, Texture.redSandstoneSide,
              Texture.redSandstoneTop, Texture.redSandstoneBottom);
        case "chiseled_red_sandstone":
          return new TexturedBlock(name, Texture.redSandstoneDecorated,
              Texture.redSandstoneTop, Texture.redSandstoneBottom);
        case "cut_red_sandstone":
          return new TexturedBlock(name, Texture.redSandstoneCut,
              Texture.redSandstoneTop, Texture.redSandstoneBottom);
        case "red_sandstone_stairs":
          return stairs(tag, Texture.redSandstoneSide,
              Texture.redSandstoneTop, Texture.redSandstoneBottom);
        case "magma_block": {
          Block block = new MinecraftBlock(name, Texture.magma);
          block.emittance = 0.6f;
          return block;
        }
        case "nether_wart_block":
          return new MinecraftBlock(name, Texture.netherWartBlock);
        case "red_nether_bricks":
          return new MinecraftBlock(name, Texture.redNetherBrick);
        case "bone_block":
          return log(tag, Texture.boneSide, Texture.boneTop);
        case "observer":
          // TODO
          return new UnknownBlock(name);
        case "shulker_box":
          // TODO
          return new UnknownBlock(name);
        case "white_shulker_box":
          // TODO
          return new UnknownBlock(name);
        case "orange_shulker_box":
          // TODO
          return new UnknownBlock(name);
        case "magenta_shulker_box":
          // TODO
          return new UnknownBlock(name);
        case "light_blue_shulker_box":
          // TODO
          return new UnknownBlock(name);
        case "yellow_shulker_box":
          // TODO
          return new UnknownBlock(name);
        case "lime_shulker_box":
          // TODO
          return new UnknownBlock(name);
        case "pink_shulker_box":
          // TODO
          return new UnknownBlock(name);
        case "gray_shulker_box":
          // TODO
          return new UnknownBlock(name);
        case "light_gray_shulker_box":
          // TODO
          return new UnknownBlock(name);
        case "cyan_shulker_box":
          // TODO
          return new UnknownBlock(name);
        case "purple_shulker_box":
          // TODO
          return new UnknownBlock(name);
        case "blue_shulker_box":
          // TODO
          return new UnknownBlock(name);
        case "brown_shulker_box":
          // TODO
          return new UnknownBlock(name);
        case "green_shulker_box":
          // TODO
          return new UnknownBlock(name);
        case "red_shulker_box":
          // TODO
          return new UnknownBlock(name);
        case "black_shulker_box":
          // TODO
          return new UnknownBlock(name);
        case "white_glazed_terracotta":
          return glazedTerracotta(tag, Texture.terracottaWhite);
        case "orange_glazed_terracotta":
          return glazedTerracotta(tag, Texture.terracottaOrange);
        case "magenta_glazed_terracotta":
          return glazedTerracotta(tag, Texture.terracottaMagenta);
        case "light_blue_glazed_terracotta":
          return glazedTerracotta(tag, Texture.terracottaLightBlue);
        case "yellow_glazed_terracotta":
          return glazedTerracotta(tag, Texture.terracottaYellow);
        case "lime_glazed_terracotta":
          return glazedTerracotta(tag, Texture.terracottaLime);
        case "pink_glazed_terracotta":
          return glazedTerracotta(tag, Texture.terracottaPink);
        case "gray_glazed_terracotta":
          return glazedTerracotta(tag, Texture.terracottaGray);
        case "light_gray_glazed_terracotta":
          return glazedTerracotta(tag, Texture.terracottaSilver);
        case "cyan_glazed_terracotta":
          return glazedTerracotta(tag, Texture.terracottaCyan);
        case "purple_glazed_terracotta":
          return glazedTerracotta(tag, Texture.terracottaPurple);
        case "blue_glazed_terracotta":
          return glazedTerracotta(tag, Texture.terracottaBlue);
        case "brown_glazed_terracotta":
          return glazedTerracotta(tag, Texture.terracottaBrown);
        case "green_glazed_terracotta":
          return glazedTerracotta(tag, Texture.terracottaGreen);
        case "red_glazed_terracotta":
          return glazedTerracotta(tag, Texture.terracottaRed);
        case "black_glazed_terracotta":
          return glazedTerracotta(tag, Texture.terracottaBlack);
        case "white_concrete":
          return new MinecraftBlock(name, Texture.concreteWhite);
        case "orange_concrete":
          return new MinecraftBlock(name, Texture.concreteOrange);
        case "magenta_concrete":
          return new MinecraftBlock(name, Texture.concreteMagenta);
        case "light_blue_concrete":
          return new MinecraftBlock(name, Texture.concreteLightBlue);
        case "yellow_concrete":
          return new MinecraftBlock(name, Texture.concreteYellow);
        case "lime_concrete":
          return new MinecraftBlock(name, Texture.concreteLime);
        case "pink_concrete":
          return new MinecraftBlock(name, Texture.concretePink);
        case "gray_concrete":
          return new MinecraftBlock(name, Texture.concreteGray);
        case "light_gray_concrete":
          return new MinecraftBlock(name, Texture.concreteSilver);
        case "cyan_concrete":
          return new MinecraftBlock(name, Texture.concreteCyan);
        case "purple_concrete":
          return new MinecraftBlock(name, Texture.concretePurple);
        case "blue_concrete":
          return new MinecraftBlock(name, Texture.concreteBlue);
        case "brown_concrete":
          return new MinecraftBlock(name, Texture.concreteBrown);
        case "green_concrete":
          return new MinecraftBlock(name, Texture.concreteGreen);
        case "red_concrete":
          return new MinecraftBlock(name, Texture.concreteRed);
        case "black_concrete":
          return new MinecraftBlock(name, Texture.concreteBlack);
        case "white_concrete_powder":
          return new MinecraftBlock(name, Texture.concretePowderWhite);
        case "orange_concrete_powder":
          return new MinecraftBlock(name, Texture.concretePowderOrange);
        case "magenta_concrete_powder":
          return new MinecraftBlock(name, Texture.concretePowderMagenta);
        case "light_blue_concrete_powder":
          return new MinecraftBlock(name, Texture.concretePowderLightBlue);
        case "yellow_concrete_powder":
          return new MinecraftBlock(name, Texture.concretePowderYellow);
        case "lime_concrete_powder":
          return new MinecraftBlock(name, Texture.concretePowderLime);
        case "pink_concrete_powder":
          return new MinecraftBlock(name, Texture.concretePowderPink);
        case "gray_concrete_powder":
          return new MinecraftBlock(name, Texture.concretePowderGray);
        case "light_gray_concrete_powder":
          return new MinecraftBlock(name, Texture.concretePowderSilver);
        case "cyan_concrete_powder":
          return new MinecraftBlock(name, Texture.concretePowderCyan);
        case "purple_concrete_powder":
          return new MinecraftBlock(name, Texture.concretePowderPurple);
        case "blue_concrete_powder":
          return new MinecraftBlock(name, Texture.concretePowderBlue);
        case "brown_concrete_powder":
          return new MinecraftBlock(name, Texture.concretePowderBrown);
        case "green_concrete_powder":
          return new MinecraftBlock(name, Texture.concretePowderGreen);
        case "red_concrete_powder":
          return new MinecraftBlock(name, Texture.concretePowderRed);
        case "black_concrete_powder":
          return new MinecraftBlock(name, Texture.concretePowderBlack);
        case "turtle_egg":
          // TODO
          return new UnknownBlock(name);
        case "dead_tube_coral_block":
          // TODO
          return new UnknownBlock(name);
        case "dead_brain_coral_block":
          // TODO
          return new UnknownBlock(name);
        case "dead_bubble_coral_block":
          // TODO
          return new UnknownBlock(name);
        case "dead_fire_coral_block":
          // TODO
          return new UnknownBlock(name);
        case "dead_horn_coral_block":
          // TODO
          return new UnknownBlock(name);
        case "tube_coral_block":
          // TODO
          return new UnknownBlock(name);
        case "brain_coral_block":
          // TODO
          return new UnknownBlock(name);
        case "bubble_coral_block":
          // TODO
          return new UnknownBlock(name);
        case "fire_coral_block":
          // TODO
          return new UnknownBlock(name);
        case "horn_coral_block":
          // TODO
          return new UnknownBlock(name);
        case "tube_coral":
          // TODO
          return new UnknownBlock(name);
        case "brain_coral":
          // TODO
          return new UnknownBlock(name);
        case "bubble_coral":
          // TODO
          return new UnknownBlock(name);
        case "fire_coral":
          // TODO
          return new UnknownBlock(name);
        case "horn_coral":
          // TODO
          return new UnknownBlock(name);
        case "dead_tube_coral":
          // TODO
          return new UnknownBlock(name);
        case "dead_brain_coral":
          // TODO
          return new UnknownBlock(name);
        case "dead_bubble_coral":
          // TODO
          return new UnknownBlock(name);
        case "dead_fire_coral":
          // TODO
          return new UnknownBlock(name);
        case "dead_horn_coral":
          // TODO
          return new UnknownBlock(name);
        case "tube_coral_fan":
          // TODO
          return new UnknownBlock(name);
        case "tube_coral_wall_fan":
          // TODO
          return new UnknownBlock(name);
        case "brain_coral_fan":
          // TODO
          return new UnknownBlock(name);
        case "brain_coral_wall_fan":
          // TODO
          return new UnknownBlock(name);
        case "bubble_coral_fan":
          // TODO
          return new UnknownBlock(name);
        case "bubble_coral_wall_fan":
          // TODO
          return new UnknownBlock(name);
        case "fire_coral_fan":
          // TODO
          return new UnknownBlock(name);
        case "fire_coral_wall_fan":
          // TODO
          return new UnknownBlock(name);
        case "horn_coral_fan":
          // TODO
          return new UnknownBlock(name);
        case "horn_coral_wall_fan":
          // TODO
          return new UnknownBlock(name);
        case "dead_tube_coral_fan":
          // TODO
          return new UnknownBlock(name);
        case "dead_tube_coral_wall_fan":
          // TODO
          return new UnknownBlock(name);
        case "dead_brain_coral_fan":
          // TODO
          return new UnknownBlock(name);
        case "dead_brain_coral_wall_fan":
          // TODO
          return new UnknownBlock(name);
        case "dead_bubble_coral_fan":
          // TODO
          return new UnknownBlock(name);
        case "dead_bubble_coral_wall_fan":
          // TODO
          return new UnknownBlock(name);
        case "dead_fire_coral_fan":
          // TODO
          return new UnknownBlock(name);
        case "dead_fire_coral_wall_fan":
          // TODO
          return new UnknownBlock(name);
        case "dead_horn_coral_fan":
          // TODO
          return new UnknownBlock(name);
        case "dead_horn_coral_wall_fan":
          // TODO
          return new UnknownBlock(name);
        case "blue_ice":
          // TODO
          return new UnknownBlock(name);
        case "conduit":
          // TODO
          return new UnknownBlock(name);
        case "polished_granite_stairs":
          // TODO
          return new UnknownBlock(name);
        case "smooth_red_sandstone_stairs":
          // TODO
          return new UnknownBlock(name);
        case "mossy_stone_brick_stairs":
          // TODO
          return new UnknownBlock(name);
        case "polished_diorite_stairs":
          // TODO
          return new UnknownBlock(name);
        case "mossy_cobblestone_stairs":
          // TODO
          return new UnknownBlock(name);
        case "end_stone_brick_stairs":
          return stairs(tag, Texture.stoneBrick);
        case "stone_stairs":
          return stairs(tag, Texture.cobblestone);
        case "smooth_sandstone_stairs":
          // TODO
          return new UnknownBlock(name);
        case "smooth_quartz_stairs":
          // TODO
          return new UnknownBlock(name);
        case "granite_stairs":
          // TODO
          return new UnknownBlock(name);
        case "andesite_stairs":
          // TODO
          return new UnknownBlock(name);
        case "red_nether_brick_stairs":
          // TODO
          return new UnknownBlock(name);
        case "polished_andesite_stairs":
          // TODO
          return new UnknownBlock(name);
        case "diorite_stairs":
          // TODO
          return new UnknownBlock(name);
        case "polished_granite_slab":
          return slab(tag, Texture.smoothGranite);
        case "smooth_red_sandstone_slab":
          return slab(tag, Texture.redSandstoneSmooth);
        case "mossy_stone_brick_slab":
          return slab(tag, Texture.mossyStoneBrick);
        case "polished_diorite_slab":
          return slab(tag, Texture.smoothDiorite);
        case "mossy_cobblestone_slab":
          return slab(tag, Texture.mossStone);
        case "end_stone_brick_slab":
          return slab(tag, Texture.endBricks);
        case "smooth_sandstone_slab":
          return slab(tag, Texture.sandstoneSmooth, Texture.sandstoneTop);
        case "smooth_quartz_slab":
          // TODO 1.14
          return slab(tag, Texture.quartzSide, Texture.quartzTop);
        case "granite_slab":
          return slab(tag, Texture.granite);
        case "andesite_slab":
          return slab(tag, Texture.andesite);
        case "red_nether_brick_slab":
          return slab(tag, Texture.redNetherBrick);
        case "polished_andesite_slab":
          return slab(tag, Texture.smoothAndesite);
        case "diorite_slab":
          return slab(tag, Texture.diorite);
        case "brick_wall":
          // TODO
          return new UnknownBlock(name);
        case "prismarine_wall":
          // TODO
          return new UnknownBlock(name);
        case "red_sandstone_wall":
          // TODO
          return new UnknownBlock(name);
        case "mossy_stone_brick_wall":
          // TODO
          return new UnknownBlock(name);
        case "granite_wall":
          // TODO
          return new UnknownBlock(name);
        case "stone_brick_wall":
          // TODO
          return new UnknownBlock(name);
        case "nether_brick_wall":
          // TODO
          return new UnknownBlock(name);
        case "andesite_wall":
          // TODO
          return new UnknownBlock(name);
        case "red_nether_brick_wall":
          // TODO
          return new UnknownBlock(name);
        case "sandstone_wall":
          // TODO
          return new UnknownBlock(name);
        case "end_stone_brick_wall":
          // TODO
          return new UnknownBlock(name);
        case "diorite_wall":
          // TODO
          return new UnknownBlock(name);
        case "scaffolding":
          // TODO
          return new UnknownBlock(name);
        case "oak_door":
          // TODO
          return new UnknownBlock(name);
        case "iron_door":
          // TODO
          return new UnknownBlock(name);
        case "spruce_door":
          // TODO
          return new UnknownBlock(name);
        case "birch_door":
          // TODO
          return new UnknownBlock(name);
        case "jungle_door":
          // TODO
          return new UnknownBlock(name);
        case "acacia_door":
          // TODO
          return new UnknownBlock(name);
        case "dark_oak_door":
          // TODO
          return new UnknownBlock(name);
        case "repeater":
          // TODO
          return new UnknownBlock(name);
        case "comparator":
          // TODO
          return new UnknownBlock(name);
        case "composter":
          // TODO
          return new UnknownBlock(name);
        case "fire":
          return new Fire();
        case "wheat":
          return wheat(tag);
        case "sign":
          // TODO
          return new UnknownBlock(name);
        case "oak_sign":
          // TODO
          return new UnknownBlock(name);
        case "Sign":
          // TODO
          return new UnknownBlock(name);
        case "wall_sign":
          // TODO
          return new UnknownBlock(name);
        case "oak_wall_sign":
          // TODO
          return new UnknownBlock(name);
        case "spruce_sign":
          // TODO
          return new UnknownBlock(name);
        case "spruce_wall_sign":
          // TODO
          return new UnknownBlock(name);
        case "birch_sign":
          // TODO
          return new UnknownBlock(name);
        case "birch_wall_sign":
          // TODO
          return new UnknownBlock(name);
        case "jungle_sign":
          // TODO
          return new UnknownBlock(name);
        case "jungle_wall_sign":
          // TODO
          return new UnknownBlock(name);
        case "acacia_sign":
          // TODO
          return new UnknownBlock(name);
        case "acacia_wall_sign":
          // TODO
          return new UnknownBlock(name);
        case "dark_oak_sign":
          // TODO
          return new UnknownBlock(name);
        case "dark_oak_wall_sign":
          // TODO
          return new UnknownBlock(name);
        case "redstone_wire":
          return redstone_wire(tag);
        case "sugar_cane":
          return new SpriteBlock(name, Texture.sugarCane);
        case "kelp":
          return new SpriteBlock(name, Texture.kelp);
        case "kelp_plant":
          return new SpriteBlock(name, Texture.kelpPlant);
        case "dried_kelp_block":
          // TODO
          return new UnknownBlock(name);
        case "bamboo":
          // TODO
          return new UnknownBlock(name);
        case "bamboo_sapling":
          // TODO
          return new UnknownBlock(name);
        case "cake":
          // TODO
          return new UnknownBlock(name);
        case "white_bed":
          return bed(tag, Texture.bedWhite);
        case "orange_bed":
          return bed(tag, Texture.bedOrange);
        case "magenta_bed":
          return bed(tag, Texture.bedMagenta);
        case "light_blue_bed":
          return bed(tag, Texture.bedLightBlue);
        case "yellow_bed":
          return bed(tag, Texture.bedYellow);
        case "lime_bed":
          return bed(tag, Texture.bedLime);
        case "pink_bed":
          return bed(tag, Texture.bedPink);
        case "gray_bed":
          return bed(tag, Texture.bedGray);
        case "light_gray_bed":
          return bed(tag, Texture.bedSilver);
        case "cyan_bed":
          return bed(tag, Texture.bedCyan);
        case "purple_bed":
          return bed(tag, Texture.bedPurple);
        case "blue_bed":
          return bed(tag, Texture.bedBlue);
        case "brown_bed":
          return bed(tag, Texture.bedBrown);
        case "green_bed":
          return bed(tag, Texture.bedGreen);
        case "red_bed":
          return bed(tag, Texture.bedRed);
        case "black_bed":
          return bed(tag, Texture.bedBlack);
        case "pumpkin_stem":
          // TODO
          return new UnknownBlock(name);
        case "attached_pumpkin_stem":
          // TODO
          return new UnknownBlock(name);
        case "melon_stem":
          // TODO
          return new UnknownBlock(name);
        case "attached_melon_stem":
          // TODO
          return new UnknownBlock(name);
        case "nether_wart":
          // TODO
          return new UnknownBlock(name);
        case "brewing_stand":
          // TODO
          return new UnknownBlock(name);
        case "cauldron":
          // TODO
          return new UnknownBlock(name);
        case "flower_pot":
          // TODO
          return new UnknownBlock(name);
        case "potted_poppy":
          // TODO
          return new UnknownBlock(name);
        case "potted_dandelion":
          // TODO
          return new UnknownBlock(name);
        case "potted_oak_sapling":
          // TODO
          return new UnknownBlock(name);
        case "potted_spruce_sapling":
          // TODO
          return new UnknownBlock(name);
        case "potted_birch_sapling":
          // TODO
          return new UnknownBlock(name);
        case "potted_jungle_sapling":
          // TODO
          return new UnknownBlock(name);
        case "potted_red_mushroom":
          // TODO
          return new UnknownBlock(name);
        case "potted_brown_mushroom":
          // TODO
          return new UnknownBlock(name);
        case "potted_cactus":
          // TODO
          return new UnknownBlock(name);
        case "potted_dead_bush":
          // TODO
          return new UnknownBlock(name);
        case "potted_fern":
          // TODO
          return new UnknownBlock(name);
        case "potted_acacia_sapling":
          // TODO
          return new UnknownBlock(name);
        case "potted_dark_oak_sapling":
          // TODO
          return new UnknownBlock(name);
        case "potted_blue_orchid":
          // TODO
          return new UnknownBlock(name);
        case "potted_allium":
          // TODO
          return new UnknownBlock(name);
        case "potted_azure_bluet":
          // TODO
          return new UnknownBlock(name);
        case "potted_red_tulip":
          // TODO
          return new UnknownBlock(name);
        case "potted_orange_tulip":
          // TODO
          return new UnknownBlock(name);
        case "potted_white_tulip":
          // TODO
          return new UnknownBlock(name);
        case "potted_pink_tulip":
          // TODO
          return new UnknownBlock(name);
        case "potted_oxeye_daisy":
          // TODO
          return new UnknownBlock(name);
        case "potted_bamboo":
          // TODO
          return new UnknownBlock(name);
        case "potted_cornflower":
          // TODO
          return new UnknownBlock(name);
        case "potted_lily_of_the_valley":
          // TODO
          return new UnknownBlock(name);
        case "potted_wither_rose":
          // TODO
          return new UnknownBlock(name);
        case "carrots":
          // TODO
          return new UnknownBlock(name);
        case "potatoes":
          // TODO
          return new UnknownBlock(name);
        case "skeleton_skull":
          // TODO
          return new UnknownBlock(name);
        case "skeleton_wall_skull":
          // TODO
          return new UnknownBlock(name);
        case "wither_skeleton_skull":
          // TODO
          return new UnknownBlock(name);
        case "wither_skeleton_wall_skull":
          // TODO
          return new UnknownBlock(name);
        case "zombie_head":
          // TODO
          return new UnknownBlock(name);
        case "zombie_wall_head":
          // TODO
          return new UnknownBlock(name);
        case "player_head":
          // TODO
          return new UnknownBlock(name);
        case "player_wall_head":
          // TODO
          return new UnknownBlock(name);
        case "creeper_head":
          // TODO
          return new UnknownBlock(name);
        case "creeper_wall_head":
          // TODO
          return new UnknownBlock(name);
        case "dragon_head":
          // TODO
          return new UnknownBlock(name);
        case "dragon_wall_head":
          // TODO
          return new UnknownBlock(name);
        case "white_banner":
          // TODO
          return new UnknownBlock(name);
        case "orange_banner":
          // TODO
          return new UnknownBlock(name);
        case "magenta_banner":
          // TODO
          return new UnknownBlock(name);
        case "light_blue_banner":
          // TODO
          return new UnknownBlock(name);
        case "yellow_banner":
          // TODO
          return new UnknownBlock(name);
        case "lime_banner":
          // TODO
          return new UnknownBlock(name);
        case "pink_banner":
          // TODO
          return new UnknownBlock(name);
        case "gray_banner":
          // TODO
          return new UnknownBlock(name);
        case "light_gray_banner":
          // TODO
          return new UnknownBlock(name);
        case "cyan_banner":
          // TODO
          return new UnknownBlock(name);
        case "purple_banner":
          // TODO
          return new UnknownBlock(name);
        case "blue_banner":
          // TODO
          return new UnknownBlock(name);
        case "brown_banner":
          // TODO
          return new UnknownBlock(name);
        case "green_banner":
          // TODO
          return new UnknownBlock(name);
        case "red_banner":
          // TODO
          return new UnknownBlock(name);
        case "black_banner":
          // TODO
          return new UnknownBlock(name);
        case "white_wall_banner":
          // TODO
          return new UnknownBlock(name);
        case "orange_wall_banner":
          // TODO
          return new UnknownBlock(name);
        case "magenta_wall_banner":
          // TODO
          return new UnknownBlock(name);
        case "light_blue_wall_banner":
          // TODO
          return new UnknownBlock(name);
        case "yellow_wall_banner":
          // TODO
          return new UnknownBlock(name);
        case "lime_wall_banner":
          // TODO
          return new UnknownBlock(name);
        case "pink_wall_banner":
          // TODO
          return new UnknownBlock(name);
        case "gray_wall_banner":
          // TODO
          return new UnknownBlock(name);
        case "light_gray_wall_banner":
          // TODO
          return new UnknownBlock(name);
        case "cyan_wall_banner":
          // TODO
          return new UnknownBlock(name);
        case "purple_wall_banner":
          // TODO
          return new UnknownBlock(name);
        case "blue_wall_banner":
          // TODO
          return new UnknownBlock(name);
        case "brown_wall_banner":
          // TODO
          return new UnknownBlock(name);
        case "green_wall_banner":
          // TODO
          return new UnknownBlock(name);
        case "red_wall_banner":
          // TODO
          return new UnknownBlock(name);
        case "black_wall_banner":
          // TODO
          return new UnknownBlock(name);
        case "beetroots":
          // TODO
          return new UnknownBlock(name);
        case "loom":
          // TODO
          return new UnknownBlock(name);
        case "barrel":
          // TODO
          return new UnknownBlock(name);
        case "smoker":
          // TODO
          return new UnknownBlock(name);
        case "blast_furnace":
          // TODO
          return new UnknownBlock(name);
        case "cartography_table":
          // TODO
          return new UnknownBlock(name);
        case "fleching_table":
          // TODO
          return new UnknownBlock(name);
        case "grindstone":
          // TODO
          return new UnknownBlock(name);
        case "lectern":
          // TODO
          return new UnknownBlock(name);
        case "smithing_table":
          // TODO
          return new UnknownBlock(name);
        case "stonecutter":
          // TODO
          return new UnknownBlock(name);
        case "bell":
          // TODO
          return new UnknownBlock(name);
        case "lantern":
          // TODO
          return new UnknownBlock(name);
        case "sweet_berry_bush":
          // TODO
          return new UnknownBlock(name);
        case "campfire":
          // TODO
          return new UnknownBlock(name);
        case "frosted_ice":
          // TODO
          return new UnknownBlock(name);
        case "spawner":
          return new MinecraftBlockTranslucent(name, Texture.monsterSpawner);
        case "nether_portal":
          // TODO
          return new UnknownBlock(name);
        case "end_portal":
          // TODO
          return new UnknownBlock(name);
        case "end_gateway":
          // TODO
          return new UnknownBlock(name);
        case "command_block":
          // TODO
          return new UnknownBlock(name);
        case "chain_command_block":
          // TODO
          return new UnknownBlock(name);
        case "repeating_command_block":
          // TODO
          return new UnknownBlock(name);
        case "structure_block":
          // TODO
          return new UnknownBlock(name);
        case "structure_void":
          // TODO
          return new UnknownBlock(name);
        case "jigsaw_block":
          // TODO
          return new UnknownBlock(name);
        case "barrier":
          // Invisible.
          return Air.INSTANCE;
      }
    }
    return Air.INSTANCE;
  }

  private static String blockName(Tag tag) {
    String name = tag.get("Name").stringValue();
    if (name.startsWith("minecraft:")) {
      name = name.substring(10); // Remove "minecraft:" prefix.
    }
    return name;
  }

  private static Block largeFlower(Tag tag, Texture top, Texture bottom) {
    String name = tag.get("Name").stringValue();
    String half = tag.get("Properties").get("half").stringValue("lower");
    return new SpriteBlock(String.format("%s (half=%s)", name, half),
        half.equals("upper") ? top : bottom);
  }

  private static Block log(Tag tag, Texture side, Texture top) {
    String name = blockName(tag);
    String axis = tag.get("Properties").get("axis").stringValue("y");
    return new Log(name, side, top, axis);
  }

  private static Block slab(Tag tag, Texture texture) {
    String name = blockName(tag);
    String type = tag.get("Properties").get("type").stringValue("bottom");
    return new Slab(name, texture, type);
  }

  private static Block slab(Tag tag, Texture sideTexture, Texture topTexture) {
    String name = blockName(tag);
    String type = tag.get("Properties").get("type").stringValue("bottom");
    return new Slab(name, sideTexture, topTexture, type);
  }

  private static Block stairs(Tag tag, Texture texture) {
    String name = blockName(tag);
    Tag properties = tag.get("Properties");
    String half = properties.get("half").stringValue("bottom");
    String shape = properties.get("shape").stringValue("straight");
    String facing = properties.get("facing").stringValue("south");
    return new Stairs(name, texture, half, shape, facing);
  }

  private static Block stairs(Tag tag, Texture side, Texture top, Texture bottom) {
    String name = blockName(tag);
    Tag properties = tag.get("Properties");
    String half = properties.get("half").stringValue("bottom");
    String shape = properties.get("shape").stringValue("straight");
    String facing = properties.get("facing").stringValue("south");
    return new Stairs(name, side, top, bottom, half, shape, facing);
  }

  private static Block glazedTerracotta(Tag tag, Texture texture) {
    String name = blockName(tag);
    String facing = tag.get("Properties").get("facing").stringValue("south");
    return new GlazedTerracotta(name, texture, facing);
  }

  private static Block bed(Tag tag, Texture texture) {
    String name = blockName(tag);
    Tag properties = tag.get("Properties");
    String part = properties.get("part").stringValue("head");
    String facing = properties.get("facing").stringValue("south");
    return new Bed(name, texture, part, facing);
  }

  private static Block hugeMushroom(Tag tag, Texture skin) {
    String name = blockName(tag);
    Tag properties = tag.get("Properties");
    String east = properties.get("east").stringValue("true");
    String west = properties.get("west").stringValue("true");
    String north = properties.get("north").stringValue("true");
    String south = properties.get("south").stringValue("true");
    String up = properties.get("up").stringValue("true");
    String down = properties.get("down").stringValue("true");
    return new TexturedBlock(name,
        north.equals("true") ? skin : Texture.mushroomPores,
        south.equals("true") ? skin : Texture.mushroomPores,
        west.equals("true") ? skin : Texture.mushroomPores,
        east.equals("true") ? skin : Texture.mushroomPores,
        up.equals("true") ? skin : Texture.mushroomPores,
        down.equals("true") ? skin : Texture.mushroomPores);
  }

  private static Block snowCovered(Tag tag, Block block) {
    String snowy = tag.get("Properties").get("snowy").stringValue("false");
    if (snowy.equals("true")) {
      block = new SnowCovered(block);
    }
    return block;
  }

  private static Block piston(Tag tag, boolean isSticky) {
    String name = blockName(tag);
    Tag properties = tag.get("Properties");
    String extended = properties.get("extended").stringValue("false");
    String facing = properties.get("facing").stringValue("north");
    return new Piston(name, isSticky, extended.equals("true"), facing);
  }

  private static Block pistonHead(Tag tag) {
    String name = blockName(tag);
    Tag properties = tag.get("Properties");
    String facing = properties.get("facing").stringValue("north");
    String type = properties.get("type").stringValue("normal");
    return new PistonHead(name, type.equals("sticky"), facing);
  }

  private static Block dispenser(Tag tag) {
    String facing = tag.get("Properties").get("facing").stringValue("north");
    return new Dispenser(facing);
  }

  private static Block rail(Tag tag, Texture straightTrack) {
    String name = blockName(tag);
    Tag properties = tag.get("Properties");
    String shape = properties.get("shape").stringValue("north-south");
    return new Rail(name, straightTrack, shape);
  }

  private static Block detector_rail(Tag tag) {
    Tag properties = tag.get("Properties");
    String powered = properties.get("powered").stringValue("false");
    Texture straightTrack = powered.equals("true")
        ? Texture.detectorRailOn
        : Texture.detectorRail;
    return rail(tag, straightTrack);
  }

  private static Block powered_rail(Tag tag) {
    Tag properties = tag.get("Properties");
    String powered = properties.get("powered").stringValue("false");
    Texture straightTrack = powered.equals("true")
        ? Texture.poweredRailOn
        : Texture.poweredRailOff;
    return rail(tag, straightTrack);
  }

  private static Block wall_torch(Tag tag, Texture texture) {
    String name = blockName(tag);
    Tag properties = tag.get("Properties");
    String facing = properties.get("facing").stringValue("north");
    return new WallTorch(name, texture, facing);
  }

  private static Block redstone_wire(Tag tag) {
    Tag properties = tag.get("Properties");
    String north = properties.get("north").stringValue("none");
    String south = properties.get("south").stringValue("none");
    String east = properties.get("east").stringValue("none");
    String west = properties.get("west").stringValue("none");
    int power = 0;
    try {
      power = Integer.parseInt(properties.get("power").stringValue("0"));
    } catch (NumberFormatException ignored) {
    }
    return new RedstoneWire(power, north, south, east, west);
  }

  private static Block chest(Tag tag) {
    Tag properties = tag.get("Properties");
    String facing = properties.get("facing").stringValue("north");
    String type = properties.get("type").stringValue("single");
    return new Chest(type, facing);
  }

  private static Block end_rod(Tag tag) {
    Tag properties = tag.get("Properties");
    String facing = properties.get("facing").stringValue("up");
    return new EndRod(facing);
  }

  private static Block furnace(Tag tag) {
    Tag properties = tag.get("Properties");
    String facing = properties.get("facing").stringValue("north");
    String lit = properties.get("lit").stringValue("false");
    return new Furnace(facing, lit.equals("true"));
  }

  private static Block wheat(Tag tag) {
    int age = 7;
    try {
      age = Integer.parseInt(tag.get("Properties").get("age").stringValue("7"));
    } catch (NumberFormatException ignored) {
    }
    return new Wheat(age);
  }
}