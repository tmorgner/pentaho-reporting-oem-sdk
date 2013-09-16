package org.pentaho.reporting.sdk.sequence;

public class SampleDataSequenceTestGenerator
{
  public static void main(final String[] args) throws Exception
  {
    final SampleDataSequenceTest test = new SampleDataSequenceTest();
    test.setUp();
    test.runGenerate(SampleDataSequenceTest.QUERIES_AND_RESULTS);
  }
}
