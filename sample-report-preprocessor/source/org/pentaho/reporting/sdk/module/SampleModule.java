package org.pentaho.reporting.sdk.element;

import org.pentaho.reporting.libraries.base.boot.AbstractModule;
import org.pentaho.reporting.libraries.base.boot.ModuleInitializeException;
import org.pentaho.reporting.libraries.base.boot.SubSystem;

public class SampleModule extends AbstractModule
{
  public SampleElementModule() throws ModuleInitializeException
  {
    loadModuleInfo();
  }

  public void initialize(final SubSystem subSystem) throws ModuleInitializeException
  {

  }
}
