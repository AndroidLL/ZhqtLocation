package zhqt.lmw.zhqtlocation.entity;

import java.io.Serializable;
import java.util.Date;


public class Location implements Serializable 
{

	public int id;
	public int cid;
	public int gid;
	public String imsi;
	public Date time;
	public Double gpslat;
	public Double gpslon;
	public Double lat;
	public Double lon;
	public Float speed;
	public Float altitude;
	public String addr;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getCid() {
		return cid;
	}
	public void setCid(int cid) {
		this.cid = cid;
	}
	public int getGid() {
		return gid;
	}
	public void setGid(int gid) {
		this.gid = gid;
	}
	public String getImsi() {
		return imsi;
	}
	public void setImsi(String imsi) {
		this.imsi = imsi;
	}
	public Date getTime() {
		return time;
	}
	public void setTime(Date time) {
		this.time = time;
	}
	public Double getGpslat() {
		return gpslat;
	}
	public void setGpslat(Double gpslat) {
		this.gpslat = gpslat;
	}
	public Double getGpslon() {
		return gpslon;
	}
	public void setGpslon(Double gpslon) {
		this.gpslon = gpslon;
	}
	public Double getLat() {
		return lat;
	}
	public void setLat(Double lat) {
		this.lat = lat;
	}
	public Double getLon() {
		return lon;
	}
	public void setLon(Double lon) {
		this.lon = lon;
	}
	public Float getSpeed() {
		return speed;
	}
	public void setSpeed(Float speed) {
		this.speed = speed;
	}
	public Float getAltitude() {
		return altitude;
	}
	public void setAltitude(Float altitude) {
		this.altitude = altitude;
	}
	public String getAddr() {
		return addr;
	}
	public void setAddr(String addr) {
		this.addr = addr;
	}
}
