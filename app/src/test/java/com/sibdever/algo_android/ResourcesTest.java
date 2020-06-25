package com.sibdever.algo_android;

import org.junit.Test;

import response.UserResponse;

import static org.junit.Assert.assertEquals;

public class ResourcesTest {

    @Test
    public void testPicture() {
        UserResponse response = UserResponse.ALREADY_EXIST;
        assertEquals(response.toString(), "ALREADY_EXIST");
    }
}
