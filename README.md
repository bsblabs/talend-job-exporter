# talend-job-exporter

Talend plugin allowing to use Talend TOS DI in command line to export jobs.


## How to use it

Talend TOS DI can be called with the following arguments:

* **-nosplash** Start Talend without the GUI.
* **-data \[DIRECTORY\]** Specify where the Talend workspace is located.
* **-application com.bsb.tools.talend.JobExportApplication** Specify that the application to execute is our application.
* **-targetFile \[ZIP FILE\]** Specify the FQN of the zip file containing exported jobs.
* **-projectName \[PROJECT NAME\]** Specify the name of the Talend project in the current workspace.
* **-jobsToExport \[PATTERN\]** Specify with a regex the jobs to export (based on their labels).
* **-clean** Clean the workspace.

Example exporting all "Extractions" jobs (for instance "Extractions/fromComponentBToC"):

``
C:\tools\talend\5.5.1\TOS_DI-win-x86_64.exe -nosplash -data C:\projects\trunk\com.bsb.myProject.transformation\src\main\talend -application com.bsb.tools.talend.export.JobExportApplication -targetFile C:\projects\trunk\com.bsb.myProject.transformation\target\Extractions.zip -projectName MY_PROJECT_TRANSFORMATION -jobsToExport "Extractions/[0-9a-zA-Z]*" -clean
``


## Dependencies

Dependencies declared in _pom.xml_ are based on the libraries defined in the Talend TOS DI 5.5.1 distribution (in the "plugins" directory). Dependencies can be installed on your local Maven repository with the [install-file Maven command](https://maven.apache.org/plugins/maven-install-plugin/examples/specific-local-repo.html).


## Install the plugin

Once built with the standard Maven command `mvn clean install`, the jar file can be moved in the "plugins" directory of your Talend TOS DI installation.