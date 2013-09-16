package org.pentaho.reporting.sdk.sequence;

import java.net.URISyntaxException;

import org.pentaho.reporting.engine.classic.core.DataFactory;
import org.pentaho.reporting.engine.classic.core.ReportDataFactoryException;
import org.pentaho.reporting.engine.classic.core.testsupport.DataSourceTestBase;

public class SampleDataFactoryTest extends DataSourceTestBase
{
  public static final String[][] QUERIES_AND_RESULTS = new String[][]{{"SELECT * FROM yourDatabase", "query1-results.txt"}};

  public SampleDataFactoryTest()
  {
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

  protected DataFactory createDataFactory(final String s) throws ReportDataFactoryException
  {
    try
    {
      SampleDataFactory sampleDataFactory = new SampleDataFactory();
      sampleDataFactory.setQueryPattern(getClass().getResource("SampleQuery.json").toURI().toASCIIString());
      sampleDataFactory.setQuery("default", s);
      return sampleDataFactory;
    }
    catch (URISyntaxException e)
    {
      throw new ReportDataFactoryException("Failed", e);
    }
  }
}
