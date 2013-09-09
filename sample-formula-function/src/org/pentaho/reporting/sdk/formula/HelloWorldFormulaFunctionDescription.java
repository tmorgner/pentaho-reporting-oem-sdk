package org.pentaho.reporting.sdk.formula;

import org.pentaho.reporting.libraries.formula.function.AbstractFunctionDescription;
import org.pentaho.reporting.libraries.formula.function.FunctionCategory;
import org.pentaho.reporting.libraries.formula.function.userdefined.UserDefinedFunctionCategory;
import org.pentaho.reporting.libraries.formula.typing.Type;
import org.pentaho.reporting.libraries.formula.typing.coretypes.TextType;

public class HelloWorldFormulaFunctionDescription extends AbstractFunctionDescription
{
  public HelloWorldFormulaFunctionDescription()
  {
    super("HELLOWORLD", "org.pentaho.reporting.sdk.formula.HelloWorldFunctionBundle");
  }

  public FunctionCategory getCategory()
  {
    return UserDefinedFunctionCategory.CATEGORY;
  }

  public Type getValueType()
  {
    return TextType.TYPE;
  }

  public int getParameterCount()
  {
    return 1;
  }

  public Type getParameterType(final int i)
  {
    return TextType.TYPE;
  }

  public boolean isParameterMandatory(final int i)
  {
    return false;
  }
}
