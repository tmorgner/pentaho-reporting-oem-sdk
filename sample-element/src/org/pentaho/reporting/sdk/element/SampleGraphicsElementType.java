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

package org.pentaho.reporting.sdk.element;

import java.text.MessageFormat;

import org.pentaho.reporting.engine.classic.core.DataRow;
import org.pentaho.reporting.engine.classic.core.DefaultImageReference;
import org.pentaho.reporting.engine.classic.core.ReportElement;
import org.pentaho.reporting.engine.classic.core.filter.types.ContentType;
import org.pentaho.reporting.engine.classic.core.filter.types.ElementTypeUtils;
import org.pentaho.reporting.engine.classic.core.function.ExpressionRuntime;
import org.pentaho.reporting.engine.classic.core.metadata.ElementType;
import org.pentaho.reporting.libraries.resourceloader.factory.drawable.DrawableWrapper;

public class SampleGraphicsElementType extends ContentType
{
  public static final ElementType INSTANCE = new SampleGraphicsElementType();
  public static final String ELEMENT_TYPE_NAME = "sdk-sample-graphics-element";
  public static final String TEMPLATE_STRING = "template-string";
  public static final String BACKGROUND_IMAGE = "background-image";

  public SampleGraphicsElementType()
  {
    super(ELEMENT_TYPE_NAME);
  }

  public Object getDesignValue(final ExpressionRuntime runtime, final ReportElement element)
  {
    final String message = ElementTypeUtils.getStringAttribute
        (element, SampleElementModule.NAMESPACE, TEMPLATE_STRING, "Hello, {0}");
    final String textToPrint = MessageFormat.format(message, "Report-Designer!");

    final Object backgroundImageRaw = element.getAttribute(SampleElementModule.NAMESPACE, BACKGROUND_IMAGE);
    final Object image = filter(runtime, element, backgroundImageRaw);
    if (image instanceof DefaultImageReference)
    {
      return new SampleGraphicsDrawable(textToPrint, (DefaultImageReference) image);
    }
    else if (image instanceof DrawableWrapper)
    {
      return new SampleGraphicsDrawable(textToPrint, (DrawableWrapper) image);
    }
    else
    {
      return new SampleGraphicsDrawable(textToPrint);
    }
  }

  public Object getValue(final ExpressionRuntime runtime, final ReportElement element)
  {
    // Elements can get their value from attributes directly, or can query a field and/or value via
    // the helper methods on the ElementTypeUtils class.
    final String message = ElementTypeUtils.getStringAttribute
        (element, SampleElementModule.NAMESPACE, TEMPLATE_STRING, "Hello, {0}");

    // The data-row grants access to all currently computed values. Use this to access fields from
    // the data-source or calculated measures from the named expressions and functions.
    final DataRow dataRow = runtime.getDataRow();
    final Object userName = dataRow.get("env::username");
    final String textToPrint = MessageFormat.format(message, userName);

    final Object backgroundImageRaw = element.getAttribute(SampleElementModule.NAMESPACE, BACKGROUND_IMAGE);
    final Object image = filter(runtime, element, backgroundImageRaw);
    if (image instanceof DefaultImageReference)
    {
      return new SampleGraphicsDrawable(textToPrint, (DefaultImageReference) image);
    }
    else if (image instanceof DrawableWrapper)
    {
      return new SampleGraphicsDrawable(textToPrint, (DrawableWrapper) image);
    }
    else
    {
      return new SampleGraphicsDrawable(textToPrint);
    }
  }
}
