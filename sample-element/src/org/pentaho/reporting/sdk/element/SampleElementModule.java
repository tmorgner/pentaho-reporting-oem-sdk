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

package org.pentaho.reporting.sdk.element;

import org.pentaho.reporting.engine.classic.core.metadata.ElementMetaDataParser;
import org.pentaho.reporting.engine.classic.core.metadata.ElementTypeRegistry;
import org.pentaho.reporting.engine.classic.core.modules.parser.bundle.BundleElementRegistry;
import org.pentaho.reporting.engine.classic.core.modules.parser.bundle.BundleStyleRegistry;
import org.pentaho.reporting.engine.classic.core.style.StyleKey;
import org.pentaho.reporting.libraries.base.boot.AbstractModule;
import org.pentaho.reporting.libraries.base.boot.ModuleInitializeException;
import org.pentaho.reporting.libraries.base.boot.SubSystem;
import org.pentaho.reporting.sdk.element.xml.SampleElementStyleReadHandler;
import org.pentaho.reporting.sdk.element.xml.SampleElementStyleWriter;
import org.pentaho.reporting.sdk.element.xml.SampleGraphicsElementReadHandler;
import org.pentaho.reporting.sdk.element.xml.SampleGraphicsElementWriteHandler;
import org.pentaho.reporting.sdk.element.xml.SampleTextElementReadHandler;
import org.pentaho.reporting.sdk.element.xml.SampleTextElementWriteHandler;

public class SampleElementModule extends AbstractModule
{
  public static final String NAMESPACE =
      "http://reporting.pentaho.org/namespaces/engine/classic/sdk/sample-element/1.0";

  public SampleElementModule() throws ModuleInitializeException
  {
    loadModuleInfo();
  }

  public void initialize(final SubSystem subSystem) throws ModuleInitializeException
  {
    StyleKey.registerClass(SampleElementStyleKeys.class);

    BundleStyleRegistry.getInstance().register(SampleElementStyleWriter.class);
    BundleStyleRegistry.getInstance().register(NAMESPACE, SampleElementStyleWriter.TAG_NAME, SampleElementStyleReadHandler.class);

    ElementTypeRegistry.getInstance().registerNamespacePrefix(NAMESPACE, "sdk-sample-element");
    ElementMetaDataParser.initializeOptionalElementMetaData("org/pentaho/reporting/sdk/element/meta-elements.xml");

    BundleElementRegistry.getInstance().register(SampleTextElementType.ELEMENT_TYPE_NAME, SampleTextElementWriteHandler.class);
    BundleElementRegistry.getInstance().register(SampleGraphicsElementType.ELEMENT_TYPE_NAME, SampleGraphicsElementWriteHandler.class);

    BundleElementRegistry.getInstance().register(NAMESPACE, SampleGraphicsElementType.ELEMENT_TYPE_NAME, SampleGraphicsElementReadHandler.class);
    BundleElementRegistry.getInstance().register(NAMESPACE, SampleTextElementType.ELEMENT_TYPE_NAME, SampleTextElementReadHandler.class);

  }
}
