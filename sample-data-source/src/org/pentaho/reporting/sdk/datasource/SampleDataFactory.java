package org.pentaho.reporting.sdk.datasource;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Map;
import javax.swing.table.TableModel;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONValue;
import org.pentaho.reporting.engine.classic.core.DataRow;
import org.pentaho.reporting.engine.classic.core.ReportDataFactoryException;
import org.pentaho.reporting.engine.classic.core.util.TypedTableModel;
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
      URL url = new URL(b.toString());
      InputStream inputStream = url.openStream();
      Map o = (Map) JSONValue.parseKeepingOrder(inputStream);
      Map root = (Map) o.get("query");
      Map results = (Map) root.get("results");
      JSONArray result = (JSONArray) results.get("Result");

      TypedTableModel model = new TypedTableModel();
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

      return model;
    }
    catch (IOException e)
    {
      throw new ReportDataFactoryException("Failed");
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
    ArrayList<String> hash = new ArrayList<String>();
    hash.add(realQuery);
    hash.add(urlPattern);
    return hash;

  }
}
