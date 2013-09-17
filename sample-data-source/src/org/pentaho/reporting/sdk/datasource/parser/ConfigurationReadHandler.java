package org.pentaho.reporting.sdk.datasource.parser;

import org.pentaho.reporting.libraries.base.util.StringUtils;
import org.pentaho.reporting.libraries.xmlns.parser.AbstractXmlReadHandler;
import org.pentaho.reporting.libraries.xmlns.parser.ParseException;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class ConfigurationReadHandler extends AbstractXmlReadHandler
{
  private String urlPattern;

  public ConfigurationReadHandler()
  {
  }

  protected void startParsing(final Attributes attrs) throws SAXException
  {
    urlPattern = attrs.getValue(getUri(), SampleDataFactoryTags.URL_PATTERN_ATTR);
    if (StringUtils.isEmpty(urlPattern))
    {
      throw new ParseException
          (String.format("Mandatory attribute '%s' missing.", SampleDataFactoryTags.URL_PATTERN_ATTR), getLocator());
    }
  }

  public String getUrlPattern()
  {
    return urlPattern;
  }

  public Object getObject() throws SAXException
  {
    return null;
  }
}
