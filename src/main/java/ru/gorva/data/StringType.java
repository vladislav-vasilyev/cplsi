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
package ru.gorva.data;

/**
 * The {@code StringType} class represents string data type.
 *
 * @author Vladyslav Vasyliev
 *         Created on 25.11.16.
 */
public class StringType extends BaseType {
    private String value;

    public StringType(String string) {
        this.type = Types.STRING;
        this.value = string;
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        if (obj instanceof StringType) {
            StringType value = (StringType) obj;
            return this.value.equals(value.value);
        }
        return false;
    }

    @Override
    public Object getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }
}
