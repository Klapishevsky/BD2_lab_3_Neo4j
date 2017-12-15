package socianetwork.Models;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

public class Group {
    @Getter
    @Setter
    private String groupName;
    @Getter
    private final ArrayList<Person> subscribersList;

    public Group(String groupName){
        this.groupName = groupName;
        subscribersList = new ArrayList<>();
    }

}
