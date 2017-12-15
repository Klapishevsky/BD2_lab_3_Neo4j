package socianetwork;
import org.neo4j.graphdb.*;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import socianetwork.Models.Group;
import socianetwork.Models.Person;
import socianetwork.Models.SocialNetwork;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;
import java.util.Map.*;

public class Neo4jConnection {

    private final GraphDatabaseService graphDB;
    private SocialNetwork socialNetwork;

    public Neo4jConnection(){
        graphDB = new GraphDatabaseFactory().newEmbeddedDatabase(new File("data/socialnetwork"));
        socialNetwork= new SocialNetwork();
    }

    private void registerShutdownHook(final GraphDatabaseService graphDb) {
        // Registers a shutdown hook for the Neo4j instance so that it
        // shuts down nicely when the VM exits (even if you "Ctrl-C" the
        // running application).
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                graphDb.shutdown();
            }
        });
    }

    public void closeConnection(){
        registerShutdownHook(graphDB);
    }

    public void addPersonNode(ArrayList<Person> personToAdd){
        try (Transaction tx = graphDB.beginTx()) {
            personToAdd.forEach((person) -> {
                Node personNode = graphDB.createNode(Label.label("Person"));
                personNode.setProperty("fullName", person.getFullName());
                personNode.setProperty("gender", person.getGender());
                personNode.setProperty("age", person.getAge());
                socialNetwork.addPerson(person);
            });
            System.out.println("Added");
            tx.success();
        }
    }

    public void deletePerson(Person per){
        try (Transaction tx = graphDB.beginTx()) {

            Node perNode = graphDB.findNode(Label.label("Person"), "fullName", per.getFullName());
            perNode.delete();
            tx.success();
        }
    }

    public void setFriendRelations(Person p1, Person p2) {
        try (Transaction tx = graphDB.beginTx()) {
             {
                Node persNode = graphDB.findNode(Label.label("Person"), "fullName", p1.getFullName());
                Node friendNode = graphDB.findNode(Label.label("Person"), "fullName", p2.getFullName());
                persNode.createRelationshipTo(friendNode, RelationshipType.withName("FRIENDS"));
                friendNode.createRelationshipTo(persNode, RelationshipType.withName("FRIENDS"));
                p1.makeFriends(p2);
                System.out.println("Make friends");
                tx.success();
            }
        }
    }

    public void insertGroupsToDB(ArrayList<Group> groups) {
        try (Transaction tx = graphDB.beginTx()) {
            groups.forEach((group) -> {
                Node groupNode = graphDB.createNode(Label.label("Group"));
                groupNode.setProperty("groupName", group.getGroupName());
                socialNetwork.addGroup(group);
            });
            tx.success();
        }
    }

    public void setSubscribersRelations(Person subscriber, Group group) {
        try (Transaction tx = graphDB.beginTx()) {
            if(socialNetwork.personList.contains(subscriber) && socialNetwork.groupList.contains(group) && !subscriber.getGroupsList().contains(group)) {
                Node subscriberNode = graphDB.findNode(Label.label("Person"), "fullName", subscriber.getFullName());
                Node groupNode = graphDB.findNode(Label.label("Group"), "groupName", group.getGroupName());
                subscriberNode.createRelationshipTo(groupNode, RelationshipType.withName("SUBSCRIBED"));
                groupNode.createRelationshipTo(subscriberNode, RelationshipType.withName("HAS A SUBSCRIBER"));
                subscriber.subscribeGroup(group);
            }
            tx.success();
        }
    }

    public void publishTweet(Person publisher, String[] tweets) {
        try (Transaction tx = graphDB.beginTx()) {
            Node publisherNode = graphDB.findNode(Label.label("Person"), "fullName", publisher.getFullName());
            publisherNode.setProperty("tweetsList", tweets);
            publisher.publishTweets(tweets);
            tx.success();
        }
    }

    public Result getOrderedFullName(){
        Result resultSet;
        try (Transaction tx = graphDB.beginTx()) {
            String query
                    = "MATCH (per:Person) "
                    + "RETURN per.fullName AS People "
                    + "ORDER BY People";
            resultSet = graphDB.execute(query);
            tx.success();
        }
        return resultSet;
    }

    public Result getMenWithOrderedAge() {
        Result resultSet;
        try (Transaction tx = graphDB.beginTx()) {
            String query
                    = "MATCH (per:Person) "
                    + "WHERE per.gender=false "
                    + "RETURN per.fullName, per.age AS Age "
                    + "ORDER BY Age";
            resultSet = graphDB.execute(query);
            tx.success();
        }
        return resultSet;
    }

    public Result getMenFriends(String personFullName) {
        Result resultSet;
        try (Transaction tx = graphDB.beginTx()) {
            String query
                    = "MATCH (pers:Person)"
                    + "-[:FRIENDS]-> (friend:Person) "
                    + "WHERE pers.fullName = \'" + personFullName + "\' "
                    + "RETURN friend.fullName AS Friends "
                    + "ORDER BY Friends";
            resultSet = graphDB.execute(query);
            tx.success();
        }
        return resultSet;
    }

    public Result getCountOfFriends(){
        Result resultSet;
        try (Transaction tx = graphDB.beginTx()) {
            String query
                    = "MATCH (pers:Person)"
                    + "OPTIONAL MATCH (pers:Person)-[:FRIENDS]-> (friend:Person) "
                    + "RETURN length(collect(friend)) AS FriendsCount, pers.fullName AS People "
                    + "ORDER BY People";
            resultSet = graphDB.execute(query);
            tx.success();
        }
        return resultSet;
    }

    public Result getOrderedGroups() {
        Result resultSet;
        try (Transaction tx = graphDB.beginTx()) {
            String query
                    = "MATCH (group:Group) "
                    + "RETURN group.groupName AS Groups "
                    + "ORDER BY Groups";
            resultSet = graphDB.execute(query);
            tx.success();
        }
        return resultSet;
    }

    public Result getCountOfSubscribers() {
        Result resultSet;
        try (Transaction tx = graphDB.beginTx()) {
            String query
                    = "MATCH (group:Group)"
                    + "OPTIONAL MATCH (pers:Person)-[:SUBSCRIBED]-> (group:Group) "
                    + "RETURN group.groupName AS Groups, length(collect(pers)) AS SubscribersCount "
                    + "ORDER BY SubscribersCount DESC";
            resultSet = graphDB.execute(query);
            tx.success();
        }
        return resultSet;
    }

    public Result getOrderedPeopleGroupsCount() {
        Result resultSet;
        try (Transaction tx = graphDB.beginTx()) {
            String query
                    = "MATCH (pers:Person) "
                    + "OPTIONAL MATCH (pers)-[:SUBSCRIBED]-> (group) "
                    + "RETURN pers.fullName AS People, length(collect(group)) AS GroupsCount "
                    + "ORDER BY GroupsCount DESC";
            resultSet = graphDB.execute(query);
            tx.success();
        }
        return resultSet;
    }

    public Result getDeepFriendsGroupsCount(String personFullName) {
        Result resultSet;
        try (Transaction tx = graphDB.beginTx()) {
            String query
                    = "MATCH (pers:Person)"
                    + "-[:FRIENDS]-> (friend:Person)"
                    + "-[:FRIENDS]-> (deepFriend:Person)"
                    + "-[:SUBSCRIBED]-> (group:Group) "
                    + "WHERE pers.fullName = \'" + personFullName + "\' "
                    + "RETURN count(group) AS FriendOfFriendGroupsCount";
            resultSet = graphDB.execute(query);
            tx.success();
        }
        return resultSet;
    }

    public static void printResult(Result result){
        while ( result.hasNext() )
        {
            Map<String,Object> row = result.next();
            for ( Entry<String, Object> column : row.entrySet() )
            {
                System.out.println(column.getKey() + ": " + column.getValue() + "; ");
            }
        }
    }

}
