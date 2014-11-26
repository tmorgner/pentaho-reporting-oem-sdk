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

package sdk.sample;

import java.awt.GraphicsEnvironment;

public class Main
{
  public static void main(String[] args)
  {
    final boolean useGUI;
    if (args.length == 0)
    {
      useGUI = (GraphicsEnvironment.isHeadless() == false);
    }
    else if ("--gui".equals(args[0]))
    {
      useGUI = true;
    }
    else if ("--no-gui".equals(args[0]))
    {
      useGUI = false;
    }
    else
    {
      System.err.println("Usage");
      System.err.println("'run'          - run in automatic mode.");
      System.err.println("                 If there is a GUI system, show the preview dialog, else create a PDF file.");
      System.err.println("'run --gui'    - run in GUI mode. Show the preview dialog");
      System.err.println("'run --no-gui' - run in console mode. Creates a PDF file in the current directory.");

      useGUI = false;
    }

    if (useGUI)
    {
      UseReportingGui.main(args);
    }
    else
    {
      UseReportingExport.main(args);
    }
  }
}
