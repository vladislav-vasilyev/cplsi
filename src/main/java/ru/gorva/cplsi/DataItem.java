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
 * This class represents data row with its side information is any.
 *
 * @author Vladyslav Vasyliev
 *         Created on 25.11.16.
 */
public class DataItem {
    private BaseType label;
    private BaseType[] values;
    private boolean hasSideInformation;

    public DataItem(BaseType[] values, boolean hasSideInformation) {
        this(values, hasSideInformation, null);
    }

    public DataItem(BaseType[] values, boolean hasSideInformation, BaseType label) {
        this.values = values;
        this.hasSideInformation = hasSideInformation;
        this.label = label;
    }

    public BaseType getLabel() {
        return label;
    }

    public void setLabel(BaseType label) {
        this.label = label;
    }

    public int size() {
        return values.length;
    }

    public BaseType[] getValues() {
        return values;
    }

    public boolean hasSideInformation() {
        return hasSideInformation;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        for (BaseType value: values)
            hash += 37 * hash + value.hashCode();
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        if (obj instanceof DataItem) {
            // Two object are equals in case if all their data values are equal.
            DataItem other = (DataItem) obj;
            for (int i = 0; i < values.length; ++i)
                if (!values[i].equals(other.values[i]))
                    return false;
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (BaseType value : values) {
            sb.append(value);
            sb.append(' ');
        }
        return sb.toString();
    }
}
