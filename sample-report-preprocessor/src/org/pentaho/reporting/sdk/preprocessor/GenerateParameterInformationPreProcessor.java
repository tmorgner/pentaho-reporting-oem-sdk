package org.pentaho.reporting.sdk.preprocessor;

import org.pentaho.reporting.engine.classic.core.AbstractReportPreProcessor;
import org.pentaho.reporting.engine.classic.core.AttributeNames;
import org.pentaho.reporting.engine.classic.core.MasterReport;
import org.pentaho.reporting.engine.classic.core.ReportProcessingException;
import org.pentaho.reporting.engine.classic.core.SubReport;
import org.pentaho.reporting.engine.classic.core.TableDataFactory;
import org.pentaho.reporting.engine.classic.core.function.FormulaExpression;
import org.pentaho.reporting.engine.classic.core.parameters.DefaultParameterContext;
import org.pentaho.reporting.engine.classic.core.parameters.ParameterAttributeNames;
import org.pentaho.reporting.engine.classic.core.parameters.ParameterContext;
import org.pentaho.reporting.engine.classic.core.parameters.ParameterDefinitionEntry;
import org.pentaho.reporting.engine.classic.core.parameters.ReportParameterDefinition;
import org.pentaho.reporting.engine.classic.core.states.datarow.DefaultFlowController;
import org.pentaho.reporting.engine.classic.core.style.TextStyleKeys;
import org.pentaho.reporting.engine.classic.core.util.TypedTableModel;
import org.pentaho.reporting.engine.classic.wizard.WizardProcessor;
import org.pentaho.reporting.engine.classic.wizard.model.DefaultDetailFieldDefinition;
import org.pentaho.reporting.engine.classic.wizard.model.DefaultWizardSpecification;
import org.pentaho.reporting.engine.classic.wizard.model.Length;
import org.pentaho.reporting.engine.classic.wizard.model.LengthUnit;
import org.pentaho.reporting.engine.classic.wizard.model.WizardSpecification;

public class GenerateParameterInformationPreProcessor extends AbstractReportPreProcessor
{
  public GenerateParameterInformationPreProcessor()
  {
  }

  /**
   * Adds an informative table of all parameters and their values to the report. If a report has no parameters
   * we dont need to add that table.
   *
   * This method demonstrates how to inject data via a sub-report into an existing report. It uses the
   * report-wizard for the bulk of the design-work.
   *
   * @param report
   * @param flowController
   * @return
   * @throws ReportProcessingException
   */
  public MasterReport performPreProcessing(final MasterReport report,
                                           final DefaultFlowController flowController) throws ReportProcessingException
  {
    if (report.getParameterDefinition().getParameterCount() == 0)
    {
      return report;
    }

    SubReport subReport = new SubReport();
    subReport.getDetailsHeader().getStyle().setStyleProperty(TextStyleKeys.BOLD, Boolean.TRUE);
    subReport.setDataFactory(new TableDataFactory("parameter-data", computeParameterData(report, flowController)));
    subReport.setQuery("parameter-data");
    subReport.addPreProcessor(new WizardProcessor());
    subReport.setAttribute(AttributeNames.Wizard.NAMESPACE, "wizard-spec", createReportSpec());
    subReport.setAttribute(AttributeNames.Wizard.NAMESPACE, AttributeNames.Wizard.ENABLE, Boolean.TRUE);
    subReport.addExpression(createFormula("formatted-name", "=IF(ISBLANK([label]); [name]; [value])"));
    subReport.addExpression(createFormula("formatted-value", "=CSVTEXT([value])"));

    report.getReportHeader().addSubReport(subReport);
    return report;
  }

  private FormulaExpression createFormula (String name, String formulaText)
  {
    FormulaExpression fe = new FormulaExpression();
    fe.setName(name);
    fe.setFormula(formulaText);
    return fe;
  }

  /**
   * Create a simple wizard specification to display the data. This produces the same result as
   * using the report-design-wizard in the Pentaho-Report-Designer.
   *
   * @return
   */
  private WizardSpecification createReportSpec()
  {
    DefaultDetailFieldDefinition[] fields = new DefaultDetailFieldDefinition[2];
    fields[0] = new DefaultDetailFieldDefinition("formatted-name");
    fields[0].setDisplayName("Parameter Name");
    fields[0].setFieldTypeHint(String.class);
    fields[0].setNullString("(not specified)");
    fields[0].setWidth(new Length(LengthUnit.PERCENTAGE, 30));
    fields[1] = new DefaultDetailFieldDefinition("formatted-value");
    fields[1].setDisplayName("Parameter Value");
    fields[1].setFieldTypeHint(String.class);
    fields[1].setNullString("(not specified)");
    fields[1].setWidth(new Length(LengthUnit.PERCENTAGE, 70));

    DefaultWizardSpecification wspec = new DefaultWizardSpecification();
    wspec.setDetailFieldDefinitions(fields);
    return wspec;
  }

  /**
   * Convert the parameters defined on the report into a table-model that can be consumed by the
   * sub-report.
   *
   * @param report
   * @param flowController
   * @return
   * @throws ReportProcessingException
   */
  private TypedTableModel computeParameterData(final MasterReport report,
                                               final DefaultFlowController flowController) throws ReportProcessingException
  {
    ReportParameterDefinition parameterDefinition = report.getParameterDefinition();
    ParameterContext pc = new DefaultParameterContext(report);
    TypedTableModel data = new TypedTableModel();
    data.addColumn("name", String.class);
    data.addColumn("label", String.class);
    data.addColumn("value", Object.class);
    for (ParameterDefinitionEntry p : parameterDefinition.getParameterDefinitions())
    {
      String label = p.getParameterAttribute
          (ParameterAttributeNames.Core.NAMESPACE, ParameterAttributeNames.Core.LABEL, pc);
      Object value = flowController.getMasterRow().getGlobalView().get(p.getName());
      data.addRow(p.getName(), label, value);
    }
    return data;
  }
}
