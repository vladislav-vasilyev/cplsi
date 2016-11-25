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
 * The {@code BaseType} class represents the base data type for the clustering algorithm.
 * @author Vladyslav Vasyliev
 *         Created on 25.11.16.
 */
public abstract class BaseType {
    public interface Types {
        int INTEGER = 0;
        int REAL = 1;
        int STRING = 2;
    }

    protected int type;

    /**
     * Factory method which creates the data value from the given object.
     * @param value to be wrapped.
     * @return value wrapped into one of the BaseType's children.
     */
    public static BaseType createDataValue(Object value) {
        String string = value.toString().trim();
        try {
            int integer = Integer.parseInt(string);
            return new IntegerType(integer);
        } catch (NumberFormatException e) {
            try {
                double real = Double.parseDouble(string);
                return new RealType(real);
            } catch (NumberFormatException e2) {
                return new StringType(string);
            }
        }
    }

    /**
     * Factory method which creates the data value from the given object.
     * @param type Type of the instance to be created.
     * @return Instance of the specified type with default value.
     */
    public static BaseType createDataValue(int type) {
        switch (type) {
            case Types.INTEGER:
                return new IntegerType(0);
            case Types.REAL:
                return new RealType(0);
            case Types.STRING:
                return new StringType("");
            default:
                throw new IllegalArgumentException("Unknown type! Possible values are " +
                        "Types.INTEGER, Types.REAL, and Types.STRING.");
        }
    }

    /**
     * @return Value of the object.
     */
    public abstract Object getValue();

    /**
     * @return Type of the object. Possible values are INTEGER, REAL, STRING.
     */
    public int getType() {
        return type;
    }

}
