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

package org.pentaho.reporting.sdk.expression;

import java.util.ArrayList;
import java.util.Locale;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.pentaho.reporting.engine.classic.core.function.Expression;
import org.pentaho.reporting.engine.classic.core.metadata.ExpressionMetaData;
import org.pentaho.reporting.engine.classic.core.metadata.ExpressionPropertyMetaData;
import org.pentaho.reporting.engine.classic.core.metadata.ExpressionRegistry;

public class ExpressionTestHelper
{
  private static final Log logger = LogFactory.getLog(ExpressionTestHelper.class);

  private ExpressionTestHelper()
  {
  }

  public static boolean validateElementMetaData(Class<? extends Expression> elementType)
  {
    ExpressionMetaData metaData = ExpressionRegistry.getInstance().getExpressionMetaData(elementType.getName());

    if (metaData == null)
    {
      logger.warn("No Metadata defined");
      return false;
    }

    if (validateCanInstantiate(metaData))
    {
      return false;
    }

    final String typeName = metaData.getName();
    logger.debug("Processing " + typeName);

    ArrayList<String> missingProperties = new ArrayList<String>();
    validateCoreMetaData(metaData, missingProperties);
    validatePropertyMetaData(metaData, missingProperties);

    flushSystemErr();

    for (String property : missingProperties)
    {
      System.out.println(property);
    }

    return missingProperties.isEmpty();
  }

  private static void flushSystemErr()
  {
    System.err.flush();
    try
    {
      Thread.sleep(25);
    }
    catch (InterruptedException e)
    {
      // wait for system.error to print. it makes the logging cleaner
    }
  }

  private static boolean validateCanInstantiate(final ExpressionMetaData metaData)
  {
    final Object type = metaData.create();
    if (type == null)
    {
      return true;
    }
    return false;
  }

  private static void validateCoreMetaData(final ExpressionMetaData metaData,
                                           final ArrayList<String> missingProperties)
  {
    final Locale locale = Locale.getDefault();
    final String typeName = metaData.getName();

    final String displayName = metaData.getDisplayName(locale);
    if (isValid(displayName, metaData.getName(), missingProperties) == false)
    {
      logger.warn("ExpressionMetaData '" + typeName + ": No valid display name");
    }
    if (metaData.isDeprecated())
    {
      final String deprecateMessage = metaData.getDeprecationMessage(locale);
      if (isValid(deprecateMessage, "Property", missingProperties) == false)
      {
        logger.warn("ExpressionMetaData '" + typeName + ": No valid deprecate message");
      }
    }
    final String grouping = metaData.getGrouping(locale);
    if (isValid(grouping, "User-Defined", missingProperties) == false)
    {
      logger.warn("ExpressionMetaData '" + typeName + ": No valid grouping message");
    }
  }

  private static void validatePropertyMetaData(final ExpressionMetaData metaData,
                                               final ArrayList<String> missingProperties)
  {
    final Locale locale = Locale.getDefault();
    final String typeName = metaData.getName();

    final ExpressionPropertyMetaData[] styleMetaDatas = metaData.getPropertyDescriptions();
    for (int j = 0; j < styleMetaDatas.length; j++)
    {
      final ExpressionPropertyMetaData propertyMetaData = styleMetaDatas[j];
      final String propertyDisplayName = propertyMetaData.getDisplayName(locale);
      if (isValid(propertyDisplayName, propertyMetaData.getName(), missingProperties) == false)
      {
        logger.warn("ExpressionMetaData '" + typeName + ": Property " + propertyMetaData.getName() + ": No DisplayName");
      }

      final String propertyGrouping = propertyMetaData.getGrouping(locale);
      if (isValid(propertyGrouping, "Required", missingProperties) == false)
      {
        logger.warn("ExpressionMetaData '" + typeName + ": Property " + propertyMetaData.getName() + ": Grouping is not valid");
      }
      if (propertyMetaData.isDeprecated())
      {
        final String deprecateMessage = propertyMetaData.getDeprecationMessage(locale);
        if (isValid(deprecateMessage, "Deprecated", missingProperties) == false)
        {
          logger.warn(
              "ExpressionMetaData '" + typeName + ": Property " + propertyMetaData.getName() + ": No valid deprecate message");
        }
      }
    }
  }

  private static boolean isValid(String translation, String displayName, ArrayList<String> missingProperties)
  {
    if (translation == null)
    {
      return false;
    }
    if (translation.length() > 2 &&
        translation.charAt(0) == '!' &&
        translation.charAt(translation.length() - 1) == '!')
    {
      final String retval = translation.substring(1, translation.length() - 1);
      missingProperties.add(retval + "=" + displayName);
      return false;
    }
    return true;
  }

}
