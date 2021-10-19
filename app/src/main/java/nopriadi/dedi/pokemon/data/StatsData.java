/*
 * Created by Dedi Nopriadi on 10/14/21, 1:48 PM
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 10/14/21, 1:48 PM
 */

package nopriadi.dedi.pokemon.data;

public class StatsData {
    String statsName;
    Integer statsPercen;

    public StatsData() {
    }

    public String getStatsName() {
        return statsName;
    }

    public void setStatsName(String statsName) {
        this.statsName = statsName;
    }

    public Integer getStatsPercen() {
        return statsPercen;
    }

    public void setStatsPercen(Integer statsPercen) {
        this.statsPercen = statsPercen;
    }
}
