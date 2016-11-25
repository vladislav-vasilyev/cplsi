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

import java.util.*;

/**
 * @author Vladyslav Vasyliev
 *         Created on 25.11.16.
 */
public class Nmi {

    private int k;
    private double size;
    private double result;
    private List<BaseType> distinctLabels;
    private Cluster[] clusters1;
    private Cluster[] clusters2;

    public Nmi(Cluster[] clusters1, Cluster[] clusters2, Set<BaseType> distinctLabels, int totalSize) {
        this.k = clusters1.length;
        this.size = totalSize;
        this.clusters1 = clusters1;
        this.clusters2 = clusters2;
        this.distinctLabels = new ArrayList<>(distinctLabels);
    }

    public double nmi() {
        return result;
    }

    private double calculateEntropy(Cluster[] clusters) {
        double entropy = 0;
        double p;
        for (Cluster cluster : clusters) {
            p = cluster.size() / size;
            entropy -= p * Math.log(p);
        }
        return entropy;
    }

    private double calculateEntropy(Cluster[] clusters1, Cluster[] clusters2) {
        double entropy = 0;
        double p;
        List<DataItem> list;
        Cluster cluster2 = null;
        for (int i = 0; i < clusters1.length; ++i) {
            list = new ArrayList(clusters1[i].getValues());
            for (Cluster c: clusters2)
                if (c.getLabel().equals(clusters1[i].getLabel()))
                    cluster2 = c;
            list.removeAll(cluster2.getValues());
            p = list.size() / size;
            if (p != 0)
                entropy -= p * Math.log(p);
        }
        return entropy;
    }

    public double calculateNmi() {
        double h1 = calculateEntropy(clusters1);
        double h2 = calculateEntropy(clusters2);
        double h3 = calculateEntropy(clusters1, clusters2);
        result = (h1 - h3) / Math.sqrt(h1 * h2);
        return result;
    }

}
