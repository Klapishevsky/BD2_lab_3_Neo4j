package socianetwork.Models;


import lombok.Getter;

import java.util.ArrayList;

@Getter
public class SocialNetwork {
    public ArrayList<Person> personList;
    public ArrayList<Group> groupList;

    public SocialNetwork(){
        personList= new ArrayList<>();
        groupList = new ArrayList<>();
    }

    public void addPerson(Person per){
        if(per != null && !personList.contains(per)) {
            personList.add(per);
        };
    }

    public void addGroup(Group group){
        if(group != null && !groupList.contains(group)) {
            groupList.add(group);
        };
    }

    public ArrayList<Person> generatePersons(int count){
        ArrayList<Person> list = new ArrayList<>();

        return list;
    }
}
