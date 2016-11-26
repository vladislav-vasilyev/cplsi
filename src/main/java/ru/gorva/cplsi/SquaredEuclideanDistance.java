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

import ru.gorva.data.BaseType;

/**
 * This class represents is Squared Euclidean distance implementation.
 *
 * @author Vladyslav Vasyliev
 *         Created on 25.11.16.
 */
public class SquaredEuclideanDistance implements SimilarityMeasure {

    /**
     * @return Real value for the given {@code BaseType} object.
     */
    private double extractValue(BaseType value) {
        Object obj = value.getValue();
        return (obj instanceof Double) ? (double) obj : (int) obj;
    }

    /**
     * Calculates Squared Euclidean distance between two {@code DataItem} objects.
     *
     * @param obj1   Data item 1.
     * @param obj2   Data item 2.
     * @param k      Number of clusters.
     * @param lambda Trade-off parameter.
     * @return Distance between specified {@code DataItem} objects.
     */
    @Override
    public double distance(DataItem obj1, DataItem obj2, int k, double lambda) {
        int size = obj1.size() - k;
        double result = 0, subResult = 0;
        BaseType[] values1 = obj1.getValues();
        BaseType[] values2 = obj2.getValues();

        for (int i = 0; i < size; ++i)
            result += Math.pow(extractValue(values1[i]) - extractValue(values2[i]), 2.0);
        if (obj1.hasSideInformation())
            for (int i = size; i < size + k; ++i)
                subResult += Math.pow(extractValue(values1[i]) - extractValue(values2[i]), 2.0);

        return result + lambda * subResult;
    }
}
