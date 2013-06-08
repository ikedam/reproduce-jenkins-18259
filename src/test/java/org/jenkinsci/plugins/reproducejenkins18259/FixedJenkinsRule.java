/**
 * 
 */
package org.jenkinsci.plugins.reproducejenkins18259;

import hudson.model.FreeStyleProject;
import hudson.slaves.DumbSlave;

import org.jvnet.hudson.test.JenkinsRule;

/**
 * Fixes for Jenkins < 1.479.
 */
public class FixedJenkinsRule extends JenkinsRule
{
    public FreeStyleProject createFreeStyleProject() throws java.io.IOException
    {
        // createFreeStyleProject is protected with Jenkins < 1.479
        return super.createFreeStyleProject();
    }
    
    public DumbSlave createOnlineSlave() throws Exception
    {
        // createOnlineSlave is protected with Jenkins < 1.479
        return super.createOnlineSlave();
    }
}
