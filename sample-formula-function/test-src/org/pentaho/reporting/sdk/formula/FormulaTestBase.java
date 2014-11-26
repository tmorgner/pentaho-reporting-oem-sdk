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

package org.pentaho.reporting.sdk.formula;

import java.util.Locale;

import junit.framework.TestCase;
import org.pentaho.reporting.libraries.base.util.ObjectUtilities;
import org.pentaho.reporting.libraries.base.util.StringUtils;
import org.pentaho.reporting.libraries.formula.Formula;
import org.pentaho.reporting.libraries.formula.FormulaContext;
import org.pentaho.reporting.libraries.formula.LibFormulaBoot;
import org.pentaho.reporting.libraries.formula.function.FunctionDescription;

public abstract class FormulaTestBase extends TestCase
{
  private FormulaContext context;

  protected FormulaTestBase()
  {
  }

  protected FormulaTestBase(final String s)
  {
    super(s);
  }

  protected void setUp() throws Exception
  {
    context = new TestFormulaContext(TestFormulaContext.testCaseDataset);
    LibFormulaBoot.getInstance().start();
  }

  protected abstract Object[][] createDataTest();

  public FormulaContext getContext()
  {
    return context;
  }

  protected void runDefaultTest() throws Exception
  {
    final Object[][] dataTest = createDataTest();
    runTest(dataTest);
  }

  protected void runTest(final Object[][] dataTest) throws Exception
  {
    for (int i = 0; i < dataTest.length; i++)
    {
      final Object[] objects = dataTest[i];
      performTest((String) objects[0], objects[1]);
    }
  }

  protected void performTest(final String formul, final Object result) throws Exception
  {
    performTest(formul, result, this.context);
  }

  protected void performTest(final String formul, final Object result, final FormulaContext context) throws Exception
  {
    final Formula formula = new Formula(formul);
    formula.initialize(context);
    final Object eval = formula.evaluateTyped().getValue();
    if (result instanceof Comparable && eval instanceof Comparable)
    {
      final Comparable n = (Comparable) result;
      try
      {
        assertTrue("Failure numeric comparison on " + formul + ": " + result + " vs. " + eval, n.compareTo(eval) == 0);
      }
      catch (final ClassCastException cce)
      {
        cce.printStackTrace();
        fail("Failure numeric comparison on " + formul + ": " + result + " vs. " + eval);
      }
    }
    else if (result instanceof Object[] && eval instanceof Object[])
    {
      final boolean b = ObjectUtilities.equalArray((Object[]) result, (Object[]) eval);
      if (b == false)
      {
        System.out.println(printArray(result));
        System.out.println(printArray(eval));
        fail("Failure on array comparison: " + formul);
      }
    }
    else
    {
      assertEquals("Failure on " + formul, result, eval);
    }
  }

  private String printArray(final Object o)
  {
    if (o instanceof Object[] == false)
    {
      return String.valueOf(o);
    }

    StringBuilder b = new StringBuilder();
    b.append("Object[]{");
    final Object[] array = (Object[]) o;
    for (int i = 0; i < array.length; i++)
    {
      if (i > 0)
      {
        b.append(", ");
      }
      b.append(array[i]);
    }
    b.append("}");
    return b.toString();
  }

  protected void performTranslationTest(String function)
  {
    FunctionDescription functionDesc = context.getFunctionRegistry().getMetaData(function);
    assertFalse(StringUtils.isEmpty(functionDesc.getDisplayName(Locale.ENGLISH)));
    assertFalse(StringUtils.isEmpty(functionDesc.getDescription(Locale.ENGLISH)));
    int count = functionDesc.getParameterCount();
    for (int x = 0; x < count; x++)
    {
      assertFalse(StringUtils.isEmpty(functionDesc.getParameterDescription(x, Locale.ENGLISH)));
      assertFalse(StringUtils.isEmpty(functionDesc.getParameterDisplayName(x, Locale.ENGLISH)));
    }
  }
}
