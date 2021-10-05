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

import org.junit.Test;

import static se.llbit.util.FuzzyUnicodeMatcher.*;
import static org.junit.Assert.assertEquals;

public class FuzzyUnicodeMatcherTest {
  private int cp(String s) {
    return s.codePointAt(0);
  }

  @FunctionalInterface
  interface AssertCodePointEqualityMetric {
    void assertValueForMetric(double expected, String value1, String value2);
  }

  @Test
  public void testCodePointEqualityMetric() {
    AssertCodePointEqualityMetric assertMetric = (double expected, String value1, String value2) -> {
      assertEquals(expected, FuzzyUnicodeMatcher.codePointEqualityMetric(cp(value1), cp(value2)));
    };

    assertMetric.assertValueForMetric(METRIC_IDENTICAL, "e", "e");
		assertMetric.assertValueForMetric(METRIC_TERTIARY_DIFFERENCE, "e", "E");
		assertMetric.assertValueForMetric(METRIC_SECONDARY_DIFFERENCE, "e", "ě");
		assertMetric.assertValueForMetric(METRIC_PRIMARY_DIFFERENCE, "e", "f");
  }
}
