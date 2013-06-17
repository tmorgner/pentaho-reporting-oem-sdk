package org.pentaho.reporting.sdk.element;

import java.text.MessageFormat;

import org.pentaho.reporting.engine.classic.core.DataRow;
import org.pentaho.reporting.engine.classic.core.ReportElement;
import org.pentaho.reporting.engine.classic.core.filter.types.AbstractElementType;
import org.pentaho.reporting.engine.classic.core.filter.types.ElementTypeUtils;
import org.pentaho.reporting.engine.classic.core.function.ExpressionRuntime;

public class SampleElementType extends AbstractElementType
{
  public SampleElementType()
  {
    super("sdk-sample-element");
  }

  public Object getDesignValue(final ExpressionRuntime runtime, final ReportElement element)
  {
//    final String message = ElementTypeUtils.getStringAttribute
//        (element, SampleElementModule.NAMESPACE, "template-string", "Hello, {0}");
//    return MessageFormat.format(message, "Report-Designer!");
    return null;
  }

  public Object getValue(final ExpressionRuntime runtime, final ReportElement element)
  {
    // Elements can get their value from attributes directly, or can query a field and/or value via
    // the helper methods on the ElementTypeUtils class.
//    final String message = ElementTypeUtils.getStringAttribute
//        (element, SampleElementModule.NAMESPACE, "template-string", "Hello, {0}");

    // The data-row grants access to all currently computed values. Use this to access fields from
    // the data-source or calculated measures from the named expressions and functions.
    final DataRow dataRow = runtime.getDataRow();
    final Object userName = dataRow.get("env::user-name");
return null;
//    return MessageFormat.format(message, userName);
  }
}
