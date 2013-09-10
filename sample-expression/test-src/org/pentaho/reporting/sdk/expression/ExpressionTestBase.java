package org.pentaho.reporting.sdk.expression;

import java.awt.Color;
import java.awt.Insets;
import java.awt.print.PageFormat;
import javax.swing.table.TableModel;

import org.junit.Before;
import org.junit.Test;
import org.pentaho.reporting.engine.classic.core.ClassicEngineBoot;
import org.pentaho.reporting.engine.classic.core.MasterReport;
import org.pentaho.reporting.engine.classic.core.SimplePageDefinition;
import org.pentaho.reporting.engine.classic.core.TableDataFactory;
import org.pentaho.reporting.engine.classic.core.designtime.DesignTimeDataSchemaModel;
import org.pentaho.reporting.engine.classic.core.elementfactory.CrosstabBuilder;
import org.pentaho.reporting.engine.classic.core.function.Expression;
import org.pentaho.reporting.engine.classic.core.modules.gui.base.PreviewDialog;
import org.pentaho.reporting.engine.classic.core.testsupport.DebugReportRunner;
import org.pentaho.reporting.engine.classic.core.testsupport.RelationalReportBuilder;
import org.pentaho.reporting.engine.classic.core.util.PageSize;
import org.pentaho.reporting.engine.classic.core.util.TypedTableModel;
import org.pentaho.reporting.libraries.designtime.swing.LibSwingUtil;

public abstract class ExpressionTestBase
{
  public static final String ROW_DIMENSION_A = "Row-Dimension-A";
  public static final String ROW_DIMENSION_B = "Row-Dimension-B";
  public static final String COLUMN_DIMENSION_A = "Column-Dimension-A";
  public static final String COLUMN_DIMENSION_B = "Column-Dimension-B";
  public static final String VALUE = "Value";

  public ExpressionTestBase()
  {
  }

  @Before
  public void setUp() throws Exception
  {
    ClassicEngineBoot.getInstance().start();
  }

  @Test
  public void testMetaData() throws Exception
  {
    ExpressionTestHelper.validateElementMetaData(HelloWorldExpression.class);
  }

  protected abstract TableModel createTableModel();


  protected void configureStandardColumns(final TypedTableModel model)
  {
    model.addColumn(ROW_DIMENSION_A, String.class);
    model.addColumn(ROW_DIMENSION_B, String.class);
    model.addColumn(COLUMN_DIMENSION_A, String.class);
    model.addColumn(COLUMN_DIMENSION_B, String.class);
    model.addColumn(VALUE, Object.class);
    model.addColumn("validate-relational", Object.class);
    model.addColumn("validate-cell", Object.class);
    model.addColumn("validate-row-b", Object.class);
    model.addColumn("validate-row-a", Object.class);
    model.addColumn("validate-column-a", Object.class);
    model.addColumn("validate-column-b", Object.class);
  }

  @Test
  public void testCrosstabReport() throws Exception
  {
    final MasterReport report = configureReport(createCrosstabReport(createTableModel()), false);

    DebugReportRunner.execGraphics2D(report);
  }

  @Test
  public void testRelationalReport() throws Exception
  {
    final MasterReport report = configureReport(createRelationalReport(createTableModel()), true);

    DebugReportRunner.execGraphics2D(report);
  }

  protected abstract Expression create(final String name,
                                       final String filter,
                                       final String group);

  protected boolean isFailHardOnError()
  {
    return false;
  }

