package org.pentaho.reporting.sdk.expression;

import org.pentaho.reporting.engine.classic.core.function.AbstractExpression;
import org.pentaho.reporting.engine.classic.core.function.Expression;
import org.pentaho.reporting.libraries.base.util.StringUtils;
import org.pentaho.reporting.libraries.formula.lvalues.TypeValuePair;
import org.pentaho.reporting.libraries.formula.typing.coretypes.TextType;

public class HelloWorldExpression extends AbstractExpression
{
  private String field;

  public HelloWorldExpression()
  {
  }

  public String getField()
  {
    return field;
  }

  public void setField(final String field)
  {
    this.field = field;
  }

  public Expression getInstance()
  {
    return super.getInstance();
  }

  private String computeGreetingName()
  {
    if (StringUtils.isEmpty(getField()))
    {
      return null;
    }

    Object raw = getDataRow().get(getField());
    if (raw == null)
    {
      return null;
    }

    String text = String.valueOf(raw);
    if (StringUtils.isEmpty(text))
    {
      return null;
    }

    return text;
  }

  public Object getValue()
  {
    String text = computeGreetingName();
    if (text == null)
    {
      return new TypeValuePair(TextType.TYPE, String.format("Hello World!", text));
    }

    return new TypeValuePair(TextType.TYPE, String.format("Hello World, %s!", text));
  }
}
