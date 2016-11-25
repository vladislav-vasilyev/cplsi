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
 * The {@code IntegerType} represents integer data type.
 *
 * @author Vladyslav Vasyliev
 *         Created on 25.11.16.
 */
public class IntegerType extends NumericType {
    public static IntegerType ZERO = new IntegerType(0);
    public static IntegerType ONE = new IntegerType(1);

    private int value;

    public IntegerType(int value) {
        this.type = Types.INTEGER;
        this.value = value;
    }

    @Override
    public Object getValue() {
        return value;
    }

    /**
     * Element-wise addition of the current values by values stored in
     * {@code value}.
     *
     * @param value Second operand of the operation.
     * @return {@code this}.
     */
    @Override
    public NumericType add(NumericType value) {
        Object obj = value.getValue();
        int val = (value.getType() == Types.REAL) ? (int) ((double) obj) : (int) obj;
        this.value += val;
        return this;
    }

    /**
     * Element-wise division of the current values by values stored in
     * {@code value}.
     *
     * @param value Denominator of the operation.
     * @return {@code this}.
     */
    @Override
    public NumericType div(NumericType value) {
        Object obj = value.getValue();
        int val = (value.getType() == Types.REAL) ? (int) ((double) obj) : (int) obj;
        this.value /= val;
        return this;
    }

    @Override
    public int hashCode() {
        return value;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        if (obj instanceof IntegerType) {
            IntegerType value = (IntegerType) obj;
            return this.value == value.value;
        }
        return false;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

}
