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

package org.pentaho.reporting.sdk.sequence;

import org.pentaho.reporting.engine.classic.core.DataFactory;
import org.pentaho.reporting.engine.classic.core.ReportDataFactoryException;
import org.pentaho.reporting.engine.classic.core.TableDataFactory;
import org.pentaho.reporting.engine.classic.core.designtime.datafactory.DesignTimeDataFactoryContext;
import org.pentaho.reporting.engine.classic.core.modules.misc.datafactory.sequence.SequenceDataFactory;
import org.pentaho.reporting.engine.classic.core.testsupport.DataSourceTestBase;
import org.pentaho.reporting.engine.classic.core.util.TypedTableModel;

public class SampleDataSequenceTest extends DataSourceTestBase
{
  private static class TestDataFactoryContext extends DesignTimeDataFactoryContext
  {
    private TestDataFactoryContext()
    {

    }

    public DataFactory getContextDataFactory()
    {
      final TypedTableModel tableModel = new TypedTableModel();
      tableModel.addColumn("Id", Integer.class);
      tableModel.addColumn("Value", String.class);
      tableModel.addRow(1, "North");
      tableModel.addRow(2, "East");
      tableModel.addRow(3, "South");
      tableModel.addRow(4, "West");
      return new TableDataFactory("backend-query", tableModel);
    }
  }

  public static final String[][] QUERIES_AND_RESULTS =  new String[][]{{"query1", "query1-results.txt"}};

  public SampleDataSequenceTest()
  {
  }

  protected void initializeDataFactory(final DataFactory dataFactory) throws ReportDataFactoryException
  {
    TestDataFactoryContext dataFactoryContext = new TestDataFactoryContext();
    dataFactory.initialize(dataFactoryContext);
  }

  public void testSaveAndLoad() throws Exception
  {
    runSaveAndLoad(QUERIES_AND_RESULTS);
  }

  public void testDerive() throws Exception
  {
    runDerive(QUERIES_AND_RESULTS);
  }

  public void testSerialize() throws Exception
  {
    runSerialize(QUERIES_AND_RESULTS);
  }

  public void testQuery() throws Exception
  {
    runTest(QUERIES_AND_RESULTS);
  }

  protected DataFactory createDataFactory(final String query) throws ReportDataFactoryException
  {
    SampleDataSequence sequence = new SampleDataSequence();
    sequence.setBackendQuery("backend-query");
    sequence.setAllParameterText("(Select All)");
    sequence.setAllParameterValue("-1");

    SequenceDataFactory dataFactory = new SequenceDataFactory();
    dataFactory.addSequence("default", sequence);
    return dataFactory;
  }


}
