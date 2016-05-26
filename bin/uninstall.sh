#!/bin/sh

clear



# Test JAVA_HOME
if [ -z "$JAVA_HOME" ]; then
    echo "XFormsDB uninstall"
    echo "=================="
    echo "The JAVA_HOME environment variable has not been set."
    echo ""
    echo "Please, see Section 3 (System Requirements) in the /README.txt"
    echo "file for more information about how to set the JAVA_HOME"
    echo "environment variable."
    exit
fi

# Set JAVA_OPTS
export JAVA_OPTS="-Xms512m -Xmx512m -XX:+CMSClassUnloadingEnabled"

# Set ANT_HOME
export ANT_HOME=$(pwd)/../tools/ant
# Convert path in case of Cygwin
# export ANT_HOME=`cygpath --path --windows "$ANT_HOME"`

# Set ANT_CLASSPATH
export ANT_CLASSPATH=.:$ANT_HOME/lib/ant-launcher.jar
# Convert path in case of Cygwin
# export ANT_CLASSPATH=`cygpath --path --windows "$ANT_CLASSPATH"`


echo "XFormsDB uninstall"
echo "=================="
echo "Setting environment variables..."
echo ""
echo "Environment Variables:"
echo "JAVA_HOME="$JAVA_HOME
echo "JAVA_OPTS="$JAVA_OPTS
echo "ANT_HOME="$ANT_HOME
echo "ANT_CLASSPATH="$ANT_CLASSPATH
echo ""
echo ""
echo "Launching Ant..."
echo ""


# Change the execution directory
cd ..

# Launch Ant with predefined arguments
export ANT_ARGS="uninstall"
"$JAVA_HOME/bin/java" -cp "$ANT_CLASSPATH" org.apache.tools.ant.launch.Launcher $ANT_ARGS

# Reset Ant arguments
export ANT_ARGS=

# Change the execution directory back to where it was
cd bin