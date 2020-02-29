package com.alex_borzikov.newhorizonstourism;
import com.alex_borzikov.newhorizonstourism.api.ApiClient;
import com.alex_borzikov.newhorizonstourism.api.JsonParser;
import com.alex_borzikov.newhorizonstourism.data.PointInfoItem;
import com.alex_borzikov.newhorizonstourism.data.QuestInfoItem;
import com.alex_borzikov.newhorizonstourism.data.QuestListItem;
import com.alex_borzikov.newhorizonstourism.data.TaskInfoItem;

import org.json.JSONException;
import org.junit.Test;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

public class InfoTest {

    @Test
    public void questListParsingTest() throws JSONException, IOException {

        String res = ApiClient.getGuestList("english");

        System.out.println("Act get " + res );

        List<QuestListItem> parsingResult = JsonParser.parseQuestList(res);

        List<String> questsNames = parsingResult.stream().map(QuestListItem::getName).collect(Collectors.toList());
        List<String> questsDescriptions = parsingResult.stream().map(QuestListItem::getDescriptionShort).collect(Collectors.toList());

        System.out.println("Names:");
        for(String item : questsNames) {
            System.out.println(item);
        }

        System.out.println("Desc: ");
        for (String item : questsDescriptions){
            System.out.println(item);
        }
    }

    @Test
    public void questInfoTest() throws IOException, JSONException {

        String res = ApiClient.getQuestInfo("1", "english");

        System.out.println("Act get " + res );

        QuestInfoItem info = JsonParser.parseQuestInfo(res);

        System.out.println(info.getName() + " " + info.getDescriptionShort() + " "
                + info.getDescriptionName() + " " + info.getPictureName() + " " + info.getPointsCount());

    }

    @Test
    public void pointInfoTest() throws IOException, JSONException {

        String res = ApiClient.getPointInfo("1", "english");

        System.out.println("Act get " + res );

        PointInfoItem info = JsonParser.parsePointInfo(res);

        System.out.println(info.getName() + " " + info.getPictureName() + " "
                + info.getDescriptionName() + " " + info.getTaskId()
                + " " + info.getLocationX() + " " + info.getLocationY());

    }

    @Test
    public void taskInfoTest() throws IOException, JSONException {

        String res = ApiClient.getTaskInfo("1", "english",
                "languageTester", "Hello");

        System.out.println("Act get " + res );

        TaskInfoItem info = JsonParser.parseTaskInfo(res);

        System.out.println(info.getDescriptionShort() + " " + info.getPictureName() + " "
                + info.getChoice1() + " " + info.getChoice2() + " " + info.getChoice3());

    }
}