/*
 * Copyright (c) 2016 Vladyslav Vasyliev
 *
 * This file is part of cplsi project.
 *
 * cplsi is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 3 of the License.
 *
 * cplsi is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with cplsi.  If not, see <http://www.gnu.org/licenses/>.
 *
 *
 */

package ru.gorva.cplsi;

/**
 * @author Vladyslav Vasyliev
 *         Created on 26.11.16.
 */
public class DissimilarityMeasure implements SimilarityMeasure {
    /**
     * Calculates distance between two {@code DataItem} objects.
     *
     * @param obj1   Data item 1.
     * @param obj2   Data item 2.
     * @param k      Number of clusters.
     * @param lambda Trade-off parameter.
     * @return Distance between specified {@code DataItem} objects.
     */
    @Override
    public double distance(DataItem obj1, DataItem obj2, int k, double lambda) {
        // TODO: huang98extensions.pdf
        return 0;
    }
}
