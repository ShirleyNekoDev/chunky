/*
 * Copyright (c) 2021 Chunky Contributors
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
package se.llbit.util;

import java.text.Collator;
import java.text.Normalizer;
import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.PrimitiveIterator;
import java.util.WeakHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Maximilian Stiede
 */
public class FuzzyUnicodeMatcher<T> implements Comparator<T> {
  Map<T, Float> cache;

  public FuzzyUnicodeMatcher(
    Collection<T> searchSpace,
    Function<T, String> stringExtractor,
    String searchString
  ) {
    Function<T, String> extractAndNormalize = stringExtractor.andThen(s -> Normalizer.normalize(s, Normalizer.Form.NFKC));
    String normalizedSearchString = Normalizer.normalize(searchString, Normalizer.Form.NFKC);

    cache = new WeakHashMap<>(searchSpace.size());
    cache = searchSpace
      .stream().parallel()
      .collect(Collectors.toMap(
        element -> element,
        element -> match(extractAndNormalize.apply(element), normalizedSearchString),
        (item1, item2) -> item1,
        WeakHashMap::new
      ));
  }

  public final static int
    METRIC_IDENTICAL = 4,
    METRIC_TERTIARY_DIFFERENCE = 2,
    METRIC_SECONDARY_DIFFERENCE = 1,
    METRIC_PRIMARY_DIFFERENCE = 0;

  private final static Collator c1, c2, c3;

  static {
    c1 = Collator.getInstance();
    c1.setDecomposition(Collator.NO_DECOMPOSITION);
    c1.setStrength(Collator.PRIMARY);
    c2 = Collator.getInstance();
    c2.setDecomposition(Collator.NO_DECOMPOSITION);
    c1.setStrength(Collator.SECONDARY);
    c3 = Collator.getInstance();
    c3.setDecomposition(Collator.NO_DECOMPOSITION);
    c1.setStrength(Collator.TERTIARY);
  }

  static float codePointEqualityMetric(int codePoint1, int codePoint2) {
    return 0F;
  }

  static float match(String string, String searchString) {
    int rating = 0;
    PrimitiveIterator.OfInt codePoints = string.codePoints().iterator();
    for (int i = 0; codePoints.hasNext(); ++i) {
      int codePoint = codePoints.nextInt();
      int type = Character.getType(codePoint);
      if (Character.isAlphabetic(codePoint)) {

      } else {

      }
    }
    return 0F;
  }

  @Override
  public int compare(T elem1, T elem2) {
    return Float.compare(cache.get(elem1), cache.get(elem2));
  }
}
