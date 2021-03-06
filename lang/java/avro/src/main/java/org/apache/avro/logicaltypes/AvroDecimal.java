/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.avro.logicaltypes;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.ByteBuffer;

import org.apache.avro.AvroTypeException;
import org.apache.avro.Conversions.DecimalConversion;
import org.apache.avro.LogicalTypes;
import org.apache.avro.LogicalTypes.Decimal;
import org.apache.avro.Schema;
import org.apache.avro.Schema.Type;
import org.apache.avro.generic.GenericFixed;

public class AvroDecimal extends Decimal implements AvroPrimitive {
  private static final DecimalConversion DECIMAL_CONVERTER = new DecimalConversion();
  public static final String NAME = "DECIMAL";
  public static final String TYPENAME = LogicalTypes.DECIMAL;

  public AvroDecimal(String text) {
    super(text);
  }

  public AvroDecimal(int precision, int scale) {
    super(precision, scale);
  }

  public static AvroDecimal create(int precision, int scale) {
    return new AvroDecimal(precision, scale);
  }

  public AvroDecimal(Schema schema) {
    super(schema);
  }

  @Override
  public String toString() {
    return NAME + "(" + getPrecision() + "," + getScale() + ")";
  }

  @Override
  public Schema getRecommendedSchema() {
    return addToSchema(Schema.create(Type.BYTES));
  }

  @Override
  public ByteBuffer convertToRawType(Object value) {
    BigDecimal v = null;
    if (value == null) {
      return null;
    } else {
      if (value instanceof ByteBuffer) {
        return (ByteBuffer) value;
      } else if (value instanceof byte[]) {
        return ByteBuffer.wrap((byte[]) value);
      } else if (value instanceof GenericFixed) {
        return ByteBuffer.wrap(((GenericFixed) value).bytes());
      } else if (value instanceof BigDecimal) {
        ByteBuffer buffer = DECIMAL_CONVERTER.toBytes((BigDecimal) value, null, this);
        return buffer;
      } else if (value instanceof Number) {
        Number n = (Number) value;
        v = BigDecimal.valueOf(Double.valueOf(n.toString())).setScale(getScale(), RoundingMode.HALF_DOWN);
        return convertToRawType(v);
      } else if (value instanceof CharSequence) {
        try {
          v = new BigDecimal(((CharSequence) value).toString());
          return convertToRawType(v);
        } catch (NumberFormatException e) {
          throw new AvroTypeException("Cannot convert the string \"" + value + "\" into a BigDecimal");
        }
      }
    }
    throw new AvroTypeException(
        "Cannot convert a value of type \"" + value.getClass().getSimpleName() + "\" into a BigDecimal/ByteBuffer");
  }

  @Override
  public Type getBackingType() {
    return Type.BYTES;
  }

  @Override
  public AvroType getAvroType() {
    return AvroType.AVRODECIMAL;
  }

  @Override
  public BigDecimal convertToLogicalType(Object value) {
    if (value == null) {
      return null;
    } else if (value instanceof ByteBuffer) {
      return DECIMAL_CONVERTER.fromBytes((ByteBuffer) value, null, this);
    } else if (value instanceof GenericFixed) {
      return DECIMAL_CONVERTER.fromFixed((GenericFixed) value, null, this);
    }
    throw new AvroTypeException(
        "Cannot convert a value of type \"" + value.getClass().getSimpleName() + "\" into a BigDecimal");
  }

  @Override
  public Class<?> getConvertedType() {
    return BigDecimal.class;
  }

}
