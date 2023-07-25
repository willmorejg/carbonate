/*
Licensed to the Apache Software Foundation (ASF) under one
or more contributor license agreements.  See the NOTICE file
distributed with this work for additional information
regarding copyright ownership.  The ASF licenses this file
to you under the Apache License, Version 2.0 (the
"License"); you may not use this file except in compliance
with the License.  You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing,
software distributed under the License is distributed on an
"AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
KIND, either express or implied.  See the License for the
specific language governing permissions and limitations
under the License.

James G Willmore - LJ Computing - (C) 2023
*/
package net.ljcomputing.carbonate;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.apache.calcite.sql.SqlNode;
import org.apache.calcite.sql.SqlWriterConfig;
import org.apache.calcite.sql.dialect.PostgresqlSqlDialect;
import org.apache.calcite.sql.parser.SqlParseException;
import org.apache.calcite.sql.parser.SqlParser;
import org.apache.calcite.sql.pretty.SqlPrettyWriter;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Order(1)
class CarbonateApplicationTests {
    private static final Logger log = LoggerFactory.getLogger(CarbonateApplicationTests.class);

    @Test
    @Order(1)
    void contextLoads() {
        assertTrue(true);
    }

    @Test
    @Order(10)
    void prettySql() throws SqlParseException {
        final String sql =
                "select x as a, b as b, c as c, d,"
                        + " 'mixed-Case string',"
                        + " unquotedCamelCaseId,"
                        + " \"quoted id\" "
                        + "from"
                        + " (select *"
                        + " from t"
                        + " where x = y and a > 5"
                        + " group by z, zz"
                        + " window w as (partition by c),"
                        + "  w1 as (partition by c,d order by a, b"
                        + "   range between interval '2:2' hour to minute preceding"
                        + "    and interval '1' day following)) "
                        + "order by gg desc nulls last, hh asc";
        SqlNode sqlNode = SqlParser.create(sql).parseQuery();
        SqlWriterConfig config =
                SqlPrettyWriter.config()
                        .withLineFolding(SqlWriterConfig.LineFolding.STEP)
                        .withSelectFolding(SqlWriterConfig.LineFolding.TALL)
                        .withFromFolding(SqlWriterConfig.LineFolding.TALL)
                        .withWhereFolding(SqlWriterConfig.LineFolding.TALL)
                        .withHavingFolding(SqlWriterConfig.LineFolding.TALL)
                        .withDialect(PostgresqlSqlDialect.DEFAULT)
                        .withLineLength(65)
                        .withLeadingComma(true)
                        .withIndentation(2)
                        .withClauseEndsLine(true);
        SqlPrettyWriter writer = new SqlPrettyWriter(config);
        String prettySql = writer.format(sqlNode);
        log.debug("sql: {}", prettySql);
        assertTrue(true);
    }
}
