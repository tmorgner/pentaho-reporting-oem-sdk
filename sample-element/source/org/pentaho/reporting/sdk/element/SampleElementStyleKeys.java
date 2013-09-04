package org.pentaho.reporting.sdk.element;

import org.pentaho.reporting.engine.classic.core.style.StyleKey;

public class SampleElementStyleKeys
{
  /**
   * A style-key definition for the drawable's background alpha setting. This key accepts a float
   * between zero and one. It is persistet (transient flag = false) and inheritable (inheritable flag
   * is true).
   *
   * Style values can always be <code>null</code> at some point, so your code should be prepared to handle
   * this case.
   */
  public static final StyleKey IMAGE_BACKGROUND_ALPHA =
      StyleKey.getStyleKey("sdk-image-background-alpha", Float.class, false, true);

  private SampleElementStyleKeys()
  {
  }
}
