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
