package com.upic.po;


public class ResultData {
    private DataCodeEnum type;

    private String code;

    private String msg;

    private long allTime;//秒

    private long allMemeryl;//所占内存

	public DataCodeEnum getType() {
		return type;
	}

	public void setType(DataCodeEnum type) {
		this.type = type;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public long getAllTime() {
		return allTime;
	}

	public void setAllTime(long allTime) {
		this.allTime = allTime;
	}

	public long getAllMemeryl() {
		return allMemeryl;
	}

	public void setAllMemeryl(long allMemeryl) {
		this.allMemeryl = allMemeryl;
	}

	@Override
	public String toString() {
		return "ResultData [type=" + type + ", code=" + code + ", msg=" + msg + ", allTime=" + allTime + ", allMemeryl="
				+ allMemeryl + "]";
	}
    
    
}
