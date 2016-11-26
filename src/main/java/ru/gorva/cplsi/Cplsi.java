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
import ru.gorva.data.IntegerType;
import ru.gorva.data.NumericType;
import ru.gorva.data.RealType;

import java.util.*;

/**
 * The {@code Cpls } class is an implementation of the "Clustering with
 * Partition Level Side Information" method.
 *
 * @author Vladyslav Vasyliev
 *         Created on 20.11.16.
 */
public class Cplsi {
    private int k;                      // Amount of clusters.
    private Set<BaseType> labelSet;                    // 'cluster label' set
    private Map<BaseType, Integer> labelMap;           // Mapping from 'cluster label' -> 'cluster label index'
    private Map<Integer, BaseType> reverseLabelMap;    // Mapping from 'cluster label index' -> 'cluster label'
    private Map<BaseType[], Integer> labels;           // Index in 'values' -> 'cluster label index'
    private Map<BaseType[], BaseType> origLabels;     // Index in 'values' -> 'cluster label'
    private List<BaseType[]> values;                   // Initial values

    private SimilarityMeasure measure;        // Distance measure of the clustering algorithm.

    private List<DataItem> matrix;           // Concatenated matrix.

    private Cluster[] clusters;


    // ------------------------------------------------------------------------
    // Constructors and related initialization methods
    // ------------------------------------------------------------------------

    public Cplsi(List<BaseType[]> values, Map<BaseType[], BaseType> labels, Set<BaseType> labelSet, int k) {
        measure = new SquaredEuclideanDistance();
        this.values = values;
        this.origLabels = labels;
        this.k = k;
        this.labelSet = labelSet;
        this.labels = convertLabels(labels, labelSet);
    }

    /**
     * Generate indices for labels and substitute them.
     *
     * @param origLabels Labels read from file.
     * @param labelSet   Distinct label set.
     * @return Map with labels substituted with corresponding indices.
     */
    private Map<BaseType[], Integer> convertLabels(Map<BaseType[], BaseType> origLabels, Set<BaseType> labelSet) {
        Map<BaseType, Integer> map = new HashMap<>();
        Map<Integer, BaseType> rmap = new HashMap<>();
        Object[] distinctLabels = labelSet.toArray();
        // Save direct and reversed mapping from label value to its index.
        for (int i = 0; i < distinctLabels.length; ++i) {
            map.put((BaseType) distinctLabels[i], i);
            rmap.put(i, (BaseType) distinctLabels[i]);
        }
        labelMap = map;
        reverseLabelMap = rmap;
        // Substitute labels with corresponding indices.
        Map<BaseType[], Integer> labels = new HashMap<>();
        for (Map.Entry<BaseType[], BaseType> value : origLabels.entrySet())
            labels.put(value.getKey(), map.get(value.getValue()));
        return labels;
    }


    // ------------------------------------------------------------------------
    // Getters and Setters
    // ------------------------------------------------------------------------

    public SimilarityMeasure getMeasure() {
        return measure;
    }

    public void setMeasure(SimilarityMeasure measure) {
        this.measure = measure;
    }

    public Cluster[] getClusters() {
        return clusters;
    }


    // ------------------------------------------------------------------------
    // Business logic
    // ------------------------------------------------------------------------

    /**
     * Cluster provided values with the use of partition side information.
     *
     * @param lambda trade-off parameter.
     */
    public void cluster(double lambda) {
        DataItem[] prevCentroids = new DataItem[k];
        matrix = generateMatrix(values, labels);
        clusters = initializeClusters(matrix, k);

        double distance, maxDistance;

        do {
            // Copy centroids from the previous step.
            int index = 0;
            for (Cluster cluster : clusters)
                prevCentroids[index++] = cluster.getCentroid();
            // Perform one step of the clustering.
            assignToCentroids(lambda);
            updateCentroids();
            // Verify the distance between current and previous step of the clustering.
            maxDistance = Double.MIN_VALUE;
            for (int i = 0; i < k; ++i) {
                distance = measure.distance(clusters[i].getCentroid(), prevCentroids[i], k, lambda);
                if (distance > maxDistance)
                    maxDistance = distance;
            }
        } while (maxDistance > 0.000_001);
        // Remove side information from the data items.
        BaseType[] vals;
        DataItem item;
        DataItem curItem;
        Cluster cluster;
        for (int i = 0; i < clusters.length; ++i) {
            cluster = clusters[i];
            for (int j = 0; j < cluster.size(); ++j) {
                curItem = cluster.get(j);
                vals = new BaseType[curItem.size() - k];
                System.arraycopy(curItem.getValues(), 0, vals, 0, vals.length);

                item = new DataItem(vals, false);
                cluster.set(j, item);
            }
        }
    }

