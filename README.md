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
* **-version** Job-Version to export. If not set, the latest will be exported 
* **-context** Context name. If not set, "Default" will be used.

Example exporting all "Extractions" jobs (for instance "Extractions/fromComponentBToC"):

``
C:\tools\talend\6.1.1\TOS_DI-win-x86_64.exe -nosplash -data C:\projects\trunk\com.bsb.myProject.transformation\src\main\talend -application com.bsb.tools.talend.export.JobExportApplication -targetFile C:\projects\trunk\com.bsb.myProject.transformation\target\Extractions.zip -projectName MY_PROJECT_TRANSFORMATION -jobsToExport "Extractions/[0-9a-zA-Z]*" -clean
``

## How to build

* Checkout this repository to a local directory.
* Make sure you can execute `mvn` on your command line.

* The `org.talend.repository_6.1.1.20151214_1327.jar`  have to be installed in your local Maven 
repository. 
See [install-file Maven command](https://maven.apache.org/plugins/maven-install-plugin/examples/specific-local-repo.html)  for more details about the install-file Maven command e.g.:

`mvn install:install-file -Dfile=plugins\org.talend.repository_6.1.1.20151214_1327.jar -DgroupId=org.talend.studio -DartifactId=org.talend.repository -Dversion=6.1.1 -Dpackaging=jar`

 * Build the plugin with the `mvn clean install` command. 


## Install the plugin

Once built with the standard Maven command `mvn clean install`, the jar file can be moved in the "plugins" directory of your Talend TOS DI installation.