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

import org.pentaho.reporting.libraries.xmlns.parser.XmlDocumentInfo;
import org.pentaho.reporting.libraries.xmlns.parser.XmlFactoryModule;
import org.pentaho.reporting.libraries.xmlns.parser.XmlReadHandler;
import org.pentaho.reporting.sdk.datasource.SampleDataSourceModule;

public class SampleDataFactoryXmlFactoryModule implements XmlFactoryModule
{
  public SampleDataFactoryXmlFactoryModule()
  {
  }

  public XmlReadHandler createReadHandler(final XmlDocumentInfo xmlDocumentInfo)
  {
    return new SampleDataFactoryReadHandler();
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
      else if (SampleDataFactoryTags.ROOT_ELEMENT_TAG.equals(documentInfo.getRootElement()))
      {
        return XmlFactoryModule.RECOGNIZED_BY_NAMESPACE;
      }
    }
    else if (SampleDataFactoryTags.ROOT_ELEMENT_TAG.equals(documentInfo.getRootElement()))
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
