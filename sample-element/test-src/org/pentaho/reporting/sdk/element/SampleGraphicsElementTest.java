package org.pentaho.reporting.sdk.element;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import junit.framework.TestCase;
import org.pentaho.reporting.engine.classic.core.ClassicEngineBoot;
import org.pentaho.reporting.engine.classic.core.Element;
import org.pentaho.reporting.engine.classic.core.MasterReport;
import org.pentaho.reporting.engine.classic.core.ReportProcessingException;
import org.pentaho.reporting.engine.classic.core.layout.model.LogicalPageBox;
import org.pentaho.reporting.engine.classic.core.layout.model.RenderNode;
import org.pentaho.reporting.engine.classic.core.layout.model.RenderableReplacedContent;
import org.pentaho.reporting.engine.classic.core.layout.model.RenderableReplacedContentBox;
import org.pentaho.reporting.engine.classic.core.layout.output.ContentProcessingException;
import org.pentaho.reporting.engine.classic.core.modules.output.pageable.pdf.PdfReportUtil;
import org.pentaho.reporting.engine.classic.core.modules.parser.bundle.writer.BundleWriter;
import org.pentaho.reporting.engine.classic.core.style.ElementStyleKeys;
import org.pentaho.reporting.engine.classic.core.testsupport.DebugReportRunner;
import org.pentaho.reporting.engine.classic.core.testsupport.selector.MatchFactory;
import org.pentaho.reporting.libraries.resourceloader.ResourceManager;
import org.pentaho.reporting.libraries.resourceloader.factory.drawable.DrawableWrapper;

public class SampleGraphicsElementTest extends TestCase
{
  public SampleGraphicsElementTest()
  {
  }

  public void setUp() throws Exception
  {
    ClassicEngineBoot.getInstance().start();
  }

  private Element createElement()
  {
    Element e = new Element();
    e.setName("test-element");
    e.setElementType(SampleGraphicsElementType.INSTANCE);
    // e.setAttribute(SampleElementModule.NAMESPACE, SampleGraphicsElementType.BACKGROUND_IMAGE, getClass().getResource("background.png"));
    e.setAttribute(SampleElementModule.NAMESPACE, SampleGraphicsElementType.TEMPLATE_STRING, "Test message for validation: {0}");
    e.getStyle().setStyleProperty(ElementStyleKeys.MIN_WIDTH, 200f);
    e.getStyle().setStyleProperty(ElementStyleKeys.MIN_HEIGHT, 20f);
    e.getStyle().setStyleProperty(ElementStyleKeys.DRAW_SHAPE, true);
    e.getStyle().setStyleProperty(SampleElementStyleKeys.IMAGE_BACKGROUND_ALPHA, 0.5f);
    return e;
  }

  public void testRunReport() throws ReportProcessingException, ContentProcessingException
  {
    MasterReport report = new MasterReport();
    report.getReportHeader().addElement(createElement());

    runAndValidateReport(report);
  }

  private void runAndValidateReport(final MasterReport report) throws ReportProcessingException, ContentProcessingException
  {
    LogicalPageBox box = DebugReportRunner.layoutSingleBand(report, report.getReportHeader());

    RenderNode node = MatchFactory.findElementByName(box, "test-element");

    assertEquals(RenderableReplacedContentBox.class, node.getClass());
    RenderableReplacedContentBox rb = (RenderableReplacedContentBox) node;
    RenderableReplacedContent content = rb.getContent();
    Object rawObject = content.getRawObject();
    assertTrue(rawObject instanceof DrawableWrapper);
    DrawableWrapper wrapper = (DrawableWrapper) rawObject;
    assertTrue(wrapper.getBackend() instanceof  SampleGraphicsDrawable);
    SampleGraphicsDrawable sgd = (SampleGraphicsDrawable) wrapper.getBackend();
    assertEquals("Test message for validation: null", sgd.getTextToPrint());
  }

  public void testSaveAndLoad() throws Exception
  {
    MasterReport report = new MasterReport();
    report.getReportHeader().addElement(createElement());

    ByteArrayOutputStream bout = new ByteArrayOutputStream();
    BundleWriter.writeReportToZipStream(report, bout);

    ResourceManager mgr = new ResourceManager();
    MasterReport loadedReport = (MasterReport) mgr.createDirectly(bout.toByteArray(), MasterReport.class).getResource();
    assertEquals(1, loadedReport.getReportHeader().getElementCount());

    Element element = loadedReport.getReportHeader().getElement(0);
    assertTrue(element.getElementType() instanceof SampleGraphicsElementType);
    assertEquals("Test message for validation: {0}",
        element.getAttribute(SampleElementModule.NAMESPACE, SampleGraphicsElementType.TEMPLATE_STRING));
    assertEquals(0.5f,element.getStyle().getStyleProperty(SampleElementStyleKeys.IMAGE_BACKGROUND_ALPHA));

    runAndValidateReport(loadedReport);
  }

  public void testSerialize() throws Exception
  {
    MasterReport report = new MasterReport();
    report.getReportHeader().addElement(createElement());

    ByteArrayOutputStream bout = new ByteArrayOutputStream();
    ObjectOutputStream oout = new ObjectOutputStream(bout);
    oout.writeObject(report);
    oout.close();

    ObjectInputStream oin = new ObjectInputStream(new ByteArrayInputStream(bout.toByteArray()));
    MasterReport loadedReport = (MasterReport) oin.readObject();
    assertEquals(1, loadedReport.getReportHeader().getElementCount());

    Element element = loadedReport.getReportHeader().getElement(0);
    assertTrue(element.getElementType() instanceof SampleGraphicsElementType);
    assertEquals("Test message for validation: {0}",
        element.getAttribute(SampleElementModule.NAMESPACE, SampleGraphicsElementType.TEMPLATE_STRING));
    assertEquals(0.5f,element.getStyle().getStyleProperty(SampleElementStyleKeys.IMAGE_BACKGROUND_ALPHA));

    runAndValidateReport(loadedReport);
  }


  public void testExportToPdf() throws ReportProcessingException, IOException
  {
    File f = new File ("test-output");
    f.mkdirs();

    MasterReport report = new MasterReport();
    report.getReportHeader().addElement(createElement());

    PdfReportUtil.createPDF(report, "test-output/sample-graphics-element.pdf");
  }

  public void testMetaDataDefined()
  {

  }
}
