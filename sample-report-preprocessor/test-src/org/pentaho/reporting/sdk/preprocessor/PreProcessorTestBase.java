package org.pentaho.reporting.sdk.preprocessor;

import org.junit.Before;
import org.junit.Test;
import org.pentaho.reporting.engine.classic.core.ClassicEngineBoot;
import org.pentaho.reporting.engine.classic.core.ReportPreProcessor;

public abstract class PreProcessorTestBase
{
  @Before
  public void setUp() throws Exception
  {
    ClassicEngineBoot.getInstance().start();
  }

  @Test
  public void testMetaData() throws Exception
  {
    ReportPreProcessor prc = create();
    PreProcessorTestHelper.validateElementMetaData(prc.getClass());
  }

  protected abstract ReportPreProcessor create();

}
