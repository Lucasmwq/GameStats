package com.gamestats;

import com.gamestats.basededatos.JuegoBDTest;
import com.gamestats.modelo.JuegoTest;
import com.gamestats.api.ParsearJSONTest;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({
        ParsearJSONTest.class,
        JuegoBDTest.class,
        JuegoTest.class
})
public class GameStatsTestSuite {

}