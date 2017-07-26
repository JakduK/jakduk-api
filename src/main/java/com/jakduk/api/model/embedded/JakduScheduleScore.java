package com.jakduk.api.model.embedded;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author pyohwan
 * 15. 12. 25 오후 11:08
 */

@NoArgsConstructor
@Getter
@Setter
public class JakduScheduleScore {

    private Integer homeFullTime;

    private Integer awayFullTime;

    private Integer homeOverTime;

    private Integer awayOverTime;

    private Integer homePenaltyShootout;

    private Integer awayPenaltyShootout;

}
