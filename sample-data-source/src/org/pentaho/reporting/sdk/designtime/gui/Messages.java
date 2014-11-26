/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014 Thomas Morgner
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package org.pentaho.reporting.sdk.designtime.gui;

import java.util.Locale;
import java.util.MissingResourceException;
import javax.swing.Icon;
import javax.swing.KeyStroke;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.pentaho.reporting.libraries.base.util.ObjectUtilities;
import org.pentaho.reporting.libraries.base.util.ResourceBundleSupport;

public class Messages
{
  private static final Log logger = LogFactory.getLog(Messages.class);
  private static ResourceBundleSupport bundle =
      new ResourceBundleSupport(Locale.getDefault(), "org.pentaho.reporting.sdk.designtime.gui.messages",
          ObjectUtilities.getClassLoader(Messages.class));

  private Messages()
  {
  }


  /**
   * Formats the message stored in the resource bundle (using a MessageFormat).
   *
   * @param key    the resourcebundle key
   * @param param1 the parameter for the message
   * @return the formated string
   */
  public static String getString(final String key, final Object... param1)
  {
    try
    {
      return bundle.formatMessage(key, param1);
    }
    catch (MissingResourceException e)
    {
      logger.warn("Missing localization: " + key, e);
      return '!' + key + '!';
    }
  }

  public static Icon getIcon(final String key, final boolean large)
  {
    return bundle.getIcon(key, large);
  }

  public static Icon getIcon(final String key)
  {
    return bundle.getIcon(key);
  }

  public static Integer getMnemonic(final String key)
  {
    return bundle.getMnemonic(key);
  }

  public static Integer getOptionalMnemonic(final String key)
  {
    return bundle.getOptionalMnemonic(key);
  }

  public static KeyStroke getKeyStroke(final String key)
  {
    return bundle.getKeyStroke(key);
  }

  public static KeyStroke getOptionalKeyStroke(final String key)
  {
    return bundle.getOptionalKeyStroke(key);
  }

  public static KeyStroke getKeyStroke(final String key, final int mask)
  {
    return bundle.getKeyStroke(key, mask);
  }

  public static KeyStroke getOptionalKeyStroke(final String key, final int mask)
  {
    return bundle.getOptionalKeyStroke(key, mask);
  }
}
