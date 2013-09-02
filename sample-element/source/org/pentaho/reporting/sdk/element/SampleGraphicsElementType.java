package org.pentaho.reporting.sdk.element;

import java.text.MessageFormat;

import org.pentaho.reporting.engine.classic.core.DataRow;
import org.pentaho.reporting.engine.classic.core.DefaultImageReference;
import org.pentaho.reporting.engine.classic.core.ReportElement;
import org.pentaho.reporting.engine.classic.core.filter.types.ContentType;
import org.pentaho.reporting.engine.classic.core.filter.types.ElementTypeUtils;
import org.pentaho.reporting.engine.classic.core.function.ExpressionRuntime;
import org.pentaho.reporting.libraries.resourceloader.factory.drawable.DrawableWrapper;

public class SampleGraphicsElementType extends ContentType
{
  public SampleGraphicsElementType()
  {
    super("sdk-sample-graphics-element");
  }

  public Object getDesignValue(final ExpressionRuntime runtime, final ReportElement element)
  {
    final String message = ElementTypeUtils.getStringAttribute
        (element, SampleElementModule.NAMESPACE, "template-string", "Hello, {0}");
    final String textToPrint = MessageFormat.format(message, "Report-Designer!");

    final Object backgroundImageRaw = element.getAttribute(SampleElementModule.NAMESPACE, "background-image");
    final Object image = filter(runtime, element, backgroundImageRaw);
    if (image instanceof DefaultImageReference)
    {
      return new SampleGraphicsDrawable(textToPrint, (DefaultImageReference) image);
    }
    else if (image instanceof DrawableWrapper)
    {
      return new SampleGraphicsDrawable(textToPrint, (DrawableWrapper) image);
    }
    else
    {
      return new SampleGraphicsDrawable(textToPrint);
    }
  }

  public Object getValue(final ExpressionRuntime runtime, final ReportElement element)
  {
    // Elements can get their value from attributes directly, or can query a field and/or value via
    // the helper methods on the ElementTypeUtils class.
    final String message = ElementTypeUtils.getStringAttribute
        (element, SampleElementModule.NAMESPACE, "template-string", "Hello, {0}");

    // The data-row grants access to all currently computed values. Use this to access fields from
    // the data-source or calculated measures from the named expressions and functions.
    final DataRow dataRow = runtime.getDataRow();
    final Object userName = dataRow.get("env::user-name");
    final String textToPrint = MessageFormat.format(message, userName);

    final Object backgroundImageRaw = element.getAttribute(SampleElementModule.NAMESPACE, "background-image");
    final Object image = filter(runtime, element, backgroundImageRaw);
    if (image instanceof DefaultImageReference)
    {
      return new SampleGraphicsDrawable(textToPrint, (DefaultImageReference) image);
    }
    else if (image instanceof DrawableWrapper)
    {
      return new SampleGraphicsDrawable(textToPrint, (DrawableWrapper) image);
    }
    else
    {
      return new SampleGraphicsDrawable(textToPrint);
    }
  }
}
