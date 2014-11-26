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

import junit.framework.Assert;
import org.junit.Test;
import org.pentaho.reporting.engine.classic.core.AttributeNames;
import org.pentaho.reporting.engine.classic.core.Band;
import org.pentaho.reporting.engine.classic.core.MasterReport;
import org.pentaho.reporting.engine.classic.core.ReportPreProcessor;
import org.pentaho.reporting.engine.classic.core.filter.types.LabelType;
import org.pentaho.reporting.engine.classic.core.filter.types.TextFieldType;
import org.pentaho.reporting.engine.classic.core.layout.ModelPrinter;
import org.pentaho.reporting.engine.classic.core.layout.model.LogicalPageBox;
import org.pentaho.reporting.engine.classic.core.layout.model.RenderNode;
import org.pentaho.reporting.engine.classic.core.layout.table.TableTestUtil;
import org.pentaho.reporting.engine.classic.core.style.BandStyleKeys;
import org.pentaho.reporting.engine.classic.core.testsupport.DebugReportRunner;
import org.pentaho.reporting.engine.classic.core.testsupport.selector.MatchFactory;

public class SamplePreProcessorTest extends PreProcessorTestBase
{
  public SamplePreProcessorTest()
  {
  }

  protected ReportPreProcessor create()
  {
    SamplePreProcessor samplePreProcessor = new SamplePreProcessor();
    samplePreProcessor.setColor(Color.RED);
    samplePreProcessor.setText("Text");
    samplePreProcessor.setWidth(-50);
    samplePreProcessor.setX(-50);
    return samplePreProcessor;
  }

  @Test
  public void testApplyOnRun() throws Exception
  {
    MasterReport report = new MasterReport();
    report.addPreProcessor(create());
    report.getPageFooter().addElement(TableTestUtil.createDataItem("Dummy Item"));

    LogicalPageBox logicalPageBox = DebugReportRunner.layoutPage(report, 0);
    // Use this to create a print-out of the layout model. This allows you to inspect the model
    // more easily than with the debugger.
    //ModelPrinter.INSTANCE.print(logicalPageBox);

    RenderNode[] labels = MatchFactory.findElementsByElementType(logicalPageBox, LabelType.INSTANCE);
    Assert.assertEquals("Original label has been printed", 5, labels.length);
  }

  @Test
  public void testApply() throws Exception
  {
    ReportPreProcessor preProcessor = create();

    MasterReport report = new MasterReport();
    report.getPageFooter().addElement(TableTestUtil.createDataItem("Dummy Item"));
    report.addPreProcessor(preProcessor);


    MasterReport materialize = materialize(report.derive(true), preProcessor);
    Assert.assertEquals(BandStyleKeys.LAYOUT_BLOCK, materialize.getPageFooter().getLayout());
    Assert.assertEquals(2, materialize.getPageFooter().getElementCount());

    Band oldContent = (Band) materialize.getPageFooter().getElement(0);
    Assert.assertEquals(1, oldContent.getElementCount());
    Assert.assertEquals(report.getPageFooter().getElement(0).getObjectID(), oldContent.getElement(0).getObjectID());

    Band newContent = (Band) materialize.getPageFooter().getElement(1);
    Assert.assertEquals(1, newContent.getElementCount());
    Assert.assertEquals("Text",
        newContent.getElement(0).getAttribute(AttributeNames.Core.NAMESPACE, AttributeNames.Core.VALUE));

  }

}
