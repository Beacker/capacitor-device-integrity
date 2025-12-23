package com.security.deviceintegrity;

import com.getcapacitor.Logger;

public class DeviceIntegrity {

    public String echo(String value) {
        Logger.info("Echo", value);
        return value;
    }
}
