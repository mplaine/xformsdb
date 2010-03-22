@echo off

cls



rem Test JAVA_HOME
if not "%JAVA_HOME%" == "" goto gotJavaHome
    echo XFormsDB info
    echo =============
    echo The JAVA_HOME environment variable has not been set.
    echo.
    echo Please, see Section 3 (System Requirements) in the /README.txt
    echo file for more information about how to set the JAVA_HOME
    echo environment variable.
    goto:eof

:gotJavaHome
rem Set JAVA_OPTS
set JAVA_OPTS=-Xms448m -Xmx448m -XX:PermSize=32m -XX:MaxPermSize=256m -XX:+CMSClassUnloadingEnabled

rem Set ANT_HOME
set ANT_HOME=%CD%\..\tools\ant

rem Set ANT_CLASSPATH
set ANT_CLASSPATH=.;%ANT_HOME%\lib\ant-launcher.jar


echo XFormsDB info
echo =============
echo Setting environment variables...
echo.
echo Environment Variables:
echo JAVA_HOME=%JAVA_HOME%
echo JAVA_OPTS=%JAVA_OPTS%
echo ANT_HOME=%ANT_HOME%
echo ANT_CLASSPATH=%ANT_CLASSPATH%
echo.
echo.
echo Launching Ant...
echo.


rem Change the execution directory
cd..

rem Launch Ant with predefined arguments
set ANT_ARGS=info
"%JAVA_HOME%\bin\java" -cp "%ANT_CLASSPATH%" org.apache.tools.ant.launch.Launcher %ANT_ARGS%

rem Reset Ant arguments
set ANT_ARGS=

rem Change the execution directory back to where it was
cd bin



rem For keeping the window open
cmd /k