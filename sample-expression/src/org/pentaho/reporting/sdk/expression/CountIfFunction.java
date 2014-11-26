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

import java.math.BigDecimal;

import org.pentaho.reporting.engine.classic.core.event.ReportEvent;
import org.pentaho.reporting.engine.classic.core.function.AbstractFunction;
import org.pentaho.reporting.engine.classic.core.function.AggregationFunction;
import org.pentaho.reporting.engine.classic.core.function.Expression;
import org.pentaho.reporting.engine.classic.core.function.ExpressionRuntime;
import org.pentaho.reporting.engine.classic.core.function.FormulaExpression;
import org.pentaho.reporting.engine.classic.core.function.FunctionUtilities;
import org.pentaho.reporting.engine.classic.core.util.Sequence;

public class CountIfFunction extends AbstractFunction implements AggregationFunction
{
  public static final BigDecimal ONE = new BigDecimal(1);
  public static final BigDecimal ZERO = new BigDecimal(0);
  /**
   * The item count.
   */
  private Sequence<BigDecimal> count;
  private transient int lastGroupSequenceNumber;

  /**
   * The name of the group on which to reset the count. This can be set to null to compute the count for the whole
   * report.
   */
  private String group;
  private String crosstabFilterGroup;
  private FormulaExpression formulaExpression;

  public CountIfFunction()
  {
    formulaExpression = new FormulaExpression();
    count = new Sequence<BigDecimal>();
  }

  public String getFormula()
  {
    return formulaExpression.getFormula();
  }

  public void setFormula(final String formula)
  {
    formulaExpression.setFormula(formula);
  }

  public String getCrosstabFilterGroup()
  {
    return crosstabFilterGroup;
  }

  public void setCrosstabFilterGroup(final String crosstabFilterGroup)
  {
    this.crosstabFilterGroup = crosstabFilterGroup;
  }

  /**
   * Returns the name of the group (possibly null) for this function.  The item count is reset to zero at the start of
   * each instance of this group.
   *
   * @return the group name.
   */
  public String getGroup()
  {
    return group;
  }

  /**
   * Setss the name of the group for this function.  The item count is reset to zero at the start of each instance of
   * this group.  If the name is null, all items in the report are counted.
   *
   * @param group The group name.
   */
  public void setGroup(final String group)
  {
    this.group = group;
  }

  public void setRuntime(final ExpressionRuntime runtime)
  {
    super.setRuntime(runtime);
    formulaExpression.setRuntime(runtime);
  }

  public Object clone() throws CloneNotSupportedException
  {
    CountIfFunction fn = (CountIfFunction) super.clone();
    fn.formulaExpression = (FormulaExpression) formulaExpression.clone();
    fn.count = count.clone();
    return fn;
  }

  public Expression getInstance()
  {
    CountIfFunction fn = (CountIfFunction) super.getInstance();
    fn.formulaExpression = (FormulaExpression) formulaExpression.getInstance();
    fn.count = count.clone();
    fn.lastGroupSequenceNumber = 0;
    return fn;
  }

  protected void clear()
  {
    this.lastGroupSequenceNumber = 0;
    this.count.clear();
  }

  /**
   * Receives notification that a new report is about to start.  The item count is set to zero.
   *
   * @param event the event.
   */
  public void reportInitialized(final ReportEvent event)
  {
    clear();
  }

  /**
   * Receives notification that a new group is about to start.  Checks to see if the group that is starting is the same
   * as the group defined for this function...if so, the item count is reset to zero.
   *
   * @param event Information about the event.
   */
  public void groupStarted(final ReportEvent event)
  {
    if (FunctionUtilities.isDefinedGroup(getGroup(), event))
    {
      clear();
    }

    if (FunctionUtilities.isDefinedGroup(getCrosstabFilterGroup(), event))
    {
      final int groupIndex = event.getState().getCurrentGroupIndex();
      this.lastGroupSequenceNumber = (int) event.getState().getCrosstabColumnSequenceCounter(groupIndex);
    }
  }

  /**
   * Received notification of a move to the next row of data.  Increments the item count.
   *
   * @param event Information about the event.
   */
  public void itemsAdvanced(final ReportEvent event)
  {
    Object value = formulaExpression.getValue();
    if (Boolean.TRUE.equals(value) == false)
    {
      return;
    }

    final BigDecimal oldValue = count.get(lastGroupSequenceNumber);
    if (oldValue == null)
    {
      count.set(lastGroupSequenceNumber, ONE);
    }
    else
    {
      count.set(lastGroupSequenceNumber, oldValue.add(ONE));
    }
  }

  public void summaryRowSelection(final ReportEvent event)
  {
    if (FunctionUtilities.isDefinedGroup(getCrosstabFilterGroup(), event))
    {
      final int groupIndex = event.getState().getCurrentGroupIndex();
      this.lastGroupSequenceNumber = (int) event.getState().getCrosstabColumnSequenceCounter(groupIndex);
    }
  }

  /**
   * Returns the number of items counted (so far) by the function.  This is either the number of items in the report, or
   * the group (if a group has been defined for the function).
   *
   * @return The item count.
   */
  public Object getValue()
  {
    final BigDecimal value = count.get(lastGroupSequenceNumber);
    if (value == null)
    {
      return ZERO;
    }
    return value;
  }

}
