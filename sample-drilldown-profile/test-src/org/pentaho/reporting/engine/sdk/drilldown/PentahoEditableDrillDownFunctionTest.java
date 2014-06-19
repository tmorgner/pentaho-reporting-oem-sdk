/*!
* This program is free software; you can redistribute it and/or modify it under the
* terms of the GNU Lesser General Public License, version 2.1 as published by the Free Software
* Foundation.
*
* You should have received a copy of the GNU Lesser General Public License along with this
* program; if not, you can obtain a copy at http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
* or from the Free Software Foundation, Inc.,
* 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
*
* This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
* without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
* See the GNU Lesser General Public License for more details.
*
* Copyright (c) 2002-2013 Pentaho Corporation..  All rights reserved.
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
