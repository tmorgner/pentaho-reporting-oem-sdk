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
    ModelPrinter.INSTANCE.print(logicalPageBox);

    RenderNode[] labels = MatchFactory.findElementsByElementType(logicalPageBox, LabelType.INSTANCE);
    Assert.assertEquals("Original label has been printed", 3, labels.length);

    RenderNode[] fields = MatchFactory.findElementsByElementType(logicalPageBox, TextFieldType.INSTANCE);
    Assert.assertEquals("Generated text-field has been printed", 7, fields.length);
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
