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

import java.io.IOException;
import java.util.Locale;

import org.pentaho.reporting.engine.classic.core.modules.parser.bundle.writer.styles.BundleStyleSetWriteHandler;
import org.pentaho.reporting.engine.classic.core.style.ElementStyleSheet;
import org.pentaho.reporting.libraries.formatting.FastDecimalFormat;
import org.pentaho.reporting.libraries.xmlns.common.AttributeList;
import org.pentaho.reporting.libraries.xmlns.writer.XmlWriter;
import org.pentaho.reporting.sdk.element.SampleElementModule;
import org.pentaho.reporting.sdk.element.SampleElementStyleKeys;

public class SampleElementStyleWriter implements BundleStyleSetWriteHandler
{
  public static final String TAG_NAME = "sample-element-style";

  private static FastDecimalFormat getAbsoluteLengthFormat()
  {
    return new FastDecimalFormat("0.###", Locale.US);
  }

  public SampleElementStyleWriter()
  {
  }

  public void writeStyle(final XmlWriter xmlWriter, final ElementStyleSheet elementStyleSheet) throws IOException
  {
    Object alphaValue = elementStyleSheet.getStyleProperty(SampleElementStyleKeys.IMAGE_BACKGROUND_ALPHA);
    if (alphaValue == null)
    {
      return;
    }

    AttributeList attrs = new AttributeList();
    attrs.setAttribute(SampleElementModule.NAMESPACE, SampleElementStyleKeys.IMAGE_BACKGROUND_ALPHA.getName(),
        getAbsoluteLengthFormat().format(alphaValue));

    xmlWriter.writeTag(SampleElementModule.NAMESPACE, TAG_NAME, attrs, XmlWriter.CLOSE);
  }
}
