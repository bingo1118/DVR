package Data;

public class BusData {
	private String deviceNO;//设备号
	private String longitude;//经度
	private String latitude;//纬度
	private String routeNo;//路线号
	private String groundSpeed;//速度
	
	public BusData(String deviceNO,String longitude,String latitude,String routeNo,String groundSpeed) {
		this.deviceNO=deviceNO;
		this.longitude=longitude;
		this.latitude=latitude;
		this.routeNo=routeNo;
		this.groundSpeed=groundSpeed;
	}
	
	

	public String getDeviceNO() {
		return deviceNO;
	}

	public void setDeviceNO(String deviceNO) {
		this.deviceNO = deviceNO;
	}



	public String getLongitude() {
		return longitude;
	}



	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}



	public String getLatitude() {
		return latitude;
	}



	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}



	public String getRouteNo() {
		return routeNo;
	}



	public void setRouteNo(String routeNo) {
		this.routeNo = routeNo;
	}



	public String getGroundSpeed() {
		return groundSpeed;
	}



	public void setGroundSpeed(String groundSpeed) {
		this.groundSpeed = groundSpeed;
	}
}
