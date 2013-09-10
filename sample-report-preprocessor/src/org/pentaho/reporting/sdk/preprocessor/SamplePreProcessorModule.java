package org.pentaho.reporting.sdk.preprocessor;

import org.pentaho.reporting.engine.classic.core.metadata.ElementMetaDataParser;
import org.pentaho.reporting.libraries.base.boot.AbstractModule;
import org.pentaho.reporting.libraries.base.boot.ModuleInitializeException;
import org.pentaho.reporting.libraries.base.boot.SubSystem;

public class SamplePreProcessorModule extends AbstractModule
{
  public SamplePreProcessorModule() throws ModuleInitializeException
  {
    loadModuleInfo();
  }

  public void initialize(final SubSystem subSystem) throws ModuleInitializeException
  {
    ElementMetaDataParser.initializeOptionalReportPreProcessorMetaData
        ("org/pentaho/reporting/sdk/preprocessor/meta-report-preprocessor.xml");
  }
}
