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
 * The {@code NumericType} represents all numeric data types.
 *
 * @author Vladyslav Vasyliev
 *         Created on 25.11.16.
 */
public abstract class NumericType extends BaseType {

    /**
     * Element-wise addition of the current values by values stored in
     * {@code value}.
     *
     * @param value Second operand of the operation.
     * @return {@code this}.
     */
    public abstract NumericType add(NumericType value);

    /**
     * Element-wise division of the current values by values stored in
     * {@code value}.
     *
     * @param value Denominator of the operation.
     * @return {@code this}.
     */
    public abstract NumericType div(NumericType value);

    // ------------------------------------------------------------------------
    // Static methods. Arithmetic operations for the numeric objects.
    // ------------------------------------------------------------------------

    /**
     * Element-wise addition of the values.
     * Result of the operation will be stored into {@code value1}.
     *
     * @param values1 Recipient of the operation.
     * @param values2 Value to be added.
     */
    public static void add(NumericType[] values1, BaseType[] values2) {
        if (values1 == null || values2 == null)
            throw new NullPointerException("Values should be initialized");
        if (values1.length != values2.length)
            throw new IllegalArgumentException("Length of the operands should be the same!");
        for (int i = 0; i < values1.length; ++i)
            values1[i].add((NumericType) values2[i]);
    }

    /**
     * Element-wise addition of the first {@code n} values.
     * Result of the operation will be stored into {@code value1}.
     *
     * @param values1 Recipient of the operation.
     * @param values2 Value to be added.
     * @param n       Number of values to be added.
     */
    public static void add(NumericType[] values1, BaseType[] values2, int n) {
        if (values1 == null || values2 == null)
            throw new NullPointerException("Values should be initialized!");
        if (values1.length != values2.length)
            throw new IllegalArgumentException("Length of the operands should be the same!");
        if (n > values1.length)
            throw new IllegalArgumentException("'n' can't exceed length of the arrays!");
        if (n < 0)
            throw new IllegalArgumentException("'n' can't be negative!");
        for (int i = 0; i < n; ++i)
            values1[i].add((NumericType) values2[i]);
    }

    /**
     * Element-wise division of the values.
     * Result of the operation will be stored into {@code value1}.
     *
     * @param values      Source and recipient of the operation.
     * @param denominator Each value of the {@code values} will be divided
     *                    on this value.
     */
    public static void div(NumericType[] values, double denominator) {
        if (values == null)
            throw new NullPointerException("Array should be initialized!");
        RealType denom = new RealType(denominator);
        for (int i = 0; i < values.length; ++i)
            values[i].div(denom);
    }

    /**
     * Element-wise division of the first {@code n} values.
     * Result of the operation will be stored into {@code value1}.
     *
     * @param values      Source and recipient of the operation.
     * @param denominator Each value of the {@code values} will be divided
     *                    on this value.
     * @param n           Number of values to be divided.
     */
    public static void div(NumericType[] values, double denominator, int n) {
        if (values == null)
            throw new NullPointerException("Values should be initialized!");
        if (n >= values.length)
            throw new IllegalArgumentException("'n' can't exceed length of the array!");
        RealType denom = new RealType(denominator);
        for (int i = 0; i < n; ++i)
            values[i].div(denom);
    }

    /**
     * Element-wise division of {@code n} values starting from the
     * {@code beginIndex} index. Result of the operation will be stored into
     * {@code value1}.
     *
     * @param values      Source and recipient of the operation.
     * @param denominator Each value of the {@code values} will be divided
     *                    on this value.
     * @param beginIndex  Index of the value from which division will be started.
     * @param n           Number of values to be divided.
     */
    public static void div(NumericType[] values, double denominator, int beginIndex, int n) {
        if (values == null)
            throw new NullPointerException("Values should be initialized!");
        if (beginIndex >= values.length)
            throw new IllegalArgumentException("'n' can't exceed length of the array!");
        if (beginIndex + n > values.length)
            throw new IllegalArgumentException("'beginIndex + n' can't exceed length of the array!");
        RealType denom = new RealType(denominator);
        for (int i = beginIndex; i < beginIndex + n; ++i)
            values[i].div(denom);
    }

}
