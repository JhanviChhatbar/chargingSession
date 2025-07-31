package com.orenda.charging_session.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CallbackPayload {

    private String stationId;
    private String driverToken;
    private String status;
}
