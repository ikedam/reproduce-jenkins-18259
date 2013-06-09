/**
 * 
 */
package org.jenkinsci.plugins.reproducejenkins18259;

import jenkins.model.Jenkins;
import hudson.model.FreeStyleBuild;
import hudson.model.FreeStyleProject;
import hudson.model.labels.LabelExpression;
import hudson.slaves.ComputerListener;
import hudson.slaves.DumbSlave;
import hudson.slaves.SlaveComputer;

import org.jvnet.hudson.test.HudsonTestCase;
import org.jvnet.hudson.test.CaptureEnvironmentBuilder;
import org.jvnet.hudson.test.SleepBuilder;

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
    
    public void testShutdownSlave() throws Exception {
        DumbSlave slave1 = createOnlineSlave(); // online, and a build finished.
        DumbSlave slave2 = createOnlineSlave(); // online, and a build finished, and disconnected.
        DumbSlave slave3 = createOnlineSlave(); // online, and a build still running.
        DumbSlave slave4 = createOnlineSlave(); // online, and not used.
        DumbSlave slave5 = createSlave();   // offline.
        
        assertNotNull(slave1);
        assertNotNull(slave2);
        assertNotNull(slave3);
        assertNotNull(slave4);
        assertNotNull(slave5);
        
        // A build runs on slave1 and finishes.
        {
            FreeStyleProject project1 = createFreeStyleProject();
            project1.setAssignedLabel(LabelExpression.parseExpression(slave1.getNodeName()));
            project1.getBuildersList().add(new SleepBuilder(1 * 1000));
            assertBuildStatusSuccess(project1.scheduleBuild2(0));
        }
        
        // A build runs on slave2 and finishes, then disconnect slave2 
        {
            FreeStyleProject project2 = createFreeStyleProject();
            project2.setAssignedLabel(LabelExpression.parseExpression(slave2.getNodeName()));
            project2.getBuildersList().add(new SleepBuilder(1 * 1000));
            assertBuildStatusSuccess(project2.scheduleBuild2(0));
            
            SlaveComputer computer2 = slave2.getComputer();
            computer2.disconnect(null);
            computer2.waitUntilOffline();
        }
        
        // A build runs on slave3 and does not finish.
        // This build will be interrupted in tearDown().
        {
            FreeStyleProject project3 = createFreeStyleProject();
            project3.setAssignedLabel(LabelExpression.parseExpression(slave3.getNodeName()));
            project3.getBuildersList().add(new SleepBuilder(10 * 60 * 1000));
            project3.scheduleBuild2(0);
            FreeStyleBuild build;
            while((build = project3.getLastBuild()) == null) {
                Thread.sleep(500);
            }
            assertTrue(build.isBuilding());
        }
    }
}
