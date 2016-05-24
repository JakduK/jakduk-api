package com.jakduk.model.web.stats;

import com.jakduk.model.db.AttendanceClub;
import lombok.Data;

import java.util.List;

/**
 * Created by pyohwan on 16. 3. 20.
 */

@Data
public class AttendanceClubResponse {
    private List<AttendanceClub> attendances;
    private Integer totalSum;
    private Integer matchSum;
    private Integer average;
}
