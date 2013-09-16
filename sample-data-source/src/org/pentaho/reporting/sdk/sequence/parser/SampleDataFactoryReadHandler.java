package org.pentaho.reporting.sdk.sequence.parser;

import org.pentaho.reporting.libraries.xmlns.parser.AbstractXmlReadHandler;
import org.pentaho.reporting.sdk.sequence.SampleDataFactory;
import org.xml.sax.SAXException;

public class SampleDataFactoryReadHandler extends AbstractXmlReadHandler
{
  private SampleDataFactory dataFactory;

  public SampleDataFactoryReadHandler()
  {
    dataFactory = new SampleDataFactory();
  }

  public SampleDataFactory getObject() throws SAXException
  {
    return dataFactory;
  }
}
