/**
 * 
 */
package org.jenkinsci.plugins.reproducejenkins18259;

import jenkins.model.Jenkins;
import hudson.model.FreeStyleProject;
import hudson.model.labels.LabelExpression;
import hudson.slaves.ComputerListener;
import hudson.slaves.DumbSlave;

import org.junit.Rule;
import org.junit.Test;

/**
 * Test for reproducing JENKINS-18259 with JenkinsRule.
 */
public class Reproduce18259JenkinsRuleTest
{
    @Rule
    public FixedJenkinsRule j = new FixedJenkinsRule();
    
    @Test
    public void testSlave() throws Exception
    {
        // Output ComputerListener registered to Jenkins.
        // When test fails, JenkinsRule.ComputerListenerImpl is not registered
        System.out.println("ComputerListener-------------------------------------------------");
        for(ComputerListener l: Jenkins.getInstance().getExtensionList(ComputerListener.class))
        {
            System.out.println(l.getClass().getName());
        }
        System.out.println("-----------------------------------------------------------------");
        
        DumbSlave slave = j.createOnlineSlave();
        FreeStyleProject p = j.createFreeStyleProject();
        
        p.setAssignedLabel(LabelExpression.parseExpression(slave.getNodeName()));
        j.assertBuildStatusSuccess(p.scheduleBuild2(p.getQuietPeriod()));
    }
}