  private MasterReport configureReport(MasterReport report, boolean relational)
  {
    report.addExpression(create("relational", null, COLUMN_DIMENSION_B));
    report.addExpression(new ValidateFunctionResultExpression("#relational", isFailHardOnError(), null));

    if (relational)
    {
      return report;
    }

    report.addExpression(create("cell", COLUMN_DIMENSION_B, ROW_DIMENSION_B));
    report.addExpression(new ValidateFunctionResultExpression("#cell", isFailHardOnError(), COLUMN_DIMENSION_B));

    report.addExpression(create("row-b", COLUMN_DIMENSION_A, ROW_DIMENSION_B));
    report.addExpression(new ValidateFunctionResultExpression("#row-b", isFailHardOnError(), COLUMN_DIMENSION_A));

    report.addExpression(create("row-a", COLUMN_DIMENSION_A, ROW_DIMENSION_A));
    report.addExpression(new ValidateFunctionResultExpression("#row-a", isFailHardOnError(), COLUMN_DIMENSION_A));

    report.addExpression(create("column-a", null, COLUMN_DIMENSION_A));
    report.addExpression(new ValidateFunctionResultExpression("#column-a", isFailHardOnError(), null));

    report.addExpression(create("column-b", COLUMN_DIMENSION_B, ROW_DIMENSION_A));
    report.addExpression(new ValidateFunctionResultExpression("#column-b", isFailHardOnError(), COLUMN_DIMENSION_B));

    return report;
  }

  protected MasterReport createCrosstabReport(final TableModel tableModel)
  {
    final MasterReport report = new MasterReport();
    report.setPageDefinition(new SimplePageDefinition(PageSize.A3, PageFormat.LANDSCAPE, new Insets(0, 0, 0, 0)));
    report.setDataFactory(new TableDataFactory("query", tableModel));
    report.setQuery("query");
    final DesignTimeDataSchemaModel dataSchemaModel = new DesignTimeDataSchemaModel(report);

    Expression dummy = create("dummy", null, null);

    final CrosstabBuilder builder = new CrosstabBuilder(dataSchemaModel);
    builder.addRowDimension(ROW_DIMENSION_A);
    builder.addRowDimension(ROW_DIMENSION_B);
    builder.addColumnDimension(COLUMN_DIMENSION_A);
    builder.addColumnDimension(COLUMN_DIMENSION_B);
    builder.addDetails(VALUE, dummy.getClass());
    report.setRootGroup(builder.create());
    return report;
  }

  protected MasterReport createRelationalReport(final TableModel tableModel,
                                                final String... additionalFields)
  {
    final MasterReport report = new MasterReport();
    report.setPageDefinition(new SimplePageDefinition(PageSize.A3, PageFormat.LANDSCAPE, new Insets(0, 0, 0, 0)));
    report.setDataFactory(new TableDataFactory("query", tableModel));
    report.setQuery("query");
    final DesignTimeDataSchemaModel dataSchemaModel = new DesignTimeDataSchemaModel(report);

    final RelationalReportBuilder builder = new RelationalReportBuilder(dataSchemaModel);
    builder.addGroup(ROW_DIMENSION_A);
    builder.addGroup(ROW_DIMENSION_B);
    builder.addGroup(COLUMN_DIMENSION_A);
    builder.addGroup(COLUMN_DIMENSION_B);
    builder.addDetails(VALUE, null, Color.lightGray);
    builder.addDetails("relational", null, Color.yellow);
    for (int i = 0; i < additionalFields.length; i++)
    {
      String additionalField = additionalFields[i];
      builder.addDetails(additionalField, null, null);
    }

    report.setRootGroup(builder.create());
    return report;
  }

  protected void showRelationalDialog()
  {
    PreviewDialog dialog = new PreviewDialog(configureReport(createRelationalReport(createTableModel()), true));
    dialog.setModal(true);
    dialog.pack();
    LibSwingUtil.centerFrameOnScreen(dialog);
    dialog.setVisible(true);
  }

  protected void showRelationalGeneratorDialog()
  {
    MasterReport relationalReport = createRelationalReport(createTableModel(),
        "cell", "row-b", "row-a", "column-a", "column-b");
    PreviewDialog dialog = new PreviewDialog(configureReport(relationalReport, false));
    dialog.setModal(true);
    dialog.pack();
    LibSwingUtil.centerFrameOnScreen(dialog);
    dialog.setVisible(true);
  }


  protected void showCrosstabDialog()
  {
    PreviewDialog dialog = new PreviewDialog(configureReport(createCrosstabReport(createTableModel()), false));
    dialog.setModal(true);
    dialog.pack();
    LibSwingUtil.centerFrameOnScreen(dialog);
    dialog.setVisible(true);
  }
}
