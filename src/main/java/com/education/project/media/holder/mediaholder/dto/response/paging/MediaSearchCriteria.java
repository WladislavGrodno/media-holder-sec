package com.education.project.media.holder.mediaholder.dto.response.paging;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MediaSearchCriteria {
    private String name;
    private String description;
    private int type;
}
