/* $Id: $
   Copyright 2012, G. Blake Meike

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */
package com.dka.profin.common;

import android.content.ContentValues;
import android.support.annotation.NonNull;
import android.support.v4.util.ArrayMap;

import java.util.Collections;
import java.util.Map;

/**
 * @author <a href="mailto:blake.meike@gmail.com">G. Blake Meike</a>
 * @version $Revision: $
 */
public class ColumnMap {

    /**
     * Column Type
     */
    public static enum Type {
        /**
         * boolean
         */
        BOOLEAN,
        /**
         * byte
         */
        BYTE,
        /**
         * byte[]
         */
        BYTEARRAY,
        /**
         * double
         */
        DOUBLE,
        /**
         * float
         */
        FLOAT,
        /**
         * int
         */
        INTEGER,
        /**
         * long
         */
        LONG,
        /**
         * short
         */
        SHORT,
        /**
         * String
         */
        STRING
    }

    ;

    /**
     * Builder
     */
    public static class Builder {

        private final Map<String, ColumnDef> colMap = new ArrayMap<>();

        /**
         * @param virtCol
         * @param actCol
         * @param type
         * @return the builder
         */
        @NonNull
        public Builder addColumn(String virtCol,
                                 String actCol,
                                 Type type) {
            colMap.put(virtCol, new ColumnDef(actCol, type));
            return this;
        }

        /**
         * @return the column map
         */
        @NonNull
        public ColumnMap build() {
            return new ColumnMap(colMap);
        }
    }

    private static class ColumnDef {

        private final String name;
        private final Type type;

        /**
         * @param name
         * @param type
         */
        public ColumnDef(String name,
                         Type type) {
            this.name = name;
            this.type = type;
        }

        /**
         * @param srcCol
         * @param src
         * @param dst
         */
        public void copy(String srcCol,
                         @NonNull ContentValues src,
                         @NonNull ContentValues dst) {
            switch (type) {
                case BOOLEAN:
                    dst.put(name, src.getAsBoolean(srcCol));
                    break;
                case BYTE:
                    dst.put(name, src.getAsByte(srcCol));
                    break;
                case BYTEARRAY:
                    dst.put(name, src.getAsByteArray(srcCol));
                    break;
                case DOUBLE:
                    dst.put(name, src.getAsDouble(srcCol));
                    break;
                case FLOAT:
                    dst.put(name, src.getAsFloat(srcCol));
                    break;
                case INTEGER:
                    dst.put(name, src.getAsInteger(srcCol));
                    break;
                case LONG:
                    dst.put(name, src.getAsLong(srcCol));
                    break;
                case SHORT:
                    dst.put(name, src.getAsShort(srcCol));
                    break;
                case STRING:
                    dst.put(name, src.getAsString(srcCol));
                    break;
            }
        }
    }


    private final Map<String, ColumnDef> colMap;

    ColumnMap(@NonNull Map<String, ColumnDef> colMap) {
        this.colMap = Collections.unmodifiableMap(colMap);
    }

    /**
     * @param vals
     * @return content values for actual table
     */
    @NonNull
    public ContentValues translateCols(@NonNull ContentValues vals) {
        ContentValues newVals = new ContentValues();
        for (Map.Entry<String, Object> entry : vals.valueSet()) {
            String colName = entry.getKey();
            ColumnDef colDef = colMap.get(colName);
            if (null == colDef) {
                throw new IllegalArgumentException(
                        "Unrecognized column: " + colName);
            }
            colDef.copy(colName, vals, newVals);
        }

        return newVals;
    }
}