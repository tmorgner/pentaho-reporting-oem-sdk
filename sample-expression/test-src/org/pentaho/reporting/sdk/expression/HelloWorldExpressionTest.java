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
