package org.pentaho.reporting.sdk.formula;

import org.pentaho.reporting.libraries.formula.EvaluationException;
import org.pentaho.reporting.libraries.formula.FormulaContext;
import org.pentaho.reporting.libraries.formula.function.Function;
import org.pentaho.reporting.libraries.formula.function.ParameterCallback;
import org.pentaho.reporting.libraries.formula.lvalues.TypeValuePair;
import org.pentaho.reporting.libraries.formula.typing.Type;
import org.pentaho.reporting.libraries.formula.typing.coretypes.TextType;

public class HelloWorldFormulaFunction implements Function
{
  public HelloWorldFormulaFunction()
  {
  }

  public TypeValuePair evaluate(final FormulaContext formulaContext,
                                final ParameterCallback parameterCallback) throws EvaluationException
  {
    if (parameterCallback.getParameterCount() > 0)
    {
      Type type = parameterCallback.getType(0);
      Object raw = parameterCallback.getValue(0);
      String text = formulaContext.getTypeRegistry().convertToText(type, raw);
      return new TypeValuePair(TextType.TYPE, String.format("Hello World, %s!", text));
    }
    else
    {
      return new TypeValuePair(TextType.TYPE, "Hello World!");
    }
  }

  public String getCanonicalName()
  {
    return "HELLOWORLD";
  }
}
