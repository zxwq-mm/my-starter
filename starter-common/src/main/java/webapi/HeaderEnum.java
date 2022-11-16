package webapi;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum HeaderEnum {

    /**
     * 业务跟踪号
     */
    BIZ_TRACE_ID("bizTraceId"),

    /**
     * 交易序列号
     */
    TRANS_SEQ_NO("transSeqNo");

    private final String code;
}
