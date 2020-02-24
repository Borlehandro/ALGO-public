package com.alex_borzikov.newhorizonstourism;

import android.util.Log;

import com.alex_borzikov.newhorizonstourism.api.ApiClient;
import com.alex_borzikov.newhorizonstourism.api.JsonParser;
import com.alex_borzikov.newhorizonstourism.api.ServerTask;
import com.alex_borzikov.newhorizonstourism.data.QuestListItem;

import org.json.JSONException;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

public class DataTest {

    @Test
    public void questListParsingTest() throws JSONException, IOException {

        String res = ApiClient.getGuestList("english");

        System.out.println("Act get " + res );

        List<QuestListItem> parsingResult = JsonParser.parseQuestList(res);

        System.out.println("It must be: ");
        for(QuestListItem item : parsingResult) {
            System.out.println(item.getName());
        }

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
}