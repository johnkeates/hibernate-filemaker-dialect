/*
 * Copyright Keates Creative Technology and/or its affiliates
 * and other contributors as indicated by the @author tags and
 * the COPYRIGHT.txt file distributed with this work.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package nl.keates.filemaker.hibernate.dialect;

import java.sql.Types;

import org.hibernate.LockMode;
import org.hibernate.dialect.Dialect;
import org.hibernate.dialect.function.NoArgSQLFunction;
import org.hibernate.dialect.function.StandardSQLFunction;
import org.hibernate.type.BlobType;
import org.hibernate.type.DateType;
import org.hibernate.type.DoubleType;
import org.hibernate.type.StringType;
import org.hibernate.type.TimeType;
import org.hibernate.type.TimestampType;
import org.hibernate.type.LongType;

public class FileMakerDialect extends Dialect {
	private static StringType STRING = StringType.INSTANCE;
    private static DoubleType DOUBLE = DoubleType.INSTANCE;
    private static DateType DATE = DateType.INSTANCE;
    private static TimeType TIME = TimeType.INSTANCE;
	private static TimestampType TIMESTAMP = TimestampType.INSTANCE;
	private static BlobType BLOB = BlobType.INSTANCE;
    private static BlobType LONG = LongType.INSTANCE;
	
    public FileMakerDialect() {
        // Register types
        registerColumnType(Types.VARCHAR, "varchar");
        registerColumnType(Types.VARCHAR, "string");
        registerColumnType(Types.DOUBLE, "double");
        registerColumnType(Types.DOUBLE, "decimal");
        registerColumnType(Types.DATE, "date");
        registerColumnType(Types.TIME, "time"); 
        registerColumnType(Types.TIMESTAMP, "timestamp"); 
        registerColumnType(Types.BLOB, "blob");

        registerFunction("sum", new StandardSQLFunction("sum", DOUBLE));
        registerFunction("avg", new StandardSQLFunction("avg", DOUBLE));
        registerFunction("count", new StandardSQLFunction("count", LONG));
        registerFunction("chr", new StandardSQLFunction("chr", STRING));
        registerFunction("current_user", new StandardSQLFunction("current_user", STRING));
        registerFunction("dayname", new StandardSQLFunction("dayname", STRING));
        registerFunction("rtrim", new StandardSQLFunction("rtrim", STRING));
        registerFunction("trim", new StandardSQLFunction("trim", STRING));
        registerFunction("ltrim", new StandardSQLFunction("ltrim", STRING));
        registerFunction("upper", new StandardSQLFunction("upper", STRING));
        registerFunction("lower", new StandardSQLFunction("lower", STRING));
        registerFunction("left", new StandardSQLFunction("left", STRING));
        registerFunction("monthname", new StandardSQLFunction("monthname", STRING));
        registerFunction("right", new StandardSQLFunction("right", STRING));
        registerFunction("substring", new StandardSQLFunction("substring", STRING));
        registerFunction("space", new StandardSQLFunction("space", STRING));
        registerFunction("strval", new StandardSQLFunction("strval", STRING));
        registerFunction("current_time", new StandardSQLFunction("current_time", STRING));
        registerFunction("time", new StandardSQLFunction("time", STRING));
        registerFunction("timeval", new StandardSQLFunction("timeval", STRING));
        registerFunction("username", new StandardSQLFunction("username", STRING));
        registerFunction("user", new StandardSQLFunction("user", STRING));
        registerFunction("abs", new StandardSQLFunction("abs", DOUBLE));
        registerFunction("atan", new StandardSQLFunction("atan", DOUBLE));
        registerFunction("atan2", new StandardSQLFunction("atan2", DOUBLE));
        registerFunction("ceiling", new StandardSQLFunction("ceiling", DOUBLE));
        registerFunction("ceil", new StandardSQLFunction("ceil", DOUBLE));
        registerFunction("degrees", new StandardSQLFunction("degrees", DOUBLE));
        registerFunction("deg", new StandardSQLFunction("deg", DOUBLE));
        registerFunction("day", new StandardSQLFunction("day", DOUBLE));
        registerFunction("dayofweek", new StandardSQLFunction("dayofweek", DOUBLE));
        registerFunction("mod", new StandardSQLFunction("mod", DOUBLE));
        registerFunction("exp", new StandardSQLFunction("exp", DOUBLE));
        registerFunction("floor", new StandardSQLFunction("floor", DOUBLE));
        registerFunction("hour", new StandardSQLFunction("hour", DOUBLE));
        registerFunction("int", new StandardSQLFunction("int", DOUBLE));
        registerFunction("length", new StandardSQLFunction("length", DOUBLE));
        registerFunction("month", new StandardSQLFunction("month", DOUBLE));
        registerFunction("ln", new StandardSQLFunction("ln", DOUBLE));
        registerFunction("log", new StandardSQLFunction("log", DOUBLE));
        registerFunction("max", new StandardSQLFunction("max", DOUBLE));
        registerFunction("min", new StandardSQLFunction("min", DOUBLE));
        registerFunction("minute", new StandardSQLFunction("minute", DOUBLE));
        registerFunction("numval", new StandardSQLFunction("numval", DOUBLE));
        registerFunction("pi", new StandardSQLFunction("pi", DOUBLE));
        registerFunction("radians", new StandardSQLFunction("radians", DOUBLE));
        registerFunction("round", new StandardSQLFunction("round", DOUBLE));
        registerFunction("second", new StandardSQLFunction("second", DOUBLE));
        registerFunction("sign", new StandardSQLFunction("sign", DOUBLE));
        registerFunction("sin", new StandardSQLFunction("sin", DOUBLE));
        registerFunction("sqrt", new StandardSQLFunction("sqrt", DOUBLE));
        registerFunction("tan", new StandardSQLFunction("tan", DOUBLE));
        registerFunction("year", new StandardSQLFunction("year", DOUBLE));
        registerFunction("curdate", new NoArgSQLFunction("curdate", DATE));
        registerFunction("current_date", new NoArgSQLFunction("current_date", DATE));
        registerFunction("curtime", new NoArgSQLFunction("curtime", TIME));

    }

    @Override
    public LimitHandler getLimitHandler() {

        return new AbstractLimitHandler() {
            @Override
            public boolean supportsLimit() {
                return true;
            }

            @Override
            public String processSql(String sql, RowSelection selection) {

                String soff = String.format(" offset %d rows /*?*/", selection.getFirstRow());
                String slim = String.format(" fetch first %d rows only /*?*/", selection.getMaxRows());
                StringBuilder sb = (new StringBuilder(sql.length() + soff.length() + slim.length())).append(sql);

                if (LimitHelper.hasFirstRow(selection)) {
                    sb.append(soff);
                }

                if (LimitHelper.hasMaxRows(selection)) {
                    sb.append(slim);
                }

                return sb.toString();
            }

        };
    }

    public boolean dropConstraints() {
        return false;
    }

    public boolean hasAlterTable() {
        return false;
    }

    public boolean supportsColumnCheck() {
        return false;
    }

    public boolean supportsCascadeDelete() {
        return false;
    }

    public boolean supportsLockTimeouts() { 
        return false; 
    }

    public boolean canCreateSchema() {
        return false;
    }

    public String getCurrentTimestampSQLFunctionName() {
        return "current_timestamp";
    }

    public boolean isCurrentTimestampSelectStringCallable() {
        return false;
    }

    public boolean supportsCurrentTimestampSelection() {
        return true;
    }

    public boolean supportsOuterJoinForUpdate() {
        return false;
    }

    public boolean supportsTableCheck() {
        return false;
    }

    public boolean supportsUnionAll() {
        return false;
    }

    public boolean supportsUnique() {
        return false;
    }

    public String toBooleanValueString(boolean arg0) {
        if (arg0) {
            return "{b'true'}"; 
        }
        return "{b'false'}"; 
    }

	public String getForUpdateNowaitString() {
		return "";
	}

	public String getForUpdateNowaitString(String aliases) {
		return "";
	}

	public String getForUpdateString() {
		return ""; 
	}

	public String getForUpdateString(LockMode lockMode) {
		return ""; 
	}

	public String getForUpdateString(String aliases) {
		return ""; 
	}
		
	@Override
	public boolean supportsSequences() {
		return false;
	}
	
	@Override
	public boolean supportsPooledSequences() {
		return false;
	}

}

