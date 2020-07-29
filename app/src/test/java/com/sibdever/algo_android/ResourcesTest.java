package com.sibdever.algo_android;

import com.sibdever.algo_android.api.commands.Command;
import com.sibdever.algo_android.api.commands.InfoCommand;

import org.junit.Test;

public class ResourcesTest {

    @Test
    public void test() {

        InfoCommand command = InfoCommand.builder(Command.CommandType.LOGIN)
                .param("name", "postman")
                .param("password", "123456")
                .build();

        System.out.println(command.execute());

    }
}