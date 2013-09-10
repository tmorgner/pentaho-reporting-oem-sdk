package org.pentaho.reporting.sdk.expression;

import javax.swing.table.TableModel;

import org.pentaho.reporting.engine.classic.core.function.Expression;
import org.pentaho.reporting.engine.classic.core.util.TypedTableModel;

public class HelloWorldExpressionTest extends ExpressionTestBase
{
  public HelloWorldExpressionTest()
  {
  }

  protected Expression create(final String name, final String filter, final String group)
  {
    HelloWorldExpression helloWorldExpression = new HelloWorldExpression();
    helloWorldExpression.setName(name);
    helloWorldExpression.setField("name");
    return helloWorldExpression;
  }

  protected TableModel createTableModel()
  {
    final TypedTableModel model = new TypedTableModel();
    model.addColumn("name", String.class);
    configureStandardColumns(model);

    model.addRow(null, "R1", "r1", "C1", "c1", 1,
        "Hello World!", "Hello World!",  "Hello World!", "Hello World!", "Hello World!", "Hello World!");
    model.addRow("You", "R1", "r1", "C1", "c1", 1,
        "Hello World, You!", "Hello World, You!",  "Hello World, You!", "Hello World, You!", "Hello World, You!", "Hello World, You!");
    return model;
  }
}
