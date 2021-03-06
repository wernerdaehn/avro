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
import java.time.ZonedDateTime;
import java.util.Date;

import org.apache.avro.AvroTypeException;
import org.apache.avro.LogicalTypes;
import org.apache.avro.LogicalTypes.TimestampMicros;
import org.apache.avro.Schema;
import org.apache.avro.Schema.Type;
import org.apache.avro.data.TimeConversions.TimestampMicrosConversion;

/**
 * Wrapper of LogicalTypes.timestampMicros()
 *
 */
public class AvroTimestampMicros extends TimestampMicros implements AvroPrimitive {
  private static final Schema SCHEMA;
  private static final AvroTimestampMicros ELEMENT = new AvroTimestampMicros();
  static {
    SCHEMA = ELEMENT.addToSchema(Schema.create(Type.LONG));
  }
  public static final String NAME = "TIMESTAMPMICROS";
  public static final String TYPENAME = LogicalTypes.TIMESTAMP_MICROS;
  private static final TimestampMicrosConversion CONVERTER = new TimestampMicrosConversion();

  private AvroTimestampMicros() {
    super();
  }

  @Override
  public String toString() {
    return NAME;
  }

  public static AvroTimestampMicros create() {
    return ELEMENT;
  }

  @Override
  public Long convertToRawType(Object value) {
    if (value == null) {
      return null;
    } else if (value instanceof Long) {
      return (Long) value;
    } else if (value instanceof Number) {
      return ((Number) value).longValue();
    } else if (value instanceof Date) {
      return convertToRawType(((Date) value).toInstant());
    } else if (value instanceof ZonedDateTime) {
      return convertToRawType(((ZonedDateTime) value).toInstant());
    } else if (value instanceof CharSequence) {
      return convertToRawType(Instant.parse((CharSequence) value));
    } else if (value instanceof Instant) {
      return CONVERTER.toLong((Instant) value, SCHEMA, this);
    }
    throw new AvroTypeException(
        "Cannot convert a value of type \"" + value.getClass().getSimpleName() + "\" into a TimestampMicros");
  }

  @Override
  public Instant convertToLogicalType(Object value) {
    if (value == null) {
      return null;
    } else if (value instanceof Long) {
      return CONVERTER.fromLong((Long) value, null, this);
    }
    throw new AvroTypeException(
        "Cannot convert a value of type \"" + value.getClass().getSimpleName() + "\" into a TimestampMicros");
  }

  @Override
  public Type getBackingType() {
    return Type.LONG;
  }

  @Override
  public Schema getRecommendedSchema() {
    return SCHEMA;
  }

  @Override
  public AvroType getAvroType() {
    return AvroType.AVROTIMESTAMPMICROS;
  }

  @Override
  public Class<?> getConvertedType() {
    return Instant.class;
  }

}
