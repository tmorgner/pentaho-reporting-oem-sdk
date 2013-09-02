package org.pentaho.reporting.sdk.element;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

import org.pentaho.reporting.engine.classic.core.DefaultImageReference;
import org.pentaho.reporting.engine.classic.core.ResourceBundleFactory;
import org.pentaho.reporting.engine.classic.core.imagemap.ImageMap;
import org.pentaho.reporting.engine.classic.core.style.StyleSheet;
import org.pentaho.reporting.engine.classic.core.util.ReportDrawable;
import org.pentaho.reporting.libraries.base.config.Configuration;
import org.pentaho.reporting.libraries.resourceloader.factory.drawable.DrawableWrapper;

public class SampleGraphicsDrawable implements ReportDrawable
{
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

  }

  public ImageMap getImageMap(final Rectangle2D bounds)
  {
    return null;
  }
}
