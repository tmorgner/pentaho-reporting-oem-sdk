package org.pentaho.reporting.sdk.datasource.parser;

import org.pentaho.reporting.engine.classic.core.modules.parser.base.common.QueryDefinitionReadHandler;
import org.pentaho.reporting.engine.classic.core.modules.parser.base.common.QueryDefinitionsReadHandler;
import org.pentaho.reporting.libraries.xmlns.parser.AbstractXmlReadHandler;
import org.pentaho.reporting.libraries.xmlns.parser.PropertyReadHandler;
import org.pentaho.reporting.libraries.xmlns.parser.XmlReadHandler;
import org.pentaho.reporting.sdk.datasource.SampleDataFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class SampleDataFactoryReadHandler extends AbstractXmlReadHandler
{
  private QueryDefinitionsReadHandler queryDefinitionsReadHandler;
  private ConfigurationReadHandler configurationReadHandler;
  private PropertyReadHandler globalScriptReadHandler;
  private SampleDataFactory dataFactory;

  public SampleDataFactoryReadHandler()
  {
    dataFactory = new SampleDataFactory();
  }

  protected XmlReadHandler getHandlerForChild(final String uri,
                                              final String tagName,
                                              final Attributes atts) throws SAXException
  {
    if (isSameNamespace(uri) == false)
    {
      return null;
    }

    if (SampleDataFactoryTags.CONFIG_TAG.equals(tagName))
    {
      configurationReadHandler = new ConfigurationReadHandler();
      return configurationReadHandler;
    }
    if (SampleDataFactoryTags.QUERY_DEFINITIONS_TAG.equals(tagName))
    {
      queryDefinitionsReadHandler = new QueryDefinitionsReadHandler();
      return queryDefinitionsReadHandler;
    }
    if (SampleDataFactoryTags.GLOBAL_SCRIPT_TAG.equals(tagName))
    {
      globalScriptReadHandler = new PropertyReadHandler(SampleDataFactoryTags.LANGUAGE_ATTR, true);
      return globalScriptReadHandler;
    }
    return super.getHandlerForChild(uri, tagName, atts);
  }

  protected void doneParsing() throws SAXException
  {
    if (configurationReadHandler != null)
    {
      dataFactory.setUrlPattern(configurationReadHandler.getUrlPattern());
    }
    if (queryDefinitionsReadHandler != null)
    {
      for (QueryDefinitionReadHandler sq : queryDefinitionsReadHandler.getScriptedQueries())
      {
        dataFactory.setQuery(sq.getName(), sq.getQuery(), sq.getScriptLanguage(), sq.getScript());
      }
    }
    if (globalScriptReadHandler != null)
    {
      dataFactory.setGlobalScript(globalScriptReadHandler.getResult());
      dataFactory.setGlobalScriptLanguage(globalScriptReadHandler.getName());
    }
  }

  public SampleDataFactory getObject() throws SAXException
  {
    return dataFactory;
  }
}
