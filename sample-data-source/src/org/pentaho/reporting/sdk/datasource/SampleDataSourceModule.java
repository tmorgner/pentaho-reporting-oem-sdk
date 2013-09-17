package org.pentaho.reporting.sdk.datasource;

import org.pentaho.reporting.engine.classic.core.metadata.ElementMetaDataParser;
import org.pentaho.reporting.engine.classic.core.modules.parser.base.DataFactoryXmlResourceFactory;
import org.pentaho.reporting.libraries.base.boot.AbstractModule;
import org.pentaho.reporting.libraries.base.boot.ModuleInitializeException;
import org.pentaho.reporting.libraries.base.boot.SubSystem;
import org.pentaho.reporting.sdk.datasource.parser.SampleDataFactoryXmlFactoryModule;

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
    ElementMetaDataParser.initializeOptionalDataFactoryMetaData
        ("org/pentaho/reporting/sdk/datasource/meta-datafactory.xml");

    // register the parser for PRPT-bundles
    DataFactoryXmlResourceFactory.register(SampleDataFactoryXmlFactoryModule.class);
  }
}
