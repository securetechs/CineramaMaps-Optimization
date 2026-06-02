package main.com.cineramamaps.constant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ExpandableListDataPump {
    public static HashMap<String, List<String>> getData() {
    HashMap<String, List<String>> expandableListDetail = new HashMap<String, List<String>>();

    List<String> cricket = new ArrayList<String>();
    cricket.add("Men Hair Cut 1");
    cricket.add("Men Hair Cut 2");
    cricket.add("Men Hair Cut 3");
    cricket.add("Men Hair Cut 4");
    cricket.add("Men Hair Cut 5");

    List<String> football = new ArrayList<String>();
    football.add("Shaving 1");
    football.add("Shaving 2");
    football.add("Shaving 3");
    football.add("Shaving 4");
    football.add("Shaving 5");

    List<String> basketball = new ArrayList<String>();
    basketball.add("Die 1");
    basketball.add("Die 2");
    basketball.add("Die 3");
    basketball.add("Die 4");
    basketball.add("Die 5");

    expandableListDetail.put("Men Hair Cut", cricket);
    expandableListDetail.put("Shaving", football);
    expandableListDetail.put("Die", basketball);
    return expandableListDetail;
}
}
