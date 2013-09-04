package org.pentaho.reporting.sdk.element.xml;

import java.io.IOException;
import java.util.Locale;

import org.pentaho.reporting.engine.classic.core.modules.parser.bundle.writer.styles.BundleStyleSetWriteHandler;
import org.pentaho.reporting.engine.classic.core.style.ElementStyleSheet;
import org.pentaho.reporting.libraries.formatting.FastDecimalFormat;
import org.pentaho.reporting.libraries.xmlns.common.AttributeList;
import org.pentaho.reporting.libraries.xmlns.writer.XmlWriter;
import org.pentaho.reporting.sdk.element.SampleElementModule;
import org.pentaho.reporting.sdk.element.SampleElementStyleKeys;

public class SampleElementStyleWriter implements BundleStyleSetWriteHandler
{
  public static final String TAG_NAME = "sample-element-style";

  private static FastDecimalFormat getAbsoluteLengthFormat()
  {
    return new FastDecimalFormat("0.###", Locale.US);
  }

  public SampleElementStyleWriter()
  {
  }

  public void writeStyle(final XmlWriter xmlWriter, final ElementStyleSheet elementStyleSheet) throws IOException
  {
    Object alphaValue = elementStyleSheet.getStyleProperty(SampleElementStyleKeys.IMAGE_BACKGROUND_ALPHA);
    if (alphaValue == null)
    {
      return;
    }

    AttributeList attrs = new AttributeList();
    attrs.setAttribute(SampleElementModule.NAMESPACE, SampleElementStyleKeys.IMAGE_BACKGROUND_ALPHA.getName(),
        getAbsoluteLengthFormat().format(alphaValue));

    xmlWriter.writeTag(SampleElementModule.NAMESPACE, TAG_NAME, attrs, XmlWriter.CLOSE);
  }
}
