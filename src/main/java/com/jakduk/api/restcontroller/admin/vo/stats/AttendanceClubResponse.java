package com.jakduk.api.restcontroller.admin.vo.stats;

import com.jakduk.api.model.db.AttendanceClub;
import lombok.Data;

import java.util.List;

/**
 * @author pyohwan
 * 16. 3. 20 오후 10:57
 */

@Data
public class AttendanceClubResponse {
    private List<AttendanceClub> attendances;
    private Integer totalSum;
    private Integer matchSum;
    private Integer average;
}
