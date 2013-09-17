package org.pentaho.reporting.sdk.datasource;

public class SampleDataFactoryTestGenerator
{
  public static void main(final String[] args) throws Exception
  {
    final SampleDataFactoryTest test = new SampleDataFactoryTest();
    test.setUp();
    test.runGenerate(SampleDataFactoryTest.QUERIES_AND_RESULTS);
  }
}
