package org.pentaho.reporting.sdk.expression;

import javax.swing.table.TableModel;

import org.pentaho.reporting.engine.classic.core.function.Expression;
import org.pentaho.reporting.engine.classic.core.util.TypedTableModel;

public class CountIfFunctionTest extends ExpressionTestBase
{
  public CountIfFunctionTest()
  {
  }

  protected TableModel createTableModel()
  {
    final TypedTableModel model = new TypedTableModel();
    model.addColumn("filter", Object.class);
    configureStandardColumns(model);

    // column-order:
    // filter,
    // row-a, row-b, col-a, col-b, value,
    // relational, cell, row-a-agg, row-b-agg, col-a-agg, col-b-agg

    model.addRow(1, "R1", "r1", "C1", "c1",  1, 1, 1, 1, 1, 1, 1);
    model.addRow(0, "R1", "r1", "C1", "c2",  2, 0, 0, 1, 1, 1, 0);
    model.addRow(1, "R1", "r1", "C2", "c1",  3, 1, 1, 1, 1, 1, 1);
    model.addRow(0, "R1", "r1", "C2", "c2",  4, 0, 0, 1, 1, 1, 0);
    model.addRow(1, "R1", "r2", "C1", "c1",  5, 1, 1, 1, 2, 1, 2);
    model.addRow(0, "R1", "r2", "C1", "c2",  6, 0, 0, 1, 2, 1, 0);
    model.addRow(1, "R1", "r2", "C2", "c1",  7, 1, 1, 1, 2, 1, 2);
    model.addRow(0, "R1", "r2", "C2", "c2",  8, 0, 0, 1, 2, 1, 0);
    model.addRow(1, "R1", "r2", "C2", "c2",  8, 1, 1, 2, 3, 2, 1);
    model.addRow(0, "R1", "r2", "C2", "c2",  8, 1, 1, 2, 3, 2, 1); //

    model.addRow(1, "R2", "r1", "C1", "c1", 10, 1, 1, 1, 1, 1, 1);
    model.addRow(0, "R2", "r1", "C1", "c2", 11, 0, 0, 1, 1, 1, 0);
    model.addRow(1, "R2", "r1", "C2", "c1", 12, 1, 1, 1, 1, 1, 1);
    model.addRow(0, "R2", "r1", "C2", "c2", 13, 0, 0, 1, 1, 1, 0);
    model.addRow(1, "R2", "r2", "C1", "c1", 14, 1, 1, 1, 2, 1, 2);
    model.addRow(0, "R2", "r2", "C1", "c2", 15, 0, 0, 1, 2, 1, 0);
    model.addRow(1, "R2", "r2", "C2", "c1", 16, 1, 1, 1, 2, 1, 2);
    model.addRow(0, "R2", "r2", "C2", "c2", 17, 0, 0, 1, 2, 1, 0);
    return model;
  }

  protected Expression create(final String name,
                            final String filter,
                            final String group)
  {
    final CountIfFunction detailsSum = new CountIfFunction();
    detailsSum.setName(name);
    detailsSum.setFormula("=([filter] = 1)");
    detailsSum.setCrosstabFilterGroup(filter);
    detailsSum.setGroup(group);
    return detailsSum;
  }

  protected boolean isFailHardOnError()
  {
    return true;
  }
}


