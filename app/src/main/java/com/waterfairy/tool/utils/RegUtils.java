package com.waterfairy.tool.utils;

import java.util.regex.Pattern;

/**
 * Created by water_fairy on 2017/2/17.
 */

public class RegUtils {
    public boolean checkPhone(String mobile) {
        Pattern pattern = Pattern.compile("^1[3|4|5|7|8][0-9]{9}$");
        return pattern.matcher(mobile).matches();
    }

}
