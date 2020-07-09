package com.sibdever.algo_android;

import com.sibdever.algo_android.api.ApiClient;
import com.sibdever.algo_android.api.Command;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class ResourcesTest {

    @Test
    public void test() {
        Command command = Command.LOGIN;

        Map<String, String> args = new HashMap<>();
        args.put("name", "newUserAmazon");
        args.put("password", "123456");
        command.setArguments(args);

        System.out.println(ApiClient.send(command));

    }
}