package toby.test.gateway.common.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

public class BaseEntity implements Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7250364654548488862L;

	public BaseEntity(LocalDateTime createTime, LocalDateTime upDateTime, Integer version) {
		super();
		this.createTime = createTime;
		this.upDateTime = upDateTime;
		this.version = version;
	}

	private LocalDateTime createTime;

	private LocalDateTime upDateTime;

	private Integer version;

	public LocalDateTime getCreateTime() {
		return createTime;
	}

	public void setCreateTime(LocalDateTime createTime) {
		this.createTime = createTime;
	}

	public LocalDateTime getUpDateTime() {
		return upDateTime;
	}

	public void setUpDateTime(LocalDateTime upDateTime) {
		this.upDateTime = upDateTime;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}
}
