package org.pentaho.reporting.sdk.element;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.font.FontRenderContext;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.TextLayout;
import java.awt.geom.Rectangle2D;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.pentaho.reporting.engine.classic.core.DefaultImageReference;
import org.pentaho.reporting.engine.classic.core.ResourceBundleFactory;
import org.pentaho.reporting.engine.classic.core.imagemap.ImageMap;
import org.pentaho.reporting.engine.classic.core.style.ElementStyleKeys;
import org.pentaho.reporting.engine.classic.core.style.StyleSheet;
import org.pentaho.reporting.engine.classic.core.util.ReportDrawable;
import org.pentaho.reporting.libraries.base.config.Configuration;
import org.pentaho.reporting.libraries.base.util.StringUtils;
import org.pentaho.reporting.libraries.base.util.WaitingImageObserver;
import org.pentaho.reporting.libraries.resourceloader.factory.drawable.DrawableWrapper;

public class SampleGraphicsDrawable implements ReportDrawable
{
  private final Log logger = LogFactory.getLog(SampleGraphicsDrawable.class);

  private Configuration configuration;
  private StyleSheet styleSheet;
  private ResourceBundleFactory resourceBundleFactory;
  private String textToPrint;
  private DefaultImageReference rasterImageBackground;
  private DrawableWrapper vectorImageBackground;

  public SampleGraphicsDrawable(final String textToPrint)
  {
    this.textToPrint = textToPrint;
  }

  public SampleGraphicsDrawable(final String textToPrint,
                                final DrawableWrapper vectorImageBackground)
  {
    this(textToPrint);
    this.vectorImageBackground = vectorImageBackground;
  }

  public SampleGraphicsDrawable(final String textToPrint,
                                final DefaultImageReference rasterImageBackground)
  {
    this(textToPrint);
    this.rasterImageBackground = rasterImageBackground;
  }

  public DefaultImageReference getRasterImageBackground()
  {
    return rasterImageBackground;
  }

  public String getTextToPrint()
  {
    return textToPrint;
  }

  public DrawableWrapper getVectorImageBackground()
  {
    return vectorImageBackground;
  }

  public Configuration getConfiguration()
  {
    return configuration;
  }

  public void setConfiguration(final Configuration configuration)
  {
    this.configuration = configuration;
  }

  public ResourceBundleFactory getResourceBundleFactory()
  {
    return resourceBundleFactory;
  }

  public void setResourceBundleFactory(final ResourceBundleFactory resourceBundleFactory)
  {
    this.resourceBundleFactory = resourceBundleFactory;
  }

  public StyleSheet getStyleSheet()
  {
    return styleSheet;
  }

  public void setStyleSheet(final StyleSheet styleSheet)
  {
    this.styleSheet = styleSheet;
  }

  public void draw(final Graphics2D graphics2D, final Rectangle2D bounds)
  {
    Graphics2D g = (Graphics2D) graphics2D.create();
    g.setColor((Color) styleSheet.getStyleProperty(ElementStyleKeys.PAINT, Color.BLACK));
    if (styleSheet.getBooleanStyleProperty(ElementStyleKeys.DRAW_SHAPE))
    {
      g.draw(bounds);
    }

    if (styleSheet.getBooleanStyleProperty(ElementStyleKeys.FILL_SHAPE))
    {
      Graphics2D g2 = (Graphics2D) g.create();
      Color fillColor = (Color) styleSheet.getStyleProperty(ElementStyleKeys.FILL_COLOR, Color.WHITE);
      g2.setColor(fillColor);
      g2.fill(bounds);
      g2.dispose();
    }

    if (vectorImageBackground != null)
    {
      Graphics2D g2 = (Graphics2D) g.create();
      vectorImageBackground.draw(g2, bounds);
      g2.dispose();
    }
    if (rasterImageBackground != null)
    {
      Graphics2D g2 = (Graphics2D) g.create();

      Image image = rasterImageBackground.getImage();
      WaitingImageObserver obs = new WaitingImageObserver(image);
      obs.waitImageLoaded();

      g.setColor(Color.WHITE);
      g.setBackground(Color.WHITE);

      while (g2.drawImage(image,
          (int) bounds.getX(), (int) bounds.getY(),
          (int) bounds.getWidth(), (int) bounds.getHeight(), null) == false)
      {
        obs.waitImageLoaded();
        if (obs.isError())
        {
          logger.warn("Error while loading the image during the rendering.");
          break;
        }
      }
      g2.dispose();
    }

    if (StringUtils.isEmpty(textToPrint) == false)
    {
      AttributedCharacterIterator paragraph = new AttributedString(textToPrint).getIterator();
      int paragraphStart = paragraph.getBeginIndex();
      int paragraphEnd = paragraph.getEndIndex();
      FontRenderContext frc = g.getFontRenderContext();
      LineBreakMeasurer lineMeasurer = new LineBreakMeasurer(paragraph, frc);

      float breakWidth = (float) bounds.getWidth();
      float drawPosY = 0;
      // Set position to the index of the first character in the paragraph.
      lineMeasurer.setPosition(paragraphStart);

      while (lineMeasurer.getPosition() < paragraphEnd)
      {
        TextLayout layout = lineMeasurer.nextLayout(breakWidth).getJustifiedLayout(breakWidth);
        float drawPosX = layout.isLeftToRight() ? 0 : breakWidth - layout.getAdvance();

        drawPosY += layout.getAscent();

        layout.draw(g, drawPosX, drawPosY);

        drawPosY += layout.getDescent() + layout.getLeading();
      }
    }
  }

  public ImageMap getImageMap(final Rectangle2D bounds)
  {
    return null;
  }
}
