/* Copyright (c) 2022 Chunky contributors
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
package se.llbit.fxutil;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;
import java.net.URL;

public class FXMLLoaderUtil {
  class FXMLInitializer {

  }

  // TODO: check Credits , Poser , ChunkyFx , SceneChooser , ...
  <T> void load(URL location) {
    FXMLLoader loader = new FXMLLoader(getClass().getResource("Credits.fxml"));
//    loader.setController();
//    loader.setRoot();
    try {
      Parent root = loader.load();
    } catch (IOException ex) {
      // in case we fail to locate the given file, nothing can be done
      // the FX error handler will notify the user
      throw new RuntimeException("Failed to load node for FXML document \"" + location + "\".", ex);
    }
//    T controller = loader.getController();
//    return
  }
}
