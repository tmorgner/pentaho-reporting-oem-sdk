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

import junit.framework.Assert;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.pentaho.reporting.engine.classic.core.event.ReportEvent;
import org.pentaho.reporting.engine.classic.core.function.AbstractFunction;
import org.pentaho.reporting.engine.classic.core.function.FunctionUtilities;

public class ValidateFunctionResultExpression extends AbstractFunction
{
  private static final Log logger = LogFactory.getLog(ValidateFunctionResultExpression.class);

  private boolean failHard;
  private int currentDataItem;
  private String crosstabFilterGroup;

  public ValidateFunctionResultExpression()
  {
  }

  public ValidateFunctionResultExpression(final String name,
                                          final boolean failHard,
                                          final String validateCrosstabFilter)
  {
    setName(name);
    this.crosstabFilterGroup = validateCrosstabFilter;
    this.failHard = failHard;
  }

  public String getCrosstabFilterGroup()
  {
    return crosstabFilterGroup;
  }

  public void setCrosstabFilterGroup(final String crosstabFilterGroup)
  {
    this.crosstabFilterGroup = crosstabFilterGroup;
  }

  public boolean isFailHard()
  {
    return failHard;
  }

  public void setFailHard(final boolean failHard)
  {
    this.failHard = failHard;
  }

  public Object getValue()
  {
    return null;
  }

  public void summaryRowSelection(final ReportEvent event)
  {
    if (FunctionUtilities.isDefinedGroup(getCrosstabFilterGroup(), event))
    {
      final String targetName = getName().substring(1);
      final Object expressionValue = getDataRow().get(targetName);
      final Object tableModelValue = getDataRow().get("validate-" + targetName);

      currentDataItem = event.getState().getCurrentDataItem();
      long sequenceCounter = event.getState().getCrosstabColumnSequenceCounter(3);

      if (!equalNumeric(expressionValue, tableModelValue))
      {
        logger.debug(String.format("!*%12s %3d:%d# %s - %s", targetName, currentDataItem, sequenceCounter, expressionValue, tableModelValue));
        if (failHard)
        {
          Assert.assertEquals(tableModelValue, expressionValue);
        }
      }
      else
      {
        logger.debug(String.format(" *%12s %3d:%d# %s - %s", targetName, currentDataItem, sequenceCounter, expressionValue, tableModelValue));
      }
    }
  }

  public void itemsAdvanced(final ReportEvent event)
  {
    final String targetName = getName().substring(1);
    final Object expressionValue = getDataRow().get(targetName);
    final Object tableModelValue = getDataRow().get("validate-" + targetName);

    currentDataItem = event.getState().getCurrentDataItem();
    long sequenceCounter = event.getState().getCrosstabColumnSequenceCounter(3);

    if (!equalNumeric(expressionValue, tableModelValue))
    {
      logger.debug(String.format("! %12s %3d:%d# %s - %s", targetName, currentDataItem, sequenceCounter, expressionValue, tableModelValue));
      if (failHard)
      {
        Assert.assertEquals(tableModelValue, expressionValue);
      }
    }
    else
    {
      logger.debug(String.format("  %12s %3d:%d# %s - %s", targetName, currentDataItem, sequenceCounter, expressionValue, tableModelValue));
    }
  }

  private boolean equalNumeric(final Object o1, final Object o2)
  {
    if (o1 instanceof Number == false)
    {
      return false;
    }
    if (o2 instanceof Number == false)
    {
      return false;
    }
    final Number n1 = (Number) o1;
    final Number n2 = (Number) o2;
    return Math.abs(n1.doubleValue() - n2.doubleValue()) < 0.0000005;
  }
}
