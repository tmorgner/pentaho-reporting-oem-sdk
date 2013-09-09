package org.pentaho.reporting.sdk.expression;

import org.pentaho.reporting.engine.classic.core.metadata.ElementMetaDataParser;
import org.pentaho.reporting.libraries.base.boot.AbstractModule;
import org.pentaho.reporting.libraries.base.boot.ModuleInitializeException;
import org.pentaho.reporting.libraries.base.boot.SubSystem;

public class SampleExpressionModule extends AbstractModule
{
  public SampleExpressionModule() throws ModuleInitializeException
  {
    loadModuleInfo();
  }

  public void initialize(final SubSystem subSystem) throws ModuleInitializeException
  {
    ElementMetaDataParser.initializeOptionalExpressionsMetaData
        ("org/pentaho/reporting/sdk/expression/HelloWorldExpressionBundle.properties");
  }
}
