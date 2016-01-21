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

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.MessageFormat;
import java.util.*;
import javax.swing.table.TableModel;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONValue;
import org.pentaho.reporting.engine.classic.core.DataFactoryContext;
import org.pentaho.reporting.engine.classic.core.DataRow;
import org.pentaho.reporting.engine.classic.core.ReportDataFactoryException;
import org.pentaho.reporting.engine.classic.core.util.TypedTableModel;
import org.pentaho.reporting.libraries.base.util.DebugLog;
import org.pentaho.reporting.libraries.base.util.URLEncoder;

public class SampleDataFactory extends AbstractScriptableDataFactory
{
  public static final String YAHOO_QUERY_DEFAULT =
      "http://query.yahooapis.com/v1/public/yql?q={0}&format=json&diagnostics=true&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys";
  private String urlPattern;

  public SampleDataFactory()
  {
    urlPattern = null;
  }

  public String getUrlPattern()
  {
    return urlPattern;
  }

  public void setUrlPattern(final String queryPattern)
  {
    this.urlPattern = queryPattern;
  }

  @Override
  public void initialize(DataFactoryContext dataFactoryContext) throws ReportDataFactoryException {
    super.initialize(dataFactoryContext);
  }

  public void close()
  {
  }

  protected TableModel queryDataInternal(final String realQuery,
                                         final DataRow parameters) throws ReportDataFactoryException
  {
    QueryParametrizer parametrizer = new QueryParametrizer(parameters, Locale.US);
    String query = parametrizer.translateAndLookup(realQuery);

    String queryEncoded = URLEncoder.encodeUTF8(query);
    String urlPattern = this.urlPattern != null ? this.urlPattern : YAHOO_QUERY_DEFAULT;
    StringBuilder b = new StringBuilder();
    b.append(MessageFormat.format(urlPattern, queryEncoded));

    Map<String, String> collectedParameter = parametrizer.getCollectedParameter();
    for (Map.Entry<String, String> entry : collectedParameter.entrySet())
    {
      String key = entry.getKey();
      String value = entry.getValue();
      b.append('&');
      b.append(key);
      b.append('=');
      b.append(URLEncoder.encodeUTF8(value));
    }

    try
    {
      DebugLog.log("Using query: " + b);
      URL url = new URL(b.toString());
      InputStream inputStream = url.openStream();
      Map o = (Map) JSONValue.parseKeepingOrder(inputStream);
      Map root = (Map) o.get("query"); // NON-NLS
      Map results = (Map) root.get("results"); // NON-NLS
      TypedTableModel model = new TypedTableModel();
      for(Object or: results.values())
      {
        if (or instanceof JSONArray == false)
        {
          continue;
        }
        JSONArray result = (JSONArray) or; // NON-NLS
        for (Object o1 : result)
        {
          Map m = (Map) o1;
          if (model.getRowCount() == 0)
          {
            for (Object key : m.keySet())
            {
              model.addColumn(String.valueOf(key), Object.class);
            }
          }
          model.addRow(m.values().toArray());
        }

      }

      return model;
    }
    catch (IOException e)
    {
      throw new ReportDataFactoryException("Failed", e);
    }
  }

  public String getDisplayConnectionName()
  {
    return urlPattern != null ? urlPattern : YAHOO_QUERY_DEFAULT;
  }

  protected String[] getReferencedFieldsInternal(final String query,
                                                 final DataRow parameters) throws ReportDataFactoryException
  {
    QueryParametrizer parametrizer = new QueryParametrizer(parameters, Locale.US);
    parametrizer.translateAndLookup(query);

    LinkedHashSet<String> collectedFields = parametrizer.getCollectedFields();
    return collectedFields.toArray(new String[collectedFields.size()]);
  }

  protected Object getQueryHashInternal(final String realQuery,
                                        final DataRow parameter) throws ReportDataFactoryException
  {
    ArrayList<String> hash = new ArrayList<>();
    hash.add(realQuery);
    hash.add(urlPattern);
    return hash;

  }
}
