package com.roadmapper.deviceid;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Toast;

import java.util.HashMap;

public class Helper {

    public static class Technology {
        public static final String GSM = "GSM";
        public static final String TD_SCDMA = "TD-SCDMA";
        public static final String CDMA = "CDMA";
        public static final String UMTS = "UMTS";
        public static final String LTE = "LTE";
    }

    public static class Preferences {
        public static final String PREFS_NAME = "MyPrefsFile";
    }

    public static void showToast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    public static void showSnackbar(View rootLayout, String msg) {
        Snackbar.make(rootLayout, msg, Snackbar.LENGTH_SHORT).show();
    }

    public static Band createGSM_TDSCDMABand(String bandString, String technology) {
        String frequency = bandString.replace(technology, ""); // make it "850"
        return new Band(frequency, technology);
    }

    public static Band createUMTS_LTEBand(String bandString, String technology) {
        String temp = bandString.replace(technology, ""); // make it "850 (B0)"
        int bandNum = Integer.parseInt(temp.substring(temp.indexOf("(") + 1, temp.indexOf(")")).replace("B", ""));
        String frequency = temp.substring(0, temp.indexOf("(") - 1);
        return new Band(frequency, bandNum, technology);
    }


    public static Band createCDMABand(String bandString) {
        String temp = bandString.replace(Technology.CDMA, ""); // make it "800 (BC0)"
        int bandNum = Integer.parseInt(temp.substring(temp.indexOf("(") + 1, temp.indexOf(")")).replace("BC", ""));
        String frequency = temp.substring(0, temp.indexOf("(") - 1);
        return new Band(frequency, bandNum, Technology.CDMA);
    }

