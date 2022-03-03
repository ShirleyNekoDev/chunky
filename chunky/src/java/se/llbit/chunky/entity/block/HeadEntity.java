/*
 * Copyright (c) 2017 Jesper Öqvist <jesper@llbit.se>
 * Copyright (c) 2021 Chunky contributors
 *
 * This file is part of Chunky.
 *
 * Chunky is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Chunky is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with Chunky.  If not, see <http://www.gnu.org/licenses/>.
 */
package se.llbit.chunky.entity.block;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Map;
import java.util.WeakHashMap;

import se.llbit.chunky.entity.Entity;
import se.llbit.chunky.renderer.scene.PlayerModel;
import se.llbit.chunky.resources.PlayerSkinCache;
import se.llbit.chunky.resources.PlayerTexture;
import se.llbit.chunky.resources.Texture;
import se.llbit.chunky.resources.texturepack.PlayerTextureLoader;
import se.llbit.chunky.resources.texturepack.TextureFormatError;
import se.llbit.json.JsonObject;
import se.llbit.json.JsonValue;
import se.llbit.log.Log;
import se.llbit.math.QuickMath;
import se.llbit.math.Transform;
import se.llbit.math.Vector3;
import se.llbit.math.primitive.Box;
import se.llbit.math.primitive.Primitive;
import se.llbit.util.mojangapi.MojangApi;
import se.llbit.util.mojangapi.PlayerSkin;

/**
 * A player head (skull) entity.
 *
 * @author Jesper Öqvist <jesper@llbit.se>
 */
public class HeadEntity extends Entity {

  /**
   * The rotation of the skull when attached to a wall.
   */
  private final int rotation;

  /**
   * Decides if the skull is attached to a wall or the floor.
   */
  private final int placement;

  private final PlayerSkin skin;

  public HeadEntity(Vector3 position, PlayerSkin skin, int rotation, int placement) {
    super(position);
    this.skin = skin;
    this.rotation = rotation;
    this.placement = placement;
  }

  @Override
  public Collection<Primitive> primitives(Vector3 offset) {
    PlayerTexture texture;
    if(skin != null && skin.getURI() != null) {
      texture = PlayerSkinCache.INSTANCE
        .getOrDownload(skin.getURI())
        .orElse(Texture.steve);
    } else {
      texture = Texture.steve;
    }

    Collection<Primitive> faces = new LinkedList<>();
    double wallHeight = 0;
    if (placement >= 2) {
      wallHeight = 4 / 16.;
    }
    Transform transform = Transform.NONE
        .translate(position.x + offset.x + 0.5,
            position.y + offset.y + 4 / 16. + wallHeight,
            position.z + offset.z + 0.5);
    Box head = new Box(-4 / 16., 4 / 16., -4 / 16., 4 / 16., -4 / 16., 4 / 16.);
    Box hat = new Box(-4.25 / 16., 4.25 / 16., -4.25 / 16., 4.25 / 16., -4.25 / 16., 4.25 / 16.);
    switch (placement) {
      case 0:
        // Unused.
        break;
      case 1:
        // On floor.
        transform = Transform.NONE.rotateY(-rotation * Math.PI / 8)
            .chain(transform);
        break;
      case 2:
        // Facing north.
        transform = Transform.NONE.translate(0, 0, 4 / 16.)
            .chain(transform);
        break;
      case 3:
        // Facing south.
        transform = Transform.NONE.translate(0, 0, 4 / 16.)
            .rotateY(Math.PI)
            .chain(transform);
        break;
      case 4:
        // Facing west.
        transform = Transform.NONE.translate(0, 0, 4 / 16.)
            .rotateY(QuickMath.HALF_PI)
            .chain(transform);
        break;
      case 5:
        // Facing east.
        transform = Transform.NONE.translate(0, 0, 4 / 16.)
            .rotateY(-QuickMath.HALF_PI)
            .chain(transform);
        break;
    }
    head.transform(transform);
    head.addFrontFaces(faces, texture, texture.getUV().headFront);
    head.addBackFaces(faces, texture, texture.getUV().headBack);
    head.addTopFaces(faces, texture, texture.getUV().headTop);
    head.addBottomFaces(faces, texture, texture.getUV().headBottom);
    head.addRightFaces(faces, texture, texture.getUV().headRight);
    head.addLeftFaces(faces, texture, texture.getUV().headLeft);
    hat.transform(transform);
    hat.addFrontFaces(faces, texture, texture.getUV().hatFront);
    hat.addBackFaces(faces, texture, texture.getUV().hatBack);
    hat.addLeftFaces(faces, texture, texture.getUV().hatLeft);
    hat.addRightFaces(faces, texture, texture.getUV().hatRight);
    hat.addTopFaces(faces, texture, texture.getUV().hatTop);
    hat.addBottomFaces(faces, texture, texture.getUV().hatBottom);
    return faces;
  }

  @Override
  public JsonValue toJson() {
    JsonObject json = new JsonObject();
    json.add("kind", "head");
    json.add("position", position.toJson());
    //json.add("type", type);
    json.add("rotation", rotation);
    json.add("placement", placement);
    json.add("skin", skin.getURI().toString());
    return json;
  }

  public static Entity fromJson(JsonObject json) {
    Vector3 position = new Vector3();
    position.fromJson(json.get("position").object());
    //int type = json.get("type").intValue(0);
    int rotation = json.get("rotation").intValue(0);
    int placement = json.get("placement").intValue(0);
    try {
      URI skinURI = new URI(json.get("skin").stringValue(""));
      return new HeadEntity(position, new PlayerSkin(skinURI, null), rotation, placement);
    }catch (URISyntaxException ex) {
      throw new IllegalStateException("Invalid player skin URI", ex);
    }
  }


}
