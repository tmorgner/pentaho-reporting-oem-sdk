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

package org.pentaho.reporting.engine.sdk.drilldown;

import org.junit.Assert;
import org.pentaho.reporting.engine.classic.core.ClassicEngineBoot;
import org.pentaho.reporting.engine.classic.core.StaticDataRow;
import org.pentaho.reporting.engine.classic.core.function.ExpressionRuntime;
import org.pentaho.reporting.engine.classic.core.function.GenericExpressionRuntime;
import org.pentaho.reporting.engine.classic.core.function.ReportFormulaContext;
import org.pentaho.reporting.engine.classic.core.layout.output.DefaultProcessingContext;
import org.pentaho.reporting.engine.classic.extensions.drilldown.DrillDownProfileMetaData;
import org.pentaho.reporting.libraries.formula.FormulaContext;
import org.pentaho.reporting.libraries.formula.FormulaTestBase;
import org.pentaho.reporting.libraries.formula.common.TestFormulaContext;

import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Thomas Morgner.
 */
public class PentahoEditableDrillDownFunctionTest extends FormulaTestBase
{
  private ReportFormulaContext reportFormulaContext;

  public PentahoEditableDrillDownFunctionTest()
  {
  }

  @Override
  protected void setUp() throws Exception {
    super.setUp();
    ClassicEngineBoot.getInstance().start();

    final ExpressionRuntime runtime = new GenericExpressionRuntime
        (new StaticDataRow(), new DefaultTableModel(), 0, new DefaultProcessingContext());
    reportFormulaContext = new ReportFormulaContext(new TestFormulaContext(TestFormulaContext.testCaseDataset), runtime);
  }

  @Override
  public FormulaContext getContext() {
    return reportFormulaContext;
  }

  @Override
  protected void performTest(String formul, Object result) throws Exception {
    super.performTest(formul, result, getContext());
  }

  protected Object[][] createDataTest()
  {
    return new Object[][]{
        {"DRILLDOWN(\"local-sugar-analyzer-edit\"; 0; {\"::pentaho-path\" ; \"/public/steel-wheels/test.prpt\" | \"test\" ; \"value\" | \"mtest\" ; {\"v1\"; \"v2\"; \"v3\" }})",
            "http://localhost:8080/pentaho/api/repos/:public:steel-wheels:test.prpt/edit?test=value&mtest=v1&mtest=v2&mtest=v3"},
        {"DRILLDOWN(\"remote-sugar-analyzer-edit\"; \"http://domain.example/\"; {\"::pentaho-path\" ; \"/public/steel-wheels/test.prpt\" | \"test\" ; \"value\" | \"mtest\" ; {\"v1\"; \"v2\"; \"v3\" }})",
            "http://domain.example/api/repos/:public:steel-wheels:test.prpt/edit?test=value&mtest=v1&mtest=v2&mtest=v3"},
    };

  }

  public void testProfilesExist()
  {
    Assert.assertNotNull(DrillDownProfileMetaData.getInstance().getDrillDownProfile("local-sugar-analyzer-edit"));
    Assert.assertNotNull(DrillDownProfileMetaData.getInstance().getDrillDownProfile("remote-sugar-analyzer-edit"));
  }

  public void testDefault() throws Exception
  {
    runDefaultTest();
  }

}
