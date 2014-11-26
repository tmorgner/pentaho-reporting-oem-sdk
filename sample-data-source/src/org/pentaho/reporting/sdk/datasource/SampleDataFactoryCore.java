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

package org.pentaho.reporting.sdk.datasource;

import org.pentaho.reporting.engine.classic.core.DataFactory;
import org.pentaho.reporting.engine.classic.core.DataRow;
import org.pentaho.reporting.engine.classic.core.metadata.DataFactoryCore;
import org.pentaho.reporting.engine.classic.core.metadata.DataFactoryMetaData;
import org.pentaho.reporting.engine.classic.core.metadata.ResourceReference;
import org.pentaho.reporting.libraries.resourceloader.ResourceManager;

public class SampleDataFactoryCore implements DataFactoryCore
{
  public SampleDataFactoryCore()
  {
  }

  public String getDisplayConnectionName(final DataFactoryMetaData dataFactoryMetaData, final DataFactory dataFactory)
  {
    SampleDataFactory sampleDataFactory = (SampleDataFactory) dataFactory;
    return sampleDataFactory.getDisplayConnectionName();
  }

  public String[] getReferencedFields(final DataFactoryMetaData dataFactoryMetaData,
                                      final DataFactory dataFactory,
                                      final String query,
                                      final DataRow dataRow)
  {
    SampleDataFactory sampleDataFactory = (SampleDataFactory) dataFactory;
    return sampleDataFactory.getReferencedFields(query, dataRow);
  }

  public ResourceReference[] getReferencedResources(final DataFactoryMetaData dataFactoryMetaData,
                                                    final DataFactory dataFactory,
                                                    final ResourceManager resourceManager,
                                                    final String query,
                                                    final DataRow dataRow)
  {
    return new ResourceReference[0];
  }

  public Object getQueryHash(final DataFactoryMetaData dataFactoryMetaData,
                             final DataFactory dataFactory,
                             final String query,
                             final DataRow dataRow)
  {
    SampleDataFactory sampleDataFactory = (SampleDataFactory) dataFactory;
    return sampleDataFactory.getQueryHash(query, dataRow);
  }
}
