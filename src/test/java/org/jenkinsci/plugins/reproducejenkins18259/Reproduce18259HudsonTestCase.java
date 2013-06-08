/**
 * 
 */
package org.jenkinsci.plugins.reproducejenkins18259;

import jenkins.model.Jenkins;
import hudson.model.FreeStyleProject;
import hudson.model.labels.LabelExpression;
import hudson.slaves.ComputerListener;
import hudson.slaves.DumbSlave;

import org.jvnet.hudson.test.HudsonTestCase;
import org.jvnet.hudson.test.CaptureEnvironmentBuilder;

/**
 * Test for reproducing JENKINS-18259 with HudsonTestCase.
 */
public class Reproduce18259HudsonTestCase extends HudsonTestCase
{
    public void testSlave() throws Exception
    {
        // Output ComputerListener registered to Jenkins.
        // When test fails, HudsonTestCase.ComputerListenerImpl is not registered
        System.out.println("ComputerListener-------------------------------------------------");
        for(ComputerListener l: Jenkins.getInstance().getExtensionList(ComputerListener.class))
        {
            System.out.println(l.getClass().getName());
        }
        System.out.println("-----------------------------------------------------------------");
        
        DumbSlave slave = createOnlineSlave();
        FreeStyleProject p = createFreeStyleProject();
        p.setAssignedLabel(LabelExpression.parseExpression(slave.getNodeName()));
        p.getBuildersList().add(new CaptureEnvironmentBuilder());
        
        assertBuildStatusSuccess(p.scheduleBuild2(p.getQuietPeriod()));
    }
}
