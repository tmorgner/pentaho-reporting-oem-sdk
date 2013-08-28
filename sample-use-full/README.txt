SDK Module: Using the reporting engine with a full set of dependencies
----------------------------------------------------------------------

This example shows how to run a single report in a Java-Application.
Use this module to quickly setup a project that intends to use, but
not extend the Pentaho Reporting Engine.

To use the build-scripts in this module, you need Apache Ant 1.8 or newer.
You can download Apache Ant from http://ant.apache.org/


Preparing the build
-------------------

The build-scripts of this project will download all required libraries,
including the Pentaho-Reporting libraries themselves, from the Pentaho
Maven repository at "http://repository.pentaho.org/"

To initiate the download, run

  ant resolve

from the command line.

This will download approximately 120MB of libraries. If you do not use
either the Pentaho-Data-Integration, Mondrian or Olap4J data-sources, you
can reduce the size of the download by using the "sample-use-lightweight"
SDK module instead. This would reduce the download size to 30MB.


Building the samples
--------------------

After resolving, invoke "ant dist" to compile the samples. When the
compilation completes, you will find the file
"pentaho-reporting-sample-full-TRUNK-SNAPSHOT.jar" in the local directory.
The build also produced a zip-file of the project in the "dist" directory.

Run this jar via:

  ant -jar pentaho-reporting-sample-full-TRUNK-SNAPSHOT.jar


Note: This jar references the libraries from the "lib" directory. It will
not run without a lib directory of the same structure as in this project.


Examining the samples: class sdk.sample.UseReportingGui
-------------------------------------------------------

This class loads a Pentaho Report file and shows the Print-Preview dialog
that comes with Pentaho Reporting.

Before you can use the Pentaho Reporting Engine, you have to initialize the
system. This will load up all configurations and prepares all resources
needed to process a report. If you do not boot the reporting engine, parsing
or running a report will fail with errors.

      ClassicEngineBoot.getInstance().start();

Once the reporting engine is booted up, we aquire a reference to the embedded
PRPT file. A PRPT file is a report-definition file that is produced by the
Pentaho Report Designer.

      Hint:
      You can also create these files programmatically, see the SDK module
      "sample-generate-report-files" for an example.


Pentaho Reporting uses a Resource-Manager for loading resources. The resource
manager accepts URLs, Files or byte-arrays as input. It also provides an
SPI (Service Provider Interface) to add your own storage mechanism to the
resource loading system.

      ResourceManager mgr = new ResourceManager();
      MasterReport report = (MasterReport)
                mgr.createDirectly(url, MasterReport.class).getResource();

The MasterReport object is the actual report-definition. It provides a rich
API to alter the report definition programmatically. However, in the majority
of cases, there should be no need to change the report-object at all.

The report definition contains the parameter definition, all data-sources
used by the parameters or the report-processing, as well as the actual
layout template that defines how your report will look on paper.


In this sample, we simply hand over the MasterReport object to the
PreviewDialog. The (modal) dialog provides a print-preview and offers
a wide range of export options. If you report defines parameter, then
the dialog also allows you to enter values for your parameter before
the report runs.

