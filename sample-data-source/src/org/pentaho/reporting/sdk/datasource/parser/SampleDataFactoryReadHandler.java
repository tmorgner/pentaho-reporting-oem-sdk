/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014 Thomas Morgner
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

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
