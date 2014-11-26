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

package org.pentaho.reporting.sdk.element.xml;

import org.pentaho.reporting.engine.classic.core.modules.parser.bundle.layout.StyleReadHandler;
import org.pentaho.reporting.engine.classic.core.style.ElementStyleSheet;
import org.pentaho.reporting.libraries.xmlns.common.ParserUtil;
import org.pentaho.reporting.libraries.xmlns.parser.AbstractXmlReadHandler;
import org.pentaho.reporting.sdk.element.SampleElementStyleKeys;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class SampleElementStyleReadHandler extends AbstractXmlReadHandler implements StyleReadHandler
{
  private ElementStyleSheet styleSheet;

  public SampleElementStyleReadHandler()
  {
  }

  public void setStyleSheet(final ElementStyleSheet styleSheet)
  {
    this.styleSheet = styleSheet;
  }

  public ElementStyleSheet getStyleSheet()
  {
    return styleSheet;
  }

  public ElementStyleSheet getObject() throws SAXException
  {
    return getStyleSheet();
  }

  /**
   * Starts parsing.
   *
   * @param attrs the attributes.
   * @throws SAXException if there is a parsing error.
   */
  protected void startParsing(final Attributes attrs) throws SAXException
  {
    final String alphaText = attrs.getValue(getUri(), SampleElementStyleKeys.IMAGE_BACKGROUND_ALPHA.name);
    if (alphaText != null)
    {
      float alpha = ParserUtil.parseFloat(alphaText,
          "Attribute '" + SampleElementStyleKeys.IMAGE_BACKGROUND_ALPHA + "' is not a valid float", getLocator());
      styleSheet.setStyleProperty(SampleElementStyleKeys.IMAGE_BACKGROUND_ALPHA, alpha);
    }

  }
}
