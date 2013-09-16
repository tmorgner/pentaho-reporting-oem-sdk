package org.pentaho.reporting.sdk.sequence;

import org.pentaho.reporting.engine.classic.core.metadata.ElementMetaDataParser;
import org.pentaho.reporting.libraries.base.boot.AbstractModule;
import org.pentaho.reporting.libraries.base.boot.ModuleInitializeException;
import org.pentaho.reporting.libraries.base.boot.SubSystem;

public class SampleDataSourceModule extends AbstractModule
{
  public static final String NAMESPACE =
        "http://reporting.pentaho.org/namespaces/engine/classic/sdk/sample-data-source/1.0";

  public SampleDataSourceModule() throws ModuleInitializeException
  {
    loadModuleInfo();
  }

  public void initialize(final SubSystem subSystem) throws ModuleInitializeException
  {
    ElementMetaDataParser.initializeOptionalDataFactoryMetaData(
        "org/pentaho/reporting/sdk/sequence/meta-datafactory.xml");
  }
}
