package org.pentaho.reporting.sdk.module;

import org.junit.Assert;
import org.junit.Test;
import org.pentaho.reporting.engine.classic.core.ClassicEngineBoot;

public class StartTest
{
  @Test
  public void testEngineStart()
  {
    ClassicEngineBoot.getInstance().start();
    Assert.assertTrue(ClassicEngineBoot.getInstance().getPackageManager().isModuleAvailable(SampleModule.class.getName()));
  }
}
