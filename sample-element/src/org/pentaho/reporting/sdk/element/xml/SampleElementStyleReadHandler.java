package org.pentaho.reporting.sdk.element.xml;

import org.pentaho.reporting.engine.classic.core.modules.parser.bundle.layout.StyleReadHandler;
import org.pentaho.reporting.engine.classic.core.style.ElementStyleSheet;
import org.pentaho.reporting.libraries.xmlns.common.ParserUtil;
import org.pentaho.reporting.libraries.xmlns.parser.AbstractXmlReadHandler;
import org.pentaho.reporting.sdk.element.SampleElementStyleKeys;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class SampleElementStyleReadHandler extends AbstractXmlReadHandler implements StyleReadHandler
{
  private ElementStyleSheet styleSheet;

  public SampleElementStyleReadHandler()
  {
  }

  public void setStyleSheet(final ElementStyleSheet styleSheet)
  {
    this.styleSheet = styleSheet;
  }

  public ElementStyleSheet getStyleSheet()
  {
    return styleSheet;
  }

  public ElementStyleSheet getObject() throws SAXException
  {
    return getStyleSheet();
  }

  /**
   * Starts parsing.
   *
   * @param attrs the attributes.
   * @throws SAXException if there is a parsing error.
   */
  protected void startParsing(final Attributes attrs) throws SAXException
  {
    final String alphaText = attrs.getValue(getUri(), SampleElementStyleKeys.IMAGE_BACKGROUND_ALPHA.name);
    if (alphaText != null)
    {
      float alpha = ParserUtil.parseFloat(alphaText,
          "Attribute '" + SampleElementStyleKeys.IMAGE_BACKGROUND_ALPHA + "' is not a valid float", getLocator());
      styleSheet.setStyleProperty(SampleElementStyleKeys.IMAGE_BACKGROUND_ALPHA, alpha);
    }

  }
}
