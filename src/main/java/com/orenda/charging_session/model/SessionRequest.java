package com.orenda.charging_session.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SessionRequest {

    private String stationId;
    private String driverTokenId;
    private String callBackURL;

}
