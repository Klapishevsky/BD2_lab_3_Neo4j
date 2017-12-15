package socianetwork.Models;

import org.junit.Test;

import static org.junit.Assert.*;

public class GroupTest {
    /**
     * Test of getGroupName method, of class Group.
     */
    @Test
    public void testGetGroupName() {
        System.out.println("getGroupName");
        Group instance = new Group("_groupName");
        String expResult = "_groupName";
        String result = instance.getGroupName();
        assertEquals(expResult, result);
    }

    /**
     * Test of setGroupName method, of class Group.
     */
    @Test
    public void testSetGroupName() {
        System.out.println("setGroupName");
        String groupName = "name";
        Group instance = new Group("_groupName");
        instance.setGroupName(groupName);
        assertEquals(groupName, instance.getGroupName());
    }
}