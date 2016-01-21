/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014 Thomas Morgner
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package org.pentaho.reporting.sdk.datasource;

import org.pentaho.reporting.engine.classic.core.DataFactory;
import org.pentaho.reporting.engine.classic.core.ReportDataFactoryException;
import org.pentaho.reporting.engine.classic.core.testsupport.DataSourceTestBase;

import java.net.URISyntaxException;

public class SampleDataFactoryTest extends DataSourceTestBase {
  public static final String[][] QUERIES_AND_RESULTS = new String[][]{{"SELECT * FROM yourDatabase", "query1-results.txt"}};

  public SampleDataFactoryTest() {
  }

  public void testSaveAndLoad() throws Exception {
    runSaveAndLoad(QUERIES_AND_RESULTS);
  }

  public void testDerive() throws Exception {
    runDerive(QUERIES_AND_RESULTS);
  }

  public void testSerialize() throws Exception {
    runSerialize(QUERIES_AND_RESULTS);
  }

  public void testQuery() throws Exception {
    runTest(QUERIES_AND_RESULTS);
  }

  protected DataFactory createDataFactory(final String s) throws ReportDataFactoryException {
    try {
      SampleDataFactory sampleDataFactory = new SampleDataFactory();
      sampleDataFactory.setUrlPattern(getClass().getResource("SampleQuery.json").toURI().toASCIIString());
      sampleDataFactory.setQuery("default", s);
      return sampleDataFactory;
    } catch (URISyntaxException e) {
      throw new ReportDataFactoryException("Failed", e);
    }
  }
}
