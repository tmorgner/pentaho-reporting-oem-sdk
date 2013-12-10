package org.pentaho.reporting.engine.sdk.drilldown;

import java.net.URL;

import org.pentaho.reporting.engine.classic.extensions.drilldown.DrillDownModule;
import org.pentaho.reporting.engine.classic.extensions.drilldown.DrillDownProfileMetaData;
import org.pentaho.reporting.libraries.base.boot.AbstractModule;
import org.pentaho.reporting.libraries.base.boot.ModuleInitializeException;
import org.pentaho.reporting.libraries.base.boot.SubSystem;
import org.pentaho.reporting.libraries.base.util.ObjectUtilities;

public class SampleDrilldownModule extends AbstractModule
{
  public SampleDrilldownModule() throws ModuleInitializeException
  {
    loadModuleInfo();
  }

  public void initialize(final SubSystem subSystem) throws ModuleInitializeException
  {
    final URL expressionMetaSource = ObjectUtilities.getResource
        ("org/pentaho/reporting/engine/sdk/drilldown/drilldown-profile.xml", DrillDownModule.class);
    if (expressionMetaSource == null)
    {
      throw new ModuleInitializeException("Error: Could not find the drilldown meta-data description file");
    }
    try
    {
      DrillDownProfileMetaData.getInstance().registerFromXml(expressionMetaSource);
    }
    catch (Exception e)
    {
      throw new ModuleInitializeException("Error: Could not parse the drilldown meta-data description file", e);
    }
  }
}
