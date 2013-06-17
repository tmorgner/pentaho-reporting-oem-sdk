package org.pentaho.reporting.sdk.element;

import org.pentaho.reporting.libraries.base.boot.AbstractModule;
import org.pentaho.reporting.libraries.base.boot.ModuleInitializeException;
import org.pentaho.reporting.libraries.base.boot.SubSystem;

public class SampleElementModule extends AbstractModule
{
  public static final String NAMESPACE =
      "http://reporting.pentaho.org/namespaces/engine/classic/sdk/sample-element/1.0";

  public SampleElementModule() throws ModuleInitializeException
  {
    loadModuleInfo();
  }

  public void initialize(final SubSystem subSystem) throws ModuleInitializeException
  {

  }
}
