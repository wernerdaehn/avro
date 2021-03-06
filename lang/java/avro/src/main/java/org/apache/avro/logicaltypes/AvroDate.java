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

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

import org.apache.avro.AvroTypeException;
import org.apache.avro.LogicalTypes;
import org.apache.avro.Schema;
import org.apache.avro.Schema.Type;
import org.apache.avro.data.TimeConversions.DateConversion;

/**
 * Based on a Avro Type.INT holds the date portion without time.
 *
 */
public class AvroDate extends LogicalTypes.Date implements AvroPrimitive {
  public static final String NAME = "DATE";
  public static final String TYPENAME = LogicalTypes.DATE;
  private static final Schema SCHEMA;
  private static final AvroDate ELEMENT = new AvroDate();
  private static final DateConversion CONVERTER = new DateConversion();

  static {
    SCHEMA = ELEMENT.addToSchema(Schema.create(Type.INT));
  }

  private AvroDate() {
    super();
  }

  public static AvroDate create() {
    return ELEMENT;
  }

  @Override
  public String toString() {
    return NAME;
  }

  @Override
  public Integer convertToRawType(Object value) {
    if (value == null) {
      return null;
    } else if (value instanceof Integer) {
      return (Integer) value;
    } else if (value instanceof LocalDate) {
      return CONVERTER.toInt((LocalDate) value, null, this);
    } else if (value instanceof Number) {
      return convertToRawType(((Number) value).intValue());
    } else if (value instanceof CharSequence) {
      return convertToRawType(LocalDate.parse((CharSequence) value));
    } else if (value instanceof Date) {
      return convertToRawType(((Date) value).toInstant());
    } else if (value instanceof ZonedDateTime) {
      return convertToRawType(((ZonedDateTime) value).toLocalDate());
    } else if (value instanceof Instant) {
      Instant d = (Instant) value;
      return convertToRawType(ZonedDateTime.ofInstant(d, ZoneId.of("UTC")));
    }
    throw new AvroTypeException(
        "Conversion from type \"" + value.getClass().getSimpleName() + "\" into a LocalDate/Integer is not supported");
  }

  @Override
  public LocalDate convertToLogicalType(Object value) {
    if (value == null) {
      return null;
    } else if (value instanceof Integer) {
      return CONVERTER.fromInt((Integer) value, null, this);
    }
    throw new AvroTypeException("Cannot convert a value of type \"" + value.getClass().getSimpleName()
        + "\" into a LocalDate, value must be an INTEGER");
  }

  @Override
  public Type getBackingType() {
    return Type.INT;
  }

  @Override
  public Schema getRecommendedSchema() {
    return SCHEMA;
  }

  @Override
  public AvroType getAvroType() {
    return AvroType.AVRODATE;
  }

  @Override
  public Class<?> getConvertedType() {
    return LocalDate.class;
  }

}
