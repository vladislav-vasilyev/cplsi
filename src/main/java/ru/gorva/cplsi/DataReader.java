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

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import ru.gorva.data.BaseType;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.util.*;

/**
 * A comma separated file reader.
 *
 * @author Vladyslav Vasyliev
 *         Created on 25.11.16.
 */
public class DataReader {
    private File file;
    private Map<BaseType[], BaseType> labels;
    private Set<BaseType> distinctLabels;
    private List<BaseType[]> values;

    /**
     * @param fileName Name of the file to be read.
     */
    public DataReader(String fileName) {
        this.file = new File(fileName);
    }

    /**
     * @param file File to be read.
     */
    public DataReader(File file) {
        this.file = file;
    }

    /**
     * @return List of the read values.
     * @throws IOException in case of reading errors.
     */
    public ArrayList<BaseType[]> parse() throws IOException {
        try (Reader in = new java.io.FileReader(file)) {
            Iterable<CSVRecord> records = CSVFormat.DEFAULT.parse(in);
            int index;
            BaseType[] row;
            ArrayList<BaseType[]> values = new ArrayList<>();

            Iterator<String> iterator;
            for (CSVRecord record : records) {
                row = new BaseType[record.size()];
                values.add(row);
                iterator = record.iterator();
                index = 0;
                // Wrap each value into provided data types.
                while (iterator.hasNext())
                    row[index] = BaseType.createDataValue(iterator.next());
            }
            this.values = values;
            this.labels = new HashMap<>();
            return values;
        }
    }

    /**
     * @param labelIndex Index of the column which represents the label information.
     * @return List of the read values.
     * @throws IOException in case of reading errors.
     */
    public ArrayList<BaseType[]> parse(int labelIndex, int...skip) throws IOException {
        try (Reader in = new java.io.FileReader(file)) {
            Iterable<CSVRecord> records = CSVFormat.DEFAULT.parse(in);
            int index, colIndex, toEscape = skip.length + 1;
            BaseType value;
            BaseType[] row;
            Set<Integer> skipSet = new HashSet<>();
            Iterator<String> iterator;
            Map<BaseType[], BaseType> labels = new HashMap<>();
            ArrayList<BaseType[]> values = new ArrayList<>();

            for (Integer ival : skip)
                skipSet.add(ival);
            for (CSVRecord record : records) {
                row = new BaseType[record.size() - toEscape];
                values.add(row);
                index = 0;
                colIndex = 0;
                iterator = record.iterator();
                while (iterator.hasNext()) {
                    // Wrap each value into provided data types.
                    value = BaseType.createDataValue(iterator.next());
                    if (skipSet.contains(index)) {
                        ++index;
                        continue;
                    }
                    // Save information about record label if any.
                    if (index++ == labelIndex)
                        labels.put(row, value);
                    else
                        row[colIndex++] = value;
                }
            }
            this.values = values;
            this.labels = labels;
            this.distinctLabels = deriveDistinctLabels(labels);
            return values;
        }
    }

    /**
     * Derives distinct set of the label values.
     * @param labels Labels for the values.
     * @return Set of the cluster labels.
     */
    private Set<BaseType> deriveDistinctLabels(Map<BaseType[], BaseType> labels) {
        return new HashSet<>(labels.values());
    }

    /**
     * @return Set of the cluster labels.
     */
    public Set<BaseType> getDistinctLabels() {
        return distinctLabels;
    }

    /**
     * @return Map which contains labels for the given rows.
     */
    public Map<BaseType[], BaseType> getLabels() {
        return labels;
    }

    /**
     * @return Data values of the file.
     */
    public List<BaseType[]> getValues() {
        return values;
    }
}
