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

import org.pentaho.reporting.engine.classic.core.Element;
import org.pentaho.reporting.engine.classic.core.modules.parser.bundle.writer.BundleWriterException;
import org.pentaho.reporting.engine.classic.core.modules.parser.bundle.writer.BundleWriterState;
import org.pentaho.reporting.engine.classic.core.modules.parser.bundle.writer.elements.AbstractElementWriteHandler;
import org.pentaho.reporting.libraries.docbundle.WriteableDocumentBundle;
import org.pentaho.reporting.libraries.xmlns.common.AttributeList;
import org.pentaho.reporting.libraries.xmlns.writer.XmlWriter;
import org.pentaho.reporting.libraries.xmlns.writer.XmlWriterSupport;
import org.pentaho.reporting.sdk.element.SampleElementModule;
import org.pentaho.reporting.sdk.element.SampleTextElementType;

public class SampleTextElementWriteHandler extends AbstractElementWriteHandler
{
  public SampleTextElementWriteHandler()
  {
  }

  /**
   * Writes a single element as XML structure.
   *
   * @param bundle    the bundle to which to write to.
   * @param state     the current write-state.
   * @param xmlWriter the xml writer.
   * @param element   the element.
   * @throws IOException           if an IO error occured.
   * @throws BundleWriterException if an Bundle writer.
   */
  public void writeElement(final WriteableDocumentBundle bundle,
                           final BundleWriterState state,
                           final XmlWriter xmlWriter,
                           final Element element)
      throws IOException, BundleWriterException
  {
    if (bundle == null)
    {
      throw new NullPointerException();
    }
    if (state == null)
    {
      throw new NullPointerException();
    }
    if (xmlWriter == null)
    {
      throw new NullPointerException();
    }
    if (element == null)
    {
      throw new NullPointerException();
    }

    final AttributeList attList = createMainAttributes(element, xmlWriter);
    xmlWriter.writeTag(SampleElementModule.NAMESPACE, SampleTextElementType.ELEMENT_TYPE_NAME, attList, XmlWriterSupport.OPEN);
    writeElementBody(bundle, state, element, xmlWriter);
    xmlWriter.writeCloseTag();
  }
}