    /**
     * Generate concatenated matrix.
     *
     * @param values List of the initial values.
     * @param labels Partial side information.
     * @return Concatenated data matrix.
     */
    private List<DataItem> generateMatrix(List<BaseType[]> values, Map<BaseType[], Integer> labels) {
        boolean hasSideInfo;
        BaseType[] newRow;
        List<DataItem> matrix = new ArrayList<>(values.size());
        for (BaseType[] item : values) {
            // Extends item by k values.
            newRow = new BaseType[item.length + k];
            // Copy all old values from the unextended item.
            System.arraycopy(item, 0, newRow, 0, item.length);
            // Fill other values with zero.
            for (int i = 0; i < k; ++i)
                newRow[item.length + i] = IntegerType.ZERO;
            // Mark item if there are any side information.
            hasSideInfo = labels.containsKey(item);
            if (hasSideInfo) {
                newRow[item.length + labels.get(item)] = IntegerType.ONE;
                matrix.add(new DataItem(newRow, true, reverseLabelMap.get(labels.get(item))));
            } else
                matrix.add(new DataItem(newRow, false));
        }
        return matrix;
    }

    /**
     * Initialize clusters with items related to different clusters.
     *
     * @param matrix Concatenated data matrix.
     * @param k      Number of cluster.
     * @return Clusters with initialized centroids and labels.
     */
    private Cluster[] initializeClusters(List<DataItem> matrix, int k) {
        Cluster[] clusters = new Cluster[k];
        // Initialize clusters.
        for (int i = 0; i < k; ++i)
            clusters[i] = new Cluster(reverseLabelMap.get(i));
        // Split all values with side information into distinct clusters.
        for (DataItem item : matrix)
            if (item.hasSideInformation()) {
                int labelIndex = labelMap.get(item.getLabel());
                clusters[labelIndex].add(item);
            }
        // If there are empty clusters, then assign random values without labels to them.
        int index = 0;
        DataItem item;
        for (Cluster cluster : clusters)
            if (cluster.size() == 0)
                for (; index < matrix.size(); ++index)
                    if (!matrix.get(index).hasSideInformation()) {
                        item = matrix.get(index++);
                        assert (item != null);
                        cluster.add(item);
                        break;
                    }
        this.clusters = clusters;
        // Calculate coordinates of centroids.
        updateCentroids();
        return clusters;
    }

    /**
     * Remove all values from the {@code clusters}.
     */
    private void clearClusters(Cluster[] clusters) {
        for (Cluster cluster : clusters)
            cluster.getValues().clear();
    }

    /**
     * Assign data values to the nearest centroids.
     *
     * @param lambda Trade-off parameter of the CPLSI algorithm.
     */
    private void assignToCentroids(double lambda) {
        int i;
        int minIndex;
        double distance;
        double minDistance;
        clearClusters(clusters);
        for (DataItem item : matrix) {
            // Find closest centroid for the value.
            minIndex = -1;
            minDistance = Double.MAX_VALUE;
            for (i = 0; i < clusters.length; ++i) {
                distance = measure.distance(item, clusters[i].getCentroid(), k, lambda);
                if (distance < minDistance) {
                    minDistance = distance;
                    minIndex = i;
                }
            }
            // Assign item to the closest centroid.
            clusters[minIndex].add(item);
        }
    }

    /**
     * Update coordinates of the clusters' centroids.
     */
    private void updateCentroids() {
        NumericType[] mean;
        for (Cluster cluster : clusters) {
            if (cluster.size() != 0) {
                assert (cluster.size() > 0);
                assert (cluster.get(0) != null);
                int dataLength = cluster.get(0).size();
                mean = createRealArray(dataLength);
                dataLength -= k;
                for (DataItem row : cluster) {
                    // Use whole length of the record only if there is side information.
                    if (row.hasSideInformation())
                        NumericType.add(mean, row.getValues());
                    else
                        NumericType.add(mean, row.getValues(), dataLength);
                }
                NumericType.div(mean, cluster.size(), dataLength);
                NumericType.div(mean, cluster.size(), dataLength, k);
                cluster.setCentroid(new DataItem(mean, true, cluster.getLabel()));
            }
        }
    }

    /**
     * Create array of real values.
     *
     * @param n Size of the array.
     * @return Array of real values.
     */
    private NumericType[] createRealArray(int n) {
        RealType[] mean = new RealType[n];
        for (int i = 0; i < n; ++i)
            mean[i] = (RealType) BaseType.createDataValue(BaseType.Types.REAL);
        return mean;
    }

}
