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

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Locale;

import org.pentaho.reporting.engine.classic.core.DataRow;
import org.pentaho.reporting.engine.classic.core.util.PropertyLookupParser;
import org.pentaho.reporting.libraries.base.util.CSVTokenizer;
import org.pentaho.reporting.libraries.formatting.FastMessageFormat;

public class QueryParametrizer extends PropertyLookupParser
{
  private LinkedHashMap<String,String> collectedParameter;
  private LinkedHashSet<String> collectedFields;
  private DataRow parameters;
  private Locale locale;
  private int counter;

  /**
   * Default Constructor.
   */
  public QueryParametrizer(final DataRow parameters,
                           final Locale locale)
  {
    if (locale == null)
    {
      throw new NullPointerException("Locale must not be null");
    }
    if (parameters == null)
    {
      throw new NullPointerException("Parameter datarow must not be null");
    }

    this.collectedFields = new LinkedHashSet<>();
    this.collectedParameter = new LinkedHashMap<>();
    this.parameters = parameters;
    this.locale = locale;
    setMarkerChar('$');
    setOpeningBraceChar('{');
    setClosingBraceChar('}');
  }

  /**
   * Looks up the property with the given name. This replaces the name with the current index position.
   *
   * @param name the name of the property to look up.
   * @return the translated value.
   */
  protected String lookupVariable(final String name)
  {
    final CSVTokenizer tokenizer = new CSVTokenizer(name, false);
    if (tokenizer.hasMoreTokens() == false)
    {
      // invalid reference ..
      return null;
    }

    counter += 1;
    String parameterReference = "param_" + counter; // NON-NLS

    final String parameterName = tokenizer.nextToken();
    final Object o = parameters.get(parameterName);
    String subType = null;
    final StringBuilder b = new StringBuilder(name.length() + 4);
    b.append('{');
    b.append("0");
    while (tokenizer.hasMoreTokens())
    {
      b.append(',');
      final String token = tokenizer.nextToken();
      b.append(token);
      if (subType == null)
      {
        subType = token;
      }
    }
    b.append('}');
    final String formatString = b.toString();

    if ("string".equals(subType))
    {
      if (o == null)
      {
        return "null"; // NON-NLS
      }
      return quote(String.valueOf(o));
    }

    final FastMessageFormat messageFormat = new FastMessageFormat(formatString, locale);
    final String parameterValue = messageFormat.format(new Object[]{o});

    collectedFields.add(parameterName);
    collectedParameter.put(parameterReference, parameterValue);
    return "@" + parameterReference;
  }

  public LinkedHashSet<String> getCollectedFields()
  {
    return collectedFields;
  }

  public LinkedHashMap<String, String> getCollectedParameter()
  {
    return collectedParameter;
  }

  protected static String quote(final String original)
  {
    // This solution needs improvements. Copy blocks instead of single
    // characters.
    final int length = original.length();
    final StringBuilder b = new StringBuilder(length * 12 / 10);
    b.append('"');

    for (int i = 0; i < length; i++)
    {
      final char c = original.charAt(i);
      if (c == '"')
      {
        b.append('"');
        b.append('"');
      }
      else
      {
        b.append(c);
      }
    }
    b.append('"');
    return b.toString();
  }

}
