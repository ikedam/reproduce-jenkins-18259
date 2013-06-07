/**
 * 
 */
package org.jenkinsci.plugins.reproducejenkins18259;

import hudson.model.FreeStyleProject;
import hudson.model.labels.LabelExpression;
import hudson.slaves.DumbSlave;

import org.jvnet.hudson.test.HudsonTestCase;
import org.jvnet.hudson.test.CaptureEnvironmentBuilder;

/**
 * @author yasuke
 *
 */
public class Reproduce18259HudsonTestCase extends HudsonTestCase
{
    public void testSlave() throws Exception
    {
        DumbSlave slave = createOnlineSlave();
        FreeStyleProject p = createFreeStyleProject();
        p.setAssignedLabel(LabelExpression.parseExpression(slave.getNodeName()));
        p.getBuildersList().add(new CaptureEnvironmentBuilder());
        
        assertBuildStatusSuccess(p.scheduleBuild2(p.getQuietPeriod()));
    }
}
