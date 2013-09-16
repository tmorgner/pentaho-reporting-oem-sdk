package org.pentaho.reporting.sdk.sequence.parser;

import org.pentaho.reporting.libraries.xmlns.parser.XmlDocumentInfo;
import org.pentaho.reporting.libraries.xmlns.parser.XmlFactoryModule;
import org.pentaho.reporting.libraries.xmlns.parser.XmlReadHandler;
import org.pentaho.reporting.sdk.sequence.SampleDataSourceModule;
import org.pentaho.reporting.sdk.sequence.writer.SampleDataFactoryBundleWriterHandler;

public class SampleDataFactoryXmlFactoryModule implements XmlFactoryModule
{
  public SampleDataFactoryXmlFactoryModule()
  {
  }

  public XmlReadHandler createReadHandler(final XmlDocumentInfo xmlDocumentInfo)
  {
    return null;
  }

  public int getDocumentSupport(final XmlDocumentInfo documentInfo)
  {
    final String rootNamespace = documentInfo.getRootElementNameSpace();
    if (rootNamespace != null && rootNamespace.length() > 0)
    {
      if (SampleDataSourceModule.NAMESPACE.equals(rootNamespace) == false)
      {
        return XmlFactoryModule.NOT_RECOGNIZED;
      }
      else if (SampleDataFactoryBundleWriterHandler.ROOT_ELEMENT_TAG.equals(documentInfo.getRootElement()))
      {
        return XmlFactoryModule.RECOGNIZED_BY_NAMESPACE;
      }
    }
    else if (SampleDataFactoryBundleWriterHandler.ROOT_ELEMENT_TAG.equals(documentInfo.getRootElement()))
    {
      return XmlFactoryModule.RECOGNIZED_BY_TAGNAME;
    }

    return XmlFactoryModule.NOT_RECOGNIZED;
  }

  public String getDefaultNamespace(final XmlDocumentInfo xmlDocumentInfo)
  {
    return SampleDataSourceModule.NAMESPACE;
  }
}
