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

import org.apache.avro.Schema;
import org.apache.avro.Schema.Type;

/**
 * A varchar is a string up to a provided max length, holds 7-bit ASCII chars
 * only (minus special chars like backspace) and is sorted and compared binary.
 *
 */
public class AvroVarchar extends LogicalTypeWithLength {
  public static final String NAME = "VARCHAR";
  public static final String TYPENAME = NAME;
  private final Schema schema;

  private AvroVarchar(int length) {
    super(TYPENAME, length);
    schema = addToSchema(Schema.create(Type.STRING));
  }

  public AvroVarchar(String text) {
    this(getLength(text));
  }

  public AvroVarchar(Schema schema) {
    super(TYPENAME, schema);
    this.schema = schema;
  }

  public static AvroVarchar create(int length) {
    return new AvroVarchar(length);
  }

  @Override
  public CharSequence convertToRawType(Object value) {
    if (value == null) {
      return null;
    } else if (value instanceof CharSequence) {
      return (CharSequence) value;
    } else {
      return value.toString();
    }
  }

  @Override
  public CharSequence convertToLogicalType(Object value) {
    if (value == null) {
      return null;
    } else if (value instanceof CharSequence) {
      return (CharSequence) value;
    } else {
      return value.toString();
    }
  }

  @Override
  public Type getBackingType() {
    return Type.STRING;
  }

  @Override
  public Schema getRecommendedSchema() {
    return schema;
  }

  @Override
  public AvroType getAvroType() {
    return AvroType.AVROVARCHAR;
  }

  @Override
  public Class<?> getConvertedType() {
    return CharSequence.class;
  }

}
