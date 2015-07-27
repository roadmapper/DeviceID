package com.vinay.deviceid;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class HelperTest {

    private Band mBand1, mBand2, mBand3, mBand4;
    private String frequency1 = "1900", frequency2 = "700";
    private String technology1 = Helper.Technology.GSM, technology2 = Helper.Technology.LTE;
    private int band2 = 13;

    @Before
    public void setUp() {
        mBand1 = new Band(frequency1, technology1);
        mBand2 = new Band(frequency2, band2, technology2);
    }

    @Test
     public void createGSM() {
        mBand3 = Helper.createGSM_TDSCDMABand(technology1 + frequency1, technology1);
        //assertThat(resultAdd, is(equalTo(2d)));
        assertEquals(mBand1, mBand3);
    }

    @Test
    public void createLTE() {
        mBand4 = Helper.createUMTS_LTEBand(technology2 + frequency2 + " (B" + band2 + ")", technology2);
        assertEquals(mBand2, mBand4);
    }

}