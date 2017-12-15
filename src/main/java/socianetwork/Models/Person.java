package socianetwork.Models;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

public class Person {
    @Getter
    @Setter
    private String name;
    @Getter
    @Setter
    private String surname;
    @Getter
    @Setter
    private String secondName;
    @Getter
    @Setter
    private Boolean gender;
    @Getter
    @Setter
    private int age;
    @Getter
    private ArrayList<Person> friendsList;
    @Getter
    private ArrayList<Group> groupsList;
    @Getter
    private ArrayList<String> tweetsList;

    public Person(String surname, String name, String secondName, Boolean gender, int age){
        this.name = name;
        this.surname = surname;
        this.secondName = secondName;
        this.gender = gender;
        this.age = age;
        friendsList = new ArrayList<>();
        groupsList = new ArrayList<>();
        tweetsList = new ArrayList<>();
    }

    public void makeFriends(Person person){
        if(person != null && !friendsList.contains(person)){
            friendsList.add(person);
            person.getFriendsList().add(this);
        }
    }

    public void publishTweets(String[] message){
        ArrayList<String> arr = new ArrayList<>();
        for(int i=0; i<message.length; i++){
            arr.add(message[i]);
        }
        tweetsList=arr;
    }

    public void subscribeGroup(Group group){
        if(group != null && !groupsList.contains(group)){
            groupsList.add(group);
            group.getSubscribersList().add(this);
        }
    }

    public String getFullName(){
        if(!name.isEmpty() && !surname.isEmpty() && !secondName.isEmpty())
            return surname + " " + name + " " + secondName;
        else return null;
    }
}
