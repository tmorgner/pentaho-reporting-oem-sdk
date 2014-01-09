package org.pentaho.reporting.sdk.preprocessor;

import javax.swing.table.TableModel;

import junit.framework.Assert;
import org.junit.Test;
import org.pentaho.reporting.engine.classic.core.AttributeNames;
import org.pentaho.reporting.engine.classic.core.DataFactory;
import org.pentaho.reporting.engine.classic.core.MasterReport;
import org.pentaho.reporting.engine.classic.core.ReportPreProcessor;
import org.pentaho.reporting.engine.classic.core.StaticDataRow;
import org.pentaho.reporting.engine.classic.core.SubReport;
import org.pentaho.reporting.engine.classic.core.TableDataFactory;
import org.pentaho.reporting.engine.classic.core.filter.types.bands.SubReportType;
import org.pentaho.reporting.engine.classic.core.layout.ModelPrinter;
import org.pentaho.reporting.engine.classic.core.layout.model.LayoutNodeTypes;
import org.pentaho.reporting.engine.classic.core.layout.model.LogicalPageBox;
import org.pentaho.reporting.engine.classic.core.layout.model.RenderNode;
import org.pentaho.reporting.engine.classic.core.layout.table.TableTestUtil;
import org.pentaho.reporting.engine.classic.core.parameters.DefaultListParameter;
import org.pentaho.reporting.engine.classic.core.parameters.DefaultParameterDefinition;
import org.pentaho.reporting.engine.classic.core.parameters.PlainParameter;
import org.pentaho.reporting.engine.classic.core.testsupport.DebugReportRunner;
import org.pentaho.reporting.engine.classic.core.testsupport.selector.MatchFactory;
import org.pentaho.reporting.engine.classic.core.util.TypedTableModel;
import org.pentaho.reporting.engine.classic.wizard.WizardProcessor;

public class GenerateParameterInformationPreProcessorTest extends PreProcessorTestBase
{
  public GenerateParameterInformationPreProcessorTest()
  {
  }

  protected ReportPreProcessor create()
  {
    GenerateParameterInformationPreProcessor samplePreProcessor = new GenerateParameterInformationPreProcessor();
    return samplePreProcessor;
  }

  @Test
  public void testApplyOnRun() throws Exception
  {
    ReportPreProcessor preProcessor = create();
    MasterReport report = configureReport();
    report.addPreProcessor(preProcessor);

    LogicalPageBox logicalPageBox = DebugReportRunner.layoutPage(report, 0);
    ModelPrinter.INSTANCE.print(logicalPageBox);

    RenderNode[] subReports = MatchFactory.findElementsByElementType(logicalPageBox, SubReportType.INSTANCE);
    Assert.assertEquals("SubReport has been added", 1, subReports.length);

    RenderNode[] fields = MatchFactory.findElementsByNodeType(subReports[0], LayoutNodeTypes.TYPE_BOX_PARAGRAPH);
    Assert.assertEquals("Generated text-field have been printed", 6, fields.length);
  }

  private MasterReport configureReport()
  {
    PlainParameter plainParam = new PlainParameter("text-parameter", String.class);
    plainParam.setDefaultValue("Hello World!");

    DefaultListParameter listParam = new DefaultListParameter
        ("param-query" , "key", "value", "list-param", true, false, String.class);
    listParam.setDefaultValue(new String[]{"One", "Two", "Three"});

    DefaultParameterDefinition params = new DefaultParameterDefinition();
    params.addParameterDefinition(plainParam);
    params.addParameterDefinition(listParam);

    TypedTableModel model = new TypedTableModel();
    model.addColumn("key", String.class);
    model.addColumn("value", String.class);
    model.addRow("One", "First Row");
    model.addRow("Two", "Second Row");
    model.addRow("Three", "Third Row");
    model.addRow("Four", "Fourth Row");

    TableDataFactory dataFactory = new TableDataFactory();
    dataFactory.addTable("param-query", model);

    MasterReport report = new MasterReport();
    report.setDataFactory(dataFactory);
    report.getPageFooter().addElement(TableTestUtil.createDataItem("Dummy Item"));
    report.setParameterDefinition(params);
    return report;
  }

  @Test
  public void testApply() throws Exception
  {
    ReportPreProcessor preProcessor = create();

    MasterReport report = configureReport();
    report.addPreProcessor(preProcessor);


    MasterReport materialize = materialize(report.derive(true), preProcessor);

    Assert.assertEquals(1, materialize.getReportHeader().getSubReportCount());
    SubReport sr = materialize.getReportHeader().getSubReport(0);
    DataFactory dataFactory = sr.getDataFactory();
    TableModel tableModel = dataFactory.queryData("parameter-data", new StaticDataRow());
    Assert.assertEquals(tableModel.getColumnCount(), 3);
    Assert.assertEquals(tableModel.getRowCount(), 2);
    Assert.assertNotNull(sr.getAttribute(AttributeNames.Wizard.NAMESPACE, "wizard-spec"));
    Assert.assertEquals(1, sr.getPreProcessorCount());
    Assert.assertEquals(WizardProcessor.class, sr.getPreProcessor(0).getClass());
  }

}
