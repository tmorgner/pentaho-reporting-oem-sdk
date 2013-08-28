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