    public static Band getBand(String band, String technology) {
        int band_i = Integer.parseInt(band);
        HashMap<Key, Band> master_bands = new HashMap<>();
        master_bands.put(new Key(1, "UMTS"), new Band("2100", 1, "UMTS")); // IMT
        master_bands.put(new Key(1, "LTE"), new Band("2100", 1, "LTE"));
        master_bands.put(new Key(2, "UMTS"), new Band("1900", 2, "UMTS")); // PCS A-F
        master_bands.put(new Key(2, "LTE"), new Band("1900", 2, "LTE"));
        master_bands.put(new Key(3, "UMTS"), new Band("1800", 3, "UMTS")); // DCS
        master_bands.put(new Key(3, "LTE"), new Band("1800", 3, "LTE"));
        master_bands.put(new Key(4, "UMTS"), new Band("1700", 4, "UMTS")); // AWS A-F
        master_bands.put(new Key(4, "LTE"), new Band("1700", 4, "LTE"));
        master_bands.put(new Key(5, "UMTS"), new Band("850", 5, "UMTS")); // CLR
        master_bands.put(new Key(5, "LTE"), new Band("850", 5, "LTE"));
        master_bands.put(new Key(6, "UMTS"), new Band("800", 6, "UMTS"));
        master_bands.put(new Key(6, "LTE"), new Band("800", 6, "LTE"));
        master_bands.put(new Key(7, "UMTS"), new Band("2600", 7, "UMTS")); // IMT-E
        master_bands.put(new Key(7, "LTE"), new Band("2600", 7, "LTE"));
        master_bands.put(new Key(8, "UMTS"), new Band("900", 8, "UMTS")); // E-GSM
        master_bands.put(new Key(8, "LTE"), new Band("900", 8, "LTE"));
        master_bands.put(new Key(9, "UMTS"), new Band("1700", 9, "UMTS"));
        master_bands.put(new Key(9, "LTE"), new Band("1700", 9, "LTE"));
        master_bands.put(new Key(10, "UMTS"), new Band("1700", 10, "UMTS")); // EAWS A-G
        master_bands.put(new Key(10, "LTE"), new Band("1700", 10, "LTE"));
        master_bands.put(new Key(11, "UMTS"), new Band("1500", 11, "UMTS")); // LPDC
        master_bands.put(new Key(11, "LTE"), new Band("1500", 11, "LTE"));
        master_bands.put(new Key(12, "UMTS"), new Band("700", 12, "UMTS")); // LSMH A/B/C
        master_bands.put(new Key(12, "LTE"), new Band("700", 12, "LTE")); // T-Mobile 700a
        master_bands.put(new Key(13, "UMTS"), new Band("700", 13, "UMTS")); // USMH C
        master_bands.put(new Key(13, "LTE"), new Band("700", 13, "LTE")); // Verizon 700c
        master_bands.put(new Key(14, "UMTS"), new Band("700", 14, "UMTS")); // USMH D
        master_bands.put(new Key(14, "LTE"), new Band("700", 14, "LTE"));
        master_bands.put(new Key(17, "LTE"), new Band("700", 17, "LTE")); // LSMH B/C (subset of 12)
        master_bands.put(new Key(18, "LTE"), new Band("800", 18, "LTE"));
        master_bands.put(new Key(19, "UMTS"), new Band("800", 19, "UMTS"));
        master_bands.put(new Key(19, "LTE"), new Band("800", 19, "LTE"));
        master_bands.put(new Key(20, "UMTS"), new Band("800", 20, "UMTS")); // EUDD
        master_bands.put(new Key(20, "LTE"), new Band("800", 20, "LTE"));
        master_bands.put(new Key(21, "UMTS"), new Band("1500", 21, "UMTS")); // UPDC
        master_bands.put(new Key(21, "LTE"), new Band("1500", 21, "LTE"));
        master_bands.put(new Key(22, "UMTS"), new Band("3500", 22, "UMTS"));
        master_bands.put(new Key(22, "LTE"), new Band("3500", 22, "LTE"));
        master_bands.put(new Key(23, "LTE"), new Band("2000", 23, "LTE")); // S-Band
        master_bands.put(new Key(24, "LTE"), new Band("1600", 24, "LTE")); // L-Band
        master_bands.put(new Key(25, "UMTS"), new Band("1900", 25, "UMTS")); // EPCS A-G
        master_bands.put(new Key(25, "LTE"), new Band("1900", 25, "LTE"));
        master_bands.put(new Key(26, "UMTS"), new Band("850", 26, "UMTS")); // ECLR
        master_bands.put(new Key(26, "LTE"), new Band("850", 26, "LTE"));
        master_bands.put(new Key(27, "LTE"), new Band("850", 27, "LTE")); // SMR
        master_bands.put(new Key(28, "LTE"), new Band("700", 28, "LTE")); // APT
        master_bands.put(new Key(29, "LTE"), new Band("700", 29, "LTE")); // LSMH D/E carrier agg
        master_bands.put(new Key(30, "LTE"), new Band("2300", 30, "LTE")); // WCS A/B
        master_bands.put(new Key(31, "LTE"), new Band("450", 31, "LTE"));
        master_bands.put(new Key(32, "LTE"), new Band("1500", 32, "LTE")); // L-Band carrier agg
        master_bands.put(new Key(33, "LTE"), new Band("2100", 33, "LTE")); // TDD: Pre-IMT
        master_bands.put(new Key(34, "LTE"), new Band("2100", 34, "LTE")); // TDD: Pre-IMT
        master_bands.put(new Key(35, "LTE"), new Band("1900", 35, "LTE")); // TDD: PCS
        master_bands.put(new Key(36, "LTE"), new Band("1900", 36, "LTE")); // TDD: PCS
        master_bands.put(new Key(37, "LTE"), new Band("1900", 37, "LTE")); // TDD: PCS
        master_bands.put(new Key(38, "LTE"), new Band("2600", 38, "LTE")); // TDD: IMT-E
        master_bands.put(new Key(39, "LTE"), new Band("1900", 39, "LTE")); // TDD
        master_bands.put(new Key(40, "LTE"), new Band("2300", 40, "LTE")); // TDD
        master_bands.put(new Key(41, "LTE"), new Band("2500", 41, "LTE")); // TDD: BRS/EBS
        master_bands.put(new Key(42, "LTE"), new Band("3500", 42, "LTE")); // TDD
        master_bands.put(new Key(43, "LTE"), new Band("3700", 43, "LTE")); // TDD
        master_bands.put(new Key(44, "LTE"), new Band("2500", 44, "LTE")); // TDD: APT
        return master_bands.get(new Key(band_i, technology));
    }

    private static class Key {

        private final int band_num;
        private final String technology;

        public Key(int band_num, String technology) {
            this.band_num = band_num;
            this.technology = technology;
        }


        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Key)) return false;
            Key key = (Key) o;
            return band_num == key.band_num && technology.equals(key.technology);
        }

        @Override
        public int hashCode() {
            int result = band_num;
            result = 31 * result + technology.hashCode();
            return result;
        }

    }

}
