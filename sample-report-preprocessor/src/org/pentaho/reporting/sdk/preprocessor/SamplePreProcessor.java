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

package org.pentaho.reporting.sdk.preprocessor;

import java.awt.Color;

import org.pentaho.reporting.engine.classic.core.AbstractReportPreProcessor;
import org.pentaho.reporting.engine.classic.core.AttributeNames;
import org.pentaho.reporting.engine.classic.core.Band;
import org.pentaho.reporting.engine.classic.core.Element;
import org.pentaho.reporting.engine.classic.core.MasterReport;
import org.pentaho.reporting.engine.classic.core.PageFooter;
import org.pentaho.reporting.engine.classic.core.ReportProcessingException;
import org.pentaho.reporting.engine.classic.core.filter.types.LabelType;
import org.pentaho.reporting.engine.classic.core.states.datarow.DefaultFlowController;
import org.pentaho.reporting.engine.classic.core.style.BandStyleKeys;
import org.pentaho.reporting.engine.classic.core.style.ElementStyleKeys;

public class SamplePreProcessor extends AbstractReportPreProcessor
{
  private Color color;
  private String text;
  private float width;
  private float x;

  public SamplePreProcessor()
  {
    color = Color.BLACK;
    text = "Hello World!";
  }

  public Color getColor()
  {
    return color;
  }

  public void setColor(final Color color)
  {
    this.color = color;
  }

  public String getText()
  {
    return text;
  }

  public void setText(final String text)
  {
    this.text = text;
  }

  public float getWidth()
  {
    return width;
  }

  public void setWidth(final float width)
  {
    this.width = width;
  }

  public float getX()
  {
    return x;
  }

  public void setX(final float x)
  {
    this.x = x;
  }

  /**
   * Moves the existing page-footer content into a new subband and adds a new field to the
   * footer.
   *
   * @param definition
   * @param flowController
   * @return
   * @throws ReportProcessingException
   */
  public MasterReport performPreProcessing(final MasterReport definition,
                                           final DefaultFlowController flowController) throws ReportProcessingException
  {
    final PageFooter pageFooter = definition.getPageFooter();
    final Band oldContent = new Band();
    for (Element e : pageFooter)
    {
      oldContent.addElement(e);
    }

    oldContent.setLayout(pageFooter.getLayout());

    Element textField = new Element();
    textField.setElementType(LabelType.INSTANCE);
    textField.setAttribute(AttributeNames.Core.NAMESPACE, AttributeNames.Core.VALUE, text);
    textField.getStyle().setStyleProperty(ElementStyleKeys.PAINT, color);
    textField.getStyle().setStyleProperty(ElementStyleKeys.POS_X, x);
    textField.getStyle().setStyleProperty(ElementStyleKeys.MIN_WIDTH, x);
    textField.getStyle().setStyleProperty(ElementStyleKeys.DYNAMIC_HEIGHT, true);

    Band newContent = new Band();
    newContent.addElement(textField);

    pageFooter.setLayout(BandStyleKeys.LAYOUT_BLOCK);
    pageFooter.addElement(oldContent);
    pageFooter.addElement(newContent);
    return definition;
  }
}
