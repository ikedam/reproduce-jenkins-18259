/**
 * 
 */
package org.jenkinsci.plugins.reproducejenkins18259;

import hudson.model.FreeStyleProject;
import hudson.model.labels.LabelExpression;
import hudson.slaves.DumbSlave;

import org.junit.Rule;
import org.junit.Test;
import org.jvnet.hudson.test.JenkinsRule;

/**
 * @author yasuke
 *
 */
public class Reproduce18259JenkinsRuleTest
{
    @Rule
    public JenkinsRule j = new JenkinsRule();
    
    @Test
    public void testSlave() throws Exception
    {
        DumbSlave slave = j.createOnlineSlave();
        FreeStyleProject p = j.createFreeStyleProject();
        
        p.setAssignedLabel(LabelExpression.parseExpression(slave.getNodeName()));
        j.assertBuildStatusSuccess(p.scheduleBuild2(p.getQuietPeriod()));
    }
}
