package org.pentaho.reporting.sdk.module;

import org.pentaho.reporting.engine.classic.core.ClassicEngineBoot;
import org.pentaho.reporting.libraries.base.boot.AbstractModule;
import org.pentaho.reporting.libraries.base.boot.ModuleInitializeException;
import org.pentaho.reporting.libraries.base.boot.SubSystem;

public class SampleModule extends AbstractModule
{
  public SampleModule() throws ModuleInitializeException
  {
    loadModuleInfo();
  }

  public void initialize(final SubSystem subSystem) throws ModuleInitializeException
  {
    System.out.println ("Module " + getName() + " initialised.");
    System.out.println ("Our sample configuration property is: " +
        subSystem.getGlobalConfig().getConfigProperty("org.pentaho.reporting.sdk.module.SampleConfigurationProperty"));
    // note: The global configuration is also available via the "ClassicEngineBoot" class
    System.out.println ("Our sample configuration property is: " +
        ClassicEngineBoot.getInstance().getGlobalConfig().getConfigProperty
            ("org.pentaho.reporting.sdk.module.SampleConfigurationProperty"));
  }
}
