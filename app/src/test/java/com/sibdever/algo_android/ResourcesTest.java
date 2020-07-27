package com.sibdever.algo_android;

import com.sibdever.algo_android.api.commands.LoginCommand;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class ResourcesTest {

    @Test
    public void test() {

        Map<String, String> args = new HashMap<>();
        args.put("name", "postman");
        args.put("password", "123456");

        LoginCommand command = LoginCommand.builder()
                .param("name", "postman")
                .param("password", "123456")
                .build();

        System.out.println(command.execute());

        // System.out.println(ApiClient.getInfoResponse(command));

    }
}