Modules
=======

Pentaho Reporting uses modules as a plugin-system. The engine initializes
modules in a controlled way when the initialization method
ClassicEngineBoot#start() is called.

Modules can declare dependencies. Dependencies tell the initialization
routine in which order to call all modules. When you declare a dependency,
the reporting engine guarantees that all dependent modules are called before
your module is started. If a dependency is not available or failed to start,
your module will not start either. This guarantees that your module code is
only called if all pre-requisites for running correctly are met by the system.


Registering Modules
-------------------

The reporting engine needs to know that your module exists, before it can
work with it. Therefore, your module must register itself in the global
configuration using a global "/classic-engine.properties" file in the module
root.

All modules that are not created by Pentaho should be registered in the
configuration namespace of "org.pentaho.reporting.engine.classic.extensions.modules."

The registration entry has the format:

  org.pentaho.reporting.engine.classic.extensions.modules.<vendor-specific-id>.Module=<classname of the module>

The module definition we use in this SDK-section is defined as class
"org.pentaho.reporting.sdk.module.SampleModule". We choose the vendor-specific id
as "sdk.sample-module", and therefore the configuration key we enter in the
global configuration will be:

  org.pentaho.reporting.engine.classic.extensions.modules.sdk.sample-module.Module=org.pentaho.reporting.sdk.module.SampleModule


Now that the reporting engine calls your module during the boot-up process, you
can add any module specific initialization code into the module's 'initialize' method.
If you need to indicate an error to the boot-process, throw an ModuleInitializeException.
