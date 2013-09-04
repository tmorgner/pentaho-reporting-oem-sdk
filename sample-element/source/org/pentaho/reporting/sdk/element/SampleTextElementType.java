package org.pentaho.reporting.sdk.element;

import java.text.MessageFormat;

import org.pentaho.reporting.engine.classic.core.DataRow;
import org.pentaho.reporting.engine.classic.core.ReportElement;
import org.pentaho.reporting.engine.classic.core.filter.types.AbstractElementType;
import org.pentaho.reporting.engine.classic.core.filter.types.ElementTypeUtils;
import org.pentaho.reporting.engine.classic.core.function.ExpressionRuntime;
import org.pentaho.reporting.engine.classic.core.metadata.ElementType;

public class SampleTextElementType extends AbstractElementType
{
  public static final ElementType INSTANCE = new SampleTextElementType();
  public static final String TEMPLATE_STRING = "template-string";
  public static final String ELEMENT_TYPE_NAME = "sdk-sample-text-element";

  public SampleTextElementType()
  {
    super(ELEMENT_TYPE_NAME);
  }

  public Object getDesignValue(final ExpressionRuntime runtime, final ReportElement element)
  {
    final String message = ElementTypeUtils.getStringAttribute
        (element, SampleElementModule.NAMESPACE, TEMPLATE_STRING, "Hello, {0}");
    return MessageFormat.format(message, "Report-Designer!");
  }

  public Object getValue(final ExpressionRuntime runtime, final ReportElement element)
  {
    // Elements can get their value from attributes directly, or can query a field and/or value via
    // the helper methods on the ElementTypeUtils class.
    final String message = ElementTypeUtils.getStringAttribute
        (element, SampleElementModule.NAMESPACE, TEMPLATE_STRING, "Hello, {0}");

    // The data-row grants access to all currently computed values. Use this to access fields from
    // the data-source or calculated measures from the named expressions and functions.
    final DataRow dataRow = runtime.getDataRow();
    final Object userName = dataRow.get("env::username");
    return MessageFormat.format(message, userName);
  }

  @SuppressWarnings("CloneDoesntCallSuperClone")
  public ElementType clone()
  {
    // as our element implementation is stateless, this is sane.
    return this;
  }
}
