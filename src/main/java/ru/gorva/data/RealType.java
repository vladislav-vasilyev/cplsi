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
 * The {@code RealType} represents real data type.
 *
 * @author Vladyslav Vasyliev
 *         Created on 25.11.16.
 */
public class RealType extends NumericType {
    private double value;

    public RealType(double value) {
        this.type = Types.REAL;
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
        double val = (value.getType() == Types.REAL) ? (double) obj : (int) obj;
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
        double val = (value.getType() == Types.REAL) ? (double) obj : (int) obj;
        this.value /= val;
        return this;
    }

    @Override
    public int hashCode() {
        return Double.valueOf(value).hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        if (obj instanceof RealType) {
            RealType value = (RealType) obj;
            return this.value == value.value;
        }
        return false;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

}
