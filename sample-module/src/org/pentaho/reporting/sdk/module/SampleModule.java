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

package org.pentaho.reporting.sdk.module;

import org.pentaho.reporting.engine.classic.core.ClassicEngineBoot;
import org.pentaho.reporting.libraries.base.boot.AbstractModule;
import org.pentaho.reporting.libraries.base.boot.ModuleInitializeException;
import org.pentaho.reporting.libraries.base.boot.SubSystem;

public class SampleModule extends AbstractModule
{
  public SampleModule() throws ModuleInitializeException
  {
    loadModuleInfo();
  }

  public void initialize(final SubSystem subSystem) throws ModuleInitializeException
  {
    System.out.println ("Module " + getName() + " initialised.");
    System.out.println ("Our sample configuration property is: " +
        subSystem.getGlobalConfig().getConfigProperty("org.pentaho.reporting.sdk.module.SampleConfigurationProperty"));
    // note: The global configuration is also available via the "ClassicEngineBoot" class
    System.out.println ("Our sample configuration property is: " +
        ClassicEngineBoot.getInstance().getGlobalConfig().getConfigProperty
            ("org.pentaho.reporting.sdk.module.SampleConfigurationProperty"));
  }
}
