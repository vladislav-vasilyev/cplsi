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

package ru.gorva;

import ru.gorva.cplsi.*;
import ru.gorva.data.BaseType;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.*;

/**
 * @author Vladyslav Vasyliev
 *         Created on 20.11.16.
 */
public class App {

    /**
     * @return {@code quantum} of the {@code labels} labels.
     */
    static Map<BaseType[], BaseType> getSubsetOfLabels(Map<BaseType[], BaseType> labels,
                                                       Set<BaseType> distinctLabels,
                                                       double quantum, double errorQuantum) {
        Map<BaseType[], BaseType> subset = new HashMap<>();
        Object[] keys = labels.keySet().toArray();
        BaseType[] key;
        Random rand = new Random();
        for (int i = 0; i < labels.size(); ++i)
            if (rand.nextDouble() <= quantum) {
                key = (BaseType[]) keys[i];
                subset.put(key, labels.get(key));
            }
        BaseType value;
        Iterator<BaseType> iterator;
        for (Map.Entry<BaseType[], BaseType> entry : subset.entrySet()) {
            if (rand.nextDouble() <= errorQuantum) {
                iterator = distinctLabels.iterator();
                while ((value = iterator.next()).equals(entry.getValue())) ;
                entry.setValue(value);
            }
        }
        return subset;
    }

    /**
     * Generate clusters based on the comprehensive label data.
     */
    static Cluster[] createOriginalCluster(List<BaseType[]> values,
                                           Map<BaseType[], BaseType> labels,
                                           Set<BaseType> distinctLabels,
                                           int k) {
        Cluster[] clusters = new Cluster[k];
        List<BaseType> lbls = new ArrayList<>(distinctLabels);

        for (int i = 0; i < k; ++i)
            clusters[i] = new Cluster(lbls.get(i));

        BaseType label;
        for (BaseType[] row : values) {
            label = labels.get(row);
            clusters[lbls.indexOf(label)].add(new DataItem(row, true, label));
        }
        return clusters;
    }

    /**
     * Prints to console results of the clustering.
     *
     * @param values   Initial values of the data set.
     * @param original Original separation on the clusters.
     * @param clusters Clustered values.
     * @param nmi      Normalized Mutual Information.
     */
    static void printClusterInfo(PrintStream stream, List<BaseType[]> values,
                                 Cluster[] original, Cluster[] clusters, double nmi) {
        stream.println("----------------------------------------------------");
        stream.println("| Data set  | Cluster label            | Instances |");
        stream.println("----------------------------------------------------");
        for (int i = 0; i < original.length; ++i) {
            stream.println(String.format("| original  | %24s | %9d |",
                    original[i].get(0).getLabel(), original[i].size()));
        }
        stream.println("----------------------------------------------------");
        for (int i = 0; i < original.length; ++i) {
            stream.println(String.format("| clustered | %24s | %9d |",
                    clusters[i].getLabel(), clusters[i].size()));
        }
        stream.println("----------------------------------------------------");
        stream.println();
        stream.println("-------------------------------------");
        stream.println("| Instances | Clusters  | NMI       |");
        stream.println("-------------------------------------");
        stream.println(String.format("| %9d | %9d | %9.7f |", values.size(),
                clusters.length, nmi));
        stream.println("-------------------------------------");
        stream.println();
        stream.println();
    }

    /**
     * Perform CPLSI clustering and NMI measure for the specified data set.
     *
     * @param file File with data set.
     * @return Normalized Mutual Information.
     */
    static double processFile(File file, boolean printInfo, double sideQuantum, double lambda,
                              int labelIndex, int... skip) {
        List<BaseType[]> values;
        Map<BaseType[], BaseType> labels;
        Set<BaseType> distinctLabels;
        DataReader reader;
        try {
            // Read file.
            reader = new DataReader(file);
            values = reader.parse(labelIndex, skip);
            labels = reader.getLabels();
            distinctLabels = reader.getDistinctLabels();
            // Split values into clusters according to the provided labels.
            Cluster[] original = createOriginalCluster(values,
                    labels, distinctLabels, distinctLabels.size());

            // Cluster provided data.
            Cplsi cplsi = new Cplsi(values,
                    getSubsetOfLabels(labels, distinctLabels, sideQuantum, 0),
                    distinctLabels,
                    distinctLabels.size());
            cplsi.setMeasure(new DissimilarityMeasure());
            cplsi.cluster(lambda);

            Cluster[] clusters = cplsi.getClusters();
            // Calculate NMI measure.
            Nmi nmi = new Nmi(original, clusters, distinctLabels, values.size());
            double nmiValue = nmi.calculateNmi();
            // Output obtained results.
            if (printInfo)
                printClusterInfo(System.out, values, original, clusters, nmiValue);
            return nmiValue;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return -1;
    }
  
    static void statistic(PrintStream stream, File file, int index, int... skip) {
        final int n = 50;
        double nmi;
        double quantum;
        quantum = 0.1;
        stream.println(file.getName());
        stream.println("--------------------");
        stream.println("|  %   |    NMI    |");
        stream.println("--------------------");
        while (quantum < 0.51) {
            nmi = 0;
            for (int i = 0; i < n; ++i)
                nmi += processFile(file, false, quantum, 100, index, skip);
            nmi /= n;
            stream.println(String.format("| %4.2f | %9.7f |", quantum, nmi));
            quantum += 0.1;
        }
        stream.println("--------------------");
        stream.println();

    }

    public static void main(String[] args) {
        try {
            PrintStream stream = new PrintStream(new File("results.txt"));
            statistic(stream, new File("data_sets/glass/glass.data"), 10, 0);
            statistic(stream, new File("data_sets/iris/iris.data"), 4);
            statistic(stream, new File("data_sets/wine/wine.data"), 0);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
