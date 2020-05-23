package com.myproject.multifunctioncrawler.service;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class RushServiceTest {
    @Mock
    RushService rushService;

    @Test
    public void testGetRushResult(){
        rushService.getRushResult(13700000000L,1);
    }
}
