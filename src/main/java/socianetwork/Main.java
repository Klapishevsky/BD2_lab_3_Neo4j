package socianetwork;

import socianetwork.Models.Group;
import socianetwork.Models.Person;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        Neo4jConnection connection = new Neo4jConnection();
        //ArrayList<Person> arr = new ArrayList<>();
        //ArrayList<Group> group = new ArrayList<>();
        Person Ann = new Person("Tsytovtseva", "Ann", "Sergeevna", true, 19);
        Person Kitty = new Person("Hello", "Kitty", "Pinky", true, 8);
        Person Max = new Person("Sosnovskiy", "Max", "Sergeevich", false, 21);
        Person Sergiy = new Person("Stone", "Sergiy", "Sergeevich", false, 24);
        Group MDK = new Group("MDK");
        Group Sohranyonky = new Group("Sohranyonky");
        //arr.add(Ann);
        //arr.add(Kitty);
        //arr.add(Max);
        //arr.add(Sergiy);
        //group.add(MDK);
        //group.add(Sohranyonky);
        //connection.addPersonNode(arr);
        //connection.setFriendRelations(Max,Kitty);
        //connection.setFriendRelations(Max,Ann);
        //connection.setFriendRelations(Max,Sergiy);
        //connection.setFriendRelations(Ann,Kitty);
        //connection.insertGroupsToDB(group);
        //connection.setSubscribersRelations(Max, MDK);
        //connection.setSubscribersRelations(Sergiy, MDK);
        //connection.setSubscribersRelations(Ann, Sohranyonky);
        //connection.setSubscribersRelations(Kitty, Sohranyonky);
        Neo4jConnection.printResult(connection.getOrderedFullName());
        Neo4jConnection.printResult(connection.getMenWithOrderedAge());
        Neo4jConnection.printResult(connection.getMenFriends("Sosnovskiy Max Sergeevich"));
        Neo4jConnection.printResult(connection.getCountOfFriends());
        Neo4jConnection.printResult(connection.getOrderedGroups());
        Neo4jConnection.printResult(connection.getCountOfSubscribers());
        Neo4jConnection.printResult(connection.getOrderedPeopleGroupsCount());
        Neo4jConnection.printResult(connection.getDeepFriendsGroupsCount("Sosnovskiy Max Sergeevich"));
        connection.closeConnection();
    }
}
